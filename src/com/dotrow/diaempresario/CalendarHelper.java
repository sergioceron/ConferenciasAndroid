package com.dotrow.diaempresario;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import com.dotrow.diaempresario.events.Conference;
import com.dotrow.diaempresario.events.Event;
import com.dotrow.diaempresario.events.Stand;

import java.util.TimeZone;

import static android.provider.CalendarContract.ACCOUNT_TYPE_LOCAL;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 9/03/14 10:48 PM
 */
public class CalendarHelper {

	public static int createCalendar( Activity activity, Calendar calendar ) {
		Uri CALENDARS_URI = Uri.parse( getCalendarUriBase( activity ) + "calendars" );


		ContentValues values = new ContentValues();
		values.put( CalendarContract.Calendars.ACCOUNT_NAME, calendar.getAccount() );
		values.put( CalendarContract.Calendars.ACCOUNT_TYPE, calendar.getType() );
		values.put( CalendarContract.Calendars.NAME, calendar.getName() );
		values.put( CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, calendar.getName() );
		values.put( CalendarContract.Calendars.CALENDAR_COLOR, calendar.getColor() );
		values.put( CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, calendar.getAccess() );
		values.put( CalendarContract.Calendars.OWNER_ACCOUNT, calendar.getAccount() );
		values.put( CalendarContract.Calendars.CALENDAR_TIME_ZONE, TimeZone.getDefault().getID() );
		values.put( CalendarContract.Calendars.VISIBLE, calendar.isVisible() ? 1 : 0 );
		values.put( CalendarContract.Calendars.SYNC_EVENTS, calendar.isSync() ? 1 : 0 );

		try {
			Uri.Builder builder = CALENDARS_URI.buildUpon();
			builder.appendQueryParameter( CalendarContract.Calendars.ACCOUNT_NAME, "dotrow.com" );
			builder.appendQueryParameter( CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL );
			builder.appendQueryParameter( CalendarContract.CALLER_IS_SYNCADAPTER, "true" );
			Uri addedCalendar = activity.getContentResolver().insert( builder.build(), values );

			return Integer.parseInt( addedCalendar.getLastPathSegment() );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return -1;
	}

	public static boolean deleteCalendar( Activity activity, int calId ) {
		Uri CALENDARS_URI = Uri.parse( getCalendarUriBase( activity ) + "calendars" );

		try {
			Uri calendarUri = ContentUris.withAppendedId( CALENDARS_URI, calId );
			activity.getContentResolver().delete( calendarUri, null, null );
			return true;
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return false;
	}

	public static int getCalendarIdByName( Activity activity, String name ) {
		Uri CALENDARS_URI = Uri.parse( getCalendarUriBase( activity ) + "calendars" );

		Cursor cursor = activity.getContentResolver().query(
				CALENDARS_URI,
				new String[]{ "_id", "calendar_displayName" },
				null, null, null
		);

		for( int i = 0; i < cursor.getCount(); i++ ) {
			cursor.moveToNext();
			int id = cursor.getInt( 0 );
			String _name = cursor.getString( 1 );

			if( _name.equals( name ) )
				return id;
		}
		return -1;
	}

	public static long addAlarm( Activity activity, int calendarId, Event event ) {
		Uri EVENTS_URI = Uri.parse( getCalendarUriBase( activity ) + "events" );

		ContentValues values = new ContentValues();
		values.put( "calendar_id", calendarId );
		values.put( "title", event instanceof Stand ? "Stand" : "Conferencia" );
		values.put( "allDay", 0 );

		if( event instanceof Stand ) {
			values.put( "dtstart", ( (Stand) event ).getStartDate().getTime() );
			values.put( "dtend", ( (Stand) event ).getEndDate().getTime() );
		} else if( event instanceof Conference ) {
			values.put( "dtstart", ( (Conference) event ).getDate().getTime() );
			values.put( "dtend", ( (Conference) event ).getDate().getTime() );
		}
		values.put( "description", event.getTitle() );
		values.put( "hasAlarm", 1 );
		values.put( CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID() );  //add this row


		try {
			Uri addedEvent = activity.getContentResolver().insert( EVENTS_URI, values );
			return new Long( addedEvent.getLastPathSegment() );
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return -1L;
	}

	public static long addReminder( Activity activity, long eventId ) {
		Uri REMINDERS_URI = Uri.parse( getCalendarUriBase( activity ) + "reminders" );

		ContentValues values = new ContentValues();
		values.put( "event_id", eventId );
		values.put( "method", 1 );
		values.put( "minutes", 10 );

		try {
			Uri addedReminder = activity.getContentResolver().insert( REMINDERS_URI, values );
			return new Long( addedReminder.getLastPathSegment() );
		} catch ( NumberFormatException e ) {
			e.printStackTrace();
		}

		return -1L;
	}

	public static boolean removeAlarm( Activity activity, Long eventId) {
		Uri EVENTS_URI = Uri.parse( getCalendarUriBase( activity ) + "events" );

		try {
			Uri eventUri = ContentUris.withAppendedId( EVENTS_URI, eventId );
			int eventsDeleted = activity.getContentResolver().delete( eventUri, null, null );

			return eventsDeleted > 0;
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean removeReminder( Activity activity, Long reminderId ) {
		Uri REMINDERS_URI = Uri.parse( getCalendarUriBase( activity ) + "reminders" );

		try {
			Uri reminderUri = ContentUris.withAppendedId( REMINDERS_URI, reminderId );
			int remindersDeleted = activity.getContentResolver().delete( reminderUri, null, null );

			return remindersDeleted > 0;
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return false;
	}

	public static String getCalendarUriBase( Activity act ) {
		String calendarUriBase = null;
		Uri calendars = Uri.parse( "content://calendar/calendars" );
		Cursor managedCursor = null;
		try {
			managedCursor = act.managedQuery( calendars, null, null, null, null );
		} catch ( Exception e ) {
		}
		if( managedCursor != null ) {
			calendarUriBase = "content://calendar/";
		} else {
			calendars = Uri.parse( "content://com.android.calendar/calendars" );
			try {
				managedCursor = act.managedQuery( calendars, null, null, null, null );
			} catch ( Exception e ) {
			}
			if( managedCursor != null ) {
				calendarUriBase = "content://com.android.calendar/";
			}
		}
		return calendarUriBase;
	}

	public static class Calendar {
		private String name;
		private String account;
		private String type = ACCOUNT_TYPE_LOCAL;
		private int color = 0xEA8561;
		private int access = CalendarContract.Calendars.CAL_ACCESS_OWNER;
		private boolean visible = true;
		private boolean sync = true;

		public Calendar( String name, String account ) {
			this.name = name;
			this.account = account;
		}

		//region Setters & Getters
		public String getName() {
			return name;
		}

		public void setName( String name ) {
			this.name = name;
		}

		public String getAccount() {
			return account;
		}

		public void setAccount( String account ) {
			this.account = account;
		}

		public String getType() {
			return type;
		}

		public void setType( String type ) {
			this.type = type;
		}

		public int getColor() {
			return color;
		}

		public void setColor( int color ) {
			this.color = color;
		}

		public int getAccess() {
			return access;
		}

		public void setAccess( int access ) {
			this.access = access;
		}

		public boolean isVisible() {
			return visible;
		}

		public void setVisible( boolean visible ) {
			this.visible = visible;
		}

		public boolean isSync() {
			return sync;
		}

		public void setSync( boolean sync ) {
			this.sync = sync;
		}
		//endregion
	}
}
