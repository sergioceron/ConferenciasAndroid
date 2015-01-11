package com.dotrow.diaempresario.events;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import com.dotrow.diaempresario.R;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 26/01/14 01:38 PM
 */
public class EventListListener implements AdapterView.OnItemClickListener {

	private FragmentActivity main;

	public EventListListener( FragmentActivity main ) {
		this.main = main;
	}

	@Override
	public void onItemClick( AdapterView parent, View view, int position, long id ) {
		Event event = (Event) parent.getItemAtPosition( position );

		/*Bundle args = new Bundle();
		args.putSerializable( "event", event );*/


		Intent i = new Intent( main.getApplicationContext(), EventDetailActivity.class );
		i.putExtra( "event", event );
		main.startActivity( i );
		//main.overridePendingTransition(0, R.animator.event_detail_slidedown);


		/*EventDetailFragment eventDetailFragment = new EventDetailActivity();
		eventDetailFragment.setArguments( args );

		FragmentTransaction ft = main.getSupportFragmentManager().beginTransaction();
		ft.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right,
								android.R.anim.slide_in_left, android.R.anim.slide_out_right );
		ft.replace( android.R.id.tabcontent, eventDetailFragment );
		ft.addToBackStack( null );
		ft.commit(); */


		/*ActionBar actionBar = main.getActionBar();
		actionBar.setNavigationMode( ActionBar.NAVIGATION_MODE_STANDARD );
		actionBar.setDisplayHomeAsUpEnabled( true );
		actionBar.setDisplayUseLogoEnabled( true );
		actionBar.setDisplayShowHomeEnabled( true );*/
	}

}
