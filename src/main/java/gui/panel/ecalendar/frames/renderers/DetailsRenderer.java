package gui.panel.ecalendar.frames.renderers;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import gui.panel.ecalendar.styles.RendererStyles;

public class DetailsRenderer extends JLabel implements TableCellRenderer {

	public DetailsRenderer() {
		this.setName(RendererStyles.LABEL);
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		setText((value == null) ? "" : value.toString());

		return this;
	}
}
