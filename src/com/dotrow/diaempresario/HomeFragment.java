package com.dotrow.diaempresario;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 27/01/14 08:02 PM
 */
public class HomeFragment extends Fragment {
	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		ViewGroup rootView = (ViewGroup) inflater.inflate( R.layout.splash_screen, container, false );
		return rootView;
	}

}
