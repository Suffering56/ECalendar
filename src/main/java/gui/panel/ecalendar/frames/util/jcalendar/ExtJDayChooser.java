package gui.panel.ecalendar.frames.util.jcalendar;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;

import gui.panel.ecalendar.styles.CalendarStyles;

/**
 * JDayChooser is a bean for choosing a day.
 * 
 * @author Kai Toedter
 * @version $LastChangedRevision: 147 $
 * @version $LastChangedDate: 2011-06-06 20:36:53 +0200 (Mo, 06 Jun 2011) $
 */
public class ExtJDayChooser extends JPanel implements ActionListener, KeyListener, FocusListener {

	private static final long serialVersionUID = 5876398337018781820L;
	protected JPanel dayPanel;
	protected JButton[] days;
	protected String[] dayNames;

	protected Calendar today;
	protected Locale locale;

	protected boolean initialized;
	protected boolean decorationBordersVisible;
	protected boolean dayBordersVisible;
	private boolean alwaysFireDayProperty;
	protected int maxDayCharacters;

	protected Calendar calendar;
	protected int selectedDayValue;
	protected JButton selectedDayBtn;

	private boolean start = true;

	protected JButton selectedStartDayBtn;
	protected JButton selectedEndDayBtn;

	protected int selectedStartDayValue;
	protected int selectedEndDayValue;

	protected Calendar startDate;
	protected Calendar endDate;

