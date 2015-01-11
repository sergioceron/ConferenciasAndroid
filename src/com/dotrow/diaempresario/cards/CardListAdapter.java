package com.dotrow.diaempresario.cards;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 16/01/14 12:34 PM
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.dotrow.diaempresario.FontManager;
import com.dotrow.diaempresario.R;

import java.util.List;

public class CardListAdapter extends ArrayAdapter<ContactCard> {

	private FontManager fontManager;

	private final Context context;
	private final List<ContactCard> itemsArrayList;

	public CardListAdapter( Context context, List<ContactCard> itemsArrayList ) {
		super( context, R.layout.card_item, itemsArrayList );

		this.context = context;
		this.itemsArrayList = itemsArrayList;
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent ) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		ContactCard card = itemsArrayList.get( position );

		fontManager = FontManager.getInstance(context.getAssets());

		View rowView = inflater.inflate( R.layout.card_item, parent, false );

		TextView nameView = (TextView) rowView.findViewById( R.id.name );
		TextView jobView = (TextView) rowView.findViewById( R.id.job );
		TextView arrowView = (TextView) rowView.findViewById( R.id.arrow );

		nameView.setTypeface( fontManager.HELVETICA_45 );
		jobView.setTypeface( fontManager.HELVETICA_45 );
		arrowView.setTypeface( fontManager.HELVETICA_77 );

		nameView.setText( card.getName() );
		jobView.setText( card.getJob() );

		return rowView;
	}
}