package gui.panel.ecalendar.frames.parents;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;

import org.swixml.SwingEngine;

public abstract class SwixFrame {
	public SwixFrame() {
		swix = new SwingEngine(this);
	}

	abstract protected void beforeRenderInit();

	abstract protected void afterRenderInit();

	protected void render(String frameName) {
		render(frameName, true);
	}

	protected void render(String frameName, boolean pack) {
		try {
			beforeRenderInit();

			swix.render(frameName + ".xml");
			
			frame = (JFrame) swix.getRootComponent();
			
			afterRenderInit();
			
			if (pack) {
				pack();
			}
		} catch (Exception ex) {
			System.out.println(this.getClass().getName() + ".renderException: " + ex);
		}
	}

	protected void pack() {
		frame.pack();
		frame.setMinimumSize(frame.getSize());
		frame.setLocationRelativeTo(null);
	}

	public void show() {
		frame.setVisible(true);
	}
	
	public void hide() {
		frame.setVisible(false);
	}

	// control
	public Action CANCEL_ACTION = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			if (e != null) {
				frame.dispose();
			}
		}
	};
	// main
	protected SwingEngine swix;
	public JFrame frame;
}
