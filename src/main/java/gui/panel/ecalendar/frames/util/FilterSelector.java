package gui.panel.ecalendar.frames.util;

import gui.panel.ecalendar.data.JCalendarTableView;
import gui.panel.ecalendar.data.remote.RemoteService;
import gui.panel.ecalendar.frames.filters.FilterCategory;
import gui.panel.ecalendar.frames.filters.FilterCountries;
import gui.panel.ecalendar.frames.filters.FilterDate;
import gui.panel.ecalendar.frames.filters.FilterImportance;

public class FilterSelector {

	public void selectFilter(int columnIndex, RemoteService remote) {
		switch (columnIndex) {
		case JCalendarTableView.DATE: {
			new FilterDate(remote).show();
		}
			break;
		case JCalendarTableView.COUNTRY: {
			new FilterCountries(remote).show();
		}
			break;
		case JCalendarTableView.IMPORTANCE: {
			new FilterImportance(remote).show();
		}
			break;
		case JCalendarTableView.CATEGORY: {
			new FilterCategory(remote).show();
		}
			break;
		default:
			break;
		}
	}

	public boolean isFilterColumn(int columnIndex) {
		switch (columnIndex) {
		case JCalendarTableView.DATE:
			return true;
		case JCalendarTableView.COUNTRY:
			return true;
		case JCalendarTableView.IMPORTANCE:
			return true;
		case JCalendarTableView.CATEGORY:
			return true;
		default:
			return false;
		}
	}
}
