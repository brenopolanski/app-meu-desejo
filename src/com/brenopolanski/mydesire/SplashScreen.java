package com.brenopolanski.mydesire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class SplashScreen extends Activity {
	private Thread splashThread;
	private boolean clickScreen = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreen);
		
		splashThread = new Thread() {
			
			@Override
			public void run() {
				try {
					synchronized(this) {
						// wait by 3 seconds
						// or ...
						wait(3000);
						// ... if the user touch the screen
						clickScreen = true;
					}
				} catch (InterruptedException ex) {}
				
				if (clickScreen) {
					// close SplashScreen
					finish();
					
					// load the MainActivity
					Intent mainIntent = new Intent();
					mainIntent.setClass(SplashScreen.this, MainActivity.class);
					startActivity(mainIntent);
				}
			}
		};
		
		splashThread.start();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		// if the user click in back button
		// the system end the Thread
		splashThread.interrupt();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			synchronized(splashThread) {
				clickScreen = true;
				
				// end the command wait(3000)
				splashThread.notifyAll();
			}
		}
		
		return true;
	}
}
