package gui.panel.ecalendar.data;

import java.util.Date;

import p.calendar.data.CalendarRow;

public class ExtendCalendarRow implements Comparable<ExtendCalendarRow> {

	public ExtendCalendarRow(CalendarRow row) {
		init(row, false);
	}

	/**
	 * Для freeUpdates:
	 * Параметр true (в методе init) означает, что данная строка
	 * обновлена - и ей нужно задать стиль .updateStyle
	 */
	public void update(CalendarRow row) {
		init(row, true);
	}

	private void init(CalendarRow row, boolean isUpdate) {
		this.updated = isUpdate;

		this.id = row.getid();
		this.primaryDate = row.getDate();
		this.date = Converter.getDate(primaryDate);
		this.time = Converter.getTime(primaryDate);
		this.dateTime = Converter.getDateTime(primaryDate);
		this.timeLeft = Converter.getTimeLeft(primaryDate);
		this.importance = Converter.getImportance(row);
		this.category = Converter.getCategory(row);

		this.country = Converter.excludeNull(row.getCountry().toString());
		this.event = Converter.excludeNull(row.getEvent());
		this.source = Converter.excludeNull(row.getSource());

		this.value = Converter.valueConverter(row.getValue());
		this.valueNote = Converter.excludeNull(row.getValueNote());
		this.forecast = Converter.valueConverter(row.getForecast());
		this.forecastNote = Converter.excludeNull(row.getForecastNote());
		this.prevValue = Converter.valueConverter(row.getPervValue());
		this.prevValueNote = Converter.excludeNull(row.getPerfValueNote());
	}

	public long getId() {
		return id;
	}

	public Date getPrimaryDate() {
		return primaryDate;
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	public String getDateTime() {
		return dateTime;
	}

	public String getTimeLeft() {
		return timeLeft;
	}

	public String getCountry() {
		return country;
	}

	public String getImportance() {
		return importance;
	}

	public String getEvent() {
		return event;
	}

	public String getCategory() {
		return category;
	}

	public String getValue() {
		return value;
	}

	public String getValueNote() {
		return valueNote;
	}

	public String getForecast() {
		return forecast;
	}

	public String getForecastNote() {
		return forecastNote;
	}

	public String getPrevValue() {
		return prevValue;
	}

	public String getPrevValueNote() {
		return prevValueNote;
	}

	public String getSource() {
		return source;
	}

	public int compareTo(ExtendCalendarRow extRow) {
		Date thisDate = primaryDate;
		Date outDate = extRow.getPrimaryDate();
		if (thisDate.getTime() < outDate.getTime()) {
			return -1;
		} else if (thisDate.getTime() > outDate.getTime()) {
			return 1;
		} else {
			return 0;
		}
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public void setTimeLeft(String timeLeft) {
		this.timeLeft = timeLeft;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setTime(String time) {
		this.time = time;
	}

	private boolean updated;

	private long id;
	private Date primaryDate;
	private String dateTime;
	private String date;
	private String time;
	private String timeLeft;
	private String country;
	private String importance;
	private String event;
	private String category;
	private String value;
	private String valueNote;
	private String forecast;
	private String forecastNote;
	private String prevValue;
	private String prevValueNote;
	private String source;
}
