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
						// Wait by 3 seconds
						// or ...
						wait(3000);
						// ... if the user touch the screen
						clickScreen = true;
					}
				} catch (InterruptedException ex) {}
				
				if (clickScreen) {
					// Close SplashScreen
					finish();
					
					// Load the LoginActivity
					Intent loginIntent = new Intent();
					loginIntent.setClass(SplashScreen.this, LoginActivity.class);
					startActivity(loginIntent);
				}
			}
		};
		
		splashThread.start();
	}
	
	/**
	 * Method that destroys Thread
	 */
	@Override
	public void onPause() {
		super.onPause();
		
		// If the user click in back button
		// the system end the Thread
		splashThread.interrupt();
	}
	
	/**
	 * Method called when a touch screen motion event occurs
	 * @param event - Type of event touch 
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			synchronized(splashThread) {
				clickScreen = true;
				
				// End the command wait(3000)
				splashThread.notifyAll();
			}
		}
		
		return true;
	}
}
