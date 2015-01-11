package com.dotrow.diaempresario.sponsors;

import java.io.Serializable;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 28/01/14 11:45 PM
 */
public class Sponsor implements Serializable, Comparable<Sponsor> {
	private String id;
	private String url;
	private String name;
	private String email;
	private String image;
	private String subtype;

	public Sponsor() {
	}

	public String getId() {
		return id;
	}

	public void setId( String id ) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl( String url ) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail( String email ) {
		this.email = email;
	}

	public String getImage() {
		return image;
	}

	public void setImage( String image ) {
		this.image = image;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype( String subtype ) {
		this.subtype = subtype;
	}

	public int getSubtypeIndex() {
		if( getSubtype().equals( "sponsor" ) ) {
			return 1;
		} else if( getSubtype().equals( "allied" ) ) {
			return 2;
		}
		return 3;
	}

	@Override
	public int compareTo( Sponsor other ) {
		return other.getSubtypeIndex() - this.getSubtypeIndex();
	}
}
