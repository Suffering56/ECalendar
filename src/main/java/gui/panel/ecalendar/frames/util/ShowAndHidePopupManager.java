package gui.panel.ecalendar.frames.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

import gui.panel.ecalendar.data.JCalendarTableView;
import gui.panel.ecalendar.frames.FrameCalendar;

public class ShowAndHidePopupManager {

	public ShowAndHidePopupManager(FrameCalendar sender) {
		this.sender = sender;
		init();
	}

	private void init() {
		sender.showDateColumnHeaderItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sender.showColumn(JCalendarTableView.DATE);
			}
		});
		sender.showTimeColumnHeaderItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sender.showColumn(JCalendarTableView.TIME);
			}
		});
		sender.showTimeLeftColumnHeaderItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sender.showColumn(JCalendarTableView.TIME_LEFT);
			}
		});
		sender.showCountryColumnHeaderItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sender.showColumn(JCalendarTableView.COUNTRY);
			}
		});
		sender.showImportanceColumnHeaderItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sender.showColumn(JCalendarTableView.IMPORTANCE);
			}
		});
		sender.showEventColumnHeaderItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sender.showColumn(JCalendarTableView.EVENT);
			}
		});
		sender.showCategoryColumnHeaderItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sender.showColumn(JCalendarTableView.CATEGORY);
			}
		});
		sender.showValueColumnHeaderItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sender.showColumn(JCalendarTableView.VALUE);
			}
		});
		sender.showValueNoteColumnHeaderItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sender.showColumn(JCalendarTableView.VALUE_NOTE);
			}
		});
		sender.showForecastColumnHeaderItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sender.showColumn(JCalendarTableView.FORECAST);
			}
		});
		sender.showForecastNoteColumnHeaderItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sender.showColumn(JCalendarTableView.FORECAST_NOTE);
			}
		});
		sender.showPrevValueColumnHeaderItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sender.showColumn(JCalendarTableView.PREV_VALUE);
			}
		});
		sender.showPrevValueNoteColumnHeaderItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sender.showColumn(JCalendarTableView.PREV_VALUE_NOTE);
			}
		});
		sender.showSourceColumnHeaderItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sender.showColumn(JCalendarTableView.SOURCE);
			}
		});

		itemList.add(sender.showDateColumnHeaderItem);
		itemList.add(sender.showTimeColumnHeaderItem);
		itemList.add(sender.showTimeLeftColumnHeaderItem);
		itemList.add(sender.showCountryColumnHeaderItem);
		itemList.add(sender.showImportanceColumnHeaderItem);
		itemList.add(sender.showEventColumnHeaderItem);
		itemList.add(sender.showCategoryColumnHeaderItem);

		itemList.add(sender.showValueColumnHeaderItem);
		itemList.add(sender.showValueNoteColumnHeaderItem);
		itemList.add(sender.showForecastColumnHeaderItem);
		itemList.add(sender.showForecastNoteColumnHeaderItem);
		itemList.add(sender.showPrevValueColumnHeaderItem);
		itemList.add(sender.showPrevValueNoteColumnHeaderItem);
		itemList.add(sender.showSourceColumnHeaderItem);
	}

	public List<JMenuItem> getItemList() {
		return itemList;
	}

	private FrameCalendar sender;
	private final List<JMenuItem> itemList = new ArrayList<JMenuItem>();
}
