package gui.panel.ecalendar.frames.filters;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import gui.panel.ecalendar.data.Converter;
import gui.panel.ecalendar.data.remote.RemoteService;
import gui.panel.ecalendar.frames.parents.FilterFrame;
import p.calendar.InfoCalendarAPI.COLUMN;
import p.calendar.SearchFilter;
import p.calendar.data.CalendarRow.CATEGORY;

public class FilterCategory extends FilterFrame {
	public FilterCategory(RemoteService remote) {
		super(remote);
		stateMap = remote.getCategoryStateMap();

		render("ecalendar/filters/FilterCategory");
	}
	
	@Override
	protected void beforeRenderInit() {
		//do nothing
	}

	@Override
	protected void afterRenderInit() {
		globalCheckBox.setSelected(true);
		initContent();
		pack();
	}

	private void initContent() {
		int columnsCount = calculateColumnCount();
		applyLayout(columnsCount);

		int col = 0;
		int row;
		CATEGORY item;
		int currentItemIndex = 0;
		List<CATEGORY> categoriesList = new ArrayList<CATEGORY>();
		categoriesList.addAll(Arrays.asList(CATEGORY.values()));

		for (int i = 0; i < columnsCount; i++) {
			col += 2;
			row = 0;

			for (int j = 0; j < MAX_ROWS; j++) {
				if (currentItemIndex < categoriesLength) {
					item = categoriesList.get(currentItemIndex);
					currentItemIndex++;
					row += 2;
				} else {
					/*
					 * Выходим из цикла, если мы уже
					 * добавили все категории на панель.
					 */
					break;
				}

				String englishText = item.toString();
				String russianText = Converter.translateCategory(item);

				String shortCaption = createShortCaption(russianText);
				String tooltipText = createTooltip(russianText);

				JCheckBox box = new JCheckBox();
				box.setText(shortCaption);
				box.setToolTipText(tooltipText);

				if (!stateMap.containsKey(englishText)) {
					stateMap.put(englishText, true);
				} else {
					/*
					 * Если хотя бы один checkbox не выбран
					 * снимаем флажок "все категории"
					 */
					if (!stateMap.get(englishText)) {
						globalCheckBox.setSelected(false);
					}
				}
				box.setSelected(stateMap.get(englishText));

				boxMap.put(englishText, box);
				contentPanel.add(box, constraints.xy(col, row));
			}
		}
	}

	/**
	 * Вычисляем количество необходимых колонок.
	 */
	private int calculateColumnCount() {
		int columnsCount = categoriesLength / MAX_ROWS;
		int mod = categoriesLength % MAX_ROWS;

		if (mod > 0) {
			columnsCount++;
		}
		return columnsCount;
	}

	/**
	 * Применяем FormLayout (количество колонок и строк
	 * вычисляется динамически)
	 */
	private void applyLayout(int columnsCount) {
		int rowsCount = categoriesLength < MAX_ROWS ? categoriesLength : MAX_ROWS;

		StringBuilder colSpecs = new StringBuilder("10px:g");
		for (int i = 0; i < columnsCount; i++) {
			colSpecs.append(singleColSpec);
		}
		colSpecs.append(":g");

		StringBuilder rowSpecs = new StringBuilder("10px");
		for (int i = 0; i < rowsCount; i++) {
			rowSpecs.append(singleRowSpec);
		}

		FormLayout layout = new FormLayout(colSpecs.toString(), rowSpecs.toString());
		contentPanel.setLayout(layout);
	}

	/**
	 * Извлекаем из категории ее заголовок -
	 * текст, который будет в названии checkbox-а
	 * (это весь текст до первого слэша)
	 */
	private String createShortCaption(String russianText) {
		StringBuilder shortCaption = new StringBuilder();
		for (char c : russianText.toCharArray()) {
			if (c != SLASH) {
				shortCaption.append(c);
			} else {
				break;
			}
		}
		return shortCaption.toString();
	}

	/**
	 * Извлекаем из полного названия категории tooltip
	 * (считаем что слэш (/) - это символ переноса строки)
	 */
	private String createTooltip(String russianText) {
		StringBuilder tooltipText = new StringBuilder("<html>");
		for (char c : russianText.toCharArray()) {
			if (c != SLASH) {
				tooltipText.append(c);
			} else {
				tooltipText.append("<br/>");
			}
		}

		tooltipText.append("</html>");
		return tooltipText.toString();
	}

	/**
	 * Создаем строку для запроса, на основе выбранных checkbox-ов.
	 * И отправляем ее на сервер в качестве параметра запроса.
	 */
	protected void applyFilter() {
		List<CATEGORY> filterCollection = new ArrayList<CATEGORY>();
		
		for (String key : boxMap.keySet()) {
			JCheckBox box = boxMap.get(key);
			
			if (stateMap.containsKey(key)) {
				stateMap.put(key, box.isSelected());
			}

			if (box.isSelected()) {
				CATEGORY cat = null;
				try {
					cat = CATEGORY.valueOf(key);
					filterCollection.add(cat);
				} catch (IllegalArgumentException ex) {
					System.out.println("FilterCategory->applyFilter(). Не удалось преобразовать строку в категорию!");
				}
			}
		}
		
		String newFilter = "";
		if (enableCheckBox.isSelected()) {
			newFilter = SearchFilter.Category_equals_(filterCollection);
		}
		
		sendRequest(newFilter);
	}
	
	protected void sendRequest(String newFilter) {
		remote.sendFilterRequest(COLUMN.Category, newFilter);
	}

	public Action ALL_CATEGORIES_ACTION = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			boolean selected = globalCheckBox.isSelected();
			for (JCheckBox box : boxMap.values()) {
				box.setSelected(selected);
			}
		}
	};

	protected JPanel contentPanel;
	private JCheckBox globalCheckBox;
	protected JCheckBox enableCheckBox;

	private Map<String, JCheckBox> boxMap = new HashMap<String, JCheckBox>();
	private Map<String, Boolean> stateMap = new HashMap<String, Boolean>();

	private final int categoriesLength = CATEGORY.values().length;
	private final CellConstraints constraints = new CellConstraints();

	/**
	 * Отвечает за максимальное количество строк в одной колонке.
	 * (чем меньше значение MAX_ROWS, тем больше будет колонок)
	 */
	private static final int MAX_ROWS = 6;

	private static final char SLASH = '/';
	private static final String singleColSpec = ", f:p, 10px";
	private static final String singleRowSpec = ", f:p, 10px";
}
