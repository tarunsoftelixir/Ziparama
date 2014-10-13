package com.ziparama.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import webservice.LoadingAllPinMapAsync;
import webservice.LoadingMyPinMapAsync;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.work.prefs.AppPreferences;
import com.ziparama.json.LatLongData;

public class DeletePin extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<List<LatLongData>> {

	MyAdapterDelete adapterdelete;
	ProgressDialog progress;
	Intent intent;
	ImageView cancle;
	HashMap<String, String> map;
	AppPreferences myPrefs;
	ListView list;
	List<LatLongData> listedelete;
  TextView tv;
	ArrayList<Map<String, String>> deletepinarray = new ArrayList<Map<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delete);
		myPrefs = AppPreferences.getInstance(DeletePin.this);
        cancle=(ImageView) findViewById(R.id.btn_cancel_delete);
		list = (ListView) findViewById(R.id.list_delete);
		tv=(TextView) findViewById(R.id.norecord_pur);
		intent = getIntent();
		this.getSupportLoaderManager().initLoader(10001, null, this).forceLoad();
		cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(DeletePin.this,MainActivity.class);
				startActivity(intent);
			}
		});

	}

	public void onLoadFinished(Loader<List<LatLongData>> loader,
			List<LatLongData> list) {
		

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				map = new HashMap<String, String>();
				map.put("deletetitle", list.get(i).getTitle() + "");
				map.put("mid", list.get(i).getMid() + "");
			
				deletepinarray.add(map);
			}
			
		}
		attachAdapetr();
		progress.dismiss();
	}

	public Loader<List<LatLongData>> onCreateLoader(int id, Bundle bundle) {
		Log.d("Oncreate", "loading");
		progress = new ProgressDialog(this);
		progress.setMessage("Loading...");
		progress.show();
		if (intent != null && intent.getStringExtra("GET") != null && intent.getStringExtra("GET").equals("ALL_PINS"))
			return new LoadingAllPinMapAsync(this,
					"http://174.121.164.66/~indianet/ziparama-api/get.php");
		ArrayList<NameValuePair> array = new ArrayList<NameValuePair>();
		array.add(new BasicNameValuePair("user_id", myPrefs.isUId()));

		return new LoadingMyPinMapAsync(this,
				"http://174.121.164.66/~indianet/ziparama-api/get.php", array);
	}

	@Override
	public void onLoaderReset(Loader<List<LatLongData>> arg0) {
		// TODO Auto-generated method stub

	}
	public void attachAdapetr( ){
		if (deletepinarray.size()>0) {
				Toast.makeText(DeletePin.this, "Arrayisnotnull", Toast.LENGTH_LONG).show();
				adapterdelete = new MyAdapterDelete(DeletePin.this, deletepinarray);
				list.setAdapter(adapterdelete);
			}else
			{
				Toast.makeText(DeletePin.this, "nullarraylist", Toast.LENGTH_LONG).show();
				
					tv.setVisibility(View.VISIBLE);
				
			}
		
		}

}
