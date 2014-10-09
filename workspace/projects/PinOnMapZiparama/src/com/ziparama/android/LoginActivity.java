package com.ziparama.android;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;
import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.connection.ziparama.TwitterLogin;
import com.facebook.AccessToken;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.work.prefs.AppPreferences;

@SuppressWarnings("deprecation")
public class LoginActivity extends Activity {
	protected static final String TAG = "Login";
	Button loginbtn;
	ImageView closebtn, googlebtn, imageview, fbbtn, twitterbtn;
	private static String APP_ID = "356716587814498"; // Replace your App ID
	ImageView menu_btn, searbtn;
	PopupMenu menu; // here
	private Facebook facebook;
	public static String U_id;
	// public ImageLoader imageLoader;
	AppPreferences mPrefrence;
	Intent intent;
	EditText searchtxt;
	String searchtext;

	static String TWITTER_CONSUMER_KEY = "TnP0d5RV6LvJ5znQGV1aXOCpa";
	static String TWITTER_CONSUMER_SECRET = "hI9W7yTcU2YloeZwLsGjgxO0HfdOEDbiJi1HclbkcgH8NCfBPT";

	// Preference Constants
	static String PREFERENCE_NAME = "twitter_oauth";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

	static final String TWITTER_CALLBACK_URL = "oauth://ziparama";

	// Twitter oauth urls
	static final String URL_TWITTER_AUTH = "auth_url";
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

