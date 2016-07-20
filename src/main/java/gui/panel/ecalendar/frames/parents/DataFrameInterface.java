package gui.panel.ecalendar.frames.parents;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTable;

import gui.panel.ecalendar.data.ExtendCalendarRow;

public interface DataFrameInterface {

	public void updateModel(List<ExtendCalendarRow> rows);

	public JTable getTable();

	public JFrame getFrame();

	public void setWaitCursor();

	public void setDefaultCursor();
}
