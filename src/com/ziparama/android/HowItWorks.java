package com.ziparama.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.connection.ziparama.ConnectionDetector;
import com.work.prefs.AppPreferences;

public class HowItWorks extends Activity {
	ImageButton btn;
	ImageView menu_btn, searbtn;
	PopupMenu menu;
	AppPreferences prefs;
	String searchtext;
	EditText searchtxt;

	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.how_it_works);

		btn = (ImageButton) findViewById(R.id.backbutton);
		menu_btn = (ImageView) findViewById(R.id.menu_img);
		searchtxt = (EditText) findViewById(R.id.searchbox);
		searbtn = (ImageView) findViewById(R.id.searchbtn);

		prefs = AppPreferences.getInstance(HowItWorks.this);
		

		
		intent = getIntent();
		searbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				searchtext = searchtxt.getText().toString();
				Intent intent = new Intent(HowItWorks.this, ViewAllPins.class);
				intent.putExtra("SEARCH_TEXT", searchtext);
				intent.putExtra("GET", "ALL_PINS");
				intent.putExtra("SEARCH", "SEARCH");
				startActivity(intent);

			}
		});
		menu_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				menu = new PopupMenu(HowItWorks.this, menu_btn);
				menu.getMenuInflater().inflate(R.menu.chooser, menu.getMenu());
				if (prefs.isRegistered()) {
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

					// TODO Auto-generated method stub

					public boolean onMenuItemClick(MenuItem menu) {
						// TODO Auto-generated method stub
						switch (menu.getItemId()) {
						case R.id.pinmap:
							startActivity(new Intent(HowItWorks.this,
									DragOnlyMap.class));
							break;
						case R.id.viewmypin:
							if (!prefs.isRegistered())
								startActivity(new Intent(HowItWorks.this,
										LoginActivity.class));
							else {
								Intent intent = new Intent(HowItWorks.this,
										ViewAllPins.class);
								intent.putExtra("GET", "MY_PINS");
								startActivity(intent);
								finish();
							}
							break;
						case R.id.viewallpins:
							Intent intent = new Intent(HowItWorks.this,
									ViewAllPins.class);
							intent.putExtra("GET", "ALL_PINS");
							startActivity(intent);
							break;

						case R.id.howitwork:
							startActivity(new Intent(HowItWorks.this,
									HowItWorks.class));
							break;
						case R.id.editpin:
							if (prefs.isRegistered()) {
								Intent intent1 = new Intent(HowItWorks.this,
										EditPin.class);
							intent1.putExtra("GET", "MY_PINS");
							startActivity(intent1);
							finish();
							}
							break;
						
						case R.id.deletepin:
							if (prefs.isRegistered()) {
								Intent intent2 = new Intent(HowItWorks.this,
										DeletePin.class);
							intent2.putExtra("GET", "MY_PINS");
							startActivity(intent2);
							finish();
								//new DeleteMyPin(ViewAllPins.this,"Doremon");
							}
							break;
						case R.id.logout:
							// TODO Auto-generated method stub
							if (prefs.isRegistered()) {
								prefs.setRegistered(false);
								prefs.setEmail("");
								prefs.setGender("");
								prefs.setFirstName("");
								prefs.setLastName("");
								prefs.setDOB("");
								Toast.makeText(HowItWorks.this,
										"Logout Successfully !!!!!!",
										Toast.LENGTH_LONG).show();
								startActivity(new Intent(HowItWorks.this,
										MainActivity.class));
							} else {
								Toast.makeText(HowItWorks.this,
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
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(HowItWorks.this, MainActivity.class));
			}
		});
	}
}
