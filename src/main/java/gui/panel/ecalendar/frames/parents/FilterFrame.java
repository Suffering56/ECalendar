package gui.panel.ecalendar.frames.parents;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import gui.panel.ecalendar.data.remote.RemoteService;

public abstract class FilterFrame extends SwixFrame {

	public FilterFrame(RemoteService remote) {
		super();
		this.remote = remote;
	}

	abstract protected void applyFilter();

	// main
	protected RemoteService remote;
	// control
	public Action APPLY_ACTION = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			if (e != null) {
				applyFilter();
				frame.dispose();
			}
		}
	};
}
