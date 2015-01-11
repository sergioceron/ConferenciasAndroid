package com.dotrow.diaempresario.cards;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import java.io.FileOutputStream;
import java.net.URL;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 22/01/14 11:08 PM
 */
public class CardImageThread extends Thread {
	private final static String IMAGES_URL = "http://www.dotrow.com/ceron/cards/images/";

	private String image;
	private Handler handler;
	private Drawable drawable;

	private Context context;

	public CardImageThread( String image, Context context, Handler handler ) {
		this.image = image;
		this.context = context;
		this.handler = handler;
	}

	@Override
	public void run() {
		try {
			URL url = new URL( IMAGES_URL + image );
			Bitmap bmp = BitmapFactory.decodeStream( url.openConnection().getInputStream() );
			drawable = new BitmapDrawable( bmp );
			savePicture( image, bmp );
		} catch ( Exception e ) {
			e.printStackTrace();
			drawable = null;
		}
		handler.sendEmptyMessage( 0 );
	}

	public Drawable getDrawable() {
		return drawable;
	}

	private void savePicture(String filename, Bitmap b){
		try {
			FileOutputStream out = context.openFileOutput(filename, Context.MODE_PRIVATE);
			b.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