	public static Twitter twitter;
	public static RequestToken requestToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		searbtn = (ImageView) findViewById(R.id.searchbtn);
		googlebtn = (ImageView) findViewById(R.id.button_google);
		searchtxt = (EditText) findViewById(R.id.searchbox);
		// closebtn = (ImageView) findViewById(R.id.image);
		fbbtn = (ImageView) findViewById(R.id.facebook_btn);
		twitterbtn = (ImageView) findViewById(R.id.button_twitter);
		// imageLoader = new ImageLoader(LogInScreen.this);
		mPrefrence = AppPreferences.getInstance(this);
		facebook = new Facebook(APP_ID);
		/* menu property */
		intent = getIntent();
		adapter = new SocialAuthAdapter(new ResponseListener());
		menu_btn = (ImageView) findViewById(R.id.menu_img);
		searbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				searchtext = searchtxt.getText().toString();
				Intent intent = new Intent(LoginActivity.this, Search.class);
				intent.putExtra("str", searchtext);
				intent.putExtra("GET", "ALL_PINS");
				startActivity(intent);

			}
		});
		menu_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				menu = new PopupMenu(LoginActivity.this, menu_btn);
				menu.getMenuInflater().inflate(R.menu.chooser, menu.getMenu());
				if (mPrefrence.isRegistered()) {
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
							startActivity(new Intent(LoginActivity.this,
									DragOnlyMap.class));
							break;
						case R.id.viewmypin:
							startActivity(new Intent(LoginActivity.this,
									ViewAllPins.class));

							break;
						case R.id.viewallpins:
							startActivity(new Intent(LoginActivity.this,
									ViewAllPins.class));
							break;

						case R.id.howitwork:
							startActivity(new Intent(LoginActivity.this,
									HowItWorks.class));
							break;
						case R.id.editpin:
							if (mPrefrence.isRegistered()) {
								Intent intent1 = new Intent(LoginActivity.this,
										EditPin.class);
							intent1.putExtra("GET", "MY_PINS");
							startActivity(intent1);
							finish();
							}
							break;
						
						case R.id.deletepin:
							if (mPrefrence.isRegistered()) {
								Intent intent2 = new Intent(LoginActivity.this,
										DeletePin.class);
							intent2.putExtra("GET", "MY_PINS");
							startActivity(intent2);
							finish();
								//new DeleteMyPin(ViewAllPins.this,"Doremon");
							}
							break;
						case R.id.logout:
							// TODO Auto-generated method stub
							if (mPrefrence.isRegistered()) {
								mPrefrence.setRegistered(false);
								mPrefrence.setEmail("");
								mPrefrence.setGender("");
								mPrefrence.setFirstName("");
								mPrefrence.setLastName("");
								mPrefrence.setDOB("");
								Toast.makeText(LoginActivity.this,
										"Successfully logout",
										Toast.LENGTH_LONG).show();
								startActivity(new Intent(LoginActivity.this,
										MainActivity.class));

							} else {
								Toast.makeText(LoginActivity.this,
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
		fbbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// if(mPrefrence.isRegistered())
				loginToFacebook();

			}
		});
		
		twitterbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loginToTwitter();
			}
		});

		googlebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// startActivity(new Intent(LoginActivity.this,
				// GoogleActivity.class));
			}
		});

		initialiseFacebookButton();

	}

	private void initialiseFacebookButton() {

		facebook = new Facebook(APP_ID);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	public void loginToFacebook() {

		if (!facebook.isSessionValid()) {
			facebook.authorize(this, new String[] { "public_profile", "email",
					"user_birthday" }, Facebook.FORCE_DIALOG_AUTH,
					new Facebook.DialogListener() {

						@Override
						public void onCancel() {

						}

						@Override
						public void onComplete(Bundle values) {
							// mPrefrence.setRegistered(true);
							Log.d("goto", "goto");
							getProfileInformation();
							mPrefrence.setRegistered(true);
							// if
							// (intent.getStringExtra("CALLER").equals("main"))
							// startActivity(new Intent(LoginActivity.this,
							// MainActivity.class));
							// else if (intent.getStringExtra("CALLER").equals(
							// "addPinMap"))
							// startActivity(new Intent(LoginActivity.this,
							// AddPinMap.class));
							finish();

						}

						public void onError(DialogError error) {

						}

						@Override
						public void onFacebookError(FacebookError fberror) {

						}

					});
		}

	}

	public void getProfileInformation() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String access_token = facebook.getAccessToken();
				URL url;
				try {
					url = new URL("https://graph.facebook.com/me?access_token="
							+ access_token);

					HttpsURLConnection conn = (HttpsURLConnection) url
							.openConnection();
					if (conn.getResponseCode() == 200) {
						BufferedReader bufr = new BufferedReader(
								new InputStreamReader(conn.getInputStream()));
						String line = null;
						StringBuilder builder = new StringBuilder();
						while ((line = bufr.readLine()) != null) {
							builder.append(line);
						}

						JSONObject profile = new JSONObject(builder.toString());
						Log.e("USERDATA", profile.toString());
						U_id = profile.getString("id");
						Log.d("Uid", U_id);
						// SimpleDateFormat format = new
						// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						// String date =
						// format.format(Date.parse(profile.getString("birthday")));
						// Log.d("date", date);
						if (mPrefrence != null) {
							mPrefrence.setUid(profile.getString("id"));
							mPrefrence.setFirstName(profile
									.getString("first_name"));
							mPrefrence.setLastName(profile
									.getString("last_name"));
							mPrefrence.setEmail(profile.getString("email"));
							mPrefrence.setGender(profile.getString("gender"));
							// mPrefrence.setDOB(date);

						}

					}

					conn.disconnect();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void loginToTwitter() {
		// Check if already logged in
		// if (!mPrefrence.isRegistered()) {
		// ConfigurationBuilder builder = new ConfigurationBuilder();
		// builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
		// builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
		// Configuration configuration = builder.build();
		//
		// TwitterFactory factory = new TwitterFactory(configuration);
		// twitter = factory.getInstance();
		//
		// try {
		// requestToken = twitter
		// .getOAuthRequestToken(TWITTER_CALLBACK_URL);
		// // this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
		// // .parse(requestToken.getAuthenticationURL())));
		// new TwitterLogin(LoginActivity.this,
		// requestToken.getAuthenticationURL()).show();
		// } catch (TwitterException e) {
		// e.printStackTrace();
		// }
		// } else {
		// // user already logged into twitter
		// Toast.makeText(getApplicationContext(),
		// "Already Logged into twitter", Toast.LENGTH_LONG).show();
		// }

		
		adapter.authorize(LoginActivity.this, Provider.TWITTER);
	}

	SocialAuthAdapter adapter;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	private final class ResponseListener implements DialogListener {
		// public void onComplete(Bundle values) {
		//
		// adapter.getUserProfileAsync(new ProfileDataListener());
		// }

		@Override
		public void onBack() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onError(SocialAuthError arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onComplete(Bundle arg0) {
			// TODO Auto-generated method stub
			adapter.getUserProfileAsync(new ProfileDataListener());
		}
	}
	
	private final class ProfileDataListener implements SocialAuthListener<Profile> {

		@Override
		public void onError(SocialAuthError arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onExecute(String arg0, Profile t) {
			// TODO Auto-generated method stub
			Log.d("Custom-UI", "Receiving Data");
			   Profile profileMap = t;
			   Log.d("Custom-UI",  "Validate ID         = " + profileMap.getValidatedId());
			   Log.d("Custom-UI",  "First Name          = " + profileMap.getFullName());
			   Log.d("Custom-UI",  "Language                 = " + profileMap.getLanguage());
			   Log.d("Custom-UI",  "Location                 = " + profileMap.getLocation());
			   Log.d("Custom-UI",  "Profile Image URL  = " + profileMap.getProfileImageURL());
		}
	}

}
