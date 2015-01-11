package com.dotrow.diaempresario.calendar;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 16/01/14 12:34 PM
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.dotrow.diaempresario.FontManager;
import com.dotrow.diaempresario.R;

import java.util.ArrayList;

public class CalendarWeekAdapter extends ArrayAdapter<Day> {
	private FontManager fontManager;

	private final Context context;
	private final ArrayList<Day> itemsArrayList;

	public CalendarWeekAdapter( Context context, ArrayList<Day> itemsArrayList ) {
		super( context, R.layout.event_item, itemsArrayList );
		this.context = context;
		this.itemsArrayList = itemsArrayList;
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent ) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

		View rowView = inflater.inflate( R.layout.calendar_day, parent, false );


		fontManager = FontManager.getInstance(context.getAssets());

		TextView dayOfWeek = (TextView) rowView.findViewById( R.id.dayOfWeek );
		TextView dayOfMonth = (TextView) rowView.findViewById( R.id.dayOfMonth );

		dayOfWeek.setTypeface( fontManager.HELVETICA_65 );
		dayOfMonth.setTypeface( fontManager.HELVETICA_37 );

		if( itemsArrayList.get( position ).isSelected() ) {
			dayOfMonth.setTextColor( Color.parseColor( "#C1FF09" ) );
			dayOfWeek.setTextColor( Color.parseColor( "#FFFFFF" ) );
		}

		if( !itemsArrayList.get( position ).isEnabled() ) {
			dayOfMonth.setTextColor( Color.parseColor( "#333333" ) );
			dayOfWeek.setTextColor( Color.parseColor( "#555555" ) );
		}

		dayOfWeek.setText( itemsArrayList.get( position ).getDayName() );
		dayOfMonth.setText( String.valueOf( itemsArrayList.get( position ).getDayNumber() ) );

		return rowView;
	}

	@Override
	public boolean isEnabled( int position ) {
		return itemsArrayList.get( position ).isEnabled();
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}
}