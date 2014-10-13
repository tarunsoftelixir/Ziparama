package com.ziparama.android;

import java.util.ArrayList;
import java.util.Map;

import webservice.DeleteMyPin;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.connection.ziparama.AlertDialogManagerService;

public class MyAdapterDelete extends BaseAdapter {
	String[] data1_text;
	Activity act;

	Dialog dialogg = null;

	ArrayList<Map<String, String>> arraylist = new ArrayList<Map<String, String>>();

	class ViewHolder {
		public TextView title;
		Button deletebtn;

	}

	public MyAdapterDelete(Activity con,
			ArrayList<Map<String, String>> arraylist1) {
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

		if (row == null)
			row = mInflater.inflate(R.layout.row_delete_pin, null);

		holder = new ViewHolder();

		holder.title = (TextView) row.findViewById(R.id.title_delete);
		holder.deletebtn = (Button) row.findViewById(R.id.deletebtn);
		holder.deletebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						act);

				// set title
				alertDialogBuilder.setTitle("Delete");

				// set dialog message
				alertDialogBuilder
						.setMessage("Are You sure delete this pin ???")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, close
										// current activity
										Toast.makeText(act, "" + position,
												Toast.LENGTH_SHORT).show();

										new DeleteMyPin(act, arraylist.get(
												position).get("mid"))
												.execute("");
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										dialog.cancel();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			}
		});

		row.setBackgroundColor((position % 2) == 0 ? Color.WHITE : act
				.getResources().getColor(R.color.cellback));

		row.setTag(holder);

		holder.title.setText(arraylist.get(position).get("deletetitle"));
		// holder.description.setText(arraylist.get(position).get("deletedesp"));

		return row;
	}

}
