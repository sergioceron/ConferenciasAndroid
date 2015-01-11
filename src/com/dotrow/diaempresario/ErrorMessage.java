package com.dotrow.diaempresario;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 4/03/14 10:52 PM
 */
public class ErrorMessage {
	private String title;
	private String message;

	public ErrorMessage() {
	}

	public ErrorMessage( String title, String message ) {
		this.title = title;
		this.message = message;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle( String title ) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage( String message ) {
		this.message = message;
	}
}
