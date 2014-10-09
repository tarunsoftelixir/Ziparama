//package com.ziparama.android;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Process;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.webkit.CookieManager;
//import android.webkit.CookieSyncManager;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.connection.ziparama.AlertDialogManagerService;
//import com.connection.ziparama.ConnectionDetector;
//import com.connection.ziparama.CustomHttpClient;
//import com.work.prefs.AppPreferences;
//import com.ziparama.pinanimationonmap.utils.Constant;
//
//public class GoogleLogin extends Activity {
//
//	static WebView myWebView;
//	Button backbtn;
//	public static String varifier = null;
//	CookieManager cookieManager;
//	public static boolean status = false;
//	ConnectionDetector conectiondetector;
//	Dialog dataProcess;
//	public static HashMap<String, String> requestData;
//	AlertDialogManagerService alert = null;
//	AppPreferences mePrefrences;
//	
//	
//	 
//
//	static String client_id = "409808018247-3u81g7h0gdkvhfeoi9moiglpk525l697.apps.googleusercontent.com";
//	static String callback = "http://localhost";
//	static String scope = "https://www.googleapis.com/auth/userinfo.email"
//			+ "+"
//			+ "https://mail.google.com+https://www.googleapis.com/auth/userinfo.profile";
//	static String visibleactions = "http://schemas.google.com/AddActivity/";
//	final String url = "https://accounts.google.com/o/oauth2/auth?response_type=code&client_id="
//			+ client_id
//			+ "&redirect_uri="
//			+ callback
//			+ "&scope="
//			+ scope
//			+ "&data-requestvisibleactions=" + visibleactions;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.googlemain);
//		requestData = new HashMap<String, String>();
//
//		myWebView = (WebView) findViewById(R.id.webViewgoogle);
//		myWebView.getSettings().setJavaScriptEnabled(true);
//		conectiondetector = new ConnectionDetector(GoogleLogin.this);
//		CookieSyncManager.createInstance(this);
//		cookieManager = CookieManager.getInstance();
//
//		cookieManager.removeAllCookie();
//		cookieManager.setAcceptCookie(true);
//		dataProcess = new Dialog(GoogleLogin.this,
//				R.drawable.progress);
//		dataProcess.setCancelable(false);
//		backbtn=(Button)findViewById(R.id.backbtn);
//		
//		mePrefrences=AppPreferences.getInstance(this);
//		myWebView.setWebViewClient(new MyWebViewClient());
//		new MyWebViewClient().shouldOverrideUrlLoading(myWebView, url);
//		//
//
//		myWebView.loadUrl(url);
//		backbtn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				startActivity(new Intent(GoogleLogin.this,MainActivity.class));
//			}
//		});
//
//	}
//
//	class MyWebViewClient extends WebViewClient {
//
//		boolean loadingFinished = false;
//		boolean redirect = false;
//
//		@Override
//		public boolean shouldOverrideUrlLoading(WebView view, String url) {
//			if (!loadingFinished) {
//				redirect = true;
//			}
//
//			loadingFinished = false;
//
//			if (conectiondetector.isConnectingToInternet()) {
//				try {
//					CustomHttpClient.executeHttpGet(url, GoogleLogin.this);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					dataProcess.dismiss();
//					e.printStackTrace();
//				}
//				if (CustomHttpClient.getResponseCode() != 200) {
//					alert = new AlertDialogManagerService();
//					AlertDialogManagerService.alertflag = true;
//					alert.showAlertDialog(GoogleLogin.this,"","");
//				} else {
//					if (url.contains("ServiceLoginAuth")) {
//					}
//					if (Uri.parse(url).getHost().equals("localhost")) {
//						varifier = null;
//
//						String value[] = url.split(java.util.regex.Pattern
//								.quote("?"));
//
//						String value1[] = value[1].split("&");
//						for (int i = 0; i <= value1.length - 1; i++) {
//							String keyValue[] = value1[i].split("=");
//							String key = keyValue[0];
//							if (key.equals("code")) {
//
//								varifier = keyValue[1];
//								break;
//							}
//						}
//						if (varifier != null) {
//
//							if (!conectiondetector.isConnectingToInternet()) {
//								AlertDialogManagerService.alertflag = false;
//								alert.showAlertDialog(GoogleLogin.this,"","");
////								startActivity(new Intent(GoogleLogin.this,
////										MyPinAdd.class));
//							} else {
//								//new googleServices().execute();
////								startActivity(new Intent(GoogleLogin.this,
////										MyPinAdd.class));
//							}
//
//							myWebView.setVisibility(View.GONE);
//
//						} else {
//							Toast.makeText(
//									getApplicationContext(),
//									"Due to some technical problem couldn't complete the login. So, Please try again later.",
//									1000).show();
//						}
//						return false;
//					}
//					view.loadUrl(url);
//					return true;
//				}
//			} else {
//
//				alert = new AlertDialogManagerService();
//				AlertDialogManagerService.alertflag = true;
//				alert.showAlertDialog(GoogleLogin.this,"","");
//			}
//
//			return true;
//
//		}
//
//		@Override
//		public void onReceivedError(WebView view, int errorCode,
//				String description, String failingUrl) {
//			// TODO Auto-generated method stub
//			super.onReceivedError(view, errorCode, description, failingUrl);
//
//		}
//
//		@Override
//		public void onPageFinished(WebView view, String url) {
//			// TODO Auto-generated method stub
//
//			super.onPageFinished(view, url);
//
//			if (!redirect) {
//				
//				loadingFinished = true;
//			}
//			if (loadingFinished && !redirect)
//			{
//				dataProcess.dismiss();
//			}
//			else
//				redirect = false;
//		}
//
//		@Override
//		public void onPageStarted(WebView view, String url, Bitmap favicon) {
//			// TODO Auto-generated method stub
//			super.onPageStarted(view, url, favicon);
//			loadingFinished = false;
//
//			dataProcess.show();
//		}
//
//	}
//
//	//
//	//
//	//
//
//	public class googleServices extends AsyncTask<String, String, String> {
//
//		@Override
//		protected void onPreExecute() {
//			// TODO Auto-generated method stub
//			dataProcess.show();
//
//			super.onPreExecute();
//		}
//
//		@Override
//		protected String doInBackground(String... arg0) {
//			// TODO Auto-generated method stub
//
//			String response = null;
//			ArrayList<NameValuePair> array = new ArrayList<NameValuePair>();
//			array.add(new BasicNameValuePair("code", varifier));
//
//			array.add(new BasicNameValuePair("client_id", client_id));
//			array.add(new BasicNameValuePair("client_secret",
//					"FXOdYLWrxg9M-OoDRX0sCmeP"));
//			array.add(new BasicNameValuePair("redirect_uri", callback));
//			array.add(new BasicNameValuePair("grant_type", "authorization_code"));
//
//			try {
//
//				if (conectiondetector.isConnectingToInternet()) {
//					response = CustomHttpClient.executeHttpPost(
//							"https://accounts.google.com/o/oauth2/token",
//							array, GoogleLogin.this);
//
//				
//						alert = new AlertDialogManagerService();
//						AlertDialogManagerService.alertflag = true;
//						alert.showAlertDialog(GoogleLogin.this,"","");
//					
//				} else {
//					AlertDialogManagerService.alertflag = false;
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				myWebView.setVisibility(View.GONE);
//				dataProcess.dismiss();
//			}
//			return response;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//
//			if (result != null) {
//				new JsonParser().execute(result);
//				// Toast.makeText(getApplicationContext(), result, 1000).show();
//
//			} else {
//
//				if (conectiondetector.isConnectingToInternet()) {
//
//				
//						alert = new AlertDialogManagerService();
//						AlertDialogManagerService.alertflag = true;
//						alert.showAlertDialog(GoogleLogin.this,"","");
//						dataProcess.dismiss();
//					
//				} else {
//					AlertDialogManagerService.alertflag = false;
//					alert = new AlertDialogManagerService();
//					alert.showAlertDialog(GoogleLogin.this,"","");
//					dataProcess.dismiss();
//				}
//
//			}
//
//		}
//	}
//
//	//
//	//
//	//
//
//	public class googleServices1 extends AsyncTask<String, String, String> {
//
//		@Override
//		protected void onPreExecute() {
//
//			super.onPreExecute();
//			dataProcess.show();
//		}
//
//		@Override
//		protected String doInBackground(String... params) {
//
//			String response = null;
//
//			try {
//				if (conectiondetector.isConnectingToInternet()) {
//				response = CustomHttpClient.executeHttpGet(
//						"https://www.googleapis.com/oauth2/v1/userinfo?access_token="
//								+ params[0], GoogleLogin.this);
//
//				
//				
//						alert = new AlertDialogManagerService();
//						AlertDialogManagerService.alertflag = true;
//						alert.showAlertDialog(GoogleLogin.this,"","");
//
//				} else {
//					AlertDialogManagerService.alertflag = false;
//
//				}
//			} catch (Exception e) {
//
//				e.printStackTrace();
//				myWebView.setVisibility(View.GONE);
//				dataProcess.dismiss();
//			}
//			return response;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//
//			if (result != null) {
//				
//				new JsonParser1().execute(result);
//				
//
//			} else {
//
//				if (conectiondetector.isConnectingToInternet()) {
//
//					
//						alert = new AlertDialogManagerService();
//						AlertDialogManagerService.alertflag = true;
//						alert.showAlertDialog(GoogleLogin.this,"","");
//						dataProcess.dismiss();
//					
//				} else {
//					AlertDialogManagerService.alertflag = false;
//					alert = new AlertDialogManagerService();
//					alert.showAlertDialog(GoogleLogin.this,"","");
//					dataProcess.dismiss();
//				}
//
//			}
//
//		}
//	}
//
//	//
//	//
//	//
//
//	public class Feedsubmit extends AsyncTask<String, String, String> {
//
//		ArrayList<NameValuePair> array;
//
//		@Override
//		protected void onPreExecute() {
//
//			super.onPreExecute();
//			dataProcess.show();
//
//		}
//
//		@Override
//		protected String doInBackground(String... arg0) {
//			// TODO Auto-generated method stub
//			String response = null;
//			array = new ArrayList<NameValuePair>();
//			array.add(new BasicNameValuePair("provider", "google"));
//			array.add(new BasicNameValuePair("token", requestData
//					.get("access_token")));
//			array.add(new BasicNameValuePair("refresh_token", requestData
//					.get("refresh_token")));
//			array.add(new BasicNameValuePair("expires_in", requestData
//					.get("expires_in") + ""));
//			array.add(new BasicNameValuePair("name", requestData.get("name")));
//			array.add(new BasicNameValuePair("email", requestData.get("email")));
//			array.add(new BasicNameValuePair("uid", requestData.get("uid")));
//			array.add(new BasicNameValuePair("image", requestData.get("image")));
//
//			try {
//				response = CustomHttpClient.executeHttpPost(Constant.base_url
//						+ "user_register.json", array, GoogleLogin.this);
//				
//
//				if (conectiondetector.isConnectingToInternet()) {
//
//					
//						alert = new AlertDialogManagerService();
//						AlertDialogManagerService.alertflag = true;
//						alert.showAlertDialog(GoogleLogin.this,"","");
//					
//				} else {
//					AlertDialogManagerService.alertflag = false;
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				myWebView.setVisibility(View.GONE);
//				dataProcess.dismiss();
//			}
//
//			return response;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//
//			if (result != null) {
//				new JsonParser2().execute(result);
//
//			} else {
//				if (conectiondetector.isConnectingToInternet()) {
//
//					if (CustomHttpClient.getResponseCode() != 200) {
//						alert = new AlertDialogManagerService();
//						AlertDialogManagerService.alertflag = true;
//						alert.showAlertDialog(GoogleLogin.this,"","");
//						dataProcess.dismiss();
//					}
//				}
//
//				else {
//					AlertDialogManagerService.alertflag = false;
//					alert = new AlertDialogManagerService();
//					alert.showAlertDialog(GoogleLogin.this,"","");
//				}
//
//			}
//
//		}
//
//	}
//
//	//
//	//
//	//
//
//	class JsonParser extends AsyncTask<String, Void, HashMap<String, String>> {
//		@Override
//		protected void onPreExecute() {
//			// TODO Auto-generated method stub
//			super.onPreExecute();
//			dataProcess.show();
//
//		}
//
//		@Override
//		protected HashMap<String, String> doInBackground(String... params) {
//			// TODO Auto-generated method stub
//
//			HashMap<String, String> data = new HashMap<String, String>();
//
//			try {
//
//				JSONObject dataObject = new JSONObject(params[0]);
//				data.put("access_token", dataObject.getString("access_token"));
//				data.put("token_type", dataObject.getString("token_type"));
//				data.put("expires_in", "" + dataObject.getInt("expires_in"));
//				data.put("id_token", dataObject.getString("id_token"));
//				data.put("refresh_token", dataObject.getString("refresh_token"));
//
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				dataProcess.dismiss();
//			}
//
//			return data;
//		}
//
//		@Override
//		protected void onPostExecute(HashMap<String, String> result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//			requestData.putAll(result);
//
//			new googleServices1().execute(result.get("access_token"));
//		}
//
//	}
//
//	//
//	//
//	//
//
//	class JsonParser1 extends AsyncTask<String, Void, HashMap<String, String>> {
//		@Override
//		protected void onPreExecute() {
//			// TODO Auto-generated method stub
//			super.onPreExecute();
//			dataProcess.show();
//
//		}
//
//		@Override
//		protected HashMap<String, String> doInBackground(String... params) {
//			// TODO Auto-generated method stub
//
//			HashMap<String, String> data = new HashMap<String, String>();
//
//			try {
//
//				JSONObject dataObject = new JSONObject(params[0]);
//				data.put("name", dataObject.getString("name"));
//				Log.d("dataobjectusername", dataObject.getString("name"));
//				data.put("email", dataObject.getString("email"));
//				data.put("uid", dataObject.getString("id"));
//				data.put("image", dataObject.getString("picture"));
//				data.put("gender", dataObject.getString("gender"));
//				Log.d("Name", dataObject.getString("name"));
//				Log.d("Gender", dataObject.getString("gender"));
//				Log.d("Email", dataObject.getString("email"));
//				Log.d("Picture",dataObject.getString("image"));
//				
//				
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				dataProcess.dismiss();
//				
//
//			}
//
//			return data;
//		}
//
//		@Override
//		protected void onPostExecute(HashMap<String, String> result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//			if (result != null) {
//				requestData.putAll(result);
//				
//				new Feedsubmit().execute("");
//			} else {
//				if (conectiondetector.isConnectingToInternet()) {
//
//			
//						alert = new AlertDialogManagerService();
//						AlertDialogManagerService.alertflag = true;
//						alert.showAlertDialog(GoogleLogin.this,"","");
//						dataProcess.dismiss();
//					
//				}
//
//				else {
//					alert = new AlertDialogManagerService();
//					AlertDialogManagerService.alertflag = false;
//					alert.showAlertDialog(GoogleLogin.this,"","");
//					dataProcess.dismiss();
//				}
//
//			}
//		}
//
//	}
//
//	class JsonParser2 extends AsyncTask<String, Void, HashMap<String, String>> {
//		@Override
//		protected void onPreExecute() {
//			// TODO Auto-generated method stub
//			super.onPreExecute();
//			dataProcess.show();
//
//		}
//
//		@Override
//		protected HashMap<String, String> doInBackground(String... params) {
//			// TODO Auto-generated method stub
//
//			HashMap<String, String> data = new HashMap<String, String>();
//
//			try {
//
//				JSONObject dataObject = new JSONObject(params[0]);
//				data.put("status", dataObject.getInt("status") + "");
//				data.put("user_id", dataObject.getInt("id") + "");
//				data.put("firstid", dataObject.getInt("first") + "");
//
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				dataProcess.dismiss();
//			}
//
//			return data;
//		}
//
//		@Override
//		protected void onPostExecute(HashMap<String, String> result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//		}
//	}
//		
//
//	/********************************************************************************************************/
//
//	@Override
//	public void onBackPressed() {
//		// TODO Auto-generated method stub
//		finish();
//		super.onBackPressed();
//
//	}
//	/*public void setProfileValue(Context context) {
//	mePrefrences.setName(requestData.get("name"));
//	mePrefrences.setEmail(requestData.get("email"));
//	mePrefrences.setGender(requestData.get("gender"));
//	mePrefrences.setPicture(requestData.get("image"));
//	Log.d("Name", mePrefrences.isName());
//	Log.d("Gender", mePrefrences.isGender());
//	Log.d("Email", mePrefrences.isEmail());
//	Log.d("Picture", mePrefrences.isPicture());
//	}*/
//}
