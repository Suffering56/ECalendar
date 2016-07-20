package gui.panel.ecalendar.frames.util;

import gui.panel.ecalendar.data.JCalendarTableView;
import p.calendar.InfoCalendarAPI;

public class SortHandler {
	public SortHandler() {
		clear();
	}

	public void clear() {
		direction = InfoCalendarAPI.DIRECTION.dec;
		prevSortColumn = JCalendarTableView.DATE;
	}

	public InfoCalendarAPI.DIRECTION getDirection(int column) {
		if (column == prevSortColumn) {
			if (direction == InfoCalendarAPI.DIRECTION.asc) {
				direction = InfoCalendarAPI.DIRECTION.dec;
			} else {
				direction = InfoCalendarAPI.DIRECTION.asc;
			}
		} else {
			direction = InfoCalendarAPI.DIRECTION.asc;
			prevSortColumn = column;
		}
		return direction;
	}

	public InfoCalendarAPI.COLUMN getSortColumn(int column) {
		switch (column) {
		case JCalendarTableView.DATE:
			return InfoCalendarAPI.COLUMN.Date;
		case JCalendarTableView.COUNTRY:
			return InfoCalendarAPI.COLUMN.Country;
		case JCalendarTableView.IMPORTANCE:
			return InfoCalendarAPI.COLUMN.Importance;
		case JCalendarTableView.EVENT:
			return InfoCalendarAPI.COLUMN.Event;
		case JCalendarTableView.CATEGORY:
			return InfoCalendarAPI.COLUMN.Category;
		case JCalendarTableView.VALUE:
			return InfoCalendarAPI.COLUMN.Value;
		case JCalendarTableView.FORECAST:
			return InfoCalendarAPI.COLUMN.Forecast;
		case JCalendarTableView.PREV_VALUE:
			return InfoCalendarAPI.COLUMN.PervousValue;
		case JCalendarTableView.SOURCE:
			return InfoCalendarAPI.COLUMN.Source;
		default:
			return InfoCalendarAPI.COLUMN.Date;
		}
	}

	public boolean isSortColumn(int column) {
		switch (column) {
		case JCalendarTableView.DATE:
			return true;
		case JCalendarTableView.COUNTRY:
			return true;
		case JCalendarTableView.IMPORTANCE:
			return true;
		case JCalendarTableView.EVENT:
			return true;
		case JCalendarTableView.CATEGORY:
			return true;
		case JCalendarTableView.VALUE:
			return true;
		case JCalendarTableView.FORECAST:
			return true;
		case JCalendarTableView.PREV_VALUE:
			return true;
		case JCalendarTableView.SOURCE:
			return true;
		default:
			return false;
		}
	}

	private InfoCalendarAPI.DIRECTION direction;
	private int prevSortColumn;
}
