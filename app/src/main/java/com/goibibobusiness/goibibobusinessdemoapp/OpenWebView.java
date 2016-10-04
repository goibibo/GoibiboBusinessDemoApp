package com.goibibobusiness.goibibobusinessdemoapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
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
        myWebView.setWebViewClient(new MyWebViewClient());
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
    private class MyWebChromeClient extends WebChromeClient {

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

    // Private class to handle URL interception
    // ref: http://stackoverflow.com/a/38484061/198660
    private class MyWebViewClient extends WebViewClient {

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            final Uri uri = Uri.parse(url);
            return handleUri(uri);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            final Uri uri = request.getUrl();
            return handleUri(uri);
        }

        private boolean handleUri(final Uri uri) {
            Log.i("goibibobusinessdemoapp", "Uri =" + uri);
            final String host = uri.getHost();
            final String scheme = uri.getScheme();
            Log.i("goibibobusinessdemoapp", "Path =" + uri.getPath());
            // Based on some condition you need to determine if you are going to load the url
            // in your web view itself or in a browser.
            // You can use `host` or `scheme` or any part of the `uri` to decide.
            if (uri.getPath().toString().equals("/b2b_partner/example_extpay/payment_external_process/")) {
                // Returning false means that you are going to load this url in the webView itself
                // http://pp.goibibobusiness.com/b2b_partner/example_extpay/payment_external_process/?user_id=user345&checksum=cc299c0b0c6ec996ca1987ba72074387e0fa30b9b8714663ba4467ab2e9fd1a845f8b9aa8d0063af23357b43808cbe38b566ea254e726f3b21b0886be9933d85&amount=2843&token=0000000&timestamp=2016-10-04+15%3A37%3A55&goibibo_txnid=GOFLDEXP_EXTPAY3b0871475575671
                return false;
            } else {
                return false;
                // Returning true means that you need to handle what to do with the url
                // e.g. open web page in a Browser
//                final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
//                return true;
            }
        }

    }
}
