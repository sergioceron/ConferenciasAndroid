package com.dotrow.diaempresario.camera;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.dotrow.diaempresario.R;
import net.sourceforge.zbar.*;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 27/01/14 08:34 PM
 */
public class CameraActivity extends Activity {
	private Camera mCamera;
	private CameraPreview mPreview;
	private Handler autoFocusHandler;

	TextView scanText;
	Button scanButton;

	ImageScanner scanner;

	private boolean barcodeScanned = false;
	private boolean previewing = true;

	static {
		System.loadLibrary( "iconv" );
	}

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.camera );

		autoFocusHandler = new Handler();
		mCamera = getCameraInstance();

        /* Instance barcode scanner */
		scanner = new ImageScanner();
		scanner.setConfig( 0, Config.X_DENSITY, 3 );
		scanner.setConfig( 0, Config.Y_DENSITY, 3 );

		mPreview = new CameraPreview( this, mCamera, previewCb, autoFocusCB );
		FrameLayout preview = (FrameLayout) findViewById( R.id.cameraPreview );
		preview.addView( mPreview );

		scanText = (TextView) findViewById( R.id.scanText );
		scanButton = (Button) findViewById( R.id.ScanButton );

		scanButton.setOnClickListener( new OnClickListener() {
			public void onClick( View v ) {
				if( barcodeScanned ) {
					barcodeScanned = false;
					scanText.setText( "Scanning..." );
					mCamera.setPreviewCallback( previewCb );
					mCamera.startPreview();
					previewing = true;
					mCamera.autoFocus( autoFocusCB );
				}
			}
		} );

	}

	public void onPause() {
		super.onPause();
		releaseCamera();
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		} catch ( Exception e ) {
		}
		return c;
	}

	private void releaseCamera() {
		if( mCamera != null ) {
			previewing = false;
			mCamera.setPreviewCallback( null );
			mCamera.release();
			mCamera = null;
		}
	}

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if( previewing )
				mCamera.autoFocus( autoFocusCB );
		}
	};

	PreviewCallback previewCb = new PreviewCallback() {
		public void onPreviewFrame( byte[] data, Camera camera ) {
			Camera.Parameters parameters = camera.getParameters();
			Size size = parameters.getPreviewSize();

			Image barcode = new Image( size.width, size.height, "Y800" );
			barcode.setData( data );

			int result = scanner.scanImage( barcode );

			if( result != 0 ) {
				previewing = false;
				mCamera.setPreviewCallback( null );
				mCamera.stopPreview();

				SymbolSet symbolSet = scanner.getResults();
				for( Symbol sym : symbolSet ) {
					scanText.setText( "barcode result " + sym.getData() );
					barcodeScanned = true;

					Intent resultIntent = new Intent();
					resultIntent.putExtra( "barcode", sym.getData() );
					setResult( Activity.RESULT_OK, resultIntent );
					finish();
				}
			}
		}
	};

	// Mimic continuous auto-focusing
	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus( boolean success, Camera camera ) {
			autoFocusHandler.postDelayed( doAutoFocus, 1000 );
		}
	};

}
