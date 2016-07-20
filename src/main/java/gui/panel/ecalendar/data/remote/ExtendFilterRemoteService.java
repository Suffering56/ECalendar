package gui.panel.ecalendar.data.remote;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import gui.panel.ecalendar.frames.parents.DataFrame;
import gui.panel.ecalendar.frames.util.timezone.LocalTimeZone;
import p.calendar.InfoCalendarAPI;
import p.calendar.InfoCalendarAPI.COLUMN;
import p.calendar.InfoCalendarAPI.DIRECTION;

public class ExtendFilterRemoteService extends FilterRemoteService {

	public ExtendFilterRemoteService(DataFrame parent, boolean update) {
		super(parent, update);
		clearRequestParameters();
	}

	/**
	 * Очищает все параметры фильтрации и сортировки.
	 * Приводит все формы фильтрации к первоначальному виду.
	 */
	public void clearRequestParameters() {
		importanceStateMap.clear();
		countryStateMap.clear();
		clearColumnFiltersList();

		currentStartDate.setTime(Calendar.getInstance().getTime());
		currentEndDate.setTime(Calendar.getInstance().getTime());

		currentStartDate.add(Calendar.WEEK_OF_YEAR, -1);
		currentEndDate.add(Calendar.WEEK_OF_YEAR, 1);

		currentFilter = "";
		currentSortColumn = COLUMN.Date;
		currentSortDirection = DIRECTION.dec;
	}

	public Calendar getCurrentStartDate() {
		return currentStartDate;
	}

	public Calendar getCurrentEndDate() {
		return currentEndDate;
	}

	public void setCurrentStartDate(Calendar startDate) {
		this.currentStartDate = startDate;
	}

	public void setCurrentEndDate(Calendar endDate) {
		this.currentEndDate = endDate;
	}

	public Map<String, Boolean> getImportanceStateMap() {
		return importanceStateMap;
	}

	public Map<String, Boolean> getCountryStateMap() {
		return countryStateMap;
	}

	public Map<String, Boolean> getCategoryStateMap() {
		return categoryStateMap;
	}

	public InfoCalendarAPI.COLUMN getCurrentSortColumn() {
		return currentSortColumn;
	}

	public Calendar getDefaultStartDate() {
		Calendar result = Calendar.getInstance(LocalTimeZone.getInstance().getCurrent());
		result.add(Calendar.WEEK_OF_YEAR, -1);
		return result;
	}

	public Calendar getDefaultEndDate() {
		Calendar result = Calendar.getInstance(LocalTimeZone.getInstance().getCurrent());
		result.add(Calendar.WEEK_OF_YEAR, 1);
		return result;
	}

	private final Map<String, Boolean> importanceStateMap = new HashMap<String, Boolean>();
	private final Map<String, Boolean> countryStateMap = new HashMap<String, Boolean>();
	private final Map<String, Boolean> categoryStateMap = new HashMap<String, Boolean>();
}