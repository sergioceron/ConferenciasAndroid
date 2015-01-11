package com.dotrow.diaempresario.events;

import java.util.Date;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 21/01/14 07:01 PM
 */
public class Stand extends Event {
	private Date startDate;
	private Date endDate;
	private String organization;

	public Stand() {
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate( Date startDate ) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate( Date endDate ) {
		this.endDate = endDate;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization( String organization ) {
		this.organization = organization;
	}
}
