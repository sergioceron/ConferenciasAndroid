package com.dotrow.diaempresario.sponsors;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 16/01/14 12:34 PM
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.dotrow.diaempresario.FontManager;
import com.dotrow.diaempresario.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SponsorListAdapter extends ArrayAdapter<Sponsor> {
	private final static String IMAGES_URL = "http://conferences.dotrow.com/images/sponsor/";

	private FontManager fontManager;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	private final Context context;
	private final List<Sponsor> itemsArrayList;
	//private SponsorImageThread sponsorImageThread;
	private DisplayImageOptions options;
	private int alliesIndex = 1;
	private int organsIndex = 2;

	public SponsorListAdapter( Context context, List<Sponsor> itemsArrayList ) {
		super( context, R.layout.sponsor_item, itemsArrayList );

		options = new DisplayImageOptions.Builder()
				//.showImageOnLoading( R.drawable.ic_stub )
				//.showImageForEmptyUri( R.drawable.ic_empty )
				//.showImageOnFail( R.drawable.ic_error )
				.cacheInMemory( true )
				.cacheOnDisc( true )
				.considerExifParams( true )
				.displayer( new RoundedBitmapDisplayer( 20 ) )
				.build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder( QueueProcessingType.LIFO)
				//.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);


		this.context = context;
		this.itemsArrayList = itemsArrayList;
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent ) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		Sponsor sponsor = itemsArrayList.get( position );

		View rowView = inflater.inflate( R.layout.sponsor_item, parent, false );


		fontManager = FontManager.getInstance( context.getAssets() );

		final ImageView sponsorImage = (ImageView) rowView.findViewById( R.id.sponsorThumb );
		TextView nameView = (TextView) rowView.findViewById( R.id.name );
		TextView urlView = (TextView) rowView.findViewById( R.id.url );
		TextView separator = (TextView) rowView.findViewById( R.id.separator );

		nameView.setTypeface( fontManager.HELVETICA_45 );
		urlView.setTypeface( fontManager.HELVETICA_45 );

		nameView.setText( sponsor.getName() );
		urlView.setText( sponsor.getUrl() );

		if( position == 0 || position == alliesIndex || position == organsIndex ) {
			String titleSeparator = "";
			if( sponsor.getSubtype().equals( "sponsor" ) ) {
				titleSeparator = "Patrocinadores";
			} else if( sponsor.getSubtype().equals( "allied" ) ) {
				titleSeparator = "Aliados Estratégicos";
			} else if( sponsor.getSubtype().equals( "organization" ) ) {
				titleSeparator = "Instituciones de Apoyo";
			}
			separator.setText( titleSeparator );
			separator.setVisibility( View.VISIBLE );
		}

		ImageLoader imageLoader = ImageLoader.getInstance();

		if( sponsor.getImage() != null && sponsor.getImage().length() > 0 )
			imageLoader.displayImage(IMAGES_URL + sponsor.getImage(), sponsorImage, options, animateFirstListener);

		//sponsorImage.setImageDrawable( sponsor.getImageBitmap() );

		/*if( sponsorImageExist( sponsor.getImage() ) ) {
			Drawable image = getImageFromFile( sponsor.getImage() );
			sponsorImage.setImageDrawable( image );
		} else {
			sponsorImage.setImageDrawable( context.getResources().getDrawable( R.drawable.user_default ) );
			sponsorImageThread = new SponsorImageThread( sponsor.getImage(), context, new Handler() {
				@Override
				public void handleMessage( Message msg ) {
					if( sponsorImageThread.getDrawable() != null ) {
						sponsorImage.setImageDrawable( sponsorImageThread.getDrawable() );
					}
				}
			} );
			sponsorImageThread.start();
		}*/


		return rowView;
	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList( new LinkedList<String>() );

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate( imageView, 500 );
					displayedImages.add(imageUri);
				}
			}
		}
	}

	/*private Drawable getImageFromFile( String image ) {
		try {
			InputStream imageStream = context.openFileInput( image );
			Bitmap bmp = BitmapFactory.decodeStream( imageStream );
			imageStream.close();
			return new BitmapDrawable( bmp );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean sponsorImageExist( String image ) {
		File file = new File( context.getFilesDir(), image );
		return file.exists();
	}*/

	/*public int getAlliesIndex() {
		return alliesIndex;
	}*/

	public void setAlliesIndex( int alliesIndex ) {
		this.alliesIndex = alliesIndex;
	}

	/*public int getOrgansIndex() {
		return organsIndex;
	}*/

	public void setOrgansIndex( int organsIndex ) {
		this.organsIndex = organsIndex;
	}
}