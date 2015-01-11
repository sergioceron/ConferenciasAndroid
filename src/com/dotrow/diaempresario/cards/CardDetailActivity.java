package com.dotrow.diaempresario.cards;

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
public class CardDetailActivity extends Activity {

	private FontManager fontManager;

	private ContactCard card;
	private CardImageThread cardImageThread;

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.card_full );

		fontManager = FontManager.getInstance(this.getAssets());

		Bundle bundle = getIntent().getExtras();
		this.card = (ContactCard) bundle.getSerializable( "card" );

		//final ImageView cardImage = (ImageView) findViewById( R.id.cardImage );

		TextView cardOrganization = ( (TextView) findViewById( R.id.cardOrganization ) );
		TextView cardJob = ( (TextView) findViewById( R.id.cardJob ) );
		TextView cardName = ( (TextView) findViewById( R.id.cardName ) );
		//TextView cardEmailLbl = ( (TextView) findViewById( R.id.cardEmailLbl ) );
		TextView cardEmail = ( (TextView) findViewById( R.id.cardEmail ) );
		//TextView cardPhoneLbl = ( (TextView) findViewById( R.id.cardPhoneLbl ) );
		TextView cardPhone = ( (TextView) findViewById( R.id.cardPhone ) );

		cardName.setTypeface( fontManager.HELVETICA_37 );
		//cardEmailLbl.setTypeface( fontManager.HELVETICA_77 );
		//cardPhoneLbl.setTypeface( fontManager.HELVETICA_77 );
		cardOrganization.setTypeface( fontManager.HELVETICA_63 );
		cardJob.setTypeface( fontManager.HELVETICA_45 );
		cardEmail.setTypeface( fontManager.HELVETICA_45 );
		cardPhone.setTypeface( fontManager.HELVETICA_45 );

		cardOrganization.setText( card.getOrganization() );
		cardJob.setText( card.getJob() );
		cardName.setText( card.getName() );
		cardEmail.setText( card.getEmail() );
		cardPhone.setText( "Tel: " + card.getPhone() );

		/*if( cardImageExist( card.getImage() ) ) {
			Drawable image = getImageFromFile( card.getImage() );
			cardImage.setImageDrawable( image );
		} else {
			cardImage.setImageDrawable( getResources().getDrawable( R.drawable.user_default ) );
			cardImageThread = new CardImageThread( card.getImage(), getApplicationContext(), new Handler() {
				@Override
				public void handleMessage( Message msg ) {
					if( cardImageThread.getDrawable() != null ) {
						cardImage.setImageDrawable( cardImageThread.getDrawable() );
					}
				}
			} );
			cardImageThread.start();
		}*/
	}

	/*private Drawable getImageFromFile( String image ) {
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

	private boolean cardImageExist( String image ) {
		File file = new File( getFilesDir(), image );
		return file.exists();
	}*/

}