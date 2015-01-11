package com.dotrow.diaempresario.evaluation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 9/03/14 01:59 PM
 */
public class EvaluationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive( Context context, Intent intent ) {
		Bundle extras = intent.getExtras();

		System.out.println( "==============" + extras.get( "id" ) + "================" );

        SharedPreferences settings = context.getSharedPreferences( "Global", 0 );

		if( !settings.contains( "evaluated" ) ){
			Intent evaluationIntent = new Intent( context, GlobalEvaluation.class );
			evaluationIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
			context.startActivity( evaluationIntent );
		}
	}
}
