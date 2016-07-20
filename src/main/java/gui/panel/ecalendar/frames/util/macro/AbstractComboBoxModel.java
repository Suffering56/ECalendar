package gui.panel.ecalendar.frames.util.macro;

import java.util.Map;

import javax.swing.DefaultComboBoxModel;

public abstract class AbstractComboBoxModel extends DefaultComboBoxModel {
	public AbstractComboBoxModel(Map<String, Boolean> stateMap) {
		super();
		this.stateMap = stateMap;
	}

	public Map<String, Boolean> getStateMap() {
		return stateMap;
	}

	protected final Map<String, Boolean> stateMap;
}
