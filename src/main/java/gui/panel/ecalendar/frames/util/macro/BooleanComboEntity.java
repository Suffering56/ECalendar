package gui.panel.ecalendar.frames.util.macro;

public class BooleanComboEntity {

	public BooleanComboEntity(String englishText, String russanText, Boolean selected) {
		this.englishText = englishText;
		this.russianText = russanText;
		this.selected = selected;
	}

	public String getEnglishText() {
		return englishText;
	}

	public String getRussianText() {
		return russianText;
	}

	public Boolean isSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	private String englishText;
	private String russianText;
	private Boolean selected;
}
