package com.brenopolanski.meudesejo;

import com.brenopolanski.meudesejo.twitter.R;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.brenopolanski.meudesejo.HttpRequest.GetJSONListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements GetJSONListener {
	private Activity context;
	private GetJSONListener listener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Button p = (Button) findViewById(R.id.button1);
		final EditText nome = (EditText) findViewById(R.id.editText1);
		final EditText senha = (EditText) findViewById(R.id.editText2);
		context = this;
		listener = this;

		p.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("nome", nome.getText().toString());
				data.put("senha", senha.getText().toString());

				HttpRequest http = new HttpRequest(data, listener,
						HttpRequest.TYPEPOST);
				http.setContext(context);
				http.execute("https://android-login.herokuapp.com/login.php");

			}
		});

	}

	@Override
	public void onRemoteCallComplete(JSONArray response) {
		// TODO Auto-generated method stub
		try {
			JSONObject json = (JSONObject) response.get(0);

			SharedPreferences preferences = getSharedPreferences(
					"usuario-login", MODE_PRIVATE);
			SharedPreferences.Editor editor = preferences.edit();

			if (json.getString("sucess").equals("1")) {
				editor.putString("usuario", "1");
			} else {
				editor.putString("usuario", "0");
			}

			editor.commit();
			String s = preferences.getString("usuario", "0");

			if (s.equals("0")) {
				Toast.makeText(context, "Usuário Inválido", Toast.LENGTH_LONG)
						.show();
			} else {
				Intent myIntent = new Intent(context,
						TwitterSearchActivity.class);
				LoginActivity.this.startActivity(myIntent);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onRemoteCallFailed() {
		// TODO Auto-generated method stub
	}

}