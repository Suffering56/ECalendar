package gui.panel.ecalendar.frames.util.jcalendar;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import gui.panel.ecalendar.frames.util.timezone.LocalTimeZone;
import gui.panel.ecalendar.styles.CalendarStyles;

/**
 * JCalendar is a bean for entering a date by choosing the year, month and day.
 * 
 * @author Kai Toedter
 * @version $LastChangedRevision: 159 $
 * @version $LastChangedDate: 2011-06-22 21:07:24 +0200 (Mi, 22 Jun 2011) $
 */
public class ExtJCalendar extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = 8913369762644440133L;
	private boolean initialized = false;
	protected Locale locale;
	private final JPanel monthYearPanel;

	private Calendar calendar;
	protected ExtJYearChooser yearChooser;
	protected ExtJMonthChooser monthChooser;
	protected ExtJDayChooser dayChooser;

	/**
	 * Default JCalendar constructor.
	 */
	public ExtJCalendar() {
		locale = Locale.getDefault();

		monthYearPanel = new JPanel();
		yearChooser = new ExtJYearChooser();
		monthChooser = new ExtJMonthChooser(true);
		dayChooser = new ExtJDayChooser();

		initStyles();
		otherInit();

		calendar = Calendar.getInstance(LocalTimeZone.getInstance().getCurrent(), this.locale);
		setCalendar(calendar);
	}

	/**
	 * Применение стилей из laf.xml.
	 * 
	 * @return объект стилей для JDayChooser(основной стиль панели, стиль для дней в календаре, стиль для выделенных дней календаря)
	 */
	private void initStyles() {
		this.setName(CalendarStyles.MAIN_PANEL);
		this.monthYearPanel.setName(CalendarStyles.MONTH_AND_YEAR_PANEL);

		this.yearChooser.setName(CalendarStyles.YEAR_PANEL);
		this.yearChooser.spinner.setName(CalendarStyles.YEAR_SPINNER);
		this.yearChooser.textField.setName(CalendarStyles.YEAR_TEXTFIELD);

		this.monthChooser.setName(CalendarStyles.MONTH_PANEL);
		this.monthChooser.getSpinner().setName(CalendarStyles.MONTH_SPINNER);
		this.monthChooser.getComboBox().setName(CalendarStyles.MONTH_COMBOBOX);
	}

	private void otherInit() {
		setLayout(new BorderLayout());
		monthYearPanel.setLayout(new BorderLayout());
		monthYearPanel.add(monthChooser, BorderLayout.WEST);
		monthYearPanel.add(yearChooser, BorderLayout.CENTER);
		monthYearPanel.setBorder(BorderFactory.createEmptyBorder());

		yearChooser.setDayChooser(dayChooser);
		yearChooser.addPropertyChangeListener(this);
		monthChooser.setYearChooser(yearChooser);
		monthChooser.setLocale(this.locale);
		monthChooser.setDayChooser(dayChooser);
		monthChooser.addPropertyChangeListener(this);
		dayChooser.addPropertyChangeListener(this);
		dayChooser.setLocale(this.locale);

		add(monthYearPanel, BorderLayout.NORTH);
		add(dayChooser, BorderLayout.CENTER);
		initialized = true;
	}

	/**
	 * Gets the dayChooser attribute of the JCalendar object
	 * 
	 * @return the dayChooser value
	 */
	public ExtJDayChooser getDayChooser() {
		return dayChooser;
	}

	/**
	 * Returns the locale.
	 * 
	 * @return the value of the locale property.
	 * 
	 * @see #setLocale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Gets the monthChooser attribute of the JCalendar object
	 * 
	 * @return the monthChooser value
	 */
	public ExtJMonthChooser getMonthChooser() {
		return monthChooser;
	}

	/**
	 * Gets the yearChooser attribute of the JCalendar object
	 * 
	 * @return the yearChooser value
	 */
	public ExtJYearChooser getYearChooser() {
		return yearChooser;
	}

	/**
	 * JCalendar is a PropertyChangeListener, for its day, month and year
	 * chooser.
	 * 
	 * @param evt
	 *            the property change event
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (calendar != null) {
			Calendar c = (Calendar) calendar.clone();

			if (evt.getPropertyName().equals("day")) {
				c.set(Calendar.DAY_OF_MONTH,
						((Integer) evt.getNewValue()).intValue());
				setCalendar(c, false);
			} else if (evt.getPropertyName().equals("month")) {
				c.set(Calendar.MONTH, ((Integer) evt.getNewValue()).intValue());
				setCalendar(c, false);
			} else if (evt.getPropertyName().equals("year")) {
				c.set(Calendar.YEAR, ((Integer) evt.getNewValue()).intValue());
				setCalendar(c, false);
			} else if (evt.getPropertyName().equals("date")) {
				c.setTime((Date) evt.getNewValue());
				setCalendar(c, true);
			}
		}
	}

	/**
	 * Sets the calendar property. This is a bound property.
	 * 
	 * @param c
	 *            the new calendar
	 * @throws NullPointerException
	 *             - if c is null;
	 * @see #getCalendar
	 */
	public void setCalendar(Calendar c) {
		setCalendar(c, true);
	}

	/**
	 * Returns the calendar property.
	 * 
	 * @return the value of the calendar property.
	 */
	public Calendar getCalendar() {
		return calendar;
	}

	/**
	 * Sets the calendar attribute of the JCalendar object
	 * 
	 * @param c
	 *            the new calendar value
	 * @param update
	 *            the new calendar value
	 * @throws NullPointerException
	 *             - if c is null;
	 */
	private void setCalendar(Calendar c, boolean update) {
		if (c == null) {
			setDate(null);
		}
		Calendar oldCalendar = calendar;
		calendar = c;

		if (update) {
			yearChooser.setYear(c.get(Calendar.YEAR));
			monthChooser.setMonth(c.get(Calendar.MONTH));
			dayChooser.setDay(c.get(Calendar.DATE));
		}

		firePropertyChange("calendar", oldCalendar, calendar);
	}

	/**
	 * Returns a Date object.
	 * 
	 * @return a date object constructed from the calendar property.
	 */
	public Date getDate() {
		return new Date(calendar.getTimeInMillis());
	}

	/**
	 * Sets the date. Fires the property change "date".
	 * 
	 * @param date
	 *            the new date.
	 * @throws NullPointerException
	 *             - if the date is null
	 */
	public void setDate(Date date) {
		Date oldDate = calendar.getTime();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		yearChooser.setYear(year);
		monthChooser.setMonth(month);
		dayChooser.setCalendar(calendar);
		dayChooser.setDay(day);

		firePropertyChange("date", oldDate, date);
	}

	/**
	 * Enable or disable the JCalendar.
	 * 
	 * @param enabled
	 *            the new enabled value
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		if (dayChooser != null) {
			dayChooser.setEnabled(enabled);
			monthChooser.setEnabled(enabled);
			yearChooser.setEnabled(enabled);
		}
	}

	/**
	 * Returns true, if enabled.
	 * 
	 * @return true, if enabled.
	 */
	public boolean isEnabled() {
		return super.isEnabled();
	}

	/**
	 * Sets the locale property. This is a bound property.
	 * 
	 * @param newLocale
	 *            the new locale value
	 * 
	 * @see #getLocale
	 */
	public void setLocale(Locale newLocale) {
		if (!initialized) {
			super.setLocale(newLocale);
		} else {
			Locale oldLocale = locale;
			locale = newLocale;
			dayChooser.setLocale(locale);
			monthChooser.setLocale(locale);
			firePropertyChange("locale", oldLocale, locale);
		}
	}

	/**
	 * Gets the visibility of the decoration border.
	 * 
	 * @return true, if the decoration border is visible.
	 */
	public boolean isDecorationBordersVisible() {
		return dayChooser.isDecorationBordersVisible();
	}

	/**
	 * Sets the decoration borders visible.
	 * 
	 * @param decorationBordersVisible
	 *            true, if the decoration borders should be visible.
	 */
	public void setDecorationBordersVisible(boolean decorationBordersVisible) {
		dayChooser.setDecorationBordersVisible(decorationBordersVisible);
		setLocale(locale);
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
		dayChooser.setMaxDayCharacters(maxDayCharacters);
	}

	/**
	 * Gets the maximum number of characters of a day name or 0. If 0 is
	 * returned, dateFormatSymbols.getShortWeekdays() will be used.
	 * 
	 * @return the maximum number of characters of a day name or 0.
	 */
	public int getMaxDayCharacters() {
		return dayChooser.getMaxDayCharacters();
	}
}