	public ExtJDayChooser() {
		this.setName(CalendarStyles.DAYS_PANEL);
		alwaysFireDayProperty = true;

		locale = Locale.getDefault();
		days = new JButton[49];
		selectedDayBtn = null;
		calendar = Calendar.getInstance(locale);
		startDate = (Calendar) calendar.clone();
		endDate = (Calendar) calendar.clone();
		endDate.add(Calendar.WEEK_OF_YEAR, 1);
		today = (Calendar) calendar.clone();

		setLayout(new BorderLayout());

		dayPanel = new JPanel();
		dayPanel.setLayout(new GridLayout(7, 7));

		for (int y = 0; y < 7; y++) {
			for (int x = 0; x < 7; x++) {
				int index = x + (7 * y);

				if (y == 0) {
					days[index] = new DecoratorButton();
					days[index].setName(CalendarStyles.DAYS_DECORATED_WEEKDAY);
				} else {
					days[index] = new JButton("x") {
						private static final long serialVersionUID = -7433645992591669725L;

						public void paint(Graphics g) {
							if ("Windows".equals(UIManager.getLookAndFeel().getID())) {
								if (selectedDayBtn == this) {
									g.fillRect(0, 0, getWidth(), getHeight());
								}
							}
							super.paint(g);
						}

					};
					days[index].addActionListener(this);
					days[index].addKeyListener(this);
					days[index].addFocusListener(this);
					days[index].setName(CalendarStyles.DAYS_DEFAULT_DAY);
				}

				days[index].setFocusPainted(false);
				dayPanel.add(days[index]);
			}
		}

		init();

		setDay(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		add(dayPanel, BorderLayout.CENTER);

		initialized = true;
		updateUI();
	}

	protected void init() {
		Date date = calendar.getTime();
		calendar = Calendar.getInstance(locale);
		calendar.setTime(date);

		drawDayNames();
		drawDays();
	}

	protected void drawDays() {
		Calendar tmpCalendar = (Calendar) calendar.clone();
		tmpCalendar.set(Calendar.HOUR_OF_DAY, 0);
		tmpCalendar.set(Calendar.MINUTE, 0);
		tmpCalendar.set(Calendar.SECOND, 0);
		tmpCalendar.set(Calendar.MILLISECOND, 0);

		int firstDayOfWeek = tmpCalendar.getFirstDayOfWeek();
		tmpCalendar.set(Calendar.DAY_OF_MONTH, 1);

		int firstDay = tmpCalendar.get(Calendar.DAY_OF_WEEK) - firstDayOfWeek;

		if (firstDay < 0) {
			firstDay += 7;
		}

		int i;

		for (i = 0; i < firstDay; i++) {
			days[i + 7].setVisible(false);
			days[i + 7].setText("");
		}

		tmpCalendar.add(Calendar.MONTH, 1);
		Date firstDayInNextMonth = tmpCalendar.getTime();

		tmpCalendar.add(Calendar.MONTH, -1);

		Date date = tmpCalendar.getTime();
		int n = 0;

		while (date.before(firstDayInNextMonth)) {
			days[i + n + 7].setText(Integer.toString(n + 1));
			days[i + n + 7].setVisible(true);

			boolean isStartSelected = false;
			boolean isEndSelected = false;
			boolean isToday = isToday(tmpCalendar);

			if ((tmpCalendar.get(Calendar.YEAR) == startDate.get(Calendar.YEAR))
					&& (tmpCalendar.get(Calendar.MONTH) == startDate.get(Calendar.MONTH))
					&& ((n + 1) == this.selectedStartDayValue)) {
				selectedDayBtn = days[i + n + 7];
				selectedStartDayBtn = selectedDayBtn;
				isStartSelected = true;
			}
			if ((tmpCalendar.get(Calendar.YEAR) == endDate.get(Calendar.YEAR))
					&& (tmpCalendar.get(Calendar.MONTH) == endDate.get(Calendar.MONTH))
					&& ((n + 1) == this.selectedEndDayValue)) {
				selectedDayBtn = days[i + n + 7];
				selectedEndDayBtn = selectedDayBtn;
				isEndSelected = true;
			}

			if (isStartSelected && isEndSelected) {
				if (start) {
					isStartSelected = false;
				} else {
					isEndSelected = false;
				}
			}

			if (isStartSelected) {
				//				if (startDate.getTimeInMillis() != endDate.getTimeInMillis()) {
				if (isToday) {
					selectedDayBtn.setName(CalendarStyles.DAYS_SELECTED_START_TODAY);
				} else {
					selectedDayBtn.setName(CalendarStyles.DAYS_SELECTED_START_DAY);
				}
				//				}
			} else if (isEndSelected) {
				//				if (startDate.getTimeInMillis() != endDate.getTimeInMillis()) {
				if (isToday) {
					selectedDayBtn.setName(CalendarStyles.DAYS_SELECTED_END_TODAY);
				} else {
					selectedDayBtn.setName(CalendarStyles.DAYS_SELECTED_END_DAY);
				}
				//				}
			} else {
				if (isToday) {
					days[i + n + 7].setName(CalendarStyles.DAYS_TODAY);
				} else {
					days[i + n + 7].setName(CalendarStyles.DAYS_DEFAULT_DAY);
				}
			}

			n++;
			tmpCalendar.add(Calendar.DATE, 1);
			date = tmpCalendar.getTime();
		}

		for (int k = n + i + 7; k < 49; k++) {
			days[k].setVisible(false);
			days[k].setText("");
		}

		drawWeeks();
	}

	private void oldDaysRepaint() {
		JButton oldSelectedStartDayBtn = selectedStartDayBtn;
		JButton oldSelectedEndDayBtn = selectedEndDayBtn;

		if (start) {
			if (oldSelectedStartDayBtn != null) {
				if (oldSelectedStartDayBtn.getName().equals(CalendarStyles.DAYS_SELECTED_START_TODAY)) {
					oldSelectedStartDayBtn.setName(CalendarStyles.DAYS_TODAY);
				} else if (startDate.getTimeInMillis() == endDate.getTimeInMillis()) {
					//do nothing
				} else {
					oldSelectedStartDayBtn.setName(CalendarStyles.DAYS_DEFAULT_DAY);
				}
				oldSelectedStartDayBtn.repaint();
			}
		} else {
			if (oldSelectedEndDayBtn != null) {
				if (oldSelectedEndDayBtn.getName().equals(CalendarStyles.DAYS_SELECTED_END_TODAY)) {
					oldSelectedEndDayBtn.setName(CalendarStyles.DAYS_TODAY);
				} else if (startDate.getTimeInMillis() == endDate.getTimeInMillis()) {
					//do nothing
				} else {
					oldSelectedEndDayBtn.setName(CalendarStyles.DAYS_DEFAULT_DAY);
				}
				oldSelectedEndDayBtn.repaint();
			}
		}
	}

	public void setDay(int d) {
		if (d < 1) {
			d = 1;
		}
		Calendar tmpCalendar = (Calendar) calendar.clone();
		tmpCalendar.set(Calendar.DAY_OF_MONTH, 1);
		tmpCalendar.add(Calendar.MONTH, 1);
		tmpCalendar.add(Calendar.DATE, -1);

		int maxDaysInMonth = tmpCalendar.get(Calendar.DATE);

		if (d > maxDaysInMonth) {
			d = maxDaysInMonth;
		}

		int oldDayValue = selectedDayValue;
		selectedDayValue = d;

		oldDaysRepaint();

		for (int i = 7; i < 49; i++) {
			if (days[i].getText().equals(Integer.toString(selectedDayValue))) {
				boolean isToday = false;
				selectedDayBtn = days[i];
				if (selectedDayBtn.getName().equals(CalendarStyles.DAYS_TODAY)) {
					isToday = true;
				}

				if (start) {
					if (!selectedDayBtn.getName().equals(CalendarStyles.DAYS_SELECTED_END_TODAY)
							&& !selectedDayBtn.getName().equals(CalendarStyles.DAYS_SELECTED_END_DAY)) {
						if (isToday) {
							selectedDayBtn.setName(CalendarStyles.DAYS_SELECTED_START_TODAY);
						} else {
							selectedDayBtn.setName(CalendarStyles.DAYS_SELECTED_START_DAY);
						}
					}
				} else {
					if (!selectedDayBtn.getName().equals(CalendarStyles.DAYS_SELECTED_START_TODAY)
							&& !selectedDayBtn.getName().equals(CalendarStyles.DAYS_SELECTED_START_DAY)) {
						if (isToday) {
							selectedDayBtn.setName(CalendarStyles.DAYS_SELECTED_END_TODAY);
						} else {
							selectedDayBtn.setName(CalendarStyles.DAYS_SELECTED_END_DAY);
						}
					}
				}

				break;
			}
		}

		if (start) {
			selectedStartDayValue = selectedDayValue;
			selectedStartDayBtn = selectedDayBtn;
		} else {
			selectedEndDayValue = selectedDayValue;
			selectedEndDayBtn = selectedDayBtn;
		}

		if (alwaysFireDayProperty) {
			firePropertyChange("day", 0, selectedDayValue);
		} else {
			firePropertyChange("day", oldDayValue, selectedDayValue);
		}
	}

	private boolean isToday(Calendar tmpCalendar) {
		if ((tmpCalendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR))
				&& (tmpCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR))) {
			return true;
		} else {
			return false;
		}
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		if (!initialized) {
			super.setLocale(locale);
		} else {
			this.locale = locale;
			super.setLocale(locale);
			init();
		}
	}

	/**
	 * this is needed for JDateChooser.
	 * 
	 * @param alwaysFire
	 *            true, if day property shall be fired every time a day is
	 *            chosen.
	 */
	public void setAlwaysFireDayProperty(boolean alwaysFire) {
		alwaysFireDayProperty = alwaysFire;
	}

	/**
	 * Returns the selected day.
	 * 
	 * @return the day value
	 * 
	 * @see #setDay
	 */
	public int getDay() {
		return selectedDayValue;
	}

	/**
	 * Sets a specific month. This is needed for correct graphical
	 * representation of the days.
	 * 
	 * @param month
	 *            the new month
	 */
	public void setMonth(int month) {
		calendar.set(Calendar.MONTH, month);
		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		if (selectedDayValue > maxDays) {
			selectedDayValue = maxDays;
		}

		drawDays();
	}

	/**
	 * Sets a specific year. This is needed for correct graphical representation
	 * of the days.
	 * 
	 * @param year
	 *            the new year
	 */
	public void setYear(int year) {
		calendar.set(Calendar.YEAR, year);
		drawDays();
	}

	/**
	 * Sets a specific calendar. This is needed for correct graphical
	 * representation of the days.
	 * 
	 * @param calendar
	 *            the new calendar
	 */
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
		drawDays();
	}

	/**
	 * JDayChooser is the ActionListener for all day buttons.
	 * 
	 * @param e
	 *            the ActionEvent
	 */
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		String buttonText = button.getText();
		int day = new Integer(buttonText).intValue();
		setDay(day);
	}

	/**
	 * Enable or disable the JDayChooser.
	 * 
	 * @param enabled
	 *            The new enabled value
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		for (short i = 0; i < days.length; i++) {
			if (days[i] != null) {
				days[i].setEnabled(enabled);
			}
		}
	}

	/**
	 * Returns the day panel.
	 * 
	 * @return the day panel
	 */
	public JPanel getDayPanel() {
		return dayPanel;
	}

	/**
	 * Requests that the selected day also have the focus.
	 */
	public void setFocus() {
		if (selectedDayBtn != null) {
			this.selectedDayBtn.requestFocus();
		}
	}

	/**
	 * The decoration border is the button border of the day titles and the
	 * weeks of the year.
	 * 
	 * @return Returns true, if the decoration border is painted.
	 */
	public boolean isDecorationBordersVisible() {
		return decorationBordersVisible;
	}

	public boolean isDayBordersVisible() {
		return dayBordersVisible;
	}

	/**
	 * The decoration border is the button border of the day titles and the
	 * weeks of the year.
	 * 
	 * @param decorationBordersVisible
	 *            true, if the decoration border shall be painted.
	 */
	public void setDecorationBordersVisible(boolean decorationBordersVisible) {
		this.decorationBordersVisible = decorationBordersVisible;
		initDecorations();
	}

	public void setDayBordersVisible(boolean dayBordersVisible) {
		this.dayBordersVisible = dayBordersVisible;
		if (initialized) {
			for (int x = 7; x < 49; x++) {
				if ("Windows".equals(UIManager.getLookAndFeel().getID())) {
					days[x].setContentAreaFilled(dayBordersVisible);
				} else {
					days[x].setContentAreaFilled(true);
				}
				days[x].setBorderPainted(dayBordersVisible);
			}
		}
	}

	/**
	 * Updates the UI and sets the day button preferences.
	 */
	public void updateUI() {
		super.updateUI();

		if (initialized) {
			if ("Windows".equals(UIManager.getLookAndFeel().getID())) {
				setDayBordersVisible(false);
				setDecorationBordersVisible(false);
			} else {
				setDayBordersVisible(true);
				setDecorationBordersVisible(decorationBordersVisible);
			}
		}
	}

	/**
	 * Gets the maximum number of characters of a day name or 0. If 0 is
	 * returned, dateFormatSymbols.getShortWeekdays() will be used.
	 * 
	 * @return the maximum number of characters of a day name or 0.
	 */
	public int getMaxDayCharacters() {
		return maxDayCharacters;
	}

	/**
	 * Sets the maximum number of characters per day in the day bar. Valid
	 * values are 0-4. If set to 0, dateFormatSymbols.getShortWeekdays() will be
	 * used, otherwise theses strings will be reduced to the maximum number of
	 * characters.
	 * 
	 * @param maxDayCharacters
	 *            the maximum number of characters of a day name.
	 */
	public void setMaxDayCharacters(int maxDayCharacters) {
		if (maxDayCharacters == this.maxDayCharacters) {
			return;
		}

		if (maxDayCharacters < 0 || maxDayCharacters > 4) {
			this.maxDayCharacters = 0;
		} else {
			this.maxDayCharacters = maxDayCharacters;
		}
		drawDayNames();
		drawDays();
		invalidate();
	}

	class DecoratorButton extends JButton {
		private static final long serialVersionUID = -5306477668406547496L;

		public DecoratorButton() {
			setBorderPainted(decorationBordersVisible);
		}

		public void addMouseListener(MouseListener l) {
		}

		public boolean isFocusable() {
			return false;
		}
	};

	/**
	 * Draws the day names of the day columnes.
	 */
	private void drawDayNames() {
		int firstDayOfWeek = calendar.getFirstDayOfWeek();
		DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
		dayNames = dateFormatSymbols.getShortWeekdays();

		int day = firstDayOfWeek;

		for (int i = 0; i < 7; i++) {
			if (maxDayCharacters > 0 && maxDayCharacters < 5) {
				if (dayNames[day].length() >= maxDayCharacters) {
					dayNames[day] = dayNames[day].substring(0, maxDayCharacters);
				}
			}

			days[i].setText(dayNames[day]);

			if (day < 7) {
				day++;
			} else {
				day -= 6;
			}
		}
	}

	/**
	 * Initializes both day names and weeks of the year.
	 */
	protected void initDecorations() {
		for (int x = 0; x < 7; x++) {
			days[x].setBorderPainted(decorationBordersVisible);
			days[x].invalidate();
			days[x].repaint();
		}
	}

	/**
	 * Hides and shows the week buttons.
	 */
	protected void drawWeeks() {
		Calendar tmpCalendar = (Calendar) calendar.clone();

		for (int i = 1; i < 7; i++) {
			tmpCalendar.set(Calendar.DAY_OF_MONTH, (i * 7) - 6);

			int week = tmpCalendar.get(Calendar.WEEK_OF_YEAR);
			String buttonText = Integer.toString(week);

			if (week < 10) {
				buttonText = "0" + buttonText;
			}
		}
	}

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		if (startDate.getTimeInMillis() == endDate.getTimeInMillis()) {
			if (start) {
				if (selectedDayBtn.getName().equals(CalendarStyles.DAYS_SELECTED_START_DAY)) {
					selectedDayBtn.setName(CalendarStyles.DAYS_SELECTED_END_DAY);
				} else if (selectedDayBtn.getName().equals(CalendarStyles.DAYS_SELECTED_START_TODAY)) {
					selectedDayBtn.setName(CalendarStyles.DAYS_SELECTED_END_TODAY);
				}
			} else {
				if (selectedDayBtn.getName().equals(CalendarStyles.DAYS_SELECTED_END_DAY)) {
					selectedDayBtn.setName(CalendarStyles.DAYS_SELECTED_START_DAY);
				} else if (selectedDayBtn.getName().equals(CalendarStyles.DAYS_SELECTED_END_TODAY)) {
					selectedDayBtn.setName(CalendarStyles.DAYS_SELECTED_START_TODAY);
				}
			}
		}
		this.start = start;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		startDate.set(Calendar.HOUR, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);
		startDate.set(Calendar.MILLISECOND, 0);
		this.startDate = startDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}

	public void setEndDate(Calendar endDate) {
		endDate.set(Calendar.HOUR, 0);
		endDate.set(Calendar.MINUTE, 0);
		endDate.set(Calendar.SECOND, 0);
		endDate.set(Calendar.MILLISECOND, 0);
		this.endDate = endDate;
	}

}
