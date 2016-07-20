package gui.panel.ecalendar.data;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gui.panel.ecalendar.frames.util.timezone.LocalTimeZone;
import p.calendar.data.CalendarRow;
import p.calendar.data.CalendarRow.CATEGORY;
import p.calendar.data.CalendarRow.COUNTRY;
import p.calendar.data.CalendarRow.IMPORTANCE;

public class Converter {

	public static String getDate(Date primaryDate) {
		Calendar c = Calendar.getInstance(LocalTimeZone.getInstance().getCurrent());
		c.setTime(primaryDate);

		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		String month = String.valueOf(c.get(Calendar.MONTH) + 1);
		String year = String.valueOf(c.get(Calendar.YEAR));

		day = addZero(day);
		month = addZero(month);

		String result = day + "." + month + "." + year;
		return result;
	}

	public static String getTime(Date primaryDate) {
		Calendar c = Calendar.getInstance(LocalTimeZone.getInstance().getCurrent());
		c.setTime(primaryDate);
		String result = extractTime(c, false);
		return result;
	}

	public static String extractTime(Calendar c, boolean withSeconds) {
		String hours = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String minutes = String.valueOf(c.get(Calendar.MINUTE));
		String seconds = String.valueOf(c.get(Calendar.SECOND));

		hours = addZero(hours);
		minutes = addZero(minutes);
		seconds = addZero(seconds);

		if (withSeconds) {
			seconds = ":" + seconds;
		} else {
			seconds = "";
		}

		String result = hours + ":" + minutes + seconds;
		return result;
	}

	public static String getDateTime(Date primaryDate) {
		String eventDate = getDate(primaryDate);
		String eventTime = getTime(primaryDate);
		return eventDate + " " + eventTime;
	}

	public static String getTimeLeft(Date primaryDate) {
		Calendar eventTime = Calendar.getInstance(LocalTimeZone.getInstance().getCurrent());
		eventTime.setTime(primaryDate);
		Calendar currentTime = Calendar.getInstance(LocalTimeZone.getInstance().getCurrent());
		long diff = eventTime.getTimeInMillis() - currentTime.getTimeInMillis();

		if (diff <= 0) {
			return "-";
		} else {
			long seconds = diff / 1000;
			long minutes = seconds / 60;
			long hours = minutes / 60;

			long minutesLost = minutes % 60;

			String min = String.valueOf(minutesLost);
			String hrs = String.valueOf(hours);

			if (min.length() == 1) {
				min = "0" + min;
			}

			if (hours < 24) {
				return hrs + ":" + min;
			} else {
				return "";
			}
		}
	}

	public static String getImportance(CalendarRow row) {
		return importanceTranslator.toString(row.getImportance());
	}

	public static String getCategory(CalendarRow row) {
		return categoryTranslator.toString(row.getCategory());
	}

	public static String excludeNull(String value) {
		if (value.equals("null")) {
			value = "";
		}
		return value;
	}

	public static String valueConverter(String value) {
		value = value.trim();
		value = excludeNull(value);

		if (value.contains(",")) {
			value = value.replace(",", ".");
		}

		return value;
	}

	public static String excludePercent(String value) {
		
		StringBuilder newValue = new StringBuilder();
		
		for (int i = 0; i < value.length(); i++) {
			String ch = String.valueOf(value.charAt(i));
			if (ch.equals(".")) {
				newValue.append(".");
			} else if (ch.equals("-")) {
				newValue.append("-");
			} else {
				Matcher m = numberPattern.matcher(ch);
				if (m.matches()) {
					newValue.append(ch);
				}
			}
		}
		
		return newValue.toString();
	}

	public static String addZero(String str) {
		return addZero(str, false);
	}

	public static String addZero(String str, boolean isNegative) {
		if (isNegative) {
			int x = Integer.valueOf(str);
			x = -x;
			str = String.valueOf(x);
		}

		if (str.length() <= 1) {
			str = "0" + str;
		}

		if (isNegative) {
			str = "-" + str;
		}

		return str;
	}

	public static String translateCountry(COUNTRY c) {
		return countryTranslator.toString(c);
	}

	public static String translateImportance(IMPORTANCE i) {
		return importanceTranslator.toString(i);
	}

	public static String translateCategory(CATEGORY c) {
		return categoryTranslator.toString(c);
	}

	// main
	private static final COUNTRY countryTranslator = COUNTRY.Undefined;
	private static final IMPORTANCE importanceTranslator = IMPORTANCE.NONE;
	private static final CATEGORY categoryTranslator = CATEGORY.NOT_DEFINED;

	private static final Pattern numberPattern = Pattern.compile("\\d");
}