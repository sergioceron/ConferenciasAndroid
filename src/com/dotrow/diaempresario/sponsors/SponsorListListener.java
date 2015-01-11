package com.dotrow.diaempresario.sponsors;

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
public class SponsorListListener implements AdapterView.OnItemClickListener {
	private FragmentActivity main;

	public SponsorListListener( FragmentActivity main ) {
		this.main = main;
	}

	@Override
	public void onItemClick( AdapterView parent, View view, int position, long id ) {
		Sponsor sponsor = (Sponsor) parent.getItemAtPosition( position );


		Intent i = new Intent( main.getApplicationContext(), SponsorDetailActivity.class );
		i.putExtra( "sponsor", sponsor );
		main.startActivity( i );
		//main.overridePendingTransition(0, R.animator.event_detail_slidedown);

		/*Bundle args = new Bundle();
		args.putSerializable( "sponsor", sponsor );

		SponsorDetailActivity sponsorDetailActivity = new SponsorDetailActivity();
		sponsorDetailActivity.setArguments( args );

		FragmentTransaction ft = main.getSupportFragmentManager().beginTransaction();
		ft.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right,
				android.R.anim.slide_in_left, android.R.anim.slide_out_right );
		ft.add( android.R.id.tabcontent, sponsorDetailActivity );
		ft.addToBackStack( null );
		ft.commit();  */
	}

}
