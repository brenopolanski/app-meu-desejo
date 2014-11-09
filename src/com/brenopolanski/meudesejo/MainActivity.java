package com.brenopolanski.meudesejo;

import com.brenopolanski.mydesire.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	/**
	 * Method that is executed when the user clicks the back button android
	 */
	@Override
	public void onBackPressed() {
		// Viewing alertDialogWebAppActivity
		alertDialogWebAppActivity("Meu Desejo", "Tem certeza de que deseja sair?");
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
