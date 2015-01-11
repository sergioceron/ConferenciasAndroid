package com.dotrow.diaempresario.calendar;

import java.util.Calendar;
import java.util.Date;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 20/01/14 01:27 PM
 */
public class Day {
	private static final String[] WEEK = { "DOM", "LUN", "MAR", "MIE", "JUE", "VIE", "SAB" };

	private Date date;
	private int dayNumber;
	private String dayName;
	private boolean selected;
	private boolean enabled;

	public Day( Date date ) {
		this.date = date;

		Calendar cal = Calendar.getInstance();
		cal.setTime( date );

		this.dayNumber = cal.get( Calendar.DAY_OF_MONTH );
		this.dayName = WEEK[cal.get( Calendar.DAY_OF_WEEK ) - 1];
		this.selected = false;
		this.enabled = true;
	}

	/*public Day( Date date, boolean selected, boolean enabled ) {
		this( date );
		this.selected = selected;
		this.enabled = enabled;
	}*/

	public int getDayNumber() {
		return dayNumber;
	}

	public String getDayName() {
		return dayName;
	}

	public void setSelected( boolean selected ) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setEnabled( boolean enabled ) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public Date getDate() {
		return date;
	}
}
