package com.dotrow.diaempresario;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 24/03/14 05:11 PM
 */
public class Boot extends Activity {

	private long splashDelay = 1*1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Intent mainIntent = new Intent( getApplicationContext(), MainActivity.class );
				startActivity(mainIntent);
				finish();
			}
		};

		Timer timer = new Timer();
		timer.schedule(task, splashDelay);
	}

}