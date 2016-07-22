package gui.panel.ecalendar.frames.filters;

import gui.panel.ecalendar.data.remote.RemoteService;
import p.calendar.InfoCalendarAPI.COLUMN;

public class MacroCategoryFilter extends FilterCategory {
	public MacroCategoryFilter(RemoteService remote) {
		super(remote);
	}
	
	protected void sendRequest(String newFilter) {
		remote.saveFilterState(COLUMN.Category, newFilter);
	}
}
