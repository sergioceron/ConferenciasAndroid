package com.dotrow.diaempresario;

import android.content.res.AssetManager;
import android.graphics.Typeface;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 27/01/14 01:42 PM
 */
public class FontManager {
	private static FontManager instance;

	public static FontManager getInstance( AssetManager assetManager ) {
		return new FontManager( assetManager );
	}

	/*public static FontManager newInstance( AssetManager assetManager ) {
		instance = new FontManager( assetManager );
		return instance;
	}*/

	public final Typeface HELVETICA_37;
	public final Typeface HELVETICA_77;
	public final Typeface HELVETICA_25;
	public final Typeface HELVETICA_35;
	public final Typeface HELVETICA_63;
	public final Typeface HELVETICA_65;
	public final Typeface HELVETICA_45;

	private FontManager( AssetManager assetManager ) {
		HELVETICA_37 = Typeface.createFromAsset( assetManager, "Helvetica LT 37 Thin Condensed.ttf" );
		HELVETICA_77 = Typeface.createFromAsset( assetManager, "Helvetica LT 77 Bold Condensed.ttf" );
		HELVETICA_25 = Typeface.createFromAsset( assetManager, "Helvetica LT 25 Ultra Light.ttf" );
		HELVETICA_35 = Typeface.createFromAsset( assetManager, "Helvetica LT 35 Thin.ttf" );
		HELVETICA_63 = Typeface.createFromAsset( assetManager, "Helvetica LT 63 Medium Extended.ttf" );
		HELVETICA_65 = Typeface.createFromAsset( assetManager, "Helvetica LT 65 Medium.ttf" );
		HELVETICA_45 = Typeface.createFromAsset( assetManager, "Helvetica LT 45 Light.ttf" );
	}
}
