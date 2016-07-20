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
import p.calendar.data.CalendarRow.COUNTRY;

@SuppressWarnings("serial")
public class CountryRenderer extends JLabel implements TableCellRenderer {

	public CountryRenderer(CalendarTableModel model) {
		this.model = model;

		this.setName(RendererStyles.COUNTRY);
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
		String tooltipText;

		try {
			COUNTRY c = COUNTRY.valueOf(text);
			tooltipText = Converter.translateCountry(c);
		} catch (IllegalArgumentException ex) {
			tooltipText = text;
			text = COUNTRY.Undefined.toString();
		}

		StringBuilder path = new StringBuilder();
		path.append("/flags_32/");

		StringBuilder reservePath = new StringBuilder(path.toString());

		path.append(text.toLowerCase());
		path.append(".png");

		URL resource = null;

		try {
			resource = getClass().getResource(path.toString());
			if (resource == null) {
				reservePath.append("undefined.png");
				resource = getClass().getResource(reservePath.toString());
			}
		} catch (Exception ex) {
			reservePath.append("undefined.png");
			resource = getClass().getResource(reservePath.toString());
		}

		this.setIcon(new ImageIcon(resource));
		this.setToolTipText(tooltipText);

		
		this.setName(RendererStyles.COUNTRY);
		model.getZebraStyleKeeper().useZebraStyle(this, row);
		
		if (model.getRows().get(row).isUpdated()) {
			this.setName(RendererStyles.COUNTRY + RendererStyles.UPDATE_STYLE_PREFIX);
		}

		return this;
	}
	
	private final CalendarTableModel model;
}
