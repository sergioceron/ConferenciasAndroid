package com.dotrow.diaempresario;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.dotrow.diaempresario.evaluation.EvaluationReceiver;

import java.util.Calendar;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 22/03/14 08:51 PM
 */

public class OnBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive( Context context, Intent intent ) {
		System.out.println("+++++++++======= set alarm");
		setAlarm( context, 8, 19, 30 );
	}

	public static void setAlarm( Context context, int day, int hour, int minutes ) {
		try {
			AlarmManager alarmManager = (AlarmManager) context.getSystemService( Context.ALARM_SERVICE );
			Intent _intent = new Intent( context, EvaluationReceiver.class );
			_intent.putExtra( "id", "0" ); // 0 means global
			PendingIntent pendingIntent = PendingIntent.getBroadcast( context, 1, _intent, 0 );  // 1=code unique id

			Calendar cal = Calendar.getInstance();
			cal.clear();
			cal.set( 2014, Calendar.APRIL, day, hour, minutes );

			alarmManager.set( AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
}
