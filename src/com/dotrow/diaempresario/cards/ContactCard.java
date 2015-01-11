package com.dotrow.diaempresario.cards;

import java.io.Serializable;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 28/01/14 11:45 PM
 */
public class ContactCard implements Serializable {
	private Long id;
	private String name;
	private String email;
	private String organization;
	private String job;
	private String phone;
	private String image;
	private String type;

	public ContactCard() {
	}

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
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

	public String getOrganization() {
		return organization;
	}

	public void setOrganization( String organization ) {
		this.organization = organization;
	}

	public String getJob() {
		return job;
	}

	public void setJob( String job ) {
		this.job = job;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone( String phone ) {
		this.phone = phone;
	}

	public String getImage() {
		return image;
	}

	public void setImage( String image ) {
		this.image = image;
	}

	public String getType() {
		return type;
	}

	public void setType( String type ) {
		this.type = type;
	}

	@Override
	public boolean equals( Object o ) {
		if( o instanceof ContactCard ){
			return getName().equals( ( (ContactCard) o ).getName() ) &&
					getEmail().equals( ( (ContactCard) o ).getEmail() ) &&
					getPhone().equals( ( (ContactCard) o ).getPhone() );
		}
		return false;
	}
}
