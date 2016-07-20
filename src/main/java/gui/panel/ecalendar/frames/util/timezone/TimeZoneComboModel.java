package gui.panel.ecalendar.frames.util.timezone;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.swing.DefaultComboBoxModel;

import gui.panel.ecalendar.data.Converter;

public class TimeZoneComboModel extends DefaultComboBoxModel {

	public TimeZoneComboModel() {

		for (String str : TimeZone.getAvailableIDs()) {
			TimeZone timeZone = TimeZone.getTimeZone(str);
			UTCString utc = extractUTC(timeZone);

			if (!set.contains(utc) && (timeZone.getID().contains("Etc/GMT"))) {
				set.add(utc);
			}
		}

		list.addAll(set);

		for (UTCString utc : list) {
			this.addElement(utc.getViewString());
		}
	}

	public TimeZone getTimeZone(int index) {
		if (list.size() > index) {
			return list.get(index).getTimeZone();
		} else {
			return null;
		}
	}

	public static UTCString extractUTC(TimeZone timeZone) {

		int seconds = timeZone.getRawOffset() / 1000;
		int minutes = seconds / 60;
		int hours = minutes / 60;

		int minutesLost = minutes - hours * 60;

		if (minutesLost < 0) {
			minutesLost = -minutesLost;
		}

		String min = String.valueOf(minutesLost);
		min = Converter.addZero(min);

		String hrs = String.valueOf(hours);
		hrs = Converter.addZero(hrs, hours < 0);

		if (hours >= 0) {
			hrs = "+" + hrs;
		}

		return new UTCString(hrs, min, timeZone);
	}

	private Set<UTCString> set = new TreeSet<UTCString>();
	private List<UTCString> list = new ArrayList<UTCString>();
}
