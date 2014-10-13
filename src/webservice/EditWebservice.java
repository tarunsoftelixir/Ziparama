package webservice;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.connection.ziparama.CustomHttpClient;
import com.work.prefs.AppPreferences;
import com.ziparama.android.DeletePin;
import com.ziparama.android.EditPin;

public class EditWebservice extends AsyncTask<String, String, String> {
	// Context context;
	ProgressDialog progress;
	Context con2;
	String title_name, desp, cat_value, subcat_value, mid_p;
	AppPreferences myPrefs;
	String status;
	String imgurl;

	public EditWebservice(Context con1, String pid, String user_name,
			String desp_n, String img_url, String cat, String subcat) {
		con2 = con1;
		title_name = user_name;
		desp = desp_n;
		imgurl = img_url;
		cat_value = cat;
		subcat_value = subcat;
		mid_p = pid;

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		myPrefs = AppPreferences.getInstance(con2);
		progress = new ProgressDialog(con2);
		progress.setMessage("Loading...");
		progress.show();

	}

	@Override
	protected String doInBackground(String... params) {
		String response = null;
		ArrayList<NameValuePair> array = new ArrayList<NameValuePair>();
		array.add(new BasicNameValuePair("user_id", myPrefs.isUId()));
		array.add(new BasicNameValuePair("mid", mid_p));
		array.add(new BasicNameValuePair("title", title_name));
		array.add(new BasicNameValuePair("description", desp));
		array.add(new BasicNameValuePair("uploaded_file", imgurl));
		array.add(new BasicNameValuePair("category", cat_value));
		array.add(new BasicNameValuePair("subcategory", subcat_value));

		try {
			response = CustomHttpClient.executeHttpPost(
					"http://174.121.164.66/~indianet/ziparama-api/edit.php",
					array, con2);
			JSONObject jsonarray = new JSONObject(response);
			status = jsonarray.getString("status");
			Log.d("EditPinDetail", response);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		if (result != null) {
			Log.d("Editstatus", result);
			if (status.equals("1")) {

				Toast.makeText(con2, "your pin is edit successfully!!!",
						Toast.LENGTH_LONG).show();
				con2.startActivity(new Intent(con2, EditPin.class));

			} else if (status.equals("0")) {
				Toast.makeText(con2, "your pin is not edit", Toast.LENGTH_LONG)
						.show();
			}
			progress.dismiss();

		} else {
			Log.d("status", result);
		}

	}
}