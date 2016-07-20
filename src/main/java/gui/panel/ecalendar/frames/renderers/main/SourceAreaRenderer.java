package gui.panel.ecalendar.frames.renderers.main;

import java.awt.Component;

import javax.swing.JTable;

import gui.panel.ecalendar.data.CalendarTableModel;
import gui.panel.ecalendar.styles.RendererStyles;

public class SourceAreaRenderer extends EventAreaRenderer {

	public SourceAreaRenderer(CalendarTableModel model) {
		super(model);
		this.setName(RendererStyles.SOURCE);
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
			if (table.getRowHeight(row) < this.getPreferredSize().height) {
				table.setRowHeight(row, this.getPreferredSize().height);
			}
		}
		
		this.setName(RendererStyles.SOURCE);
		model.getZebraStyleKeeper().useZebraStyle(this, row);

		if (model.getRows().get(row).isUpdated()) {
			this.setName(RendererStyles.SOURCE + RendererStyles.UPDATE_STYLE_PREFIX);
		}

		return this;
	}
}
