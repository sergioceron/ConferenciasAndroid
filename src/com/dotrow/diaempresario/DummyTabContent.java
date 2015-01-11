package com.dotrow.diaempresario;

import android.content.Context;
import android.view.View;
import android.widget.TabHost.TabContentFactory;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 3/02/14 08:47 PM
 */

public class DummyTabContent implements TabContentFactory {
	private Context mContext;

	public DummyTabContent( Context context ) {
		mContext = context;
	}

	@Override
	public View createTabContent( String tag ) {
		View v = new View( mContext );
		return v;
	}
}