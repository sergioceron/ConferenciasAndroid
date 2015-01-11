package com.dotrow.diaempresario.events;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 16/01/14 12:34 PM
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.dotrow.diaempresario.FontManager;
import com.dotrow.diaempresario.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class EventListAdapter extends ArrayAdapter<Event> {
	private final static SimpleDateFormat hourFormatter = new SimpleDateFormat( "HH:mm" );

	private FontManager fontManager;

	private final Context context;
	private final List<Event> itemsArrayList;

	public EventListAdapter( Context context, List<Event> itemsArrayList ) {
		super( context, R.layout.event_item, itemsArrayList );

		this.context = context;
		this.itemsArrayList = itemsArrayList;
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent ) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

		Event event = itemsArrayList.get( position );

		String hour = new String();
		if( event instanceof Conference ) {
			hour = hourFormatter.format( ( (Conference) event ).getDate() );
		} else if( event instanceof Stand ) {
			hour = String.format( "%s\n-\n%s",
					hourFormatter.format( ( (Stand) event ).getStartDate() ),
					hourFormatter.format( ( (Stand) event ).getEndDate() ) );
		}

		View rowView = inflater.inflate( R.layout.event_item, parent, false );


		fontManager = FontManager.getInstance(context.getAssets());

		TextView labelView = (TextView) rowView.findViewById( R.id.label );
		TextView valueView = (TextView) rowView.findViewById( R.id.value );
		TextView dayView = (TextView) rowView.findViewById( R.id.day );
		ImageView staredView = (ImageView) rowView.findViewById( R.id.stared );
		ImageView scheduledView = (ImageView) rowView.findViewById( R.id.scheduled );

		labelView.setTypeface( fontManager.HELVETICA_45 );
		valueView.setTypeface( fontManager.HELVETICA_45 );
		dayView.setTypeface( fontManager.HELVETICA_63 );

		labelView.setText( event.getTitle() );
		valueView.setText( event.getLocation() );
		dayView.setText( hour );

		if( isStared( event ) ) {
			staredView.setImageResource( R.drawable.event_stared );
		} else {
			staredView.setImageResource( R.drawable.event_unstared );
		}

		if( isScheduled( event ) ) {
			scheduledView.setImageResource( R.drawable.event_alarm_set );
		} else {
			scheduledView.setImageResource( R.drawable.event_alarm_unset );
		}

		return rowView;
	}

	private boolean isStared( Event event ){
		SharedPreferences settings = context.getSharedPreferences( "Events", 0 );
		return settings.contains( event.getId() + ":stared" );
	}

	private boolean isScheduled( Event event ){
		SharedPreferences settings = context.getSharedPreferences( "Events", 0 );
		return settings.contains( event.getId() + ":scheduled" );
	}
}