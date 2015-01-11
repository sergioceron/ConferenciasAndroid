package com.dotrow.diaempresario;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 20/01/14 03:14 PM
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class FragmentPageAdapter extends FragmentPagerAdapter {

	List<Fragment> fragments;

	public FragmentPageAdapter( FragmentManager fm ) {
		super( fm );
		this.fragments = new ArrayList<Fragment>();
	}

	public void addFragment( Fragment fragment ) {
		this.fragments.add( fragment );
	}

	@Override
	public Fragment getItem( int index ) {
		return this.fragments.get( index );
	}

	@Override
	public int getCount() {
		return this.fragments.size();
	}

}
