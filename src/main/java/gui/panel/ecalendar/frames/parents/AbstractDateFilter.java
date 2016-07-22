package gui.panel.ecalendar.frames.parents;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Formatter;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import gui.panel.ecalendar.data.remote.RemoteService;
import gui.panel.ecalendar.frames.util.jcalendar.ExtJCalendar;
import gui.panel.ecalendar.frames.util.jcalendar.ExtJDayChooser;
import gui.panel.ecalendar.frames.util.timezone.LocalTimeZone;

public abstract class AbstractDateFilter extends FilterFrame {

	public AbstractDateFilter(Enablable enablable, RemoteService remote, String frameName) {
		super(enablable, remote);
		render(frameName);
	}

	@Override
	protected void beforeRenderInit() {
		startDate = (Calendar) remote.getCurrentStartDate().clone();
		endDate = (Calendar) remote.getCurrentEndDate().clone();

		swix.getTaglib().registerTag("calendar", ExtJCalendar.class);
	}

	@Override
	protected void afterRenderInit() {
		START_BORDER = startDatePanel.getBorder();
		END_BORDER = endDatePanel.getBorder();

		activeFieldLink = startDateField;
		activeCalendarLink = startDate;

		initCalendarComponents();
		initJDayChooser();
		initListeners();

		start = false;
		setStartFocus();
	}

	abstract protected void applyFilter();

	protected void initCalendarComponents() {
		startDatePanel.setBorder(EMPTY_BORDER);
		endDatePanel.setBorder(EMPTY_BORDER);

		startDateField.setText(getStringFromDate(startDate));
		startDateChildPanel.setCursor(HAND_CURSOR);

		endDateField.setText(getStringFromDate(endDate));
		endDateChildPanel.setCursor(HAND_CURSOR);

		jCalendar.getMonthChooser().setPreferredSize(new Dimension(jCalendar.getMonthChooser().getPreferredSize().width, 25));
		jCalendar.getYearChooser().setPreferredSize(new Dimension(jCalendar.getYearChooser().getPreferredSize().width, 25));
	}

	protected void initJDayChooser() {
		jDayChooser = jCalendar.getDayChooser();

		jDayChooser.setStart(false);
		jDayChooser.setEndDate(endDate);
		jCalendar.getYearChooser().setYear(endDate.get(Calendar.YEAR));
		jCalendar.getMonthChooser().setMonth(endDate.get(Calendar.MONTH));
		jDayChooser.setDay(endDate.get(Calendar.DAY_OF_MONTH));

		jDayChooser.setStart(true);
		jDayChooser.setStartDate(startDate);
		jCalendar.getYearChooser().setYear(startDate.get(Calendar.YEAR));
		jCalendar.getMonthChooser().setMonth(startDate.get(Calendar.MONTH));
		jDayChooser.setDay(startDate.get(Calendar.DAY_OF_MONTH));

		jCalendar.revalidate();
	}

	protected void initListeners() {
		jDayChooser.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				Calendar selectedDate = Calendar.getInstance(LocalTimeZone.getInstance().getCurrent());
				selectedDate.setTime(jCalendar.getDate());
				String stringDate = getStringFromDate(selectedDate);
				activeFieldLink.setText(stringDate);
				activeCalendarLink.setTime(selectedDate.getTime());

				if (start) {
					jDayChooser.setStartDate((Calendar) startDate.clone());
				} else {
					jDayChooser.setEndDate((Calendar) endDate.clone());
				}
			}
		});

		startDateField.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent evt) {
				setStartFocus();
			}
		});

		startDateLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setStartFocus();
			}
		});

		startDateChildPanel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setStartFocus();
			}
		});

		endDateField.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent evt) {
				setEndFocus();
			}
		});

		endDateLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setEndFocus();
			}
		});

		endDateChildPanel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setEndFocus();
			}
		});
	}

	protected void setStartFocus() {
		boolean oldStart = start;
		start = true;
		if (start != oldStart) {
			activeFieldLink = startDateField;
			activeCalendarLink = startDate;

			startDatePanel.setBorder(START_BORDER);
			endDatePanel.setBorder(EMPTY_BORDER);

			jDayChooser.setStart(start);
		}
	}

	protected void setEndFocus() {
		boolean oldStart = start;
		start = false;
		if (start != oldStart) {
			activeFieldLink = endDateField;
			activeCalendarLink = endDate;

			endDatePanel.setBorder(END_BORDER);
			startDatePanel.setBorder(EMPTY_BORDER);

			jDayChooser.setStart(start);
		}
	}

	protected String getStringFromDate(Calendar c) {
		Formatter formatter = new Formatter();
		String result = formatter.format("%1$td-%1$tm-%1$tY", c).toString();
		formatter.close();
		return result;
	}

	protected void setDayConstraints() {
		startDate.set(Calendar.HOUR_OF_DAY, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);
		startDate.set(Calendar.MILLISECOND, 0);

		endDate.set(Calendar.HOUR_OF_DAY, 23);
		endDate.set(Calendar.MINUTE, 59);
		endDate.set(Calendar.SECOND, 59);
	}

	// view
	protected ExtJCalendar jCalendar;
	protected ExtJDayChooser jDayChooser;
	protected JTextField startDateField;
	protected JTextField endDateField;

	protected JPanel startDatePanel;
	protected JPanel startDateChildPanel;
	protected JLabel startDateLabel;

	protected JPanel endDatePanel;
	protected JPanel endDateChildPanel;
	protected JLabel endDateLabel;

	// other
	protected JTextField activeFieldLink;
	protected Calendar activeCalendarLink;

	protected boolean start = true;
	protected Calendar startDate;
	protected Calendar endDate;

	protected static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);
	protected static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);

	protected static final Border EMPTY_BORDER = new EmptyBorder(2, 2, 2, 2);
	protected Border START_BORDER;
	protected Border END_BORDER;
}
