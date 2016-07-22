package gui.panel.ecalendar.frames.filters;

import javax.swing.JCheckBox;

import gui.panel.ecalendar.data.remote.RemoteService;
import gui.panel.ecalendar.frames.parents.AbstractDateFilter;
import gui.panel.ecalendar.frames.parents.Enablable;

public class FilterDate extends AbstractDateFilter {

	public FilterDate(Enablable enablable, RemoteService remote) {
		super(enablable, remote, "ecalendar/filters/FilterDate");
	}

	@Override
	protected void applyFilter() {
		if (enableCheckBox.isSelected()) {
			setDayConstraints();
			remote.setCurrentStartDate(startDate);
			remote.setCurrentEndDate(endDate);
			remote.sendDateRequest(startDate, endDate);
		} else {
			remote.sendDateRequest(remote.getDefaultStartDate(), remote.getDefaultEndDate());
		}
	}

	private JCheckBox enableCheckBox;
}
