package com.ziparama.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import webservice.DeleteMyPin;
import webservice.LoadingAllPinMapAsync;
import webservice.LoadingMyPinMapAsync;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.connection.ziparama.AlertDialogManagerService;
import com.connection.ziparama.ConnectionDetector;
import com.connection.ziparama.MarkerDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.work.prefs.AppPreferences;
import com.ziparama.json.LatLongData;

public class ViewAllPins extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<List<LatLongData>>, OnMarkerClickListener {

	// Google Map
	private GoogleMap googleMap;
	AppPreferences myPrefs;
	String jsonString;
	ProgressDialog progress;
	ImageView menu_btn, searbtn;
	PopupMenu menu;

	ConnectionDetector connectionDetector;
	AlertDialogManagerService alertDialog;
	Intent intent;
	EditText searchtxt;
	String searchtext;
	Map<String, LatLongData> mapData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_my_pin);

		myPrefs = AppPreferences.getInstance(ViewAllPins.this);
		connectionDetector = new ConnectionDetector(this);
		alertDialog = new AlertDialogManagerService(this);
		intent = getIntent();
		searchtxt = (EditText) findViewById(R.id.searchbox);
		searbtn = (ImageView) findViewById(R.id.searchbtn);
		
		menu_btn = (ImageView) findViewById(R.id.menu_img);
		menu_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				menu = new PopupMenu(ViewAllPins.this, menu_btn);
				menu.getMenuInflater().inflate(R.menu.chooser, menu.getMenu());

				if (myPrefs.isRegistered()) {
					menu.getMenu().findItem(R.id.logout).setTitle("Logout");
					menu.getMenu().findItem(R.id.editpin).setVisible(true);
					menu.getMenu().findItem(R.id.deletepin).setVisible(true);
				} else
				{
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
							startActivity(new Intent(ViewAllPins.this,
									DragOnlyMap.class));
							finish();
							break;
						case R.id.viewmypin:
							if (!myPrefs.isRegistered()){
								startActivity(new Intent(ViewAllPins.this,
										LoginActivity.class));
							finish();}
							else {
								Intent intent = new Intent(ViewAllPins.this,
										ViewAllPins.class);
								intent.putExtra("GET", "MY_PINS");
								startActivity(intent);
								finish();
							}

							break;
						case R.id.viewallpins:
							Intent intent = new Intent(ViewAllPins.this,
									ViewAllPins.class);
							intent.putExtra("GET", "ALL_PINS");
							startActivity(intent);
							finish();
							break;

						case R.id.howitwork:
							Log.d("works", "how it works");

							startActivity(new Intent(ViewAllPins.this,
									HowItWorks.class));
							finish();
							break;
							
						case R.id.editpin:
							if (myPrefs.isRegistered()) {
								Intent intent1 = new Intent(ViewAllPins.this,
										EditPin.class);
							intent1.putExtra("GET", "MY_PINS");
							startActivity(intent1);
							finish();
							}
							break;
						
						case R.id.deletepin:
							if (myPrefs.isRegistered()) {
								Intent intent2 = new Intent(ViewAllPins.this,
										DeletePin.class);
							intent2.putExtra("GET", "MY_PINS");
							startActivity(intent2);
							finish();
								//new DeleteMyPin(ViewAllPins.this,"Doremon");
							}
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
								Toast.makeText(ViewAllPins.this,
										"Successfully logout",
										Toast.LENGTH_LONG).show();
							} else {
								startActivity(new Intent(ViewAllPins.this,
										LoginActivity.class));
								finish();
								Toast.makeText(ViewAllPins.this,
										"Please Login !!!!!!",
										Toast.LENGTH_LONG).show();
							}

							break;
						}
						return false;
					}

				});
				menu.show();
			}
		});
		
		searbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				searchtext = searchtxt.getText().toString();
				Intent intent = new Intent(ViewAllPins.this, Search.class);
				intent.putExtra("str", searchtext);
				intent.putExtra("GET", "ALL_PINS");
				startActivity(intent);

			}
		});
		/*-----------------------------------------------------------------------------------------------*/
		myPrefs = AppPreferences.getInstance(ViewAllPins.this);
		// new GetAllPinsWebservice(ViewAllPins.this).execute("");
		try {
			// Loading map
			initilizeMap();

			// Changing map type
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

			// Showing / hiding your current location
			googleMap.setMyLocationEnabled(true);

			googleMap.setOnMapLongClickListener(new OnMapLongClickListener() {

				@Override
				public void onMapLongClick(LatLng arg0) {
					// TODO Auto-generated method stub

				}
			});

			googleMap.setOnMarkerClickListener(this);

		} catch (Exception e) {
			e.printStackTrace();
			Log.d("Oncreate", "error");
		}

		Log.d("Oncreate", "Sucess");

		getSupportLoaderManager().initLoader(10001, null, this).forceLoad();
		Log.d("Oncreate", "Sucess1");
	}

	// @Override
	// protected void onResume() {
	// super.onResume();
	// initilizeMap();
	// }

	/**
	 * function to load map If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	@Override
	public Loader<List<LatLongData>> onCreateLoader(int id, Bundle bundle) {
		Log.d("Oncreate", "loading");
		progress = new ProgressDialog(this);
		progress.setMessage("Loading...");
		progress.show();
		if (intent.getStringExtra("GET").equals("ALL_PINS"))
			return new LoadingAllPinMapAsync(this,
					"http://174.121.164.66/~indianet/ziparama-api/get.php");
		ArrayList<NameValuePair> array = new ArrayList<NameValuePair>();
		array.add(new BasicNameValuePair("user_id", myPrefs.isUId()));

		return new LoadingMyPinMapAsync(this,
				"http://174.121.164.66/~indianet/ziparama-api/get.php", array);
	}

	@Override
	public void onLoadFinished(Loader<List<LatLongData>> loader,
			List<LatLongData> list) {
		mapData = new HashMap<String, LatLongData>();
		MarkerOptions markerOption = new MarkerOptions();
		Log.d("Oncreate", "loadcomplete");
		// Adding a marker
		if (list != null)
			for (int i = 0; i < list.size(); i++) {

				markerOption.position(new LatLng(list.get(i).getLatitude(),
						list.get(i).getLongitude()));
				Marker marker = googleMap.addMarker(markerOption);
				mapData.put(marker.getId(), list.get(i));
				
				// Move the camera to last position with a zoom level
				if (i == list.size() - 1) {
					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(new LatLng(list.get(list.size() - 1)
									.getLatitude(), list.get(list.size() - 1)
									.getLongitude())).zoom(10).build();

					googleMap.animateCamera(CameraUpdateFactory
							.newCameraPosition(cameraPosition));
				}
			}
		else
			Log.d("Oncreate", "null List");
		progress.dismiss();
	}

	@Override
	public void onLoaderReset(Loader<List<LatLongData>> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		new MarkerDialog(ViewAllPins.this, mapData.get(marker.getId())
				.getTitle(), mapData.get(marker.getId()).getDescription(),
				mapData.get(marker.getId()).getImageUrl(), mapData.get(
						marker.getId()).getCategory(), mapData.get(
						marker.getId()).getSubcategory()).show();
		return false;
	}
}