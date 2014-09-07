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
	
	/**
	 * Method that is executed when the user clicks the back button android
	 */
	@Override
	public void onBackPressed() {
		// Viewing alertDialogWebAppActivity
		alertDialogWebAppActivity("my-desire", "Tem certeza de que deseja sair?");
	}
	
	/**
	 * Method to display alert
	 * @param title   - Title of the message that is displayed
	 * @param message - Description with the message that will be displayed
	 */
	public void alertDialogWebAppActivity(String title, String message) {
		// Creating alert to the current context (MainActivity)
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		// By setting title
		alertDialog.setTitle(title);

		// By setting the alert message
		alertDialog.setMessage(message);

		// Setting the icon that appears in the message
		alertDialog.setIcon(R.drawable.ic_launcher);

		// By setting "YES" button
		alertDialog.setPositiveButton("SIM",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						finish(); /*
								   * If the user clicks on the "YES" the activity
								   * will be finalized
								   */
					}
				});

		// By setting button "No"
		alertDialog.setNegativeButton("NÃO",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel(); /*
										  * If the user clicks on the "NO" the
										  * alert will be canceled without ending the
										  * activity
										  */
					}
				});

		// Viewing the Alert
		alertDialog.show();
	}
}
