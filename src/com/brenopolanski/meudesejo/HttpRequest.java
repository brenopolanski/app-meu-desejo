package com.brenopolanski.meudesejo;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

class HttpRequest extends AsyncTask<String, String, String> {
	private HashMap<String, String> mData = null;
	private Activity context;
	private DefaultHttpClient Client;
	private ProgressDialog Progress;
	private String[] param;
	private byte[] result;
	private String str = "";
	private static int TYPEREQUEST;
	public final static int TYPEPOST = 1;
	public final static int TYPEGET = 2;
	GetJSONListener getJSONListener;

	public HttpRequest(HashMap<String, String> data, GetJSONListener listener,
			int type) {
		this.mData = data;
		this.Client = new DefaultHttpClient();
		this.getJSONListener = listener;
		HttpRequest.TYPEREQUEST = type;
	}

	@Override
	protected String doInBackground(String... params) {
		this.setParam(params);

		switch (HttpRequest.TYPEREQUEST) {
		case 1:
			str = this.sendPost();
			break;
		case 2:
			str = this.sendGet();
			break;

		default:
			break;
		}

		return str;
	}

	@Override
	protected void onPostExecute(String result) {
		Progress.dismiss();

		if (result != "") {
			JSONArray jArray = null;

			try {
				jArray = new JSONArray(result);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			getJSONListener.onRemoteCallComplete(jArray);
		} else {
			getJSONListener.onRemoteCallFailed();
		}

	}

	@Override
	protected void onProgressUpdate(String... values) {
		// something...
	}

	@Override
	protected void onPreExecute() {
		if (!this.isOnline()) {
			this.cancel(true);
		} else {
			Progress = ProgressDialog.show(this.getContext(), "Aguarde",
					"processando..", true, false);
			Progress.setCancelable(false);
		}
	}

	public String sendPost() {
		HttpPost post = new HttpPost(this.param[0]);

		try {
			post.addHeader(new BasicHeader("Accept", "application/json"));
			ArrayList<NameValuePair> nameValuePair = this.setPost();
			post.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
			HttpResponse response = Client.execute(post);
			StatusLine statusLine = response.getStatusLine();

			if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK) {
				result = EntityUtils.toByteArray(response.getEntity());
				str = new String(result, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}

		return str;
	}

	public String sendGet() {

		String url = this.setGet();
		HttpGet get = new HttpGet();

		try {
			get.setURI(new URI(this.param[0] + url));
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}

		try {
			get.addHeader(new BasicHeader("Accept", "application/json"));
			HttpResponse response = Client.execute(get);
			StatusLine statusLine = response.getStatusLine();

			if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK) {
				result = EntityUtils.toByteArray(response.getEntity());
				str = new String(result, "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}

		return str;
	}

	public ArrayList<NameValuePair> setPost() {
		ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		Iterator<String> it = mData.keySet().iterator();

		while (it.hasNext()) {
			String key = it.next();
			nameValuePair.add(new BasicNameValuePair(key, mData.get(key)));
		}

		return nameValuePair;
	}

	public String setGet() {
		Iterator<String> it = mData.keySet().iterator();
		StringBuilder builder = new StringBuilder();

		int i = 0;
		String ent = null;
		while (it.hasNext()) {
			String key = it.next();

			if (i == 0) {
				ent = "?";
			} else {
				ent = "&";
			}

			builder.append(ent + key + "=" + mData.get(key));
			i++;
		}

		return builder.toString();
	}

	public Activity getContext() {
		return context;
	}

	public void setContext(Activity context) {
		this.context = context;
	}

	public boolean isOnline() {
		Activity c = this.getContext();
		ConnectivityManager connMgr = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		return (networkInfo != null && networkInfo.isConnected());
	}

	public String[] getParam() {
		return param;
	}

	public void setParam(String[] param) {
		this.param = param;
	}

	public byte[] getResult() {
		return result;
	}

	public void setResult(byte[] result) {
		this.result = result;
	}

	public interface GetJSONListener {
		public void onRemoteCallComplete(JSONArray response);

		public void onRemoteCallFailed();
	}
}
