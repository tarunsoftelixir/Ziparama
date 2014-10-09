package webservice;

import java.util.List;

import com.connection.ziparama.CustomHttpClient;
import com.ziparama.json.LatLongData;
import com.ziparama.json.LatLongJson;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.Toast;

public class LoadingAllPinMapAsync extends AsyncTaskLoader<List<LatLongData>> {

	List<LatLongData> list;
	Context context;
	String url;
	String response;

	public LoadingAllPinMapAsync(Context context, String url) {
		super(context);
		this.context = context;
		this.url = url;
	}

	@Override
	public List<LatLongData> loadInBackground() {
		// TODO Auto-generated method stub
		try {
			Log.d("Pin Webservice", "dointry");
			response = CustomHttpClient.executeHttpGet(url, context);
			
			//Thread.sleep(10000);
			//Toast.makeText(context, "Sleep", Toast.LENGTH_SHORT).show();
			list = new LatLongJson().getLatLong(response);
			Log.d("get all pins Webservice", response);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("get all pins Webservice", "error");
			e.printStackTrace();
		}
		return list;
	}

}
