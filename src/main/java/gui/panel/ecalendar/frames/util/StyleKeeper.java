package gui.panel.ecalendar.frames.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import gui.panel.ecalendar.data.ExtendCalendarRow;

public class StyleKeeper {

	public void useStyle(JComponent component, int rowIndex) {
		if (contains(rowIndex)) {
			component.setName(getStyle(rowIndex));
		}
	}

	public void useZebraStyle(JComponent component, int rowIndex) {
		if (contains(rowIndex)) {
			component.setName(component.getName() + getStyle(rowIndex));
		}
	}

	public void addStyle(long id, String newStyle) {
		stylesMap.put(id, newStyle);
	}

	public String getStyle(int rowIndex) {
		long id = rows.get(rowIndex).getId();
		return stylesMap.get(id);
	}

	public boolean contains(int rowIndex) {
		if ((rows != null) && (!rows.isEmpty())) {
			if (rowIndex < rows.size()) {
				long id = rows.get(rowIndex).getId();
				return stylesMap.containsKey(id);
			}
		}
		return false;
	}

	public void setRows(List<ExtendCalendarRow> rows) {
		clear();
		this.rows = rows;
	}

	public void clear() {
		stylesMap.clear();
	}

	private List<ExtendCalendarRow> rows;
	private final Map<Long, String> stylesMap = new HashMap<Long, String>();
}
