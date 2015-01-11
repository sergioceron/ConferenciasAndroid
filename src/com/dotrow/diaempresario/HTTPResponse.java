package com.dotrow.diaempresario;

import java.io.Serializable;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 2/02/14 10:24 PM
 */
public class HTTPResponse implements Serializable {
	private int responseCode;
	private String responseMessage;
	private String responseBody;
	private String lastModified;

	public HTTPResponse() {
	}

	public HTTPResponse( String body ) {
		this.responseCode = 200;
		this.responseBody = body;
	}

	public HTTPResponse( int code, String responseMessage ) {
		this.responseCode = code;
		this.responseBody = "";
		this.responseMessage = responseMessage;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode( int responseCode ) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage( String responseMessage ) {
		this.responseMessage = responseMessage;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody( String responseBody ) {
		this.responseBody = responseBody;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified( String lastModified ) {
		this.lastModified = lastModified;
	}
}
