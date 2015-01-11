package com.dotrow.diaempresario.sponsors;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.dotrow.diaempresario.ErrorMessage;
import com.dotrow.diaempresario.R;
import flexjson.JSONDeserializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 28/01/14 11:29 PM
 */
public class SponsorListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Sponsor>> {
	private FragmentActivity main;
	private SponsorListAdapter sponsorListAdapter;

	private List<Sponsor> sponsors;

	private LinearLayout loadSpinner;
	private ListView sponsorListView;

	private boolean preloaded;

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onActivityCreated( savedInstanceState );

		this.main = (FragmentActivity) getActivity();

		if( sponsorsFileExist() ) {
			String json = readSponsorsFile();

			JSONDeserializer<List<Sponsor>> deserializer = new JSONDeserializer<List<Sponsor>>();
			sponsors = deserializer
					.use( "values", Sponsor.class )
					.deserialize( json );

			Collections.sort( sponsors, Collections.reverseOrder() );

			preloaded = true;
		}
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
		ViewGroup rootView = (ViewGroup) inflater.inflate( R.layout.sponsor_list, container, false );
		sponsorListView = (ListView) rootView.findViewById( R.id.sponsorsView );
		loadSpinner = (LinearLayout) rootView.findViewById( R.id.progressBar1 );

		getLoaderManager().initLoader( 0, null, this ).forceLoad();

		if( sponsors != null ) {
			initView( sponsors );
		}

		return rootView;
	}

	private void initView( List<Sponsor> sponsors ) {
		sponsorListAdapter = new SponsorListAdapter( main.getApplicationContext(), sponsors );
		setIndexes();

		sponsorListView.setAdapter( sponsorListAdapter );
		sponsorListView.setOnItemClickListener( new SponsorListListener( main ) );

		/*for( Sponsor s : sponsors ) {
			s.loadImage( sponsorListAdapter );
		}*/

		loadSpinner.setVisibility( View.GONE );
	}

	private boolean sponsorsFileExist() {
		File file = new File( main.getApplicationContext().getFilesDir(), "sponsors.json" );
		return file.exists();
	}

	private String readSponsorsFile() {
		String ret = "";
		try {
			InputStream inputStream = main.openFileInput( "sponsors.json" );

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

	@Override
	public Loader<List<Sponsor>> onCreateLoader( int i, Bundle bundle ) {
		return new SponsorListLoader( main.getApplicationContext() );
	}

	@Override
	public void onLoadFinished( Loader<List<Sponsor>> listLoader, List<Sponsor> sponsors ) {
		if( sponsors != null ) {
			Collections.sort( sponsors, Collections.reverseOrder() );
			this.sponsors = sponsors;
			if( !preloaded ) {
				initView( sponsors );
				preloaded = true;
			} else {
				sponsorListAdapter.clear();
				sponsorListAdapter.addAll( sponsors );
			}
			setIndexes();
		} else if( sponsors == null && !preloaded ) {
			SponsorListLoader loader = (SponsorListLoader) listLoader;
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
								}
							} );
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	@Override
	public void onLoaderReset( Loader<List<Sponsor>> listLoader ) {
	}

	private void setIndexes() {
		int alliesIndex = -1;
		int organsIndex = -1;

		int index = 0;
		for( Sponsor sponsor : sponsors ) {
			if( alliesIndex == -1 && sponsor.getSubtype().equals( "allied" ) ) {
				alliesIndex = index;
			}
			if( organsIndex == -1 && sponsor.getSubtype().equals( "organization" ) ) {
				organsIndex = index;
			}

			index++;
		}
		sponsorListAdapter.setAlliesIndex( alliesIndex );
		sponsorListAdapter.setOrgansIndex( organsIndex );
	}

}
