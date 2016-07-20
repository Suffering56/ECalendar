package gui.panel.ecalendar.frames.filters;

import javax.swing.JCheckBox;

import gui.panel.ecalendar.data.remote.RemoteService;
import gui.panel.ecalendar.frames.parents.AbstractDateFilter;

public class FilterDate extends AbstractDateFilter {

	public FilterDate(RemoteService remote) {
		super(remote, "ecalendar/filters/FilterDate");
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
