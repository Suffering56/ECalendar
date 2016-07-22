package gui.panel.ecalendar.frames.filters;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBox;

import gui.panel.ecalendar.data.remote.RemoteService;
import gui.panel.ecalendar.frames.parents.Enablable;
import gui.panel.ecalendar.frames.parents.ExtendFilterFrame;
import p.calendar.InfoCalendarAPI.COLUMN;
import p.calendar.SearchFilter;
import p.calendar.data.CalendarRow.COUNTRY;

public class FilterCountries extends ExtendFilterFrame<COUNTRY> {
	public FilterCountries(Enablable enablable, RemoteService remote) {
		super(enablable, remote);
		stateMap = remote.getCountryStateMap();

		render("ecalendar/filters/FilterCountries");
	}

	@Override
	protected void beforeRenderInit() {
		//do nothing
	}

	@Override
	protected void afterRenderInit() {
		initNoHandleComponents(globalCheckBox, enableCheckBox);
		init();
	}

	protected void singleInit(JCheckBox box) {
		String name = box.getName();
		try {
			COUNTRY c = COUNTRY.valueOf(name);
			if (!stateMap.containsKey(name)) {
				stateMap.put(name, true);
			}
			box.setSelected(stateMap.get(name));
		} catch (IllegalArgumentException ex) {
			box.setEnabled(false);
			box.setSelected(false);
		}
	}

	protected void addFilterCriteron(JCheckBox box) {
		COUNTRY c = null;
		try {
			c = COUNTRY.valueOf(box.getName());
		} catch (IllegalArgumentException ex) {
			c = null;
		} finally {
			if (c != null) {
				filterCollection.add(c);
			}
		}
	}

	protected void sendRequest() {
		String newFilter = "";
		if (enableCheckBox.isSelected()) {
			newFilter = SearchFilter.Country_equals_(filterCollection);
		}
		remote.sendFilterRequest(COLUMN.Country, newFilter);
	}

	public Action ALL_COUNTRIES_ACTION = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			if (globalCheckBox.isSelected()) {
				iterateElements(new Handler() {
					public void handle(JCheckBox box) {
						if (box.isEnabled()) {
							box.setSelected(true);
						}
					}
				});
			} else {
				iterateElements(new Handler() {
					public void handle(JCheckBox box) {
						box.setSelected(false);
					}
				});
			}
		}
	};

	protected JCheckBox globalCheckBox;
}
