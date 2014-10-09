package webservice;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.connection.ziparama.AlertDialogManagerService;
import com.connection.ziparama.CustomHttpClient;
import com.work.prefs.AppPreferences;
import com.ziparama.android.DragOnlyMap;

public class PinWebservice extends AsyncTask<String, String, String> {
	Dialog dialog1;
	Context context;
	AlertDialogManagerService alert;
	String user_id_w, title_w, description_w, email_w, img_url_w, lat_addpin_w,
			longaddpin_w,cat_value,subcat_value;
	ArrayList<NameValuePair> array;
	ProgressDialog progress;
	AppPreferences myPrefs;

	public PinWebservice(Context context,String user_id, String title, String description,
			String email, String img_url, String lat_addpin, String long_addpin,String cat,String subcat) {
		this.context = context;
		user_id_w = user_id;
		title_w = title;
		description_w = description;
		email_w = email;
		img_url_w = img_url;
		lat_addpin_w = lat_addpin;
		longaddpin_w = long_addpin;
		cat_value=cat;
		subcat_value=subcat;
		myPrefs = AppPreferences.getInstance(context);
	}

	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progress = new ProgressDialog(context);
		progress.setMessage("Loading...");
		progress.show();
		Log.d("pin Webservice", "response");
	}

	@Override
	protected String doInBackground(String... params) {
		Log.d("Pin Webservice", "doinback");
		String response = null;
		
		array = new ArrayList<NameValuePair>();
		array.add(new BasicNameValuePair("user_id", user_id_w));
		array.add(new BasicNameValuePair("user_firstname", myPrefs
				.isFirstName()));
		array.add(new BasicNameValuePair("user_lastname", myPrefs.isLastName()));
		// array.add(new BasicNameValuePair("user_age", "23"));
		if(myPrefs.isRegistered()){
			if(myPrefs.isEmail() != "" || myPrefs.isEmail() != null)
				email_w = myPrefs.isEmail();
		}
		array.add(new BasicNameValuePair("user_email", email_w));
		array.add(new BasicNameValuePair("user_gender", myPrefs.isGender()));
		// array.add(new BasicNameValuePair("user_dob", myprefs.isDOB()));
		array.add(new BasicNameValuePair("lat", lat_addpin_w));
		array.add(new BasicNameValuePair("long", longaddpin_w));
		array.add(new BasicNameValuePair("title", title_w));
		Log.d("Image", img_url_w);
		array.add(new BasicNameValuePair("uploaded_file", img_url_w));
		array.add(new BasicNameValuePair("description", description_w));
		array.add(new BasicNameValuePair("category", cat_value));
		array.add(new BasicNameValuePair("subcategory", subcat_value));
		try {
			Log.d("Pin Webservice", "dointry");
			response = CustomHttpClient
					.executeHttpPost(
							"http://174.121.164.66/~indianet/ziparama-api/register.php",
							array, context);
			Log.d("Pin Webservice", "ahter");
			Log.d("pin Webservice", response);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("Pin Webservice", "doerror");
			e.printStackTrace();
		}
		return response;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Log.d("Pin Webservice", "result");
		progress.dismiss();
		if (result != null) {
			Log.d("Pin Webservice", result);
			String status = null;
			try {
				JSONObject json = new JSONObject(result);
				status = json.getString("status");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			AlertDialogManagerService dilog = new AlertDialogManagerService(
					context);
			if (status.equals("0"))
				dilog.showAlertDialog("Error",
						"Your information is not Submitted");
			else
				dilog.showAlertDialog("Sucess", "Sucessfully Submitted \n"
						+ "Press OK to add more pins", new Intent(context,
						DragOnlyMap.class));
			// Toast.makeText(con2, "Result", Toast.LENGTH_SHORT).show();
		} else {
			// Toast.makeText(con2, "Response Not Found",
			// Toast.LENGTH_SHORT).show();
		}

	}

}
