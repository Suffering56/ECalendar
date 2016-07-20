package gui.panel.ecalendar.frames.renderers.main;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

import gui.panel.ecalendar.data.CalendarTableModel;
import gui.panel.ecalendar.styles.RendererStyles;

public class EventAreaRenderer extends JTextArea implements TableCellRenderer {

	public EventAreaRenderer(CalendarTableModel model) {
		this.model = model;

		this.setName(RendererStyles.EVENT);
		this.setLineWrap(true);
		this.setWrapStyleWord(true);
		this.setBorder(BorderFactory.createEmptyBorder(6, 5, 6, 5));
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		this.setText((value == null) ? "" : value.toString());
		this.setSize(table.getColumnModel().getColumn(column).getWidth(),
				getPreferredSize().height);

		/*
		 * Проверяет, не спрятан ли данный столбец.
		 * Эта проверка устраняет ошибку, когда высота спрятанных столбцов была слишком большой
		 */
		if (table.getColumnModel().getColumn(column).getMaxWidth() > 0) {
			/*
			 * Данная проверка устраняет баг, когда, при изменении размера формы некорректно устанавливалась высота строки таблицы
			 * Установкой параметра resize управляет событие FrameCalendar -> frame -> componentListener -> componentResized
			 */
			if (!resize) {
				if (table.getRowHeight(row) < this.getPreferredSize().height) {
					table.setRowHeight(row, this.getPreferredSize().height + INSETS);
				}
			} else {
				table.setRowHeight(row, this.getPreferredSize().height + INSETS);
			}
		}
		
		this.setName(RendererStyles.EVENT);
		model.getZebraStyleKeeper().useZebraStyle(this, row);

		if (model.getRows().get(row).isUpdated()) {
			this.setName(RendererStyles.EVENT + RendererStyles.UPDATE_STYLE_PREFIX);
		}

		return this;
	}

	public void setResize(boolean resize) {
		this.resize = resize;
	}

	protected final CalendarTableModel model;
	private static final int INSETS = 14;
	private boolean resize = false;
}
