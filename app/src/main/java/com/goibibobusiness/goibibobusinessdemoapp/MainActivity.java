package com.goibibobusiness.goibibobusinessdemoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize default value for settings
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText_weblink);
        updateLoginURL();
        Log.v("goibibobusinessdemoapp", "STARTING APP----------------->>>>>>>>>>>>>");
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

        String prefix;
        if (target_testing) {
            prefix = "http://pp.";
        } else {
            prefix = "https://www.";
        }


//        editText.setText(prefix + "goibibobusiness.com/b2b_partner/example_bank/start_login/");
        editText.setText(prefix + "goibibobusiness.com/b2b_partner/");
    }

}
