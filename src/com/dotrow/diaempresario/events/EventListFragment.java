package com.dotrow.diaempresario.events;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import com.dotrow.diaempresario.DummyTabContent;
import com.dotrow.diaempresario.ErrorMessage;
import com.dotrow.diaempresario.FontManager;
import com.dotrow.diaempresario.R;
import com.dotrow.diaempresario.calendar.CalendarFragment;
import com.dotrow.diaempresario.calendar.CalendarListener;
import flexjson.JSONDeserializer;
import flexjson.locators.TypeLocator;
import flexjson.transformer.DateTransformer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Event>>, CalendarListener {
	private FontManager fontManager;

	private Event.Type currentType = Event.Type.CONFERENCE;
	private EventListAdapter eventListAdapter;
	private CalendarFragment calendarFragment;
	private FragmentActivity main;

	private List<Event> events;

	private LinearLayout loadSpinner;
	private ListView eventListView;

	private boolean preloaded;

	private Date minDate;
	private Date maxDate;
	private Date selDate;

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onActivityCreated( savedInstanceState );

		this.main = (FragmentActivity) getActivity();

		SharedPreferences settings = main.getSharedPreferences( "Configuration", 0 );
		minDate = new Date( settings.getLong( "mindate", 0L ) );
		maxDate = new Date( settings.getLong( "maxdate", 0L ) );
		selDate = getInitDate( minDate, maxDate );

		if( eventsFileExist() ) {
			String json = readEventsFile();
			JSONDeserializer<List<Event>> deserializer = new JSONDeserializer<List<Event>>();

			TypeLocator<String> add = new TypeLocator<String>( "type" )
					.add( "conference", Conference.class )
					.add( "stand", Stand.class );

			events = deserializer
					.use( "values", add )
					.use( "values.date", new DateTransformer( "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" ) )
					.use( "values.startDate", new DateTransformer( "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" ) )
					.use( "values.endDate", new DateTransformer( "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" ) )
					.deserialize( json );

			preloaded = true;
		}

	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		ViewGroup rootView = (ViewGroup) inflater.inflate( R.layout.events, container, false );
		eventListView = (ListView) rootView.findViewById( R.id.eventsView );
		loadSpinner = (LinearLayout) rootView.findViewById( R.id.progressBar1 );

		//region Tab Host Setup
		TabHost tabHost = (TabHost) rootView.findViewById( android.R.id.tabhost );
		tabHost.setup();


		fontManager = FontManager.getInstance(main.getAssets());

		TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged( String tabId ) {
				if( tabId.equals( "conferences" ) ) {
					currentType = Event.Type.CONFERENCE;
				} else if( tabId.equals( "stands" ) ) {
					currentType = Event.Type.STAND;
				}

				if( eventListAdapter != null ) {
					eventListAdapter.clear();
//					eventListAdapter.addAll( filter( calendarFragment.getSelDate() ) );
					eventListAdapter.addAll( filter( selDate ) );
				}
			}
		};

		tabHost.setOnTabChangedListener( tabChangeListener );

		/*TabHost.TabSpec tabConferences = tabHost.newTabSpec( "conferences" );
		tabConferences.setIndicator( "Conferencias" );
		tabConferences.setContent( new DummyTabContent( main.getBaseContext() ) );
		tabHost.addTab( tabConferences );

		TabHost.TabSpec tabStands = tabHost.newTabSpec( "stands" );
		tabStands.setIndicator( "Stands" );
		tabStands.setContent( new DummyTabContent( main.getBaseContext() ) );
		tabHost.addTab( tabStands );*/
		//endregion

		for( int i = 0; i < tabHost.getTabWidget().getChildCount(); i++ ) {
			tabHost.getTabWidget().getChildAt( i ).setPadding( 0, 0, 0, 0 );
			TextView tv = (TextView) tabHost.getTabWidget().getChildAt( i ).findViewById( android.R.id.title );
			tv.setTextSize( 17 );
			tv.setPadding( 0, 0, 0, 0 );
			tv.setTypeface( fontManager.HELVETICA_35 );
		}

		getLoaderManager().initLoader( 0, null, this ).forceLoad();

		if( events != null ) {
			initView();
		}

		return rootView;
	}

	private void initView() {
//		calendarFragment = new CalendarFragment();
//		calendarFragment.setListener( this );
//		calendarFragment.setMinDate( minDate );
//		calendarFragment.setMaxDate( maxDate );
//		calendarFragment.setSelDate( selDate );

//		main.getSupportFragmentManager().beginTransaction().add( R.id.weekPager, calendarFragment ).commit();

		eventListAdapter = new EventListAdapter( main.getApplicationContext(), filter( selDate ) );

		eventListView.setAdapter( eventListAdapter );
		eventListView.setOnItemClickListener( new EventListListener( main ) );

		loadSpinner.setVisibility( View.GONE );
	}

	private Date getInitDate( Date start, Date end ) {
		Date currentDate = new Date();
		if( currentDate.after( start ) && currentDate.before( end ) ) {
			return currentDate;
		}
		return start;
	}

	public List<Event> filter( Date date ) {
		List<Event> filtered = new ArrayList<Event>();

		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTime( date );

		int day = calendar.get( Calendar.DAY_OF_YEAR );

		for( Event event : events ) {
			calendar.clear();
			if( event instanceof Conference && currentType == Event.Type.CONFERENCE ) {
				calendar.setTime( ( (Conference) event ).getDate() );
				int eventDay = calendar.get( Calendar.DAY_OF_YEAR );
				if( eventDay == day ) {
					filtered.add( event );
				}
			} else if( event instanceof Stand && currentType == Event.Type.STAND ) {
				calendar.setTime( ( (Stand) event ).getStartDate() );
				int startDay = calendar.get( Calendar.DAY_OF_YEAR );
				calendar.clear();
				calendar.setTime( ( (Stand) event ).getEndDate() );
				int endDay = calendar.get( Calendar.DAY_OF_YEAR );
				if( day >= startDay && day <= endDay ) {
					filtered.add( event );
				}
			}
		}
		return filtered;
	}

	private boolean eventsFileExist() {
		File file = new File( main.getApplicationContext().getFilesDir(), "events.json" );
		return file.exists();
	}

	private String readEventsFile() {
		String ret = "";
		try {
			InputStream inputStream = main.openFileInput( "events.json" );

			if( inputStream != null ) {
				InputStreamReader inputStreamReader = new InputStreamReader( inputStream );
				BufferedReader bufferedReader = new BufferedReader( inputStreamReader );

				StringBuilder stringBuilder = new StringBuilder();
				String receiveString;

				while( ( receiveString = bufferedReader.readLine() ) != null ) {
					stringBuilder.append( receiveString );
				}

				inputStream.close();
				ret = stringBuilder.toString();
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return ret;
	}

	@Override
	public Loader<List<Event>> onCreateLoader( int i, Bundle bundle ) {
		return new EventListLoader( main.getApplicationContext() );
	}

	@Override
	public void onLoadFinished( Loader<List<Event>> listLoader, List<Event> events ) {
		// update event json object in memory
		if( events != null ) {
			this.events = events;
			if( !preloaded ) {
				initView();
				preloaded = true;
			} else {
				eventListAdapter.clear();
//				eventListAdapter.addAll( filter( calendarFragment.getSelDate() ) );
				eventListAdapter.addAll( filter( selDate ) );
			}
		} else if( events == null && !preloaded ) {
			EventListLoader loader = (EventListLoader) listLoader;
			ErrorMessage errorMessage = loader.getErrorMessage();
			AlertDialog.Builder builder = new AlertDialog.Builder( main );
			builder.setMessage( errorMessage.getMessage() )
					.setTitle( errorMessage.getTitle() )
					.setCancelable( false )
					.setNeutralButton( "Aceptar",
							new DialogInterface.OnClickListener() {
								public void onClick( DialogInterface dialog, int id ) {
									dialog.cancel();
									getLoaderManager().destroyLoader( 0 );
								}
							} );
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	@Override
	public void onLoaderReset( Loader<List<Event>> listLoader ) {
	}

	@Override
	public void onSelectDay( Date day ) {
		eventListAdapter.clear();
		eventListAdapter.addAll( filter( day ) );
	}

}
