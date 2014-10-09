package com.connection.ziparama;

import webservice.ImageLoader;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ziparama.android.R;
import com.ziparama.pinanimationonmap.utils.Constant;

public class MarkerDialog extends Dialog implements android.view.View.OnClickListener{

	ImageView cancel, image;
	TextView titleContent, descriptionContent,cat,subcat,morebtn;
	ProgressBar progress;
	static int flag=0;
	String titleData, descriptionData, imageUrl,category,subcategory;
	Context context;
	
	View view;
	
	public MarkerDialog(Context context, String titleData, String descriptionData, String imageUrl,String category,String subcategory) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		view = getLayoutInflater().inflate(R.layout.marker_dialog, null);
		this.titleData = titleData;
		this.descriptionData = descriptionData;
		this.imageUrl = imageUrl;
		this.context = context;
		this.category=category;
		this.subcategory=subcategory;
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.btn_cancel){
			dismiss();
		}
		 if(v.getId() == R.id.morebtn)
		{
		  if(flag==0)
		  {
			 descriptionContent.setMaxLines(100);
				descriptionContent.setMaxHeight(1000);
			flag=1;	
		  }
		  else
			  if(flag==1)
			  {
				  descriptionContent.setMaxLines(2);
					descriptionContent.setMaxHeight(80);
				flag=0;	
			  }
		  
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setGravity(Gravity.CENTER);
		
		cancel = (ImageView) view.findViewById(R.id.btn_cancel);
		image = (ImageView) view.findViewById(R.id.uploaded_image);
		titleContent = (TextView) view.findViewById(R.id.title_content);
		descriptionContent = (TextView) view.findViewById(R.id.description_content);
		cat=(TextView) view.findViewById(R.id.category_content);
		subcat=(TextView) view.findViewById(R.id.subcategory_content);
		morebtn=(TextView) view.findViewById(R.id.morebtn);
		progress = (ProgressBar) view.findViewById(R.id.image_loading);
		titleContent.setText(titleData);
		descriptionContent.setMaxLines(2);
		descriptionContent.setMaxHeight(80);
		descriptionContent.setText(descriptionData);
		cat.setText(category);
		subcat.setText(subcategory);
		if(Constant.isBitmapAvailable(imageUrl))
			image.setImageBitmap(Constant.getBitmaps(imageUrl));
		else
			new ImageLoader(context, image, progress).execute(imageUrl);
		cancel.setOnClickListener(this);
		morebtn.setOnClickListener(this);
		setContentView(view);
	}
}
