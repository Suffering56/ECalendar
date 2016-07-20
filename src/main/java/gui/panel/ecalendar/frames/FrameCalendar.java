package gui.panel.ecalendar.frames;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.JTableHeader;

import gui.panel.ecalendar.data.CalendarTableModel;
import gui.panel.ecalendar.data.Converter;
import gui.panel.ecalendar.data.ExtendCalendarRow;
import gui.panel.ecalendar.data.JCalendarTableView;
import gui.panel.ecalendar.data.remote.ExtendFilterRemoteService;
import gui.panel.ecalendar.frames.filters.MacroFilter;
import gui.panel.ecalendar.frames.parents.DataFrame;
import gui.panel.ecalendar.frames.renderers.HeaderRenderer;
import gui.panel.ecalendar.frames.renderers.main.CountryRenderer;
import gui.panel.ecalendar.frames.renderers.main.EventAreaRenderer;
import gui.panel.ecalendar.frames.renderers.main.ImportanceRenderer;
import gui.panel.ecalendar.frames.renderers.main.LabelRenderer;
import gui.panel.ecalendar.frames.renderers.main.NotesRenderer;
import gui.panel.ecalendar.frames.renderers.main.SourceAreaRenderer;
import gui.panel.ecalendar.frames.renderers.main.ValueRenderer;
import gui.panel.ecalendar.frames.util.FilterSelector;
import gui.panel.ecalendar.frames.util.ShowAndHidePopupManager;
import gui.panel.ecalendar.frames.util.SortHandler;
import gui.panel.ecalendar.frames.util.StyleKeeper;
import gui.panel.ecalendar.frames.util.timezone.LocalTimeZone;
import gui.panel.ecalendar.frames.util.timezone.TimeZoneComboModel;
import p.calendar.InfoCalendarAPI;

public class FrameCalendar extends DataFrame {

	public FrameCalendar() {
		render("ecalendar/FrameCalendar", false);
		
		frame.setMinimumSize(frame.getSize());
		frame.setLocationRelativeTo(null);
	}
	

	@Override
	protected void beforeRenderInit() {
		/**
		 * Устанавливаем часовой пояс по умолчанию "Москва"
		 */
		LocalTimeZone.getInstance().setCurrent(TimeZone.getTimeZone("Etc/GMT-3"));
	}

	@Override
	protected void afterRenderInit() {
		showHideManager = new ShowAndHidePopupManager(this);
		timeZoneComboBox.setSelectedItem(TimeZoneComboModel.extractUTC(LocalTimeZone.getInstance().getCurrent()).getViewString());

		initTable();
		initControls();

		/**
		 * Инициализация таймера, который срабатывает раз в минуту
		 * и обновляет значения столбца "Время до события"
		 * PS: так же обновляет текущее время в заголовке фрейма
		 */
		Timer updateTimer = initUpdateTimer();
		updateTimer.start();
		repaintTime();
	}
	

	public void updateModel(List<ExtendCalendarRow> rows) {
		model.setSortColumn(remote.getCurrentSortColumn());
		model.update(rows);

		/**
		 * Первая часть костыля. Исправляет баг со скачущим скроллом после обновлений.
		 * Вторая часть - находится в методе initOtherListeners();
		 */
		prevScrollBarValue = tableScrollPane.getVerticalScrollBar().getValue();
		tableScrollPane.getVerticalScrollBar().setValue(prevScrollBarValue);
	}

	private void initTable() {
		model = new CalendarTableModel();
		model.setValueStyleKeeper(new StyleKeeper());
		model.setZebraStyleKeeper(new StyleKeeper());

		table.setModel(model);

		header = table.getTableHeader();
		header.setDefaultRenderer(new HeaderRenderer(header));
		header.setReorderingAllowed(false);
		header.setCursor(new Cursor(Cursor.HAND_CURSOR));

		eventAreaRenderer = new EventAreaRenderer(model);

		table.setDefaultRenderer(JLabel.class, new LabelRenderer(model));
		table.setDefaultRenderer(Image.class, new CountryRenderer(model));
		table.setDefaultRenderer(ImageIcon.class, new ImportanceRenderer(model));
		table.setDefaultRenderer(JTextArea.class, eventAreaRenderer);
		table.setDefaultRenderer(Integer.class, new ValueRenderer(model));
		table.setDefaultRenderer(Icon.class, new NotesRenderer(model));
		table.setDefaultRenderer(Boolean.class, new SourceAreaRenderer(model));

		for (int i = 0; i < model.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setResizable(false);
		}

		remote = new ExtendFilterRemoteService(this, true);
		remote.sendFirstRequest();
	}

