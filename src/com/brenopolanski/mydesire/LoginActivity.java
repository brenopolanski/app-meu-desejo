package com.brenopolanski.mydesire;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}
	
	public void checkLogin(View v) {
		// Load the MainActivity
		Intent mainIntent = new Intent();
		mainIntent.setClass(LoginActivity.this, MainActivity.class);
		startActivity(mainIntent);
	}
}
