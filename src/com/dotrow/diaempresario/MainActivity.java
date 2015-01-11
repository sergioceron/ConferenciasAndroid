package com.dotrow.diaempresario;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.widget.Toast;
import com.dotrow.diaempresario.cards.CardListFragment;
import com.dotrow.diaempresario.events.EventListFragment;
import com.dotrow.diaempresario.sponsors.SponsorListFragment;


/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 26/01/14 10:01 PM
 */
public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
	private static final int SIGNIN_REQUEST = 1;
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	private Fragment currentFragment;

	private EventListFragment eventListFragment;
	private CardListFragment cardListFragment;
	private SponsorListFragment sponsorListFragment;
	private MapFragment mapFragment;
	private HomeFragment homeFragment;

	public MainActivity() {
		if( android.os.Build.VERSION.SDK_INT > 9 ) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy( policy );
		}
	}

	@Override
	protected void onPause() {
		try {
			super.onPause();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		//FontManager.newInstance( getAssets() );

		SharedPreferences settings = getSharedPreferences( "Configuration", 0 );

		if( settings.contains( "hash" ) ) {
			initView();
		} else {
			Intent i = new Intent( getApplicationContext(), SigninActivity.class );
			startActivityForResult( i, SIGNIN_REQUEST );
		}
	}

	public void initView() {
		setContentView( R.layout.main );
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode( ActionBar.NAVIGATION_MODE_TABS );
		actionBar.setDisplayHomeAsUpEnabled( false );
		actionBar.setHomeButtonEnabled( false );
		actionBar.setDisplayUseLogoEnabled( false );
		actionBar.setDisplayShowTitleEnabled( false );
		actionBar.setDisplayShowHomeEnabled( false );
		// for each of the sections in the app, add a tab to the action bar.
		actionBar.addTab( actionBar.newTab().setText( "" ).setIcon( R.drawable.icon_home ).setTabListener( this ) );
		actionBar.addTab( actionBar.newTab().setText( "" ).setIcon( R.drawable.icon_events ).setTabListener( this ) );
		actionBar.addTab( actionBar.newTab().setText( "" ).setIcon( R.drawable.icon_sponsors ).setTabListener( this ) );
		actionBar.addTab( actionBar.newTab().setText( "" ).setIcon( R.drawable.icon_contacts ).setTabListener( this ) );
		actionBar.addTab( actionBar.newTab().setText( "" ).setIcon( R.drawable.icon_map ).setTabListener( this ) );
	}

	@Override
	public void onActivityResult( int requestCode, int resultCode, Intent data ) {
		super.onActivityResult( requestCode, resultCode, data );
		switch( requestCode ) {
			case SIGNIN_REQUEST:
				if( resultCode == Activity.RESULT_OK ) {
					boolean signedIn = data.getBooleanExtra( "signedIn", false );
					if( signedIn ) {
						initView();
					}
				}
				break;
		}
	}

	public void onBackPressed(){
	}

	@Override
	public void onRestoreInstanceState( Bundle savedInstanceState ) {
		super.onRestoreInstanceState( savedInstanceState );
		// Restore the previously serialized current tab position.
		if( savedInstanceState.containsKey( STATE_SELECTED_NAVIGATION_ITEM ) ) {
			int navigationItem = savedInstanceState.getInt( STATE_SELECTED_NAVIGATION_ITEM );
			if( navigationItem != -1 ) {
				getActionBar().setSelectedNavigationItem( navigationItem );
			}
		}
	}

	@Override
	public void onSaveInstanceState( Bundle outState ) {
		super.onSaveInstanceState( outState );
		// Serialize the current tab position.
		int navigationItem = getActionBar().getSelectedNavigationIndex();
		outState.putInt( STATE_SELECTED_NAVIGATION_ITEM, navigationItem );
	}

	@Override
	public void onTabSelected( ActionBar.Tab tab, FragmentTransaction ft ) {
		FragmentTransaction fta = getFragmentManager().beginTransaction();
		if( currentFragment != null )
			fta.detach( currentFragment );

		switch( tab.getPosition() ) {
			case 0:
				if( homeFragment == null ) {
					homeFragment = new HomeFragment();
					fta.replace( android.R.id.tabcontent, homeFragment );
				}
				currentFragment = homeFragment;
				break;
			case 1:
				if( eventListFragment == null ) {
					eventListFragment = new EventListFragment();
					fta.replace( android.R.id.tabcontent, eventListFragment );
				}
				currentFragment = eventListFragment;
				break;
			case 3:
				if( cardListFragment == null ) {
					cardListFragment = new CardListFragment();
					fta.replace( android.R.id.tabcontent, cardListFragment );
				}
				currentFragment = cardListFragment;
				break;
			case 2:
				if( sponsorListFragment == null ) {
					sponsorListFragment = new SponsorListFragment();
					fta.replace( android.R.id.tabcontent, sponsorListFragment );
				}
				currentFragment = sponsorListFragment;
				break;
			case 4:
				if( mapFragment == null ) {
					mapFragment = new MapFragment();
					fta.replace( android.R.id.tabcontent, mapFragment );
				}
				currentFragment = mapFragment;
				break;
		}
		fta.attach( currentFragment );
		fta.addToBackStack( null );
		fta.commit();
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem menuItem ) {
		FragmentManager fm = getSupportFragmentManager();
		if( fm.getBackStackEntryCount() > 0 ) {
			fm.popBackStack();
		}
		return super.onOptionsItemSelected( menuItem );
	}

	@Override
	public void onTabUnselected( ActionBar.Tab tab, FragmentTransaction ft ) {
	}

	@Override
	public void onTabReselected( ActionBar.Tab tab, FragmentTransaction ft ) {

	}
}
