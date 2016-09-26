package com.goibibobusiness.goibibobusinessdemoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when 'open link' button is pressed */
    public void openLinkInWebView(View view) {
        Intent intent = new Intent(this, OpenWebView.class);
        EditText editText = (EditText) findViewById(R.id.editText_weblink);
        String message = editText.getText().toString();
        intent.putExtra(OpenWebView.WEBVIEW_URL, message);
        startActivity(intent);
    }
}
