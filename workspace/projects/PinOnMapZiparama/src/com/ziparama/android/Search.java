package com.ziparama.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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

public class Search extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<List<LatLongData>>, OnMarkerClickListener {

	// Google Map
	private GoogleMap googleMap;
	AppPreferences myPrefs;
	String cat1, cat2;
	String jsonString;
	ProgressDialog progress;
	ImageView menu_btn;
	PopupMenu menu;
	Map<String, LatLongData> mapData;
	ConnectionDetector connectionDetector;
	AlertDialogManagerService alertDialog;
	Intent intent;
	List<LatLongData> list = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_pin);

		myPrefs = AppPreferences.getInstance(Search.this);
		connectionDetector = new ConnectionDetector(this);
		alertDialog = new AlertDialogManagerService(this);
		menu_btn = (ImageView) findViewById(R.id.menu_img);
		intent = getIntent();
	
		
		menu_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				menu = new PopupMenu(Search.this, menu_btn);
				menu.getMenuInflater().inflate(R.menu.chooser, menu.getMenu());

				if (myPrefs.isRegistered()) {
					menu.getMenu().findItem(R.id.logout).setTitle("Logout");
				} else
					menu.getMenu().findItem(R.id.logout).setTitle("Login");

				menu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					public boolean onMenuItemClick(MenuItem menu) {

						if (!connectionDetector.isConnectingToInternet()) {
							alertDialog.showAlertDialog("Connection Error",
									"You are not connected to Internet");
							return false;
						}

						switch (menu.getItemId()) {
						case R.id.pinmap:
						       
							startActivity(new Intent(Search.this,
									DragOnlyMap.class));
							finish();
						
							break;
						case R.id.viewmypin:
							if (!myPrefs.isRegistered())
							{
								startActivity(new Intent(Search.this,
										LoginActivity.class));
								finish();
							}
							else {
								Intent intent = new Intent(Search.this,
										ViewAllPins.class);
								intent.putExtra("GET", "MY_PINS");
								startActivity(intent);
								finish();
							}
							break;

						case R.id.viewallpins:
							Intent intent = new Intent(Search.this,
									ViewAllPins.class);
							intent.putExtra("GET", "ALL_PINS");
							startActivity(intent);
							finish();
							break;

						case R.id.howitwork:
							Log.d("works", "how it works");

							startActivity(new Intent(Search.this,
									HowItWorks.class));
							finish();
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
								Toast.makeText(Search.this,
										"Successfully logout",
										Toast.LENGTH_LONG).show();
							} else {
								startActivity(new Intent(Search.this,
										LoginActivity.class));
								finish();
								Toast.makeText(Search.this,
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

		/*-----------------------------------------------------------------------------------------------*/
		myPrefs = AppPreferences.getInstance(Search.this);
		// new GetAllPinsWebservice(Search.this).execute("");
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
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				Log.d("innerpart", list.get(i).getTitle());
			
				if (list.get(i).getTitle().contains(intent.getStringExtra("str"))) {
					
					markerOption.position(new LatLng(list.get(i).getLatitude(),
							list.get(i).getLongitude()));
					Marker marker = googleMap.addMarker(markerOption);
					mapData.put(marker.getId(), list.get(i));
					// Move the camera to last position with a zoom level
					if (i == list.size() - 1) {
						CameraPosition cameraPosition = new CameraPosition.Builder()
								.target(new LatLng(list.get(list.size() - 1)
										.getLatitude(), list.get(
										list.size() - 1).getLongitude()))
								.zoom(10).build();

						googleMap.animateCamera(CameraUpdateFactory
								.newCameraPosition(cameraPosition));
					}

				}/* else {
					Toast.makeText(Search.this, "Does Not Found pin title",
							Toast.LENGTH_LONG).show();
					
				}*/
			}
		} else
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

		new MarkerDialog(Search.this, mapData.get(marker.getId()).getTitle(),
				mapData.get(marker.getId()).getDescription(), mapData.get(
						marker.getId()).getImageUrl(), mapData.get(
						marker.getId()).getCategory(), mapData.get(
						marker.getId()).getSubcategory()).show();
		return false;

	}
}