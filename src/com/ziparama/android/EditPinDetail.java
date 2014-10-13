package com.ziparama.android;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import webservice.EditWebservice;
import webservice.ImageLoader;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.connection.ziparama.AlertDialogManagerService;
import com.connection.ziparama.ConnectionDetector;
import com.connection.ziparama.CustomHttpClient;
import com.work.prefs.AppPreferences;
import com.ziparama.json.LatLongData;
import com.ziparama.pinanimationonmap.utils.Constant;

public class EditPinDetail extends FragmentActivity {

	MyAdapterEdit adapteredit;
	ProgressDialog progress;
	ProgressBar progressbar;
	Intent intent;
	String fileName = "";
	ImageView cancle;
	String imageString;
	String category_id;
	TextView limit_tv;
	Context con;
	String category_value;
	String pin_idd;
	String ImageUrl;
	String subcategory_value;
	ConnectionDetector connectionDetector;
	TextView tv;
	private ArrayAdapter<String> dataAdapter, datadapterSub;
	final static int REQUEST_TAKE_PICTURE = 1000;
	private static final int REQUEST_SEND_IMAGE = 2000;
	ImageView img;
	ImageButton rotateimg;
	AppPreferences myPrefs;
	private Spinner catspin, subcatspin;
	List<LatLongData> listedit;
	ListView list;
	String title,desp,imagepath;
	int pos;
	HashMap<String, String> map;
	ArrayList<String> listdata = new ArrayList<String>();
	ArrayList<String> listsubdata = new ArrayList<String>();

