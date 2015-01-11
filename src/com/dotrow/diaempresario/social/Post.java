package com.dotrow.diaempresario.social;

import java.util.Date;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 10/02/14 09:45 PM
 */
public class Post {
	private Long id;
	private Date date;
	private String text;

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate( Date date ) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText( String text ) {
		this.text = text;
	}
}
