package com.dotrow.diaempresario.calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.dotrow.diaempresario.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 20/01/14 11:22 AM
 */
public class CalendarWeekFragment extends Fragment {
	CalendarWeekAdapter calendarWeekAdapter;
	private CalendarFragment calendar;

	private int year;
	private int week;
	private int startDay;
	private int endDay;

	private Bundle bundle;

	public CalendarWeekFragment() {
		bundle = new Bundle();
		setArguments( bundle );
	}
	// fragment.setRetainInstance( true );

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onActivityCreated( savedInstanceState );

		this.bundle = getArguments();

		this.week = getWeek();
		this.year = getYear();
		this.startDay = getStartDay();
		this.endDay = getEndDay();
		this.calendar = getCalendar();
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		ViewGroup rootView = (ViewGroup) inflater.inflate( R.layout.calendar_week, container, false );

		calendarWeekAdapter = new CalendarWeekAdapter( getActivity(), new ArrayList<Day>() );
		buildWeek();

		GridView calendarView = (GridView) rootView.findViewById( R.id.calendarView );
		calendarView.setAdapter( calendarWeekAdapter );

		calendarView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			public void onItemClick( AdapterView parent, View v, int position, long id ) {
				Calendar date = Calendar.getInstance();
				date.clear();
				date.set( Calendar.YEAR, year );
				date.set( Calendar.WEEK_OF_YEAR, week );
				date.set( Calendar.DAY_OF_WEEK, position + 1 );

				calendar.selectDay( date.getTime() );
			}
		} );

		return rootView;
	}

	public void buildWeek(){
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set( Calendar.YEAR, year );
		cal.set( Calendar.WEEK_OF_YEAR, week );
		cal.add( Calendar.DATE, -1 );

		ArrayList<Day> days = new ArrayList<Day>();
		for( int i = 0; i < 7; i++ ) {
			cal.add( Calendar.DATE, 1 );

			Day day = new Day( cal.getTime() );
			day.setSelected( compareDays( day.getDate(), calendar.getSelDate() ) );
			day.setEnabled( i <= endDay && i >= startDay );

			days.add( day );
		}
		calendarWeekAdapter.clear();
		calendarWeekAdapter.addAll( days );
	}

	private boolean compareDays(Date a, Date b){
		Calendar cal = Calendar.getInstance();
		cal.setTime( a );
		int year_a = cal.get( Calendar.YEAR );
		int day_a = cal.get( Calendar.DAY_OF_YEAR );
		cal.setTime( b );
		int year_b = cal.get( Calendar.YEAR );
		int day_b = cal.get( Calendar.DAY_OF_YEAR );

		return year_a == year_b && day_a == day_b;
	}

	public CalendarFragment getCalendar() {
		return (CalendarFragment) bundle.get( "calendar" );
	}

	public void setCalendar( CalendarFragment calendar ) {
		bundle.putSerializable( "calendar", calendar );
	}

	public int getYear() {
		return bundle.getInt( "year" );
	}

	public void setYear( int year ) {
		bundle.putInt( "year", year );
	}

	public int getWeek() {
		return bundle.getInt( "week" );
	}

	public void setWeek( int week ) {
		bundle.putInt( "week", week );
	}

	public int getStartDay() {
		return bundle.getInt( "startDay" );
	}

	public void setStartDay( int startDay ) {
		bundle.putInt( "startDay", startDay );
	}

	public int getEndDay() {
		return bundle.getInt( "endDay" );
	}

	public void setEndDay( int endDay ) {
		bundle.putInt( "endDay", endDay );
	}
}