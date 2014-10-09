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

public class EditPin extends FragmentActivity implements
LoaderManager.LoaderCallbacks<List<LatLongData>> {

MyAdapterEdit adapteredit;
ProgressDialog progress;
Intent intent;
ImageView cancle;
TextView tv;
AppPreferences myPrefs;
List<LatLongData> listedit;
ListView list;
HashMap<String, String> map;

ArrayList<Map<String, String>> editpinarray;

@Override
protected void onCreate(Bundle savedInstanceState) {
// TODO Auto-generated method stub
requestWindowFeature(Window.FEATURE_NO_TITLE);
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_edit);
cancle=(ImageView) findViewById(R.id.btn_cancel_edit);
list = (ListView) findViewById(R.id.list_edit);
editpinarray = new ArrayList<Map<String, String>>();
Toast.makeText(EditPin.this, "EDitpin", Toast.LENGTH_LONG).show();
intent = getIntent();
myPrefs = AppPreferences.getInstance(EditPin.this);
Log.d("BeforeLoding", "AfterLoding");
tv=(TextView) findViewById(R.id.norecord_pur);
this.getSupportLoaderManager().initLoader(0, null, EditPin.this)
.forceLoad();
Log.d("AfterLoding", "AfterLoding");

cancle.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=new Intent(EditPin.this,MainActivity.class);
		startActivity(intent);
	}
});
}

public Loader<List<LatLongData>> onCreateLoader(int id, Bundle bundle) {
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

public void onLoadFinished(Loader<List<LatLongData>> loader,
	List<LatLongData> list) {


if (list != null) {
	for (int i = 0; i < list.size(); i++) {
		map = new HashMap<String, String>();
		map.put("edittitle", list.get(i).getTitle() + "");
		map.put("mypinid", list.get(i).getMid());
		Log.d("list_data", map+"");
		editpinarray.add(map);
	}
	
	Log.d("list_array", editpinarray+"");
}
attachAdapetr();
progress.dismiss();		
}

@Override
public void onLoaderReset(Loader<List<LatLongData>> arg0) {
// TODO Auto-generated method stub

}
public void attachAdapetr( ){
if (editpinarray.size()>0) {
		Toast.makeText(EditPin.this, "Arrayisnotnull", Toast.LENGTH_LONG).show();
		adapteredit = new MyAdapterEdit(EditPin.this, editpinarray);
		list.setAdapter(adapteredit);
	}else
	{
		Toast.makeText(EditPin.this, "nullarraylist", Toast.LENGTH_LONG).show();
		tv.setVisibility(View.VISIBLE);
	}

}

}