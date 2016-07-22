package gui.panel.ecalendar.frames.parents;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

public abstract class SecondaryFrame extends SwixFrame {
	public SecondaryFrame(Enablable enablable) {
		super();
		this.enablable = enablable;
	}

	@Override
	protected final void swixRender(String frameName) throws Exception {
		super.swixRender(frameName);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				enablable.enable();
			}
		});
	}

	//main
	protected Enablable enablable;
	// control
	public Action CANCEL_ACTION = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			if (e != null) {
				enablable.enable();
				frame.dispose();
			}
		}
	};
}
