package webservice;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.sax.StartElementListener;
import android.util.Log;
import android.widget.Toast;

import com.connection.ziparama.CustomHttpClient;
import com.work.prefs.AppPreferences;
import com.ziparama.android.DeletePin;

public class DeleteMyPin extends AsyncTask<String, String, String> {
	// Context context;
	ProgressDialog progress;
	Context con2;
	String delete_pin;
	AppPreferences myPrefs;
	String status;

	public DeleteMyPin(Context con1, String deletepin) {
		con2 = con1;

		delete_pin = deletepin;
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
		array.add(new BasicNameValuePair("title", delete_pin));
		try {
			response = CustomHttpClient.executeHttpPost(
					"http://174.121.164.66/~indianet/ziparama-api/delete.php",
					array, con2);
			JSONObject jsonarray=new JSONObject(response);
			status=jsonarray.getString("status");
			Log.d("DeletePin", response);

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
			Log.d("status", result);
			if(status.equals("1")){
			
			Toast.makeText(con2, "your pin is delete successfully!!!", Toast.LENGTH_LONG).show();
			con2.startActivity(new Intent(con2, DeletePin.class));
			
		    
			}else if(status.equals("0"))
			{
				Toast.makeText(con2, "your pin is not delete", Toast.LENGTH_LONG).show();
			}
			progress.dismiss();

		} else {
			Log.d("status", result);
		}

	}
}