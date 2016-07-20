package gui.panel.ecalendar.frames.util.timezone;

import java.util.TimeZone;

public class UTCString implements Comparable<UTCString> {

	public UTCString(String hrs, String min, TimeZone timeZone) {
		this.timeZone = timeZone;
		this.viewString = "UTC" + hrs + ":" + min;
		hrs = hrs.replace("+", "");
		this.compareInt = Integer.valueOf(hrs + min);
	}

	public int compareTo(UTCString utc) {
		int thisInt = compareInt;
		int outInt = utc.getCompareInt();

		if (thisInt < outInt) {
			return -1;
		} else if (thisInt > outInt) {
			return 1;
		} else {
			return 0;
		}
	}

	private int getCompareInt() {
		return compareInt;
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public String getViewString() {
		return viewString;
	}

	private TimeZone timeZone;
	private String viewString;
	private int compareInt;
}