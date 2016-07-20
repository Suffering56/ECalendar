package gui.panel.ecalendar.frames.renderers.main;

import java.awt.Component;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import gui.panel.ecalendar.data.CalendarTableModel;
import gui.panel.ecalendar.data.Converter;
import gui.panel.ecalendar.styles.RendererStyles;
import p.calendar.data.CalendarRow.IMPORTANCE;

@SuppressWarnings("serial")
public class ImportanceRenderer extends JLabel implements TableCellRenderer {

	public ImportanceRenderer(CalendarTableModel model) {
		this.model = model;

		this.setName(RendererStyles.IMPORTANCE);
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		this.setText(null);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		if (value == null) {
			value = "null";
		}

		String text = value.toString();

		StringBuilder path = new StringBuilder();

		boolean isNullResource = false;

		if (text.equals(Converter.translateImportance(IMPORTANCE.HIGH))) {
			path.append("/configs/GRAPHITE/style/img/ecalendar/importance/high_importance.png");
		} else if (text.equals(Converter.translateImportance(IMPORTANCE.MEDIUM))) {
			path.append("/configs/GRAPHITE/style/img/ecalendar/importance/medium_importance.png");
		} else if (text.equals(Converter.translateImportance(IMPORTANCE.LOW))) {
			path.append("/configs/GRAPHITE/style/img/ecalendar/importance/low_importance.png");
		} else {
			isNullResource = true;
		}

		if (!isNullResource) {
			URL resource = getClass().getResource(path.toString());
			this.setIcon(new ImageIcon(resource));
		} else {
			this.setIcon(null);
		}

		this.setToolTipText(text);
		
		this.setName(RendererStyles.IMPORTANCE);
		model.getZebraStyleKeeper().useZebraStyle(this, row);

		if (model.getRows().get(row).isUpdated()) {
			this.setName(RendererStyles.IMPORTANCE + RendererStyles.UPDATE_STYLE_PREFIX);
		}

		return this;
	}

	private final CalendarTableModel model;
}
