package gui.panel.ecalendar.data.remote;

import java.util.Calendar;
import java.util.Map;

import p.calendar.InfoCalendarAPI;

public interface RemoteService {

	public void removeSink();

	public void clearRequestParameters();

	public void sendFirstRequest();

	public void sendDateRequest(Calendar startDate, Calendar endDate);

	public void sendFilterRequest(InfoCalendarAPI.COLUMN filterColumn, String newFilter);

	public void sendSortRequest(InfoCalendarAPI.COLUMN sortColumn, InfoCalendarAPI.DIRECTION sortDirection);

	public void sendDetailsRequest(Calendar startDate, Calendar endDate, String event);

	public Calendar getDefaultStartDate();

	public Calendar getCurrentStartDate();

	public void setCurrentStartDate(Calendar startDate);

	public Calendar getDefaultEndDate();

	public Calendar getCurrentEndDate();

	public void setCurrentEndDate(Calendar endDate);

	public Map<String, Boolean> getImportanceStateMap();

	public Map<String, Boolean> getCountryStateMap();

	public Map<String, Boolean> getCategoryStateMap();

	public void saveFilterState(InfoCalendarAPI.COLUMN filterColumn, String newFilter);

	public InfoCalendarAPI.COLUMN getCurrentSortColumn();
}