	Button okbtn, canclebtn;
	TextView title_tv, desp_tv;
	ArrayList<Map<String, String>> editpinarray;
	String cat, subCat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_edit_pin);

		cancle = (ImageView) findViewById(R.id.btn_cancel_edit);
		img = (ImageView) findViewById(R.id.userImagemy);
		connectionDetector = new ConnectionDetector(this);
		editpinarray = new ArrayList<Map<String, String>>();
		con = this;

		progressbar = (ProgressBar) findViewById(R.id.image_loadingedit);
		intent = getIntent();
		pos = intent.getIntExtra("pos", pos);
		pin_idd = intent.getStringExtra("PINID");
		title = intent.getStringExtra("EditTitle");
		desp = intent.getStringExtra("Description");
		imagepath = intent.getStringExtra("ImagePath");
		subCat = intent.getStringExtra("SubCategory");
		cat = intent.getStringExtra("Category");
		limit_tv = (TextView) findViewById(R.id.limit_edit);
		rotateimg = (ImageButton) findViewById(R.id.rotateedit);
		myPrefs = AppPreferences.getInstance(EditPinDetail.this);
		
		okbtn = (Button) findViewById(R.id.okbtn);
		catspin = (Spinner) findViewById(R.id.categoryedit);
		subcatspin = (Spinner) findViewById(R.id.subcategoryedit);
		canclebtn = (Button) findViewById(R.id.canclebtn);
		title_tv = (TextView) findViewById(R.id.pintitlemy);
		desp_tv = (TextView) findViewById(R.id.pindescriptionmy);

		title_tv.setText(title);
		desp_tv.setText(desp);
		if (Constant.isBitmapAvailable(imagepath))
			img.setImageBitmap(Constant.getBitmaps(imagepath));
		else
			new ImageLoader(con, img, progressbar).execute(imagepath);
		new AsyncTaskRunner().execute("");
		imageString = getBitmapString(((BitmapDrawable) img.getDrawable())
				.getBitmap());
		limit_tv.setText(desp.length() + "/500");
		TextWatcher textWatcher = new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				limit_tv.setText(s.length() + "/500");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		};
		desp_tv.addTextChangedListener(textWatcher);
		addListenerOnSpinnerItemSelection();

		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(EditPinDetail.this,
						MainActivity.class);
				startActivity(intent);
			}
		});
		canclebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(EditPinDetail.this,
						MainActivity.class);
				startActivity(intent);
			}
		});

		/*-------------------------------------------------------------------------------------*/

		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Create instance of Alert Dialog Builder
				AlertDialog.Builder builder = new AlertDialog.Builder(
						EditPinDetail.this);
				// create array of items basic colors RGB (Red,Green,Blue)
				CharSequence[] items = { "Take from camera",
						"Choose from gallery" };
				// set title
				builder.setTitle("Chose Photo..");
				builder.setItems(items, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which) {
						case 0:
							startPhotoTaker();
							break;
						case 1:
							startImagePicker(REQUEST_SEND_IMAGE);
							break;

						}
					}
				});

				AlertDialog alert = builder.create();

				alert.show();

			}
		});

		rotateimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Matrix matrix = new Matrix();
					matrix.postRotate(90);

					Bitmap scaledBitmap = ((BitmapDrawable) img.getDrawable())
							.getBitmap();

					Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0,
							0, scaledBitmap.getWidth(),
							scaledBitmap.getHeight(), matrix, true);
					com.ziparama.pinanimationonmap.utils.Constant.currentImage = rotatedBitmap;
					// userSnap.setBackgroundDrawable(new
					// BitmapDrawable(rotatedBitmap));
					img.setImageBitmap(rotatedBitmap);

					File ext = Environment.getExternalStorageDirectory();

					File outputFile = new File(ext, fileName);
					FileOutputStream out = new FileOutputStream(outputFile);
					rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
					com.ziparama.pinanimationonmap.utils.Constant.imagePath = outputFile
							.getAbsolutePath();

					Log.e("neo",
							"Path to temp : "
									+ com.ziparama.pinanimationonmap.utils.Constant.imagePath);

					try {
						try {
							out.flush();
							out.close();
						} catch (Exception e) {
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		okbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!connectionDetector.isConnectingToInternet()) {
					new AlertDialogManagerService(EditPinDetail.this)
							.showAlertDialog("Connection Error",
									"Check your internet connectivity");

					return;
				}
				String title_ed = title_tv.getText().toString();
				String description_ed = desp_tv.getText().toString();
				if((title_ed.equals("")) && (description_ed.equals("")))
				{
					Toast.makeText(EditPinDetail.this, "Please enter title and Description", Toast.LENGTH_LONG).show();
				}
				else if(title_ed.equals(""))
				{
					Toast.makeText(EditPinDetail.this, "Please enter title", Toast.LENGTH_LONG).show();
				}
				else if(description_ed.equals(""))
				{
					Toast.makeText(EditPinDetail.this, "Please enter Description", Toast.LENGTH_LONG).show();
				}else{
				if (myPrefs.isRegistered())

					new EditWebservice(EditPinDetail.this, pin_idd, title_tv
							.getText().toString(),
							desp_tv.getText().toString(), imageString, String
									.valueOf(catspin.getSelectedItem()), String
									.valueOf(subcatspin.getSelectedItem()))
							.execute("");
				}
			}
		});

	}

	void startImagePicker(int action) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, action);
	}

	Uri mLastPhoto = null;

	void startPhotoTaker() {

		// create Intent to take a picture and return control to the calling
		// application
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File photo = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
				"cs_" + new Date(0).getTime() + ".jpg");
		mLastPhoto = Uri.fromFile(photo);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mLastPhoto);

		// start the image capture Intent
		startActivityForResult(intent, REQUEST_TAKE_PICTURE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == FragmentActivity.RESULT_OK) {

			if (requestCode == REQUEST_SEND_IMAGE) {
				Log.d("REQUEST_SEND_IMAGE", REQUEST_SEND_IMAGE + "");
				Uri uri = data.getData();
				if (uri == null) {
					return;
				}
				String tempPath = getRealPathFromURI(uri);
				Bitmap bm = decodeSampledBitmapFromFile(tempPath, 200, 200);
				imageString = getBitmapString(bm);
				img.setImageBitmap(bm);

			} else if (requestCode == REQUEST_TAKE_PICTURE) {
				// Log.d("REQUEST_TAKE_PICTURE", REQUEST_TAKE_PICTURE+"");
				File file = new File(getRealPathFromURI(mLastPhoto));
				final Handler handler = new Handler();
				MediaScannerConnection.scanFile(this,
						new String[] { file.toString() }, null,
						new MediaScannerConnection.OnScanCompletedListener() {
							public void onScanCompleted(String path,
									final Uri uri) {

								handler.post(new Runnable() {
									@Override
									public void run() {
										String tempPath = getRealPathFromURI(mLastPhoto);
										Bitmap bm = decodeSampledBitmapFromFile(
												tempPath, 200, 200);
										imageString = getBitmapString(bm);
										img.setImageBitmap(bm);

									}
								});
							}
						});

			}
		}
	}

	@SuppressWarnings("deprecation")
	public String getRealPathFromURI(Uri contentUri) {

		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = this.managedQuery(contentUri, proj, null, null,
					null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Exception e) {
			return contentUri.getPath();
		}
	}

	public String getBitmapString(Bitmap bitmap) {
		String bitmapString = null;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
		byte[] b = baos.toByteArray();
		bitmapString = Base64.encodeToString(b, Base64.DEFAULT);

		return bitmapString;
	}

	public static Bitmap decodeSampledBitmapFromFile(String pathName,
			int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(pathName, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(pathName, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	
	

	// add items into spinner dynamically
	public void addItemsOnsubcatspin() {

		subcatspin = (Spinner) findViewById(R.id.subcategoryedit);

		dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listsubdata);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		subcatspin.setAdapter(dataAdapter);
		for (int i = 0; i < listsubdata.size(); i++) {
			if (listdata.get(i).equals(subCat)) {
				subcatspin.setSelection(i);
				break;
			}
		}
	}

	public void addListenerOnSpinnerItemSelection() {

		catspin.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}

	// get the selected dropdown list value
	public void addListenerOnButton() {

		subcatspin = (Spinner) findViewById(R.id.subcategoryedit);

	}

	public class CustomOnItemSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			Toast.makeText(
					parent.getContext(),
					"OnItemSelectedListener : "
							+ parent.getItemAtPosition(pos).toString(),
					Toast.LENGTH_SHORT).show();

			new AsyncTaskRunnersubcategory((pos + 1) + "").execute();

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	}

	class AsyncTaskRunner extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			myPrefs = AppPreferences.getInstance(EditPinDetail.this);
			progress = new ProgressDialog(EditPinDetail.this);
			progress.setMessage("Loading...");

			progress.show();

		}

		@Override
		protected String doInBackground(String... params) {
			String response = null;
			JSONArray contacts = null;

			try {
				response = CustomHttpClient
						.executeHttpGet(
								"http://174.121.164.66/~indianet/ziparama-api/get-category.php",
								EditPinDetail.this);
				Log.d("response", response);

				JSONObject jsonObj = new JSONObject(response);

				contacts = jsonObj.getJSONArray("maps");
				Log.d("categoryresponse", response);
				for (int i = 0; i < contacts.length(); i++) {
					JSONObject c = contacts.getJSONObject(i);
					String name = c.getString("category");
					listdata.add(name);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progress.dismiss();
			Log.d("categoryresult", result);
			if (result != null) {

				dataAdapter = new ArrayAdapter<String>(EditPinDetail.this,
						android.R.layout.simple_spinner_item, listdata);
				dataAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				catspin.setAdapter(dataAdapter);
				for (int i = 0; i < listdata.size(); i++) {
					if (listdata.get(i).equals(cat)) {
						catspin.setSelection(i);
						break;
					}
				}

			}

		}
	}

	class AsyncTaskRunnersubcategory extends AsyncTask<String, String, String> {

		private String response = null;
		private String edit_cid = null;

		public AsyncTaskRunnersubcategory(String cid) {
			this.edit_cid = cid;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			myPrefs = AppPreferences.getInstance(EditPinDetail.this);
			progress = new ProgressDialog(EditPinDetail.this);
			progress.setMessage("Loading...");
			progress.show();

		}

		@Override
		protected String doInBackground(String... params) {
			String response = null;
			JSONArray contacts = null;
			listsubdata = new ArrayList<String>();

			ArrayList<NameValuePair> parameter = new ArrayList<NameValuePair>();
			parameter.add(new BasicNameValuePair("cid", edit_cid));

			try {
				response = CustomHttpClient
						.executeHttpPost(
								"http://174.121.164.66/~indianet/ziparama-api/get-category.php",
								parameter, EditPinDetail.this);
				Log.d("subcategoryresponse", response);

				JSONObject jsonObj = new JSONObject(response);

				contacts = jsonObj.getJSONArray("maps");
				for (int i = 0; i < contacts.length(); i++) {
					JSONObject c = contacts.getJSONObject(i);

					String name = c.getString("subcategory");
					listsubdata.add(name);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progress.dismiss();
			Log.d("subcategoryresult", result);
			if (result != null) {

				datadapterSub = new ArrayAdapter<String>(EditPinDetail.this,
						android.R.layout.simple_spinner_item, listsubdata);
				datadapterSub
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				subcatspin.setAdapter(datadapterSub);
				for (int i = 0; i < listsubdata.size(); i++) {
					if (listdata.get(i).equals(subCat)) {
						subcatspin.setSelection(i);
						break;
					}
				}
			}

		}
	}
}
