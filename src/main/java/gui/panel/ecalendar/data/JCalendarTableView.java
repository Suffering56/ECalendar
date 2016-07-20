package gui.panel.ecalendar.data;

import java.util.HashMap;
import java.util.Map;

public class JCalendarTableView {

	public static Object getValueAt(ExtendCalendarRow extRow, int columnIndex) {
		switch (columnIndex) {
		case DATE:
			return extRow.getDate();
		case TIME:
			return extRow.getTime();
		case TIME_LEFT:
			return extRow.getTimeLeft();
		case COUNTRY:
			return extRow.getCountry();
		case IMPORTANCE:
			return extRow.getImportance();
		case EVENT:
			return extRow.getEvent();
		case CATEGORY:
			return extRow.getCategory();
		case VALUE:
			return extRow.getValue();
		case VALUE_NOTE:
			return extRow.getValueNote();
		case FORECAST:
			return extRow.getForecast();
		case FORECAST_NOTE:
			return extRow.getForecastNote();
		case PREV_VALUE:
			return extRow.getPrevValue();
		case PREV_VALUE_NOTE:
			return extRow.getPrevValueNote();
		case SOURCE:
			return extRow.getSource();
		default:
			return "";
		}
	}

	// columns
	public static final int DATE = 0;
	public static final int TIME = 1;
	public static final int TIME_LEFT = 2;
	public static final int COUNTRY = 3;
	public static final int IMPORTANCE = 4;
	public static final int EVENT = 5;
	public static final int CATEGORY = 6;
	public static final int VALUE = 7;
	public static final int VALUE_NOTE = 8;
	public static final int FORECAST = 9;
	public static final int FORECAST_NOTE = 10;
	public static final int PREV_VALUE = 11;
	public static final int PREV_VALUE_NOTE = 12;
	public static final int SOURCE = 13;

	// column headings
	public static final Map<Integer, String> columnHeadingsMap = new HashMap<Integer, String>();
	static {
		columnHeadingsMap.put(DATE, "<html><p align='center'>Дата<br/>события</p></html>");
		columnHeadingsMap.put(TIME, "<html><p align='center'>Время<br/>события</p></html>");
		columnHeadingsMap.put(TIME_LEFT, "<html><p align='center'>Время до<br/>события</p></html>");		
		columnHeadingsMap.put(COUNTRY, "<html><p align='center'>Страна</p></html>"); 
		columnHeadingsMap.put(IMPORTANCE, "<html><p align='center'>Важность</p></html>");	
		columnHeadingsMap.put(EVENT, "<html><p align='center'>Событие</p></html>");	
		columnHeadingsMap.put(CATEGORY, "<html><p align='center'>Категория</p></html>");	
		columnHeadingsMap.put(VALUE, "<html><p align='center'>Фактическое<br/>значение</p></html>");
		columnHeadingsMap.put(VALUE_NOTE, "<html><p align='center'></p></html>");	
		columnHeadingsMap.put(FORECAST, "<html><p align='center'>Прогноз</p></html>");	
		columnHeadingsMap.put(FORECAST_NOTE, "<html><p align='center'></p></html>");
		columnHeadingsMap.put(PREV_VALUE, "<html><p align='center'>Предыдущее<br/>значение</p></html>");
		columnHeadingsMap.put(PREV_VALUE_NOTE, "<html><p align='center'></p></html>");
		columnHeadingsMap.put(SOURCE, "<html><p align='center'>Источник</p></html>");	
	}
}
