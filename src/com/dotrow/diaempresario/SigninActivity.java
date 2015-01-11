package com.dotrow.diaempresario;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dotrow.diaempresario.camera.CameraActivity;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 27/01/14 08:34 PM
 */
public class SigninActivity extends Activity implements LoaderManager.LoaderCallbacks<Boolean>, OnClickListener {

	private static final int INTENT_CAMERA = 1;
	private FontManager fontManager;

	private TextView lblId;
	private EditText idText;
	private Button scanButton;
	private Button enterButton;

	private LinearLayout loadSpinner;

	private String uid = "";

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.signin );


		fontManager = FontManager.getInstance(getAssets());

		loadSpinner = (LinearLayout) findViewById( R.id.progressBar );
		loadSpinner.setVisibility( View.GONE );

		lblId = (TextView) findViewById( R.id.lblId );
		idText = (EditText) findViewById( R.id.id );
		scanButton = (Button) findViewById( R.id.btnScan );
		enterButton = (Button) findViewById( R.id.btnEnter );

		lblId.setTypeface( fontManager.HELVETICA_35 );
		idText.setTypeface( fontManager.HELVETICA_35 );
		scanButton.setTypeface( fontManager.HELVETICA_35 );
		enterButton.setTypeface( fontManager.HELVETICA_35 );

		scanButton.setOnClickListener( this );
		enterButton.setOnClickListener( this );

		fontManager = FontManager.getInstance(this.getAssets());
	}

	@Override
	public void onClick( View view ) {
		switch( view.getId() ) {
			case R.id.btnScan:
				Intent i = new Intent( getApplicationContext(), CameraActivity.class );
				startActivityForResult( i, INTENT_CAMERA );
				break;
			case R.id.btnEnter:
				uid = idText.getText().toString();
				getLoaderManager().initLoader( 0, null, this ).forceLoad();
				break;
		}

		loadSpinner.setVisibility( View.VISIBLE );
	}

	@Override
	public void onActivityResult( int requestCode, int resultCode, Intent data ) {
		super.onActivityResult( requestCode, resultCode, data );
		switch( requestCode ) {
			case INTENT_CAMERA:
				if( resultCode == Activity.RESULT_OK ) {
					uid = data.getStringExtra( "barcode" );
					getLoaderManager().initLoader( 0, null, this ).forceLoad();
				}
				break;
		}
	}

	@Override
	public Loader<Boolean> onCreateLoader( int i, Bundle bundle ) {
		return new SigninLoader( uid, getApplicationContext() );
	}

	@Override
	public void onLoadFinished( Loader<Boolean> signinLoader, Boolean signedIn ) {
		if( signedIn ) {
			OnBootReceiver.setAlarm( this.getApplicationContext(), 8, 19, 30 );
			Intent resultIntent = new Intent();
			resultIntent.putExtra( "signedIn", true );
			setResult( Activity.RESULT_OK, resultIntent );
			finish();
		} else {
			SigninLoader loader = (SigninLoader) signinLoader;
			ErrorMessage errorMessage = loader.getErrorMessage();
			AlertDialog.Builder builder = new AlertDialog.Builder( this );
			builder.setMessage( errorMessage.getMessage() )
					.setTitle( errorMessage.getTitle() )
					.setCancelable( false )
					.setNeutralButton( "Aceptar",
							new DialogInterface.OnClickListener() {
								public void onClick( DialogInterface dialog, int id ) {
									dialog.cancel();
									getLoaderManager().destroyLoader( 0 );
									loadSpinner.setVisibility( View.GONE );
								}
							} );
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	@Override
	public void onLoaderReset( Loader<Boolean> signinLoader ) {
	}

	public static String getHash( Context context ) {
		SharedPreferences settings = context.getSharedPreferences( "Configuration", 0 );
		return settings.getString( "hash", "-1" );
	}
}
