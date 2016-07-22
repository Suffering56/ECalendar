package gui.panel.ecalendar.frames.parents;

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

			swixRender(frameName);

			afterRenderInit();

			if (pack) {
				pack();
			}
		} catch (Exception ex) {
			System.out.println(this.getClass().getName() + ".renderException: " + ex);
		}
	}
	
	protected void swixRender(String frameName) throws Exception {
		swix.render(frameName + ".xml");
		frame = (JFrame) swix.getRootComponent();
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

	public void enable() {
		frame.setEnabled(true);
		show();
	}

	public void disable() {
		frame.setEnabled(false);
	}
	
	// main
	protected SwingEngine swix;
	public JFrame frame;
}
