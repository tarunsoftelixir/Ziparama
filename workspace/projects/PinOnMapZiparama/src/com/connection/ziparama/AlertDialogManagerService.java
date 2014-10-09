package com.connection.ziparama;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class AlertDialogManagerService {
	
	Context context;
	Intent intent;
	
	public AlertDialogManagerService(Context context) {
			this.context = context;
	}

	public void showAlertDialog(String settitle,
			String setmessage, Intent intent) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.context);
		this.intent = intent;

		// Setting Dialog Title

		alertDialog.setTitle(settitle);
		alertDialog.setMessage(setmessage);

		// Setting OK Button
		alertDialog.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						AlertDialogManagerService.this.context
								.startActivity(AlertDialogManagerService.this.intent);
					}
				});
		alertDialog.setNegativeButton("Cancle",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		// Showing Alert Message
		alertDialog.show();
	}

	public void showAlertDialog(String setTitle,
			String setMessage) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.context);

		alertDialog.setTitle(setTitle);
		alertDialog.setMessage(setMessage);

		// Setting OK Button
		alertDialog.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
//		alertDialog.setNegativeButton("Cancle",
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.cancel();
//					}
//				});
//		// Showing Alert Message
		alertDialog.show();
	}

}
