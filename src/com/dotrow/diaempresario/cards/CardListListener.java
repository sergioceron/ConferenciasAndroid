package com.dotrow.diaempresario.cards;

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
public class CardListListener implements AdapterView.OnItemClickListener {
	private FragmentActivity main;

	public CardListListener( FragmentActivity main ) {
		this.main = main;
	}

	@Override
	public void onItemClick( AdapterView parent, View view, int position, long id ) {
		ContactCard card = (ContactCard) parent.getItemAtPosition( position );

		Intent i = new Intent( main.getApplicationContext(), CardDetailActivity.class );
		i.putExtra( "card", card );

		main.startActivity( i );
		//main.overridePendingTransition(0, R.animator.event_detail_slidedown);


		/*CardDetailFragment cardDetailFragment = new CardDetailActivity();
		cardDetailFragment.setArguments( args );

		FragmentTransaction ft = main.getSupportFragmentManager().beginTransaction();
		ft.setCustomAnimations( android.R.anim.slide_in_left, android.R.anim.slide_out_right,
				android.R.anim.slide_in_left, android.R.anim.slide_out_right );
		ft.add( android.R.id.tabcontent, cardDetailFragment );
		ft.addToBackStack( null );
		ft.commit(); */
	}

}
