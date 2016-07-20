package gui.panel.ecalendar.data;

import java.util.HashMap;
import java.util.Map;

public class JDetailsTableView {

	public static Object getValueAt(ExtendCalendarRow extRow, int columnIndex) {
		switch (columnIndex) {
		case DATE:
			return extRow.getDate();
		case TIME:
			return extRow.getTime();
		case VALUE:
			return extRow.getValue();
		case FORECAST:
			return extRow.getForecast();
		case PREV_VALUE:
			return extRow.getPrevValue();
		default:
			return "";
		}
	}

	// columns
	public static final int DATE = 0;
	public static final int TIME = 1;
	public static final int VALUE = 2;
	public static final int FORECAST = 3;
	public static final int PREV_VALUE = 4;

	// column headings
	public static final Map<Integer, String> columnHeadingsMap = new HashMap<Integer, String>();
	static {
		columnHeadingsMap.put(DATE, "<html><p align='center'>Дата выпуска</p></html>");
		columnHeadingsMap.put(TIME, "<html><p align='center'>Время</p></html>");
		columnHeadingsMap.put(VALUE, "<html><p align='center'>Факт.</p></html>");
		columnHeadingsMap.put(FORECAST, "<html><p align='center'>Прогноз</p></html>");
		columnHeadingsMap.put(PREV_VALUE, "<html><p align='center'>Пред.</p></html>");
	}
}
