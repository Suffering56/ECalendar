package gui.panel.ecalendar.data;

import java.awt.Image;
import java.util.Calendar;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.table.AbstractTableModel;

import gui.panel.ecalendar.frames.util.StyleKeeper;
import gui.panel.ecalendar.frames.util.timezone.LocalTimeZone;
import gui.panel.ecalendar.styles.RendererStyles;
import p.calendar.InfoCalendarAPI;

public class CalendarTableModel extends AbstractTableModel {

	public String getColumnName(int columnIndex) {
		return JCalendarTableView.columnHeadingsMap.get(columnIndex);
	}

	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == JCalendarTableView.COUNTRY) {
			return Image.class;
		} else if (columnIndex == JCalendarTableView.IMPORTANCE) {
			return ImageIcon.class;
		} else if (columnIndex == JCalendarTableView.EVENT) {
			return JTextArea.class;
		} else if ((columnIndex == JCalendarTableView.VALUE_NOTE) || (columnIndex == JCalendarTableView.FORECAST_NOTE)
				|| (columnIndex == JCalendarTableView.PREV_VALUE_NOTE)) {
			return Icon.class;
		} else if (columnIndex == JCalendarTableView.VALUE) {
			return Integer.class;
		} else if (columnIndex == JCalendarTableView.SOURCE) {
			return Boolean.class;
		} else {
			return JLabel.class;
		}
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		int length = (rows == null) ? 0 : rows.size();
		if (rowIndex < length) {
			return JCalendarTableView.getValueAt(rows.get(rowIndex), columnIndex);
		} else {
			return null;
		}
	}

	public int getColumnCount() {
		return JCalendarTableView.columnHeadingsMap.size();
	}

	public int getRowCount() {
		return (rows == null) ? 0 : rows.size();
	}

	public void update(List<ExtendCalendarRow> rows) {
		this.rows = rows;

		valueStyleKeeper.setRows(rows);
		updateColor();

		if (sortColumn == InfoCalendarAPI.COLUMN.Date) {
			zebraStyleKeeper.setRows(rows);
			updateZebra();
		} else {
			zebraStyleKeeper.clear();
		}

		fireTableDataChanged();
	}

	private void updateColor() {
		for (ExtendCalendarRow extRow : rows) {
			long id = extRow.getId();
			String value = extRow.getValue();
			String prevValue = extRow.getPrevValue();

			if (!value.equals("") && !prevValue.equals("")) {
				value = Converter.excludePercent(value);
				prevValue = Converter.excludePercent(prevValue);

				if (Float.valueOf(value) > Float.valueOf(prevValue)) {
					valueStyleKeeper.addStyle(id, RendererStyles.VALUE_GREEN);
				} else if (Float.valueOf(value) < Float.valueOf(prevValue)) {
					valueStyleKeeper.addStyle(id, RendererStyles.VALUE_RED);
				}
			}
		}
	}

	private void updateZebra() {
		Calendar currentDate = Calendar.getInstance(LocalTimeZone.getInstance().getCurrent());
		Calendar previousDate = Calendar.getInstance(LocalTimeZone.getInstance().getCurrent());

		boolean firstRound = true;

		String firstStylePrefix = "";
		String secondStylePrefix = RendererStyles.ZEBRA_STYLE_PREFIX;
		String currentPrefix = firstStylePrefix;

		for (ExtendCalendarRow extRow : rows) {
			long id = extRow.getId();
			currentDate.setTime(extRow.getPrimaryDate());

			if (firstRound) {
				previousDate.setTime(currentDate.getTime());
				firstRound = false;
			}

			boolean equals = compareDates(currentDate, previousDate);

			if (!equals) {
				if (currentPrefix.equals(firstStylePrefix)) {
					currentPrefix = secondStylePrefix;
				} else {
					currentPrefix = firstStylePrefix;
				}
			}

			zebraStyleKeeper.addStyle(id, currentPrefix);

			previousDate.setTime(currentDate.getTime());
		}
	}

	private boolean compareDates(Calendar currentDate, Calendar previousDate) {
		if (currentDate.get(Calendar.DAY_OF_MONTH) == previousDate.get(Calendar.DAY_OF_MONTH)) {
			if (currentDate.get(Calendar.MONTH) == previousDate.get(Calendar.MONTH)) {
				if (currentDate.get(Calendar.YEAR) == previousDate.get(Calendar.YEAR)) {
					return true;
				}
			}
		}
		return false;
	}

	public void updateTime() {
		if (rows != null) {
			for (ExtendCalendarRow row : rows) {
				row.setDate(Converter.getDate(row.getPrimaryDate()));
				row.setTime(Converter.getTime(row.getPrimaryDate()));
				row.setTimeLeft(Converter.getTimeLeft(row.getPrimaryDate()));
			}
			zebraStyleKeeper.clear();
			updateZebra();
		}
	}

	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		// do nothing
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public List<ExtendCalendarRow> getRows() {
		return rows;
	}

	public StyleKeeper getValueStyleKeeper() {
		return valueStyleKeeper;
	}

	public void setValueStyleKeeper(StyleKeeper valueStyleKeeper) {
		this.valueStyleKeeper = valueStyleKeeper;
	}

	public StyleKeeper getZebraStyleKeeper() {
		return zebraStyleKeeper;
	}

	public void setZebraStyleKeeper(StyleKeeper zebraStyleKeeper) {
		this.zebraStyleKeeper = zebraStyleKeeper;
	}

	public void setSortColumn(InfoCalendarAPI.COLUMN sortColumn) {
		this.sortColumn = sortColumn;
	}

	private List<ExtendCalendarRow> rows;

	private StyleKeeper valueStyleKeeper;
	private StyleKeeper zebraStyleKeeper;

	private InfoCalendarAPI.COLUMN sortColumn;
}