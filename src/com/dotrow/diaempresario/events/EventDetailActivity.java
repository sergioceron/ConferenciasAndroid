package com.dotrow.diaempresario.events;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.*;
import com.dotrow.diaempresario.*;
import com.dotrow.diaempresario.evaluation.EvaluationReceiver;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 20/01/14 11:22 AM
 */
public class EventDetailActivity extends Activity {
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat( "HH:mm" );

	private FontManager fontManager;

	private Event event;
	private EventImageThread eventImageThread;

	private SharedPreferences settings;
	private AlarmManager alarmManager;

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.event_full );


		fontManager = FontManager.getInstance(this.getAssets());

		Bundle bundle = getIntent().getExtras();
		this.event = (Event) bundle.getSerializable( "event" );

		settings = getSharedPreferences( "Events", 0 );
		alarmManager = (AlarmManager) getSystemService( Context.ALARM_SERVICE );

		final ImageView background = (ImageView) findViewById( R.id.background );
		final ImageView eventImage = (ImageView) findViewById( R.id.eventImage );

		TextView eventTitle = ( (TextView) findViewById( R.id.eventTitle ) );
		TextView eventDate = ( (TextView) findViewById( R.id.eventDate ) );
		TextView eventAuthorLbl = ( (TextView) findViewById( R.id.eventAuthorLbl ) );
		TextView eventAuthor = ( (TextView) findViewById( R.id.eventAuthor ) );
		TextView eventLocationLbl = ( (TextView) findViewById( R.id.eventLocationLbl ) );
		TextView eventLocation = ( (TextView) findViewById( R.id.eventLocation ) );
		TextView eventDescription = ( (TextView) findViewById( R.id.eventDescription ) );

		final ProgressBar progressBar = (ProgressBar) findViewById( R.id.progressBar );
		RatingBar ratingBar = (RatingBar) findViewById( R.id.ratingBar );

		final Button stared = ( (Button) findViewById( R.id.stared ) );
		stared.setBackgroundResource( settings.contains( event.getId() + ":stared" ) ?
				R.drawable.stared_button_set : R.drawable.stared_button_unset );
		stared.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View view ) {
				SharedPreferences.Editor editor = settings.edit();
				if( !settings.contains( event.getId() + ":stared" ) ) {
					editor.putBoolean( event.getId() + ":stared", true );
					stared.setBackgroundResource( R.drawable.stared_button_set );
				} else {
					editor.remove( event.getId() + ":stared" );
					stared.setBackgroundResource( R.drawable.stared_button_unset );
				}
				editor.commit();
			}
		} );

		final Button alarm = ( (Button) findViewById( R.id.alarm ) );
		alarm.setBackgroundResource( settings.contains( event.getId() + ":scheduled" ) ?
				R.drawable.alarm_button_set : R.drawable.alarm_button_unset );
		alarm.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View view ) {
				try {
					SharedPreferences.Editor editor = settings.edit();
					if( !settings.contains( event.getId() + ":scheduled" ) ) {
						Set<String> ids = scheduleEvent( event );
						if( ids != null ) {
							editor.putStringSet( event.getId() + ":scheduled", ids );
							alarm.setBackgroundResource( R.drawable.alarm_button_set );
						}
					} else {
						Set<String> ids = settings.getStringSet( event.getId() + ":scheduled", null );
						if( ids != null ) {
							if( unscheduleEvent( ids ) ) {
								editor.remove( event.getId() + ":scheduled" );
								alarm.setBackgroundResource( R.drawable.alarm_button_unset );
							}
						}
					}
					editor.commit();
				} catch ( Exception e ) {
					e.printStackTrace();
				}
			}
		} );

		if( !settings.contains( event.getId() + ":stared" ) ) {
			ratingBar.setOnRatingBarChangeListener( new RatingBar.OnRatingBarChangeListener() {
				public void onRatingChanged( RatingBar ratingBar, float rating, boolean fromUser ) {
					rateEvent( rating );
				}
			} );
		} else {
			ratingBar.setRating( settings.getFloat( event.getId() + ":stared", 0f ) );
			ratingBar.setEnabled( false );
		}

		final Button comment = ( (Button) findViewById( R.id.comment ) );
		comment.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View view ) {
				promptComment();
			}
		} );

		eventTitle.setTypeface( fontManager.HELVETICA_37 );
		eventDate.setTypeface( fontManager.HELVETICA_45 );
		eventAuthorLbl.setTypeface( fontManager.HELVETICA_77 );
		eventLocationLbl.setTypeface( fontManager.HELVETICA_77 );
		eventAuthor.setTypeface( fontManager.HELVETICA_45 );
		eventLocation.setTypeface( fontManager.HELVETICA_45 );

		eventTitle.setText( event.getTitle() );
		eventLocation.setText( event.getLocation() );
		eventDescription.setText( event.getDescription() );

		String type = "";
		if( event instanceof Conference ) {
			eventDate.setText( dateFormatter.format( ( (Conference) event ).getDate() ) + " Hrs." );
			eventAuthorLbl.setText( "Conferencista:" );
			eventAuthor.setText( ( (Conference) event ).getAuthor() );
			type = "conference";
		} else if( event instanceof Stand ) {
			eventDate.setText( dateFormatter.format( ( (Stand) event ).getStartDate() ) + " - " +
					dateFormatter.format( ( (Stand) event ).getEndDate() ) );
			eventAuthorLbl.setText( "Organización:" );
			eventAuthor.setText( ( (Stand) event ).getOrganization() );
			type = "stand";
		}

		if( event.getImage().length() != 0 ) {
			if( eventImageExist( event.getImage() ) ) {
				Drawable image = getImageFromFile( event.getImage() );
				eventImage.setImageDrawable( image );
				progressBar.setVisibility( View.GONE );
			} else {
				eventImageThread = new EventImageThread( type, event.getImage(), getApplicationContext(), new Handler() {
					@Override
					public void handleMessage( Message msg ) {
						if( eventImageThread.getDrawable() != null ) {
							eventImage.setImageDrawable( eventImageThread.getDrawable() );
						}
						progressBar.setVisibility( View.GONE );
					}
				} );
				eventImageThread.start();
			}
		}
	}

	private void rateEvent( float rating ) {
		HTTPRequest request = new HTTPRequest( getApplicationContext() );
		request.setHost( "http://conferences.dotrow.com" );
		request.setPath( "/rate?hash=" + SigninActivity.getHash( this.getApplicationContext() ) );
		request.setMethod( "POST" );

		request.setContent( String.format( "{ \"value\": \"%.1f\", \"event\": \"%s\" }", rating, event.getId() ) );

		AlertDialog.Builder builder = new AlertDialog.Builder( this );

		HTTPResponse response = request.execute();
		switch( response.getResponseCode() ) {
			case 200:
				SharedPreferences.Editor editor = settings.edit();
				editor.putFloat( event.getId() + ":stared", rating );
				editor.commit();
				return;
			default:
				builder.setMessage( "Error al evaluar el evento" )
						.setTitle( "Evaluación" );
		}
		builder.setCancelable( false )
				.setNeutralButton( "Aceptar",
						new DialogInterface.OnClickListener() {
							public void onClick( DialogInterface dialog, int id ) {
								dialog.cancel();
								//getLoaderManager().destroyLoader( 0 );
							}
						} );
		AlertDialog alert = builder.create();
		alert.show();
	}


	private void promptComment() {
		AlertDialog.Builder alert = new AlertDialog.Builder( this );

		alert.setTitle( "Agregar comentario" );
		alert.setMessage( "Escribe tu comentario:" );

		final EditText input = new EditText( this );
		alert.setView( input );

		alert.setPositiveButton( "Enviar", new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int whichButton ) {
				String value = input.getText().toString();
				sendComment( value );
			}
		} );

		alert.setNegativeButton( "Cancelar", new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int whichButton ) {
				dialog.dismiss();
			}
		} );

		alert.show();
	}

	private void sendComment( String comment ) {
		HTTPRequest request = new HTTPRequest( getApplicationContext() );
		request.setHost( "http://conferences.dotrow.com" );
		request.setPath( "/comment?hash=" + SigninActivity.getHash( this.getApplicationContext() ) );
		request.setMethod( "POST" );

		request.setContent( String.format( "{ \"text\": \"%s\", \"event\": \"%s\" }", comment, event.getId() ) );

		AlertDialog.Builder builder = new AlertDialog.Builder( this );

		HTTPResponse response = request.execute();
		switch( response.getResponseCode() ) {
			case 200:
				builder.setMessage( "Gracias por su comentario" )
						.setTitle( "Agregar comentario" );
				break;
			default:
				builder.setMessage( "Error al enviar el comentario" )
						.setTitle( "Agregar comentario" );
		}
		builder.setCancelable( false )
				.setNeutralButton( "Aceptar",
						new DialogInterface.OnClickListener() {
							public void onClick( DialogInterface dialog, int id ) {
								dialog.cancel();
								//getLoaderManager().destroyLoader( 0 );
							}
						} );
		AlertDialog alert = builder.create();
		alert.show();
	}

	/*private void setAlarm( Event event ) {
		try {
			Intent intent = new Intent( this, EvaluationReceiver.class );
			intent.putExtra( "id", event.getId() ); // 0 means global
			PendingIntent pendingIntent = PendingIntent.getBroadcast( getApplicationContext(), event.hashCode(), intent, 0 );

			if( event instanceof Stand ) {
				alarmManager.set( AlarmManager.RTC_WAKEUP, ( (Stand) event ).getStartDate().getTime(), pendingIntent );
			} else if( event instanceof Conference ) {
				alarmManager.set( AlarmManager.RTC_WAKEUP, ( (Conference) event ).getDate().getTime(), pendingIntent );
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}*/

	/*private void unsetAlarm( Event event ) {
		try {
			Intent intent = new Intent( this, EvaluationReceiver.class );
			intent.putExtra( "id", event.getId() ); // 0 means global
			PendingIntent pendingIntent = PendingIntent.getBroadcast( getApplicationContext(), event.hashCode(), intent, 0 );

			alarmManager.cancel( pendingIntent );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}*/

	private Set<String> scheduleEvent( Event event ) {
		int calendarId = CalendarHelper.getCalendarIdByName( this, "Calendar;Events" );
		if( calendarId == -1 ) {
			calendarId = CalendarHelper.createCalendar( this, new CalendarHelper.Calendar( "Calendar;Events", "dotrow.com" ) );
		}

		if( calendarId > 0 ) {
			final long eventId = CalendarHelper.addAlarm( this, calendarId, event );
			if( eventId > 0 ) {
				final long reminderId = CalendarHelper.addReminder( this, eventId );

				return new HashSet<String>() {{
					add( String.valueOf( eventId ) );
					add( String.valueOf( reminderId ) );
				}};
			}
		}
		return null;
	}

	private boolean unscheduleEvent( Set<String> ids ) {
		Iterator<String> it = ids.iterator();
		try {
			CalendarHelper.removeAlarm( this, new Long( it.next() ) );
			CalendarHelper.removeReminder( this, new Long( it.next() ) );
			return true;
		} catch ( NumberFormatException e ) {
			e.printStackTrace();
		}
		return false;
	}


	private Drawable getImageFromFile( String image ) {
		try {
			InputStream imageStream = openFileInput( image );
			Bitmap bmp = BitmapFactory.decodeStream( imageStream );
			imageStream.close();
			return new BitmapDrawable( bmp );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean eventImageExist( String image ) {
		File file = new File( getFilesDir(), image );
		return file.exists();
	}


}