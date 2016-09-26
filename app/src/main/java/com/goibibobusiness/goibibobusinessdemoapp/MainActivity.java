package com.goibibobusiness.goibibobusinessdemoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.zip.CRC32;

public class MainActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize default value for settings
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText_weblink);
//        Log.v("goibibobusinessdemoapp", "STARTING APP----------------->>>>>>>>>>>>>");
    }

    @Override
    protected void onResume() {
        updateLoginURL();
        super.onResume();
    }

    /** Called when 'customize login params' button is pressed */
    public void customizeLoginParams(View view){
        Intent intent = new Intent(this, LoginParams.class);
        startActivity(intent);
    }

    /** Called when 'open link' button is pressed */
    public void openLinkInWebView(View view) {
        Intent intent = new Intent(this, OpenWebView.class);
        String message = editText.getText().toString();
        intent.putExtra(OpenWebView.WEBVIEW_URL, message);
        startActivity(intent);
    }

    public void updateLoginURL() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean target_testing = sharedPref.getBoolean("target_testing_env", true);
        String url_endpoint = sharedPref.getString("target_endpoint", "start_login");
        String partner_id = sharedPref.getString("partner_id", "example_bank");
        String userid = sharedPref.getString("user_id", "user345");
        String timestamp = sharedPref.getString("time_stamp", "2015-06-17 14:30");
        String token = sharedPref.getString("token", "0000000");
        String checksum_key = sharedPref.getString("checksum_key", "testkey");
        String api_version = sharedPref.getString("api_version", "1.0");
        String vertical = sharedPref.getString("vertical", "flight");
        String f_name = sharedPref.getString("f_name", "Sherlock");
        String m_name = sharedPref.getString("m_name", "");
        String l_name = sharedPref.getString("l_name", "Holmes");
        String mobile = sharedPref.getString("mobile", "8877743215");
        String email = sharedPref.getString("email", "sherlock@guerrillamail.com");

        String crc_string = partner_id + userid + timestamp + token + checksum_key;
        CRC32 crc = new CRC32();
        crc.update(crc_string.getBytes());
        String enc = Long.toHexString(crc.getValue());


        String prefix;
        if (target_testing) {
            prefix = "http://pp.";
        } else {
            prefix = "https://www.";
        }

        String BASE_URL = prefix + "goibibobusiness.com/b2b_partner/" + partner_id + "/" + url_endpoint + "/?";

        Uri buildUri;
        if (api_version.equals("1.0")) {
            buildUri = Uri.parse(BASE_URL)
                    .buildUpon()
                    .appendQueryParameter("user_id", userid)
                    .appendQueryParameter("timestamp", timestamp)
                    .appendQueryParameter("token", token)
                    .appendQueryParameter("checksum", enc)
                    .appendQueryParameter("version", api_version)
                    .appendQueryParameter("vertical", vertical)
                    .appendQueryParameter("f_name", f_name)
                    .appendQueryParameter("m_name", m_name)
                    .appendQueryParameter("l_name", l_name)
                    .appendQueryParameter("mobile", mobile)
                    .appendQueryParameter("email", email)
                    .build();
        } else {
            buildUri = Uri.parse(BASE_URL)
                    .buildUpon()
                    .build();
        }

        String url_string = buildUri.toString();

        editText.setText(url_string);
    }

}