	private void initControls() {
		initCommonListeners();
		initPopupListeners();
		initOtherListeners();
		initOptionsPanelListeners();

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					/*
					 * Вызов контекстного меню таблицы (TABLE POPUP).
					 */
					columnIndex = getColumnByPoint(e);
					if (filter.isFilterColumn(columnIndex)) {
						tablePopup.show(table, e.getX() + 10, e.getY() + 10);
					}
				} else if (SwingUtilities.isLeftMouseButton(e)) {
					if (e.getClickCount() == 2) {
						/*
						 * Отображение формы "Подробности события" (с графиком).
						 * Вызывается по двойному клику на строку события.
						 */
						showDetails(e);
					}
				}
			}
		});

		header.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				columnIndex = getColumnByPoint(e);

				if (SwingUtilities.isRightMouseButton(e)) {
					/*
					 * Вызов контекстного меню заголовка таблицы (HEADER POPUP).
					 */
					boolean isFilterColumn = filter.isFilterColumn(columnIndex);
					setFilterHeaderItem.setVisible(isFilterColumn);

					headerPopup.show(header, e.getX() + 10, e.getY() + 10);
				} else if (SwingUtilities.isLeftMouseButton(e)) {
					if (e.getClickCount() == 2) {
						/*
						 * Сортировка по столбцу.
						 * Вызывается по двойному клику на заголовок столбца.
						 */
						doSort();
					}
				}
			}
		});

		macroShowBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				/*
				 * Клик по кнопке "Макростатистика-поиск"
				 */
				new MacroFilter(remote).show();
			}
		});
	}

	private void initPopupListeners() {
		doSortHeaderItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * Пункт контекстного меню "Сортировать"
				 */
				doSort();
			}
		});

		hideColumnHeaderItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				/*
				 * Пункт контекстного меню "Скрыть столбец"
				 */
				hideColumn(columnIndex);
			}
		});

		ActionListener filterActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				/*
				 * Пункт контекстного меню "Установить фильтр"
				 */
				filter.selectFilter(columnIndex, remote);
			}
		};

		setFilterTableItem.addActionListener(filterActionListener);
		setFilterHeaderItem.addActionListener(filterActionListener);
	}

	private void initOtherListeners() {
		timeZoneComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox box = (JComboBox) e.getSource();
				TimeZoneComboModel comboModel = (TimeZoneComboModel) box.getModel();
				TimeZone timeZone = comboModel.getTimeZone(box.getSelectedIndex());
				if (timeZone != null) {
					LocalTimeZone.getInstance().setCurrent(timeZone);
					repaintTime();
				}
			}
		});

		/**
		 * Нужeн для исправления ошибки "мерцания" строки при перерисовке
		 * А так же для исправления ошибки при изменении размера таблицы.
		 */
		timer = new Timer(1 * 1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eventAreaRenderer.setResize(false);
				timer.stop();
			}
		});

		ComponentListener resizeListener = new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				setStaticColumnWidth(JCalendarTableView.DATE, 70);
				setStaticColumnWidth(JCalendarTableView.TIME, 60);
				setStaticColumnWidth(JCalendarTableView.TIME_LEFT, 70);
				setStaticColumnWidth(JCalendarTableView.COUNTRY, 50);
				setStaticColumnWidth(JCalendarTableView.IMPORTANCE, 60);
				setStaticColumnWidth(JCalendarTableView.CATEGORY, 130);

				setStaticColumnWidth(JCalendarTableView.VALUE, 80);
				setStaticColumnWidth(JCalendarTableView.VALUE_NOTE, 30);
				setStaticColumnWidth(JCalendarTableView.FORECAST, 80);
				setStaticColumnWidth(JCalendarTableView.FORECAST_NOTE, 30);
				setStaticColumnWidth(JCalendarTableView.PREV_VALUE, 80);
				setStaticColumnWidth(JCalendarTableView.PREV_VALUE_NOTE, 30);

				eventAreaRenderer.setResize(true);
				timer.start();
				table.repaint();
			}
		};

		rootPanel.addComponentListener(resizeListener);

		/**
		 * Вторая часть костыля, который исправляет баг со скачущим скроллом.
		 * Первая часть - находится в методе updateModel();
		 */
		tableScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				prevScrollBarValue = tableScrollPane.getVerticalScrollBar().getValue();
			}
		});
	}

	private void initOptionsPanelListeners() {
		MouseListener hideOptionsPanelListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					/*
					 * Вызов контекстного меню панели настроек
					 */
					optionsPopup.show(e.getComponent(), e.getX() + 10, e.getY() + 10);
				}
			}
		};
		optionsPanel.addMouseListener(hideOptionsPanelListener);
		macroShowBtn.addMouseListener(hideOptionsPanelListener);
		timeZoneComboBox.addMouseListener(hideOptionsPanelListener);

		hideOptionsPanelMenuItem.addActionListener(new ActionListener() {
			/*
			 * Пункт контекстного меню "Скрыть панель настроек"
			 */
			public void actionPerformed(ActionEvent e) {
				optionsPanel.setVisible(false);
				showOptionsPanelMenuItem.setVisible(true);
			}
		});

		showOptionsPanelMenuItem.addActionListener(new ActionListener() {
			/*
			 * Пункт контекстного меню "Показать панель настроек"
			 */
			public void actionPerformed(ActionEvent e) {
				optionsPanel.setVisible(true);
				showOptionsPanelMenuItem.setVisible(false);
			}
		});
	}

	private void setStaticColumnWidth(int column, int width) {
		if (table.getColumnModel().getColumn(column).getMaxWidth() > 0) {
			table.getColumnModel().getColumn(column).setMaxWidth(width);
			table.getColumnModel().getColumn(column).setMinWidth(width);
			table.getColumnModel().getColumn(column).setWidth(width);
		}
	}

	private void showDetails(MouseEvent e) {
		List<ExtendCalendarRow> rows = model.getRows();
		int selectedRow = getRowByPoint(e);
		if (rows != null) {
			if (selectedRow < rows.size()) {
				new FrameDetails(rows.get(selectedRow)).show();
			}
		}
	}

	private void doSort() {
		if (sorter.isSortColumn(columnIndex)) {
			InfoCalendarAPI.DIRECTION direction = sorter.getDirection(columnIndex);
			InfoCalendarAPI.COLUMN sortColumn = sorter.getSortColumn(columnIndex);
			remote.sendSortRequest(sortColumn, direction);
		}
	}

	private int getColumnByPoint(MouseEvent e) {
		Point p = e.getPoint();
		return table.columnAtPoint(p);
	}

	private int getRowByPoint(MouseEvent e) {
		Point p = e.getPoint();
		return table.rowAtPoint(p);
	}

	public void showColumn(int index) {
		table.getColumnModel().getColumn(index).setMaxWidth(Integer.MAX_VALUE);
		table.getColumnModel().getColumn(index).setMinWidth(15);
		showHideManager.getItemList().get(index).setVisible(false);
		table.revalidate();
	}

	private void hideColumn(int index) {
		table.getColumnModel().getColumn(index).setMinWidth(0);
		table.getColumnModel().getColumn(index).setMaxWidth(0);
		table.getColumnModel().getColumn(index).setWidth(0);
		showHideManager.getItemList().get(index).setVisible(true);
		table.revalidate();
	}

	public JTable getTable() {
		return table;
	}

	public JFrame getFrame() {
		return frame;
	}

	private Timer initUpdateTimer() {
		Timer updateTimer = new Timer(5 * 1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				repaintTime();
			}
		});
		return updateTimer;
	}

	private void repaintTime() {
		//временно (для отладки)
		if (frame != null) {
			Calendar currentTime = Calendar.getInstance(LocalTimeZone.getInstance().getCurrent());
			frame.setTitle("Экономический календарь: " + Converter.extractTime(currentTime, false));
		}

		model.updateTime();
		table.repaint();
	}

	//main
	private final SortHandler sorter = new SortHandler();
	private ShowAndHidePopupManager showHideManager;
	private final FilterSelector filter = new FilterSelector();

	private Timer timer;
	private int columnIndex;
	private CalendarTableModel model;
	private EventAreaRenderer eventAreaRenderer;
	private int prevScrollBarValue = 0;

	// view
	private JPanel rootPanel;

	private JPanel optionsPanel;
	private JPopupMenu optionsPopup;
	private JMenuItem hideOptionsPanelMenuItem;
	private JMenuItem showOptionsPanelMenuItem;
	private JComboBox timeZoneComboBox;
	private JButton macroShowBtn;

	private JTable table;
	private JTableHeader header;

	private JPopupMenu tablePopup;
	private JMenuItem setFilterTableItem;

	private JPopupMenu headerPopup;
	private JMenuItem doSortHeaderItem;
	private JMenuItem setFilterHeaderItem;
	private JMenuItem hideColumnHeaderItem;

	// menu items
	public JMenuItem showDateColumnHeaderItem;
	public JMenuItem showTimeColumnHeaderItem;
	public JMenuItem showTimeLeftColumnHeaderItem;
	public JMenuItem showCountryColumnHeaderItem;
	public JMenuItem showImportanceColumnHeaderItem;
	public JMenuItem showEventColumnHeaderItem;
	public JMenuItem showCategoryColumnHeaderItem;
	public JMenuItem showValueColumnHeaderItem;
	public JMenuItem showValueNoteColumnHeaderItem;
	public JMenuItem showForecastColumnHeaderItem;
	public JMenuItem showForecastNoteColumnHeaderItem;
	public JMenuItem showPrevValueColumnHeaderItem;
	public JMenuItem showPrevValueNoteColumnHeaderItem;
	public JMenuItem showSourceColumnHeaderItem;
}