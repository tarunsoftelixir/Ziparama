package com.ziparama.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.connection.ziparama.AlertDialogManagerService;
import com.connection.ziparama.ConnectionDetector;
import com.work.prefs.AppPreferences;

public class MainActivity extends Activity {
	ImageView viewMyPins, viewallpins, howitworks, pinmap;
	Button logout_btn;
	AppPreferences myPrefs;
	ImageView menu_btn, searbtn;
	PopupMenu menu;
	String searchtext;
	EditText searchtxt;
	ConnectionDetector connectionDetector;
	AlertDialogManagerService alertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		connectionDetector = new ConnectionDetector(this);
		alertDialog = new AlertDialogManagerService(this);

		viewMyPins = (ImageView) findViewById(R.id.viewmypins);
		viewallpins = (ImageView) findViewById(R.id.viewall);
		howitworks = (ImageView) findViewById(R.id.howitwork);
		pinmap = (ImageView) findViewById(R.id.pinmap);
		myPrefs = AppPreferences.getInstance(MainActivity.this);
		searbtn = (ImageView) findViewById(R.id.searchbtn);
		searchtxt = (EditText) findViewById(R.id.searchbox);
		menu_btn = (ImageView) findViewById(R.id.menu_img);
		
		

       
		/* menu property */

		searbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				searchtext = searchtxt.getText().toString();
				 Log.d("Mainactivitytext", searchtext);
				Intent intent = new Intent(MainActivity.this, ViewAllPins.class);
				intent.putExtra("SEARCH_TEXT", searchtext);
				intent.putExtra("GET", "ALL_PINS");
				intent.putExtra("SEARCH", "SEARCH");
				startActivity(intent);
				finish();

			}
		});

		menu_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				menu = new PopupMenu(MainActivity.this, menu_btn);
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
							startActivity(new Intent(MainActivity.this,
									DragOnlyMap.class));
							finish();
							break;
						case R.id.viewmypin:
							if (!myPrefs.isRegistered()){
								startActivity(new Intent(MainActivity.this,
										LoginActivity.class));
							finish();
							}
							else {
								Intent intent = new Intent(MainActivity.this,
										ViewAllPins.class);
								intent.putExtra("GET", "MY_PINS");
								startActivity(intent);
								finish();
							}

							break;
						case R.id.viewallpins:
							Intent intent = new Intent(MainActivity.this,
									ViewAllPins.class);
							intent.putExtra("GET", "ALL_PINS");
							startActivity(intent);
							finish();
							break;

						case R.id.howitwork:
							Log.d("works", "how it works");

							startActivity(new Intent(MainActivity.this,
									HowItWorks.class));
							finish();
							break;
						case R.id.editpin:
							if (myPrefs.isRegistered()) {
								Intent intent1 = new Intent(MainActivity.this,
										EditPin.class);
							intent1.putExtra("GET", "MY_PINS");
							startActivity(intent1);
							finish();
							}
							break;
						
						case R.id.deletepin:
							if (myPrefs.isRegistered()) {
								Intent intent2 = new Intent(MainActivity.this,
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
								Toast.makeText(MainActivity.this,
										"Successfully logout",
										Toast.LENGTH_LONG).show();
							} else {
								startActivity(new Intent(MainActivity.this,
										LoginActivity.class));
								finish();
								Toast.makeText(MainActivity.this,
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

		pinmap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if (connectionDetector.isConnectingToInternet())
					startActivity(new Intent(MainActivity.this,
							DragOnlyMap.class));
				else
					alertDialog.showAlertDialog("Connection Error",
							"You are not connected to Internet");
			}
		});
		howitworks.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				startActivity(new Intent(MainActivity.this, HowItWorks.class));

			}
		});
		viewallpins.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (connectionDetector.isConnectingToInternet()) {
					Intent intent = new Intent(MainActivity.this,
							ViewAllPins.class);
					intent.putExtra("GET", "ALL_PINS");
					startActivity(intent);
				} else
					alertDialog.showAlertDialog("Connection Error",
							"You are not connected to Internet");
			}
		});
		viewMyPins.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (connectionDetector.isConnectingToInternet())
					if (!myPrefs.isRegistered())
						startActivity(new Intent(MainActivity.this,
								LoginActivity.class));
					else {
						Intent intent = new Intent(MainActivity.this,
								ViewAllPins.class);
						intent.putExtra("GET", "MY_PINS");
						startActivity(intent);
					}
				else
					alertDialog.showAlertDialog("Connection Error",
							"You are not connected to Internet");
			}
		});
		/**
		 * this code is used to get hash key for facebook
		 * 
		 */
		/*
		 * try { PackageInfo info = this.getPackageManager().getPackageInfo(
		 * "com.ziparama.android", PackageManager.GET_SIGNATURES); for
		 * (Signature signature : info.signatures) { MessageDigest md =
		 * MessageDigest.getInstance("SHA"); md.update(signature.toByteArray());
		 * Log.d("KeyHashSEStore:", Base64.encodeToString(md.digest(),
		 * Base64.DEFAULT)); } } catch (NameNotFoundException e) {
		 * 
		 * } catch (NoSuchAlgorithmException e) {
		 * 
		 * }
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
