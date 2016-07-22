package gui.panel.ecalendar.frames.filters;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

import gui.panel.ecalendar.data.remote.RemoteService;
import gui.panel.ecalendar.frames.parents.AbstractDateFilter;
import gui.panel.ecalendar.frames.util.macro.AbstractComboBoxModel;
import gui.panel.ecalendar.frames.util.macro.BooleanComboEntity;
import gui.panel.ecalendar.frames.util.macro.CheckBoxComboRenderer;
import gui.panel.ecalendar.frames.util.macro.initClasses.ImportanceComboModel;
import p.calendar.InfoCalendarAPI.COLUMN;
import p.calendar.SearchFilter;
import p.calendar.data.CalendarRow.IMPORTANCE;

public class MacroFilter extends AbstractDateFilter {

	public MacroFilter(RemoteService remote) {
		super(remote, "ecalendar/filters/MacroFilter");
	}

	@Override
	protected void afterRenderInit(){
		super.afterRenderInit();
		init();
		initShowCountryListener();
	}

	private void init() {
		importanceModel = new ImportanceComboModel(remote.getImportanceStateMap());
		importanceCombobox.setModel(importanceModel);
		importanceCombobox.setRenderer(new CheckBoxComboRenderer());

		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JComboBox box = (JComboBox) e.getSource();
				AbstractComboBoxModel model = (AbstractComboBoxModel) box.getModel();
				BooleanComboEntity entity = (BooleanComboEntity) model.getElementAt(box.getSelectedIndex());

				boolean selected = !entity.isSelected();
				entity.setSelected(selected);
				model.getStateMap().put(entity.getEnglishText(), selected);

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						box.showPopup();
					}
				});
			}
		};

		importanceCombobox.addActionListener(actionListener);
	}

	protected void applyFilter() {
		applyDateFilter();
		applyImportanceFilter();

		remote.sendDateRequest(startDate, endDate);
	}

	private void applyDateFilter() {
		setDayConstraints();
		remote.setCurrentStartDate(startDate);
		remote.setCurrentEndDate(endDate);
	}

	private void applyImportanceFilter() {
		List<IMPORTANCE> importanceFilterList = new ArrayList<IMPORTANCE>();
		for (String key : importanceModel.getStateMap().keySet()) {
			boolean selected = importanceModel.getStateMap().get(key);
			if (selected) {
				IMPORTANCE c = IMPORTANCE.valueOf(key);
				importanceFilterList.add(c);
			}
		}
		String newFilter = SearchFilter.Importance_equals_(importanceFilterList);
		remote.saveFilterState(COLUMN.Importance, newFilter);
	}

	private void initShowCountryListener() {
		showCountryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MacroCountriesFilter(remote).show();
			}
		});
		
		showCategoryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MacroCategoryFilter(remote).show();
			}
		});
	}

	// view
	private JButton showCountryButton;
	private JButton showCategoryButton;
	private JComboBox importanceCombobox;
	private ImportanceComboModel importanceModel;
}
