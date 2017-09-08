package com.example.marianarciso.nokiaoauth1;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;

import java.io.UnsupportedEncodingException;
import java.lang.Object;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
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

import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    Button btnGetData;
    TextView oauthcText;
    TextView consumKText;
    TextView noncetText;
    TextView signatText;
    TextView tempoText;


    private static final String URL = "https://developer.health.nokia.com/account/request_token?oauth_callback=%s" +
            "&oauth_consumer_key=71d055bb8b448d753e4b0d8eb00f164dc737237f319145bd84de71707c72694" +
            "&oauth_nonce=%s"+
            "&oauth_signature_method=HMAC-SHA1&oauth_timestamp=%s&oauth_version=1.0" ;

    private static final String ACCESS_TOKEN_URL = "https://developer.health.nokia.com/account/request_token";

    private static final String REDIRECT_URI = "https://a19238f7.ngrok.io/polls";

    private static final String CONS_KEY = "71d055bb8b448d753e4b0d8eb00f164dc737237f319145bd84de71707c72694";
    private static final String CONS_SECRET = "21bde44de705439020d8cf44b3e342b66408335380d1e283565fe8128";

    public String URI_ENC;
    public String SIGNAT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetData = (Button) findViewById(R.id.button);
      //  txtData = (TextView) findViewById(R.id.TextViewData);

       oauthcText  = (TextView) findViewById(R.id.oauthc);
       consumKText  = (TextView) findViewById(R.id.consumK);
       noncetText  = (TextView) findViewById(R.id.noncet);
       signatText  = (TextView) findViewById(R.id.signat);
       tempoText   = (TextView) findViewById(R.id.tempo);


        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                consumKText.setText(CONS_KEY);

                Long tsLong = System.currentTimeMillis()/1000;
                final String ts = tsLong.toString();
                final String nonce = UUID.randomUUID().toString();


                tempoText.setText(ts);
                noncetText.setText(nonce);


                try {URI_ENC = URLEncoder.encode(REDIRECT_URI,"UTF-8");
                    //oauthcText.setText(URI_ENC);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                String base_string = String.format(URL,REDIRECT_URI,nonce,ts);
                try {

                    base_string = "GET&" + URLEncoder.encode(base_string,"UTF8");
                    oauthcText.setText(base_string);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                String key = CONS_SECRET +"&";

                        /*Convert the HTTP Method to uppercase and set the output string equal to this value.
                                Append the ‘&’ character to the output string.
                                Percent encode the URL and append it to the output string.
                        Append the ‘&’ character to the output string.
                                Percent encode the parameter string and append it to the output string.*/
                try {



                    SecretKeySpec secret = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA1");
                    Mac mac = Mac.getInstance("HmacSHA1");
                    mac.init(secret);

                    //String base_string = "qwerty";

                    byte[] digest = mac.doFinal(base_string.getBytes("UTF-8"));



                   // String enc = new String(digest);

                    // Base 64 Encode the results
                   // SIGNAT = Base64.encodeToString(enc.getBytes(), Base64.NO_WRAP);
                    SIGNAT = Base64.encodeToString(digest, 0);
                    signatText.setText(SIGNAT);
                    //txtData.setText(retVal);

                   try {  SIGNAT = URLEncoder.encode(SIGNAT,"UTF-8");

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }




                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }


                final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST,ACCESS_TOKEN_URL,

                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try { JSONObject responseArray = new JSONObject(response);

                                    //txtTeste.setText(response);
                                    Toast.makeText(getApplicationContext(), response,Toast.LENGTH_LONG).show();
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




                        // params.put("Content-Type", "application/x-www-form-urlencoded");
                        params.put("oauth_callback", URI_ENC);
                        params.put("oauth_consumer_key", CONS_KEY);
                        params.put("oauth_nonce",nonce);
                        params.put("oauth_signature", SIGNAT);
                        params.put("oauth_signature_method","HMAC-SHA1");
                        params.put("oauth_timestamp",ts);
                        params.put("oauth_version","1.0");
                        return params;
                    }

                };

                requestQueue.add(stringRequest);

            }

        });}

}
