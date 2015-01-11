package com.dotrow.diaempresario.calendar;

import java.io.Serializable;
import java.util.Date;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 30/01/14 06:47 PM
 */
public interface CalendarListener extends Serializable {
	public void onSelectDay( Date day );
}
