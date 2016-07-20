package gui.panel.ecalendar.data;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class DetailsTableModel extends AbstractTableModel {

	public String getColumnName(int columnIndex) {
		return JDetailsTableView.columnHeadingsMap.get(columnIndex);
	}

	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		int length = (rows == null) ? 0 : rows.size();
		if (rowIndex < length) {
			return JDetailsTableView.getValueAt(rows.get(rowIndex), columnIndex);
		} else {
			return null;
		}
	}

	public int getColumnCount() {
		return JDetailsTableView.columnHeadingsMap.size();
	}

	public int getRowCount() {
		return (rows == null) ? 0 : rows.size();
	}

	public void update(List<ExtendCalendarRow> rows) {
		this.rows = rows;
		fireTableDataChanged();
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		// do nothing
	}

	public List<ExtendCalendarRow> getRows() {
		return rows;
	}

	private List<ExtendCalendarRow> rows;
}