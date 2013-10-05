package net.lnmcc.mediaGallery;

import com.example.mediastoregallery.R;

import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {

	private final static String TAG = "MainActivity";

	public final static int DISPLAYWIDTH = 200;
	public final static int DISPLAYHEIGHT = 200;

	TextView titleTextView;
	TextView descriptionTextView;
	ImageButton imageButton;
	Cursor cursor;
	Bitmap bmp;
	String imageFilePath;
	int fileColum;
	int titleColum;
	int displayColum;
	int descriptColum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		titleTextView = (TextView) findViewById(R.id.TitleTextView);
		descriptionTextView = (TextView) findViewById(R.id.descriptTextView);
		imageButton = (ImageButton) findViewById(R.id.ImageButton);

		String[] colums = { Media.DATA, Media._ID, Media.TITLE,
				Media.DISPLAY_NAME, Media.DESCRIPTION };
		cursor = managedQuery(Media.EXTERNAL_CONTENT_URI, colums, null, null,
				null);

			fileColum = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			titleColum = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
			displayColum = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
			descriptColum = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DESCRIPTION);

		if (cursor.moveToFirst()) {
			titleTextView.setText(cursor.getString(displayColum));
			descriptionTextView.setText(cursor.getString(descriptColum));
			imageFilePath = cursor.getString(fileColum);
			bmp = getBitmap(imageFilePath);
			imageButton.setImageBitmap(bmp);
		}

		imageButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (cursor.moveToNext()) {
					titleTextView.setText(cursor.getString(displayColum));
					descriptionTextView.setText(cursor.getString(descriptColum));
					imageFilePath = cursor.getString(fileColum);
					bmp = getBitmap(imageFilePath);
					imageButton.setImageBitmap(bmp);
				}
			}
		});
	}

	private Bitmap getBitmap(String imageFilePath) {
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(imageFilePath, bmpFactoryOptions);
		int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
				/ (float) DISPLAYHEIGHT);
		int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
				/ (float) DISPLAYWIDTH);
		if (heightRatio > 1 && widthRatio > 1) {
			if (heightRatio > widthRatio) {
				bmpFactoryOptions.inSampleSize = heightRatio;
			} else {
				bmpFactoryOptions.inSampleSize = widthRatio;
			}
		}
		bmpFactoryOptions.inJustDecodeBounds = false;
		bmp = BitmapFactory.decodeFile(imageFilePath, bmpFactoryOptions);
		return bmp;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
