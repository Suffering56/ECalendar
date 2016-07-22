package gui.panel.ecalendar.frames.parents;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import gui.panel.ecalendar.data.remote.RemoteService;

public abstract class FilterFrame extends SecondaryFrame {

	public FilterFrame(Enablable enablable, RemoteService remote) {
		super(enablable);
		this.remote =  remote;
	}

	abstract protected void applyFilter();

	// main
	protected RemoteService remote;
	// control
	public Action APPLY_ACTION = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			if (e != null) {
				enablable.enable();
				applyFilter();
				frame.dispose();
			}
		}
	};
}
