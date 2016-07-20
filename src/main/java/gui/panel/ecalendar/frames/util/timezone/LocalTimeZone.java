package gui.panel.ecalendar.frames.util.timezone;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

public class LocalTimeZone {

	private LocalTimeZone() {
		current = TimeZone.getDefault();
	}

	private void notifyListeners() {
		for (Calendar c : cListeners) {
			c.setTimeZone(current);
		}
	}

	public static LocalTimeZone getInstance() {
		if (instance == null) {
			instance = new LocalTimeZone();
		}
		return instance;
	}

	public TimeZone getCurrent() {
		return current;
	}

	public void setCurrent(TimeZone current) {
		this.current = current;
		notifyListeners();
	}

	public void addListener(Calendar c) {
		c.setTimeZone(current);
		cListeners.add(c);
	}

	public void removeListener(Calendar c) {
		if (cListeners.contains(c)) {
			cListeners.remove(c);
		}
	}

	private static LocalTimeZone instance = null;
	private TimeZone current;
	private Set<Calendar> cListeners = new HashSet<Calendar>();
}
