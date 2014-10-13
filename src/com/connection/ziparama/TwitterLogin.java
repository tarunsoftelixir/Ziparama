package com.connection.ziparama;

import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.work.prefs.AppPreferences;
import com.ziparama.android.LoginActivity;
import com.ziparama.android.R;

public class TwitterLogin extends Dialog implements View.OnClickListener {

	ImageView cancel, image;
	WebView twitterWeb;
	View view;
	String url;
	Activity context;
	String oauth_url, oauth_verifier, profile_url;
	ProgressDialog progress;

	public TwitterLogin(Activity context, String url) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		view = getLayoutInflater().inflate(R.layout.twitter_web, null);
		this.context = context;
		this.url = url;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btn_cancel) {
			dismiss();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setGravity(Gravity.CENTER);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		cancel = (ImageView) view.findViewById(R.id.btn_cancel);
		image = (ImageView) view.findViewById(R.id.uploaded_image);
		twitterWeb = (WebView) view.findViewById(R.id.twitter_login);
		setupTwitterWeb();
		cancel.setOnClickListener(this);

		setContentView(view);
		// twitterWeb.loadUrl(url);
	}

	private void setupTwitterWeb() {
		// TODO Auto-generated method stub
		twitterWeb.setVerticalScrollBarEnabled(false);
		twitterWeb.setHorizontalScrollBarEnabled(false);

		twitterWeb.getSettings().setJavaScriptEnabled(true);
		twitterWeb.getSettings().setSavePassword(false);
		twitterWeb.getSettings().setSaveFormData(false);
		twitterWeb.loadUrl(url);
		twitterWeb.setWebViewClient(new MyWebViewClient());

	}

	class MyWebViewClient extends WebViewClient {

		boolean authComplete = false;

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			// view.loadUrl(url);
			Log.d("twurlStart", url);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			Log.d("twurlShould", url);
			boolean authComplete = false;
			if (url.contains("oauth_verifier") && authComplete == false) {
				authComplete = true;
				Log.e("Url", url);
				Uri uri = Uri.parse(url);
				oauth_verifier = uri.getQueryParameter("oauth_verifier");
				dismiss();
				new AccessTokenGet().execute();
			} else if (url.contains("denied")) {
				dismiss();
				Toast.makeText(context, "Sorry !, Permission Denied",
						Toast.LENGTH_SHORT).show();
			}
			
			view.loadUrl(url);

			return authComplete;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			// if (url.contains("oauth_verifier") && authComplete == false) {
			// authComplete = true;
			// Log.e("Url", url);
			// Uri uri = Uri.parse(url);
			// oauth_verifier = uri.getQueryParameter("oauth_verifier");
			// dismiss();
			// new AccessTokenGet().execute();
			// } else if (url.contains("denied")) {
			// dismiss();
			// Toast.makeText(context, "Sorry !, Permission Denied",
			// Toast.LENGTH_SHORT).show();
			// }
			Log.d("twurlfinish", url);

		}

	}

	private class AccessTokenGet extends AsyncTask<String, String, Boolean> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(context);
			progress.setMessage("Fetching Data ...");
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.setIndeterminate(true);
			progress.show();
		}

		@Override
		protected Boolean doInBackground(String... args) {
			try {
				AccessToken accessToken = LoginActivity.twitter
						.getOAuthAccessToken(LoginActivity.requestToken,
								oauth_verifier);
				AppPreferences myPreferences = AppPreferences
						.getInstance(context);
				myPreferences.setRegistered(true);

				// edit.putString("ACCESS_TOKEN", accessToken.getToken());
				// edit.putString("ACCESS_TOKEN_SECRET",
				// accessToken.getTokenSecret());
				User user = LoginActivity.twitter.showUser(accessToken
						.getUserId());
				// profile_url = user.getOriginalProfileImageURL();
				myPreferences.setFirstName(user.getName());
				myPreferences.setFirstName("");
				myPreferences.setUid(accessToken.getUserId() + "");
				myPreferences.setEmail("");
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean response) {
			if (response) {
				progress.hide();
				// Fragment profile = new ProfileFragment();
				// FragmentTransaction ft =
				// getActivity().getFragmentManager().beginTransaction();
				// ft.replace(R.id.content_frame, profile);
				// ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				// ft.addToBackStack(null);
				// ft.commit();
				context.finish();
			}
		}
	}
}
