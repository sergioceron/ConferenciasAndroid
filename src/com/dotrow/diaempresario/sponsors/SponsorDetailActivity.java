package com.dotrow.diaempresario.sponsors;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;
import com.dotrow.diaempresario.FontManager;
import com.dotrow.diaempresario.R;

import java.io.File;
import java.io.InputStream;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 20/01/14 11:22 AM
 */
public class SponsorDetailActivity extends Activity {

	private FontManager fontManager;

	private Sponsor sponsor;
	private SponsorImageThread sponsorImageThread;

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.sponsor_full );


		fontManager = FontManager.getInstance(this.getAssets());

		Bundle bundle = getIntent().getExtras();
		this.sponsor = (Sponsor) bundle.getSerializable( "sponsor" );

		final ImageView sponsorImage = (ImageView) findViewById( R.id.sponsorImage );

		TextView sponsorName = ( (TextView) findViewById( R.id.sponsorName ) );
		TextView sponsorUrlLbl = ( (TextView) findViewById( R.id.sponsorUrlLbl ) );
		TextView sponsorUrl = ( (TextView) findViewById( R.id.sponsorUrl ) );

		sponsorName.setTypeface( fontManager.HELVETICA_37 );
		sponsorUrlLbl.setTypeface( fontManager.HELVETICA_77 );
		sponsorUrl.setTypeface( fontManager.HELVETICA_45 );

		sponsorName.setText( sponsor.getName() );
		sponsorUrl.setText( sponsor.getUrl() );

		if( sponsorImageExist( sponsor.getImage() ) ) {
			Drawable image = getImageFromFile( sponsor.getImage() );
			sponsorImage.setImageDrawable( image );
		} else {
			sponsorImage.setImageDrawable( getResources().getDrawable( R.drawable.user_default ) );
			sponsorImageThread = new SponsorImageThread( sponsor.getImage(), getApplicationContext(), new Handler() {
				@Override
				public void handleMessage( Message msg ) {
					if( sponsorImageThread.getDrawable() != null ) {
						sponsorImage.setImageDrawable( sponsorImageThread.getDrawable() );
					}
				}
			} );
			sponsorImageThread.start();
		}

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

	private boolean sponsorImageExist( String image ) {
		File file = new File( getFilesDir(), image );
		return file.exists();
	}

}