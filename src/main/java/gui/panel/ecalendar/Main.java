
package gui.panel.ecalendar;

import java.net.URL;

import javax.swing.UIManager;
import javax.swing.plaf.synth.SynthLookAndFeel;

import gui.panel.ecalendar.frames.FrameCalendar;

public class Main {
	public static void main(String[] args) {
		setLookAndFeel();
		new FrameCalendar().show();
	}

	private static void setLookAndFeel() {
		try {
			SynthLookAndFeel synth = new SynthLookAndFeel();
			URL resource = Main.class.getResource("/LookAndFeel/laf.xml");
			synth.load(resource);
			UIManager.setLookAndFeel(synth);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
