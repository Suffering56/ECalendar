package gui.panel.ecalendar.frames.parents;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import gui.panel.ecalendar.data.remote.RemoteService;

public abstract class DataFrame extends SwixFrame implements DataFrameInterface {

	public void setWaitCursor() {
		loadingPanel.setVisible(true);
		tableScrollPane.setVisible(false);
	}

	public void setDefaultCursor() {
		loadingPanel.setVisible(false);
		tableScrollPane.setVisible(true);
	}

	protected void initCommonListeners() {
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				remote.removeSink();
			}
		});
	}

	protected RemoteService remote;
	protected JScrollPane tableScrollPane;
	protected JPanel loadingPanel;
}
