package gui.panel.ecalendar.frames.renderers.main;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import gui.panel.ecalendar.data.CalendarTableModel;
import gui.panel.ecalendar.styles.RendererStyles;

public class ValueRenderer extends JLabel implements TableCellRenderer {

	public ValueRenderer(CalendarTableModel model) {
		this.model = model;
		
		this.setName(RendererStyles.VALUE_DEFAULT);
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		setText((value == null) ? "" : value.toString());
		
		this.setName(RendererStyles.VALUE_DEFAULT);
		model.getValueStyleKeeper().useStyle(this, row);

		if (model.getRows().get(row).isUpdated()) {
			this.setName(this.getName() + RendererStyles.UPDATE_STYLE_PREFIX);
		} else {
			model.getZebraStyleKeeper().useZebraStyle(this, row);
		}

		return this;
	}

	private final CalendarTableModel model;
}
