package gui.panel.ecalendar.frames.util.macro;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import gui.panel.ecalendar.styles.GeneralStyles;

public class CheckBoxComboRenderer extends JCheckBox implements ListCellRenderer {

	public CheckBoxComboRenderer() {
		this.setName(GeneralStyles.MACRO_FILTER_COMBO_CHECK_BOX_RENDERER);
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if (value != null) {
			BooleanComboEntity entity = (BooleanComboEntity) value;
			this.setText(entity.getRussianText());
			this.setSelected(entity.isSelected());
		} else {
			this.setText(null);
		}
		return this;
	}
}
