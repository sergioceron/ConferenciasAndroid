package com.dotrow.diaempresario.cards;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.*;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.dotrow.diaempresario.ErrorMessage;
import com.dotrow.diaempresario.R;
import com.dotrow.diaempresario.camera.CameraActivity;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 28/01/14 11:29 PM
 */
public class CardListFragment extends Fragment implements LoaderManager.LoaderCallbacks<ContactCard> {
	private static final int INTENT_CAMERA = 1;

	private FragmentActivity main;
	private CardListAdapter cardListAdapter;

	private List<ContactCard> contacts;

	private LinearLayout loadSpinner;
	private ListView cardListView;

	private Button scanButton;

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onActivityCreated( savedInstanceState );

		this.main = (FragmentActivity) getActivity();

		if( cardsFileExist() ) {
			String json = readCardsFile();

			JSONDeserializer<List<ContactCard>> deserializer = new JSONDeserializer<List<ContactCard>>();
			contacts = deserializer
					.use( "values", ContactCard.class )
					.deserialize( json );
		} else {
			contacts = new ArrayList<ContactCard>();
		}
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		ViewGroup rootView = (ViewGroup) inflater.inflate( R.layout.card_list, container, false );
		cardListView = (ListView) rootView.findViewById( R.id.cardsView );
		loadSpinner = (LinearLayout) rootView.findViewById( R.id.progressBar );
		scanButton = (Button) rootView.findViewById( R.id.btnScan );

		cardListAdapter = new CardListAdapter( main.getApplicationContext(), contacts );

		cardListView.setAdapter( cardListAdapter );
		cardListView.setOnItemClickListener( new CardListListener( main ) );

		scanButton.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View view ) {
				Intent i = new Intent( main, CameraActivity.class );
				startActivityForResult( i, INTENT_CAMERA );
			}
		} );

		loadSpinner.setVisibility( View.GONE );

		return rootView;
	}


	private boolean cardsFileExist() {
		File file = new File( main.getApplicationContext().getFilesDir(), "cards.json" );
		return file.exists();
	}

	private String readCardsFile() {
		String ret = "";
		try {
			InputStream inputStream = main.openFileInput( "cards.json" );

			if( inputStream != null ) {
				InputStreamReader inputStreamReader = new InputStreamReader( inputStream );
				BufferedReader bufferedReader = new BufferedReader( inputStreamReader );
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while( ( receiveString = bufferedReader.readLine() ) != null ) {
					stringBuilder.append( receiveString );
				}

				inputStream.close();
				ret = stringBuilder.toString();
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return ret;
	}

	private boolean saveCardsFile( String json ) {
		FileOutputStream outputStream;
		try {
			outputStream = main.openFileOutput( "cards.json", Context.MODE_PRIVATE );
			outputStream.write( json.getBytes() );
			outputStream.close();
			return true;
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void onActivityResult( int requestCode, int resultCode, Intent data ) {
		super.onActivityResult( requestCode, resultCode, data );
		switch( requestCode ) {
			case INTENT_CAMERA:
				if( resultCode == Activity.RESULT_OK ) {
					loadSpinner.setVisibility( View.VISIBLE );

					String barcode = data.getStringExtra( "barcode" );
					Bundle loaderBundle = new Bundle();
					loaderBundle.putString( "uid", barcode );
					getLoaderManager().initLoader( 0, loaderBundle, this ).forceLoad();
				}
				break;
		}
	}

	@Override
	public Loader<ContactCard> onCreateLoader( int i, Bundle bundle ) {
		return new CardListLoader( bundle.getString( "uid" ), main.getApplicationContext() );
	}

	@Override
	public void onLoadFinished( Loader<ContactCard> listLoader, ContactCard contact ) {
		if( contact != null ) {
			boolean alreadyExist = false;
			for( ContactCard contactCard : contacts ) {
				if( contactCard.equals( contact ) ){
					alreadyExist = true;
					break;
				}
			}

			if( !alreadyExist ){
				cardListAdapter.add( contact );

				JSONSerializer serializer = new JSONSerializer();
				String json = serializer.exclude( "*.class" ).serialize( contacts );

				if( !saveCardsFile( json ) ) {
					// TODO: log insufficient space
				}
			}
			loadSpinner.setVisibility( View.GONE );

			getLoaderManager().destroyLoader( 0 );

			Intent i = new Intent( main.getApplicationContext(), CardDetailActivity.class );
			i.putExtra( "card", contact );
			main.startActivity( i );

		}  else {
			CardListLoader loader = (CardListLoader) listLoader;
			ErrorMessage errorMessage = loader.getErrorMessage();
			AlertDialog.Builder builder = new AlertDialog.Builder( main );
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
	public void onLoaderReset( Loader<ContactCard> listLoader ) {
	}

}
