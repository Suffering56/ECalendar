package gui.panel.ecalendar.frames.renderers.main;

import java.awt.Component;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ToolTipManager;
import javax.swing.table.TableCellRenderer;

import gui.panel.ecalendar.data.CalendarTableModel;
import gui.panel.ecalendar.styles.RendererStyles;

public class NotesRenderer extends JLabel implements TableCellRenderer {

	public NotesRenderer(CalendarTableModel model) {
		this.model = model;

		this.setName(RendererStyles.NOTES);
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		this.setText(null);

		resource = getClass().getResource(path);
		ToolTipManager.sharedInstance().setInitialDelay(100);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		if (value == null) {
			value = "";
		}

		if (!value.toString().equals("")) {
			this.setIcon(new ImageIcon(resource));
			this.setToolTipText(value.toString());
		} else {
			this.setIcon(null);
			this.setToolTipText(null);
		}

		this.setName(RendererStyles.NOTES);
		model.getZebraStyleKeeper().useZebraStyle(this, row);

		if (model.getRows().get(row).isUpdated()) {
			this.setName(RendererStyles.NOTES + RendererStyles.UPDATE_STYLE_PREFIX);
		}

		return this;
	}

	private final CalendarTableModel model;
	private final String path = "/notes/note1.png";
	private URL resource;
}
