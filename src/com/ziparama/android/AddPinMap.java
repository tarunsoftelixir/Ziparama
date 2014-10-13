package com.ziparama.android;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import webservice.PinWebservice;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.connection.ziparama.AlertDialogManagerService;
import com.connection.ziparama.ConnectionDetector;
import com.connection.ziparama.CustomHttpClient;
import com.work.prefs.AppPreferences;

public class AddPinMap extends Activity {

	Button submit;
	// Intent intent;
	String fileName = "";
	String pin_idd;
	double lat_addpin, long_addpin;
	String title, email, description;
	EditText title_text, description_text, email_text;
	ImageView img = null;
	ImageView menu_btn, searbtn;
	ProgressDialog progress;
	TextView limit_tv;
	PopupMenu menu;
	ArrayList<String> mylist;
	String category_id;
	ArrayList<String> listdata = new ArrayList<String>();
	ArrayList<String> listsubdata = new ArrayList<String>();
	final static int REQUEST_TAKE_PICTURE = 1000;
	private static final int REQUEST_SEND_IMAGE = 2000;
	AppPreferences myPrefs;
	String imageString;
	String category_value;
	String subcategory_value;
	private Spinner catspin, subcatspin;
	AlertDialogManagerService alertDialog;
	ConnectionDetector connectionDetector;
	EditText searchtxt;
	ImageButton rotateimg;
	String searchtext;
	private ArrayAdapter<String> dataAdapter, datadapterSub;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_pin_new_layout);

		com.ziparama.pinanimationonmap.utils.Constant.imagePath = null;
		submit = (Button) findViewById(R.id.submitButton);
		lat_addpin = getIntent().getDoubleExtra("lat_drag", 1);
		long_addpin = getIntent().getDoubleExtra("long_drag", 2);
		img = (ImageView) findViewById(R.id.userImage);
		title_text = (EditText) findViewById(R.id.pintitle);
		catspin = (Spinner) findViewById(R.id.category);
		subcatspin = (Spinner) findViewById(R.id.subcategory);
		searbtn = (ImageView) findViewById(R.id.searchbtn);
		rotateimg = (ImageButton) findViewById(R.id.rotate);
		limit_tv = (TextView) findViewById(R.id.limit);

		description_text = (EditText) findViewById(R.id.pindescription);
		email_text = (EditText) findViewById(R.id.pinemail);
		searchtxt = (EditText) findViewById(R.id.searchbox);

		myPrefs = AppPreferences.getInstance(this);
		new AsyncTaskRunner().execute("");
		imageString = getBitmapString(((BitmapDrawable) img.getDrawable())
				.getBitmap());
		alertDialog = new AlertDialogManagerService(this);

		connectionDetector = new ConnectionDetector(this);

		addListenerOnSpinnerItemSelection();
		// addItemsOnsubcatspin();
		// addListenerOnButton();
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
		description_text.addTextChangedListener(textWatcher);
		searbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				searchtext = searchtxt.getText().toString();
				Intent intent = new Intent(AddPinMap.this, ViewAllPins.class);
				intent.putExtra("SEARCH_TEXT", searchtext);
				intent.putExtra("GET", "ALL_PINS");
				intent.putExtra("SEARCH", "SEARCH");
				startActivity(intent);

			}
		});
		if (myPrefs.isRegistered()) {
			if (myPrefs.isEmail() != "" || myPrefs.isEmail() != null)
				email_text.setVisibility(View.GONE);
		}

		menu_btn = (ImageView) findViewById(R.id.menu_img);
		menu_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				menu = new PopupMenu(AddPinMap.this, menu_btn);
				menu.getMenuInflater().inflate(R.menu.chooser, menu.getMenu());

				if (myPrefs.isRegistered()) {
					menu.getMenu().findItem(R.id.logout).setTitle("Logout");
					menu.getMenu().findItem(R.id.editpin).setVisible(true);
					menu.getMenu().findItem(R.id.deletepin).setVisible(true);
				} else {
					menu.getMenu().findItem(R.id.logout).setTitle("Login");
					menu.getMenu().findItem(R.id.editpin).setVisible(false);
					menu.getMenu().findItem(R.id.deletepin).setVisible(false);
				}

				menu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					public boolean onMenuItemClick(MenuItem menu) {

						if (!connectionDetector.isConnectingToInternet()) {
							alertDialog.showAlertDialog("Connection Error",
									"You are not connected to Internet");
							return false;
						}

						switch (menu.getItemId()) {
						case R.id.pinmap:
							startActivity(new Intent(AddPinMap.this,
									AddPinMap.class));

							break;
						case R.id.viewmypin:
							if (!myPrefs.isRegistered())
								startActivity(new Intent(AddPinMap.this,
										LoginActivity.class));
							else {
								Intent intent = new Intent(AddPinMap.this,
										ViewAllPins.class);
								intent.putExtra("GET", "MY_PINS");
								startActivity(intent);
							}

							break;
						case R.id.editpin:
							if (myPrefs.isRegistered()) {
								startActivity(new Intent(AddPinMap.this,
										EditPin.class));
							}
							break;

						case R.id.deletepin:
							if (myPrefs.isRegistered()) {
								startActivity(new Intent(AddPinMap.this,
										DeletePin.class));
							}
							break;
						case R.id.viewallpins:
							Intent intent = new Intent(AddPinMap.this,
									ViewAllPins.class);
							intent.putExtra("GET", "ALL_PINS");
							startActivity(intent);
							break;

						case R.id.howitwork:
							Log.d("works", "how it works");

							startActivity(new Intent(AddPinMap.this,
									HowItWorks.class));
							break;
						case R.id.logout:
							// TODO Auto-generated method stub
							if (myPrefs.isRegistered()) {
								myPrefs.setRegistered(false);
								myPrefs.setUid("");
								myPrefs.setEmail("");
								myPrefs.setGender("");
								myPrefs.setFirstName("");
								myPrefs.setLastName("");
								myPrefs.setDOB("");
								Toast.makeText(AddPinMap.this,
										"Successfully logout",
										Toast.LENGTH_LONG).show();
							} else {
								startActivity(new Intent(AddPinMap.this,
										LoginActivity.class));
								Toast.makeText(AddPinMap.this,
										"Please Login !!!!!!",
										Toast.LENGTH_LONG).show();
							}

							break;
						}
						finish();
						return false;
					}

				});
				menu.show();
			}
		});

		/*-----------------------------------------------------------------------------------------------*/
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Create instance of Alert Dialog Builder
				AlertDialog.Builder builder = new AlertDialog.Builder(
						AddPinMap.this);
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
				/*
				 * create instance of alert dialogand assign configuration of
				 * builderto alert dialog instance
				 */
				AlertDialog alert = builder.create();
				// Show Alert Dialog
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
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!connectionDetector.isConnectingToInternet()) {
					new AlertDialogManagerService(AddPinMap.this)
							.showAlertDialog("Connection Error",
									"Check your internet connectivity");
					return;
				}

				title = title_text.getText().toString();
				description = description_text.getText().toString();
				if((title.equals("")) && (description.equals("")))
				{
					Toast.makeText(AddPinMap.this, "Please enter title and Description", Toast.LENGTH_LONG).show();
				}
				else if(title.equals(""))
				{
					Toast.makeText(AddPinMap.this, "Please enter title", Toast.LENGTH_LONG).show();
				}
				else if(description.equals(""))
				{
					Toast.makeText(AddPinMap.this, "Please enter Description", Toast.LENGTH_LONG).show();
				}
				else
				{
					if (email_text.getVisibility() != View.GONE)
						email = email_text.getText().toString();
					else
						email = "";

					if (myPrefs.isRegistered())
						new PinWebservice(AddPinMap.this, myPrefs.isUId(), title,
								description, email, imageString, lat_addpin + "",
								long_addpin + "", String.valueOf(catspin
										.getSelectedItem()), String
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

		subcatspin = (Spinner) findViewById(R.id.subcategory);

		dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, listsubdata);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		subcatspin.setAdapter(dataAdapter);
	}

	public void addListenerOnSpinnerItemSelection() {

		catspin.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}

	// get the selected dropdown list value
	public void addListenerOnButton() {

		subcatspin = (Spinner) findViewById(R.id.subcategory);

	}

	public class CustomOnItemSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			Toast.makeText(
					parent.getContext(),
					"OnItemSelectedListener : "
							+ parent.getItemAtPosition(pos).toString(),
					Toast.LENGTH_SHORT).show();

			// if (pos == 0) {
			// return;
			// }
			Log.d("parent.getItemAtPosition(pos).toString()", parent
					.getItemAtPosition(pos).toString());
			new AsyncTaskRunnersubcategory((pos + 1) + "").execute();

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	}

	class AsyncTaskRunner extends AsyncTask<String, String, String> {

		private String response = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			myPrefs = AppPreferences.getInstance(AddPinMap.this);
			progress = new ProgressDialog(AddPinMap.this);
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
								AddPinMap.this);
				Log.d("response", response);

				JSONObject jsonObj = new JSONObject(response);

				contacts = jsonObj.getJSONArray("maps");
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
			if (result != null) {

				dataAdapter = new ArrayAdapter<String>(AddPinMap.this,
						android.R.layout.simple_spinner_item, listdata);
				dataAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				catspin.setAdapter(dataAdapter);
				// new AsyncTaskRunnersubcategory("1").execute("");
			}

		}
	}

	class AsyncTaskRunnersubcategory extends AsyncTask<String, String, String> {

		private String c_id = null;

		public AsyncTaskRunnersubcategory(String cid) {
			this.c_id = cid;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			myPrefs = AppPreferences.getInstance(AddPinMap.this);
			progress = new ProgressDialog(AddPinMap.this);
			progress.setMessage("Loading...");
			progress.show();

		}

		@Override
		protected String doInBackground(String... params) {
			String response = null;
			JSONArray contacts = null;
			listsubdata = new ArrayList<String>();
			Log.d("ciddd", c_id);
			ArrayList<NameValuePair> parameter = new ArrayList<NameValuePair>();
			parameter.add(new BasicNameValuePair("cid", c_id));

			try {
				response = CustomHttpClient
						.executeHttpPost(
								"http://174.121.164.66/~indianet/ziparama-api/get-category.php",
								parameter, AddPinMap.this);

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
			if (result != null) {

				datadapterSub = new ArrayAdapter<String>(AddPinMap.this,
						android.R.layout.simple_spinner_item, listsubdata);
				datadapterSub
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				subcatspin.setAdapter(datadapterSub);
			}

		}
	}

}