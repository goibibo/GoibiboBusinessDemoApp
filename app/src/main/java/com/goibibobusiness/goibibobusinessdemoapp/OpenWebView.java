package com.goibibobusiness.goibibobusinessdemoapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class OpenWebView extends AppCompatActivity {

    public static final String WEBVIEW_URL = "com.goibibobusiness.demoapp.WEBVIEW_URL";

    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("DemoApp WebView");
        setContentView(R.layout.activity_open_web_view);

        Intent intent = getIntent();
        String web_url = intent.getStringExtra(WEBVIEW_URL);

        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.setWebChromeClient(new MyWebChromeClient());

        // Enabling Javascript
        WebSettings myWebSettings = myWebView.getSettings();
        myWebSettings.setJavaScriptEnabled(true);

        // Enable webview debugging from chrome browser on desktop
        myWebView.setWebContentsDebuggingEnabled(true);

        // Allow webapp in webview to access native android utilities
        myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");

        // Load URL
        myWebView.loadUrl(web_url);
    }

    /** Allow android back button to navigate between webview pages */
    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    // Private class for webapp interface
    // This allows our webview to interact with android system
    private class WebAppInterface {
        Context myContext;

        // Instantiate the interface and set context
        WebAppInterface(Context c) { myContext = c; }

        /** Jump out of webview */
        @JavascriptInterface
        public void navigateToMenu() {
            AppCompatActivity activity = (AppCompatActivity) myContext;
            activity.finish();
        }

        /** Show toast notification */
        @JavascriptInterface
        public void showToast(String message) {
            Toast.makeText(myContext, message, Toast.LENGTH_SHORT).show();
        }

        /** Show alert dialog */
        @JavascriptInterface
        public void showAlert(String message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
            builder.setMessage(message);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    // Private class to handle JS alert windows
    // To edit the default title text which comes with an alert
    private class MyWebChromeClient extends android.webkit.WebChromeClient {

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            AlertDialog dialog = new AlertDialog.Builder(view.getContext()).
                    setTitle("Goibibo").
                    setMessage(message).
                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    }).create();
            dialog.show();
            result.confirm();
            return true;
        }
    }
}
