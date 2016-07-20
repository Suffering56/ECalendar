package gui.panel.ecalendar.frames.util;

public class TooltipEntity {

	public TooltipEntity(String dateTime, String value, String forecast) {
		this.dateTime = dateTime;
		this.value = value;
		this.forecast = forecast;
	}

	public String getDateTime() {
		return dateTime;
	}

	public String getValue() {
		return value;
	}

	public String getForecast() {
		return forecast;
	}

	private String dateTime;
	private String value;
	private String forecast;
}
