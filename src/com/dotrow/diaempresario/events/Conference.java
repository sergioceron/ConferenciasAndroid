package com.dotrow.diaempresario.events;

import java.util.Date;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 21/01/14 07:00 PM
 */
public class Conference extends Event {
	private Date date;
	private String author;

	public Conference() {
	}

	public Date getDate() {
		return date;
	}

	public void setDate( Date date ) {
		this.date = date;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor( String author ) {
		this.author = author;
	}
}
