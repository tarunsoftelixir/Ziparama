package com.ziparama.android;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.sax.StartElementListener;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ziparama.android.MyAdapterDelete.ViewHolder;

public class MyAdapterEdit extends BaseAdapter {
	String[] data1_text;
	Activity act;

	Dialog dialogg = null;
	int pos = 0;
	String myid;
	ArrayList<Map<String, String>> arraylist = new ArrayList<Map<String, String>>();

	class ViewHolder {
		public TextView title, description;
		Button editbtn;

	}

	public MyAdapterEdit(Activity con, ArrayList<Map<String, String>> arraylist1) {
		// TODO Auto-generated constructor stub
		super();
		act = con;
		arraylist = arraylist1;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arraylist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View row, ViewGroup parent) {
		// TODO Auto-generated method stub
   
		LayoutInflater mInflater = act.getLayoutInflater();
		ViewHolder holder;
		Log.d("EditPin In Viewallpins in adapter", "how it works");
		if (row == null)
			row = mInflater.inflate(R.layout.row_edit_pin, null);
		 
		holder = new ViewHolder();

		holder.title = (TextView) row.findViewById(R.id.title_edit);
        myid=arraylist.get(position).get("mypinid");
		// holder.description = (TextView) row.findViewById(R.id.desp_delete);
		holder.editbtn = (Button) row.findViewById(R.id.editbtn);

		holder.editbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(act,EditPinDetail.class);
				intent.putExtra("pos", position);
				intent.putExtra("PINID", myid);
				intent.putExtra("GET", "MY_PINS");
				act.startActivity(intent);
			
              
			}

		});

		row.setBackgroundColor((position % 2) == 0 ? Color.WHITE : act
				.getResources().getColor(R.color.cellback));

		row.setTag(holder);
       Log.d("hdjhs", arraylist.get(position).get("edittitle"));
		holder.title.setText(arraylist.get(position).get("edittitle"));
	
		// holder.description.setText(arraylist.get(position).get("editdesp"));

		return row;
	}

}
