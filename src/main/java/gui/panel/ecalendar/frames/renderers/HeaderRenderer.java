package gui.panel.ecalendar.frames.renderers;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import gui.panel.ecalendar.styles.RendererStyles;

public class HeaderRenderer implements TableCellRenderer {

	public HeaderRenderer(JTableHeader header) {
		header.setName(RendererStyles.HEADER);
		renderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int col) {
		return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
	}

	private DefaultTableCellRenderer renderer;
}
