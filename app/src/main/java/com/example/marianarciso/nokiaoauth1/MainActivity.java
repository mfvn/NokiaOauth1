package com.example.marianarciso.nokiaoauth1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.marianarciso.nokiaoauth1.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btnGetData;
    TextView txtData;
    TextView txtTeste;

    private static final String ACCESS_TOKEN_URL = "https://developer.health.nokia.com/account/request_token";

    private static final String REDIRECT_URI = "http://0d1197f7.ngrok.io/polls";
    private static final String CONS_KEY = "71d055bb8b448d753e4b0d8eb00f164dc737237f319145bd84de71707c72694";
    private static final String CONS_SECRET = "21bde44de705439020d8cf44b3e342b66408335380d1e283565fe8128";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetData = (Button) findViewById(R.id.button);
        txtData = (TextView) findViewById(R.id.TextViewData);
        txtTeste = (TextView) findViewById(R.id.teste);



        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST,ACCESS_TOKEN_URL,

                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {

                                    JSONObject responseArray = new JSONObject(response);

                                    txtTeste.setText(response);
                                    //Toast.makeText(getApplicationContext(), response,Toast.LENGTH_LONG).show();
                                    //String ACCESS_TOKEN = responseArray.getString("oauth_token");
                                    //String TOKEN_TYPE = responseArray.getString("oauth_token_secret");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                requestQueue.stop();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(),Toast.LENGTH_LONG).show();

                    }
                }){


                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();

                        String authString = CONS_KEY+CONS_SECRET+"&";
                        String auth = Base64.encodeToString(authString.getBytes(), Base64.NO_WRAP);

                        Long tsLong = System.currentTimeMillis()/1000;
                        String ts = tsLong.toString();

//"53r%2B38V%2FJS%2BpT2lNvXDs9YU8LgY%3D"

                        params.put("oauth_callback", REDIRECT_URI);
                        params.put("oauth_consumer_key", CONS_KEY);
                        params.put("oauth_nonce","eghdgerstvbjhesf1112ffdfffdgt" );
                        params.put("oauth_signature_method","HMAC-SHA1");
                        params.put("oauth_signature", "53r%2B38V%2FJS%2BpT2lNvXDs9YU8LgY%3D");
                        params.put("oauth_timestamp",ts);
                        params.put("oauth_version","1.0");

                        return params;
                    }

                };

                requestQueue.add(stringRequest);
            }

        });}
}