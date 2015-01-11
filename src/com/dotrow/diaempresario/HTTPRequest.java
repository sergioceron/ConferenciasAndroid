package com.dotrow.diaempresario;

import android.content.Context;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 2/03/14 05:16 PM
 */
public class HTTPRequest {
	private Context context;

	private String host;
	private String method = "GET";
	private String path = "/";
	private String lastModified = "0";
	private HTTPResponse response;
	private String content = "";

	public HTTPRequest( Context context ) {
		this.context = context;
	}

	public HTTPResponse execute() {
		ConnectionDetector connectionDetector = new ConnectionDetector( context );

		if( !connectionDetector.isConnectedToInternet() ) {
			return new HTTPResponse( 504, "No se encuentra conectado a ninguna red de datos" );
		}

		InputStream is = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL( host + path ).openConnection();
			conn.setReadTimeout( 100 * 1000 );
			conn.setConnectTimeout( 60 * 1000 );
			conn.setRequestMethod( method );
			conn.setDoInput( true );
			conn.setRequestProperty( "If-None-Match", lastModified );

			if( method.equals( "POST" ) || method.equals( "PUT" ) ){
				conn.setRequestProperty( "Content-Type", "application/json;charset=utf-8" );

				conn.connect();

				OutputStream os = new BufferedOutputStream( conn.getOutputStream() );
				os.write( content.getBytes( "UTF-8" ) );
				os.flush();
				os.close();
			} else {
				conn.connect();
			}

			switch( conn.getResponseCode() ) {
				case 200:
					is = conn.getInputStream();
					response = new HTTPResponse( readStream( is ) );
					response.setLastModified( conn.getHeaderField("ETag") );
					break;
				default:
					response = new HTTPResponse( conn.getResponseCode(), conn.getResponseMessage() );
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			response = new HTTPResponse( 600, e.getMessage() );
		} finally {
			if( is != null ) {
				try {
					is.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
		}
		return response;
	}

	private String readStream( InputStream in ) throws IOException {
		BufferedReader r;
		r = new BufferedReader( new InputStreamReader( in ) );
		StringBuilder total = new StringBuilder();
		String line;
		while( ( line = r.readLine() ) != null ) {
			total.append( line );
		}
		if( r != null ) {
			r.close();
		}
		in.close();
		return total.toString();
	}

	public String getHost() {
		return host;
	}

	public void setHost( String host ) {
		this.host = host;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod( String method ) {
		this.method = method;
	}

	public String getPath() {
		return path;
	}

	public void setPath( String path ) {
		this.path = path;
	}

	public HTTPResponse getResponse() {
		return response;
	}

	public void setResponse( HTTPResponse response ) {
		this.response = response;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified( String lastModified ) {
		this.lastModified = lastModified;
	}

	public void setContent( String content ) {
		this.content = content;
	}
}
