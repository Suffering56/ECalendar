package gui.panel.ecalendar.frames.filters;

import gui.panel.ecalendar.data.remote.RemoteService;
import gui.panel.ecalendar.frames.parents.Enablable;
import p.calendar.InfoCalendarAPI.COLUMN;

public class MacroCategoryFilter extends FilterCategory {
	public MacroCategoryFilter(Enablable enablable, RemoteService remote) {
		super(enablable, remote);
	}

	protected void sendRequest(String newFilter) {
		remote.saveFilterState(COLUMN.Category, newFilter);
	}
}
