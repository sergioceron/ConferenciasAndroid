package com.dotrow.diaempresario;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 9/02/14 11:50 PM
 */

public class ConnectionDetector {

	private Context context;

	public ConnectionDetector( Context context ) {
		this.context = context;
	}

	/**
	 * Checking for all possible internet providers
	 * *
	 */
	public boolean isConnectedToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
		if( connectivity != null ) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if( info != null )
				for( int i = 0; i < info.length; i++ )
					if( info[i].getState() == NetworkInfo.State.CONNECTED ) {
						return true;
					}

		}
		return false;
	}
}