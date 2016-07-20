package gui.panel.ecalendar.frames.filters;

import javax.swing.JCheckBox;

import gui.panel.ecalendar.data.remote.RemoteService;
import gui.panel.ecalendar.frames.parents.ExtendFilterFrame;
import p.calendar.InfoCalendarAPI.COLUMN;
import p.calendar.SearchFilter;
import p.calendar.data.CalendarRow.IMPORTANCE;

public class FilterImportance extends ExtendFilterFrame<IMPORTANCE> {
	public FilterImportance(RemoteService remote) {
		super(remote);
		stateMap = remote.getImportanceStateMap();
		
		render("ecalendar/filters/FilterImportance");
	}
	
	@Override
	protected void beforeRenderInit() {
		//do nothing
	}

	@Override
	protected void afterRenderInit() {
		initNoHandleComponents(enableCheckBox);
		init();
	}

	protected void singleInit(JCheckBox box) {
		String name = box.getName();
		if (!stateMap.containsKey(name)) {
			stateMap.put(name, true);
		}
		box.setSelected(stateMap.get(name));
	}

	protected void addFilterCriteron(JCheckBox box) {
		IMPORTANCE imp = null;
		try {
			imp = IMPORTANCE.valueOf(box.getName());
		} catch (IllegalArgumentException ex) {
			imp = null;
		} finally {
			if (imp != null) {
				filterCollection.add(imp);
			}
		}
	}

	protected void sendRequest() {
		String newFilter = "";
		if (enableCheckBox.isSelected()) {
			newFilter = SearchFilter.Importance_equals_(filterCollection);
		}
		remote.sendFilterRequest(COLUMN.Importance, newFilter);
	}
}
