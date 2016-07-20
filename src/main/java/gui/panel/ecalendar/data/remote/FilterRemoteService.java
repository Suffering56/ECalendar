
package gui.panel.ecalendar.data.remote;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import gui.panel.ecalendar.frames.parents.DataFrame;
import gui.panel.ecalendar.frames.util.timezone.LocalTimeZone;
import p.calendar.InfoCalendarAPI;
import p.calendar.InfoCalendarAPI.COLUMN;
import p.calendar.SearchFilter;

abstract public class FilterRemoteService extends DefaultRemoteService {

	public FilterRemoteService(DataFrame parent, boolean update) {
		super(parent, update);
		currentStartDate = Calendar.getInstance(LocalTimeZone.getInstance().getCurrent());
		currentEndDate = Calendar.getInstance(LocalTimeZone.getInstance().getCurrent());
		LocalTimeZone.getInstance().addListener(currentStartDate);
		LocalTimeZone.getInstance().addListener(currentEndDate);
	}

	/**
	 * Очищает параметры фильтрации по каждому столбцу.
	 */
	protected void clearColumnFiltersList() {
		columnFiltersList.clear();
		while (columnFiltersList.size() < 8) {
			columnFiltersList.add("");
		}
	}

	public void sendFirstRequest() {
		mainRequest(currentStartDate, currentEndDate, currentFilter, currentSortColumn, currentSortDirection);
	}

	public void sendDateRequest(Calendar startDate, Calendar endDate) {
		mainRequest(startDate, endDate, currentFilter, currentSortColumn, currentSortDirection);
	}

	public void sendFilterRequest(InfoCalendarAPI.COLUMN filterColumn, String newFilter) {
		updateFilter(filterColumn, newFilter);
		String filter = SearchFilter.aggregateFilters(columnFiltersList);
		mainRequest(currentStartDate, currentEndDate, filter, currentSortColumn, currentSortDirection);
	}
	
	public void saveFilterState(InfoCalendarAPI.COLUMN filterColumn, String newFilter) {
		updateFilter(filterColumn, newFilter);
		String filter = SearchFilter.aggregateFilters(columnFiltersList);
		currentFilter = filter;
	}

	public void sendSortRequest(InfoCalendarAPI.COLUMN sortColumn, InfoCalendarAPI.DIRECTION sortDirection) {
		mainRequest(currentStartDate, currentEndDate, currentFilter, sortColumn, sortDirection);
	}

	public void sendDetailsRequest(Calendar startDate, Calendar endDate, String event) {
		String filter = SearchFilter.Event_equals_(event);
		mainRequest(startDate, endDate, filter, currentSortColumn, currentSortDirection);
	}

	protected void saveRequestState(Calendar startDate, Calendar endDate, String filter, InfoCalendarAPI.COLUMN sortColumn,
			InfoCalendarAPI.DIRECTION sortDirection) {
		currentStartDate = startDate;
		currentEndDate = endDate;
		currentFilter = filter;
		currentSortColumn = sortColumn;
		currentSortDirection = sortDirection;
	}

	private void updateFilter(COLUMN filterColumn, String newFilter) {
		switch (filterColumn) {
		case Country:
			columnFiltersList.set(0, newFilter);
			break;
		case Importance:
			columnFiltersList.set(1, newFilter);
			break;
		case Event:
			columnFiltersList.set(2, newFilter);
			break;
		case Category:
			columnFiltersList.set(3, newFilter);
			break;
		case Source:
			columnFiltersList.set(4, newFilter);
			break;
		case Value:
			columnFiltersList.set(5, newFilter);
			break;
		case Forecast:
			columnFiltersList.set(6, newFilter);
			break;
		case PervousValue:
			columnFiltersList.set(7, newFilter);
			break;
		default:
			break;
		}
	}

	protected List<String> columnFiltersList = new ArrayList<String>();

	protected Calendar currentStartDate;
	protected Calendar currentEndDate;
	protected String currentFilter;
	protected InfoCalendarAPI.COLUMN currentSortColumn;
	protected InfoCalendarAPI.DIRECTION currentSortDirection;
}
