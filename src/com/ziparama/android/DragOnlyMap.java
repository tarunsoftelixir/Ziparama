package com.ziparama.android;

import android.app.Dialog;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.connection.ziparama.AlertDialogManagerService;
import com.connection.ziparama.ConnectionDetector;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.work.prefs.AppPreferences;

public class DragOnlyMap extends FragmentActivity implements LocationListener {

	GoogleMap googleMap;
	ImageView menu_btn, searbtn;
	PopupMenu menu;
	AlertDialogManagerService alertDialog;
	AppPreferences myPrefs;
	String searchtext;
	EditText searchtxt;
	ConnectionDetector connectionDetector;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google);
		myPrefs = AppPreferences.getInstance(this);
		connectionDetector = new ConnectionDetector(this);
		searbtn=(ImageView) findViewById(R.id.searchbtn);
		searchtxt = (EditText) findViewById(R.id.searchbox);
		alertDialog = new AlertDialogManagerService(this);
		
		intent = getIntent();
		Toast.makeText(getApplicationContext(),
				"Long press for add new pin on map", Toast.LENGTH_LONG).show();
		
		
		searbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				searchtext = searchtxt.getText().toString();
				Intent intent = new Intent(DragOnlyMap.this, ViewAllPins.class);
				intent.putExtra("SEARCH_TEXT", searchtext);
				intent.putExtra("GET", "ALL_PINS");
				intent.putExtra("SEARCH", "SEARCH");
				startActivity(intent);

			}
		});

		menu_btn = (ImageView) findViewById(R.id.menu_img);
		menu_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				menu = new PopupMenu(DragOnlyMap.this, menu_btn);
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
							startActivity(new Intent(DragOnlyMap.this,
									DragOnlyMap.class));
							finish();
							break;
						case R.id.editpin:
							if (myPrefs.isRegistered()) {
								Intent intent1 = new Intent(DragOnlyMap.this,
										EditPin.class);
							intent1.putExtra("GET", "MY_PINS");
							startActivity(intent1);
							finish();
							}
							break;
						
						case R.id.deletepin:
							if (myPrefs.isRegistered()) {
								Intent intent2 = new Intent(DragOnlyMap.this,
										DeletePin.class);
							intent2.putExtra("GET", "MY_PINS");
							startActivity(intent2);
							finish();
								//new DeleteMyPin(ViewAllPins.this,"Doremon");
							}
							break;
						case R.id.viewmypin:
							if (!myPrefs.isRegistered())
								startActivity(new Intent(DragOnlyMap.this,
										LoginActivity.class));
							else {
								Intent intent = new Intent(DragOnlyMap.this,
										ViewAllPins.class);
								intent.putExtra("GET", "MY_PINS");
								startActivity(intent);
							}

							break;
						case R.id.viewallpins:
							Intent intent = new Intent(DragOnlyMap.this,
									ViewAllPins.class);
							intent.putExtra("GET", "ALL_PINS");
							startActivity(intent);
							break;

						case R.id.howitwork:
							Log.d("works", "how it works");

							startActivity(new Intent(DragOnlyMap.this,
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
								Toast.makeText(DragOnlyMap.this,
										"Successfully logout",
										Toast.LENGTH_LONG).show();
							} else {
								startActivity(new Intent(DragOnlyMap.this,
										LoginActivity.class));
								Toast.makeText(DragOnlyMap.this,
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

		// Getting Google Play availability status
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

		// Getting GoogleMap object from the fragment
		googleMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		// Enabling MyLocation Layer of Google Map
		googleMap.setMyLocationEnabled(true);

		googleMap.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng latLng) {

				if (!connectionDetector.isConnectingToInternet()) {
					Toast.makeText(DragOnlyMap.this,
							"Network is not available", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				// Clears the previously touched position
				googleMap.clear();

				// Creating a marker
				MarkerOptions markerOptions = new MarkerOptions();

				// Setting the position for the marker
				markerOptions.position(latLng);

				// Setting the title for the marker.
				// This will be displayed on taping the marker
				markerOptions.title(latLng.latitude + " : " + latLng.longitude);

				// Animating to the touched position
				googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

				// Placing a marker on the touched position
				googleMap.addMarker(markerOptions);
				Intent intent = new Intent(DragOnlyMap.this, AddPinMap.class);
				intent.putExtra("lat_drag", latLng.latitude);
				intent.putExtra("long_drag", latLng.longitude);

				alertDialog.showAlertDialog("Add new PIN",
						"Do you wish to add pin here ?", intent);

			}
		});

		if (status != ConnectionResult.SUCCESS) { // Google Play Services are
													// not available

			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
					requestCode);
			dialog.show();

		} else { // Google Play Services are available

			// Getting LocationManager object from System Service
			// LOCATION_SERVICE
			LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

			// Creating a criteria object to retrieve provider
			Criteria criteria = new Criteria();

			// Getting the name of the best provider
			String provider = locationManager.getBestProvider(criteria, true);

			// Getting Current Location
			Location location = locationManager.getLastKnownLocation(provider);

			if (location != null) {
				onLocationChanged(location);
			}

			locationManager.requestLocationUpdates(provider, 20000, 0, this);
		}

	}

	@Override
	public void onLocationChanged(Location location) {

		// Getting latitude of the current location
		double latitude = location.getLatitude();

		// Getting longitude of the current location
		double longitude = location.getLongitude();

		// Creating a LatLng object for the current location
		LatLng latLng = new LatLng(latitude, longitude);

		// Showing the current location in Google Map
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

		// Zoom in the Google Map
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}

}
