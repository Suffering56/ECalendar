package gui.panel.ecalendar.frames.util.jcalendar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Kai Toedter
 * @version $LastChangedRevision: 85 $
 * @version $LastChangedDate: 2006-04-28 13:50:52 +0200 (Fr, 28 Apr 2006) $
 */
public class ExtJSpinField extends JPanel implements ChangeListener, CaretListener, ActionListener,
		FocusListener {

	private static final long serialVersionUID = 1694904792717740650L;
	protected JSpinner spinner;
	protected JTextField textField;
	protected int min;
	protected int max;
	protected int value;

	public ExtJSpinField() {
		this(Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	public ExtJSpinField(int min, int max) {
		super();
		setName("JSpinField");
		this.min = min;
		if (max < min)
			max = min;
		this.max = max;
		value = 0;
		if (value < min)
			value = min;
		if (value > max)
			value = max;

		setLayout(new BorderLayout());
		textField = new JTextField();
		textField.addCaretListener(this);
		textField.addActionListener(this);
		textField.setHorizontalAlignment(SwingConstants.RIGHT);
		textField.setBorder(BorderFactory.createEmptyBorder());
		textField.setText(Integer.toString(value));
		textField.addFocusListener(this);
		textField.setEditable(false);
		textField.setFocusable(false);

		spinner = new JSpinner() {
			private static final long serialVersionUID = -6287709243342021172L;
			private JTextField textField = new JTextField();

			public Dimension getPreferredSize() {
				Dimension size = super.getPreferredSize();
				return new Dimension(size.width, textField.getPreferredSize().height);
			}
		};
		spinner.setEditor(textField);
		spinner.addChangeListener(this);
		add(spinner, BorderLayout.CENTER);
	}

	public void adjustWidthToMaximumValue() {
		JTextField testTextField = new JTextField(Integer.toString(max));
		int width = testTextField.getPreferredSize().width;
		int height = testTextField.getPreferredSize().height;
		textField.setPreferredSize(new Dimension(width, height));
		textField.revalidate();
	}

	/**
	 * Is invoked when the spinner model changes
	 * 
	 * @param e
	 *            the ChangeEvent
	 */
	public void stateChanged(ChangeEvent e) {
		SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();
		int newValue = model.getNumber().intValue();

		int oldValue = this.value;

		//spinner inversion
		if (newValue > oldValue) {
			newValue -= 2;
		} else if (newValue < oldValue) {
			newValue += 2;
		}

		setValue(newValue);
	}

	/**
	 * Sets the value attribute of the JSpinField object.
	 * 
	 * @param newValue
	 *            The new value
	 * @param updateTextField
	 *            true if text field should be updated
	 */
	protected void setValue(int newValue, boolean updateTextField, boolean firePropertyChange) {
		int oldValue = value;
		if (newValue < min) {
			value = min;
		} else if (newValue > max) {
			value = max;
		} else {
			value = newValue;
		}

		if (updateTextField) {
			textField.setText(Integer.toString(value));
		}

		if (firePropertyChange) {
			firePropertyChange("value", oldValue, value);
		}
	}

	/**
	 * Sets the value. This is a bound property.
	 * 
	 * @param newValue
	 *            the new value
	 * 
	 * @see #getValue
	 */
	public void setValue(int newValue) {
		setValue(newValue, true, true);
		spinner.setValue(new Integer(value));
	}

	/**
	 * Returns the value.
	 * 
	 * @return the value value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Sets the minimum value.
	 * 
	 * @param newMinimum
	 *            the new minimum value
	 * 
	 * @see #getMinimum
	 */
	public void setMinimum(int newMinimum) {
		min = newMinimum;
	}

	/**
	 * Returns the minimum value.
	 * 
	 * @return the minimum value
	 */
	public int getMinimum() {
		return min;
	}

	/**
	 * Sets the maximum value and adjusts the preferred width.
	 * 
	 * @param newMaximum
	 *            the new maximum value
	 * 
	 * @see #getMaximum
	 */
	public void setMaximum(int newMaximum) {
		max = newMaximum;
	}

	/**
	 * Sets the horizontal alignment of the displayed value.
	 * 
	 * @param alignment
	 *            the horizontal alignment
	 */
	public void setHorizontalAlignment(int alignment) {
		textField.setHorizontalAlignment(alignment);
	}

	/**
	 * Returns the maximum value.
	 * 
	 * @return the maximum value
	 */
	public int getMaximum() {
		return max;
	}


	/**
	 * After any user input, the value of the textfield is proofed. Depending on
	 * being an integer, the value is colored green or red.
	 * 
	 * @param e
	 *            the caret event
	 */
	public void caretUpdate(CaretEvent e) {
		try {
			int testValue = Integer.valueOf(textField.getText()).intValue();
			if ((testValue >= min) && (testValue <= max)) {
				setValue(testValue, false, true);
			}
		} catch (Exception ex) {
		}

		textField.repaint();
	}

	/**
	 * Enable or disable the JSpinField.
	 * 
	 * @param enabled
	 *            The new enabled value
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		spinner.setEnabled(enabled);
		textField.setEnabled(enabled);
	}

	public void focusGained(FocusEvent e) {
	}

	public void actionPerformed(ActionEvent e) {
	}

	/**
	 * Returns the year chooser's spinner (which allow the focus to be set to
	 * it).
	 * 
	 * @return Component the spinner or null, if the month chooser has no
	 *         spinner
	 */
	public Component getSpinner() {
		return spinner;
	}

	/**
	 * The value of the text field is checked against a valid (green) value. If
	 * valid, the value is set and a property change is fired.
	 */
	public void focusLost(FocusEvent e) {
		actionPerformed(null);
	}

}