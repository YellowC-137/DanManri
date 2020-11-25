package com.example.dku_lf.ui.registeration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.dku_lf.R;
import com.example.dku_lf.database.FirebaseID;
import com.google.firebase.firestore.FieldValue;

public class ResetGmailActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_gmail);
        setTitle("Registeration");

        mWebView = (WebView)findViewById(R.id.password_webview);
        mWebView.setWebViewClient(new WebViewClient());

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.loadUrl("https://gsl.dankook.ac.kr/session/sso/resetPassword.do");
    }

}