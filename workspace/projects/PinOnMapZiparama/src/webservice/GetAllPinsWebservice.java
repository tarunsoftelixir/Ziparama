package webservice;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.connection.ziparama.CustomHttpClient;
import com.work.prefs.AppPreferences;
import com.ziparama.json.LatLongData;
import com.ziparama.json.LatLongJson;

public class GetAllPinsWebservice extends AsyncTask<String, Integer, List<LatLongData>> {
	Context context;
	AppPreferences myprefs;
	ProgressDialog progress;

	public GetAllPinsWebservice(Context context) {
		this.context = context;

	}

	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progress = new ProgressDialog(context);
		progress.setMessage("Loading...");
		progress.show();
		myprefs = AppPreferences.getInstance(context);
		Log.d("pin Webservice", "response");
	}

	@Override
	protected List<LatLongData> doInBackground(String... params) {
		Log.d("Pin Webservice", "doinback");
		String response = null;
		List<LatLongData> list = null;
		
		try {
			Log.d("Pin Webservice", "dointry");
			response = CustomHttpClient.executeHttpGet(params[0], context);
			Thread.sleep(10000);
		//	Toast.makeText(context, "Sleep", Toast.LENGTH_SHORT).show();
			list = new LatLongJson().getLatLong(response);
			Log.d("get all pins Webservice", response);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("get all pins Webservice", "error");
			e.printStackTrace();
		}
		return list;
	}

	@Override
	protected void onPostExecute(List<LatLongData> result) {
		super.onPostExecute(result);
		progress.dismiss();
		if (result != null) {
			Log.d("get all pins Webservice", result.toString());

		}

	}

}
