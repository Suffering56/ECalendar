package gui.panel.ecalendar.frames.util.macro.initClasses;

import java.util.Map;

import gui.panel.ecalendar.data.Converter;
import gui.panel.ecalendar.frames.util.macro.AbstractComboBoxModel;
import gui.panel.ecalendar.frames.util.macro.BooleanComboEntity;
import p.calendar.data.CalendarRow.IMPORTANCE;

public class ImportanceComboModel extends AbstractComboBoxModel {

	public ImportanceComboModel(Map<String, Boolean> stateMap) {
		super(stateMap);

		for (IMPORTANCE item : IMPORTANCE.values()) {
			if (item != IMPORTANCE.FAILED_TO_IDENTIFY) {
				String englishText = item.toString();
				String russianText = Converter.translateImportance(item);

				BooleanComboEntity entity;
				if (stateMap.containsKey(englishText)) {
					boolean value = stateMap.get(englishText);
					entity = new BooleanComboEntity(englishText, russianText, value);
				} else {
					boolean value = true;
					entity = new BooleanComboEntity(englishText, russianText, value);
					stateMap.put(englishText, value);
				}
				this.addElement(entity);
			}
		}
	}
}
