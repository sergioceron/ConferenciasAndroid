package com.dotrow.diaempresario.calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dotrow.diaempresario.FragmentPageAdapter;
import com.dotrow.diaempresario.R;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 30/01/14 10:47 PM
 */
public class CalendarFragment extends Fragment implements Serializable {

	private CalendarListener listener;

	private Date selDate;
	private Date minDate;
	private Date maxDate;

	private Bundle bundle;

	public CalendarFragment() {
		this.bundle = new Bundle();
		setArguments( bundle );
	}

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onActivityCreated( savedInstanceState );

		this.bundle = getArguments();

		this.selDate = getSelDate();
		this.minDate = getMinDate();
		this.maxDate = getMaxDate();
		this.listener = getListener();
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		ViewGroup rootView = (ViewGroup) inflater.inflate( R.layout.calendar, container, false );

		Calendar calendar = Calendar.getInstance();

		calendar.setTime( minDate );
		int startWeek = calendar.get( Calendar.WEEK_OF_YEAR );
		int startDay = calendar.get( Calendar.DAY_OF_WEEK ) - calendar.getFirstDayOfWeek();

		calendar.setTime( maxDate );
		int endWeek = calendar.get( Calendar.WEEK_OF_YEAR );
		int endDay = calendar.get( Calendar.DAY_OF_WEEK ) - calendar.getFirstDayOfWeek();

		calendar.setTime( selDate );
		int iniWeek = calendar.get( Calendar.WEEK_OF_YEAR );

		int year = calendar.get( Calendar.YEAR );

		FragmentPageAdapter adapter = new FragmentPageAdapter( getChildFragmentManager() );

		for( int week = startWeek; week <= endWeek; week++ ) {
			CalendarWeekFragment weekFragment = new CalendarWeekFragment();
			weekFragment.setCalendar( this );
			weekFragment.setYear( year );
			weekFragment.setWeek( week );
			if( week == startWeek ) {
				weekFragment.setStartDay( startDay );
				weekFragment.setEndDay( 6 );
			} else if( week == endWeek ) {
				weekFragment.setStartDay( 0 );
				weekFragment.setEndDay( endDay );
			} else {
				weekFragment.setStartDay( 0 );
				weekFragment.setEndDay( 6 );
			}

			adapter.addFragment( weekFragment );
		}

		ViewPager weekPager = (ViewPager) rootView.findViewById( R.id.weekPager );
		weekPager.setAdapter( adapter );
		weekPager.setCurrentItem( iniWeek - startWeek );

		return rootView;
	}

	public void selectDay( Date day ) {
		setSelDate( day );
		List<Fragment> fragments = getChildFragmentManager().getFragments();
		for( Fragment fragment : fragments ) {
			if( fragment instanceof CalendarWeekFragment ){
				( (CalendarWeekFragment) fragment ).buildWeek();
			}
		}
		listener.onSelectDay( day );
	}

	public CalendarListener getListener() {
		return (CalendarListener) bundle.get( "listener" );
	}

	public void setListener( CalendarListener listener ) {
		bundle.putSerializable( "listener", listener );
	}

	public Date getSelDate() {
		return (Date) bundle.get( "selDate" );
	}

	public void setSelDate( Date selDate ) {
		bundle.putSerializable( "selDate", selDate );
	}

	public Date getMinDate() {
		return (Date) bundle.get( "minDate" );
	}

	public void setMinDate( Date minDate ) {
		bundle.putSerializable( "minDate", minDate );
	}

	public Date getMaxDate() {
		return (Date) bundle.get( "maxDate" );
	}

	public void setMaxDate( Date maxDate ) {
		bundle.putSerializable( "maxDate", maxDate );
	}
}
