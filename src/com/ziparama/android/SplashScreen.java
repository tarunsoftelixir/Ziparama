package com.ziparama.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class SplashScreen extends Activity {

	private static int SPLASH_TIME_OUT = 2000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.splash_layout);
		
		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity
				
					Intent i = new Intent(SplashScreen.this, MainActivity.class);
					startActivity(i);
					
					
				// close this activity
				finish();
			}
		}, SPLASH_TIME_OUT);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		View view = findViewById(android.R.id.content);
		Animation mLoadAnimation = AnimationUtils.loadAnimation(
				getApplicationContext(), android.R.anim.fade_in);
		mLoadAnimation.setStartOffset(200);
		mLoadAnimation.setDuration(1000);
		view.startAnimation(mLoadAnimation);
	}
}
