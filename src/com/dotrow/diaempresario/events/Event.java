package com.dotrow.diaempresario.events;


import java.io.Serializable;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 16/01/14 12:34 PM
 */
public class Event implements Serializable {
	public enum Type {
		CONFERENCE,
		STAND
	};

	private String id;
	private String title;
	private String description;
	private String location;
	private String image;

	public Event() { }

	public Event(String title, String description) {
		super();
		this.title = title;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle( String title ) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription( String description ) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId( String id ) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation( String location ) {
		this.location = location;
	}

	public String getImage() {
		return image;
	}

	public void setImage( String image ) {
		this.image = image;
	}

}

