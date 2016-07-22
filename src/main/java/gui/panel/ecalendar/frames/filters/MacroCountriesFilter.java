package gui.panel.ecalendar.frames.filters;

import gui.panel.ecalendar.data.remote.RemoteService;
import gui.panel.ecalendar.frames.parents.Enablable;
import p.calendar.InfoCalendarAPI.COLUMN;
import p.calendar.SearchFilter;

public class MacroCountriesFilter extends FilterCountries {
	public MacroCountriesFilter(Enablable enablable, RemoteService remote) {
		super(enablable, remote);
	}

	@Override
	protected void sendRequest() {
		String newFilter = "";
		if (enableCheckBox.isSelected()) {
			newFilter = SearchFilter.Country_equals_(filterCollection);
		}
		remote.saveFilterState(COLUMN.Country, newFilter);
	}
}
