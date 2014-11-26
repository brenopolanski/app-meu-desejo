package com.brenopolanski.meudesejo;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.brenopolanski.meudesejo.twitter.R;
import com.github.kevinsawicki.http.HttpRequest;

public class TwitterSearchActivity extends Activity {

	private ListView lista;
	private EditText texto;
	private String accessToken;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		new AutenticacaoTask().execute();

		lista = (ListView) findViewById(R.id.lista);
		texto = (EditText) findViewById(R.id.texto);
	}

	public void buscar(View v) {
		String filtro = texto.getText().toString();
		if (accessToken == null) {
			Toast.makeText(this, "Token não disponível", Toast.LENGTH_SHORT)
					.show();
		}
		new TwitterTask().execute(filtro);
	}

	private class AutenticacaoTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Map<String, String> data = new HashMap<String, String>();
				data.put("grant_type", "client_credentials");
				String json = HttpRequest
						.post("https://api.twitter.com/oauth2/token")
						.authorization("Basic " + gerarChave()).form(data)
						.body();

				JSONObject token = new JSONObject(json);
				accessToken = token.getString("access_token");
			} catch (Exception e) {
				return null;
			}
			return null;
		}

		private String gerarChave() throws UnsupportedEncodingException {
			String key = "8lXiczCR9K7FlbNJALRbcq6T7";
			String secret = "BVSOl14f55CiCCs1DgR5kuhUMlgUPXzLfYWAqgzRyrkczglBN4";
			String token = key + ":" + secret;
			String base64 = Base64.encodeToString(token.getBytes(),
					Base64.NO_WRAP);
			return base64;
		}
	}

	private class TwitterTask extends AsyncTask<String, Void, String[]> {

		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(TwitterSearchActivity.this);
			dialog.setMessage("Aguarde");
			dialog.show();
		}

		@Override
		protected String[] doInBackground(String... params) {
			try {

				String filtro = params[0];

				if (TextUtils.isEmpty(filtro)) {
					return null;
				}

				String urlTwitter = "https://api.twitter.com/1.1/search/tweets.json?q=";

				String url = Uri.parse(urlTwitter + filtro).toString();

				Log.i(getPackageName(), url);

				String conteudo = HttpRequest.get(url)
						.authorization("Bearer " + accessToken).body();

				JSONObject jsonObject = new JSONObject(conteudo);

				JSONArray resultados = jsonObject.getJSONArray("statuses");

				String[] tweets = new String[resultados.length()];

				for (int i = 0; i < resultados.length(); i++) {
					JSONObject tweet = resultados.getJSONObject(i);
					String texto = tweet.getString("text");
					String usuario = tweet.getJSONObject("user").getString(
							"screen_name");
					tweets[i] = usuario + " - " + texto;
					Log.e(getPackageName(), tweets[i]);
				}

				return tweets;

			} catch (Exception e) {
				Log.e(getPackageName(), e.getMessage(), e);
				return null;
			}
		}

		@Override
		protected void onPostExecute(String[] result) {
			if (result != null) {
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						getBaseContext(), android.R.layout.simple_list_item_1,
						result);

				lista.setAdapter(adapter);
			}
			dialog.dismiss();
		}
	}

	/**
	 * Method that is executed when the user clicks the back button android
	 */
	@Override
	public void onBackPressed() {
		// Viewing alertDialogWebAppActivity
		alertDialogWebAppActivity("Meu Desejo",
				"Tem certeza de que deseja sair?");
	}

	/**
	 * Method to display alert
	 * 
	 * @param title
	 *            - Title of the message that is displayed
	 * @param message
	 *            - Description with the message that will be displayed
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
										 * alert will be canceled without ending
										 * the activity
										 */
					}
				});

		// Viewing the Alert
		alertDialog.show();
	}
}