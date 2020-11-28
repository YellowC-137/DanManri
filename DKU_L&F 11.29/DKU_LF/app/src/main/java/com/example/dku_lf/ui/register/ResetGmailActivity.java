package com.example.dku_lf.ui.register;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.RestrictionEntry;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;

import com.example.dku_lf.LoginActivity;
import com.example.dku_lf.R;

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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setMessage("비밀번호 변경을 완료하셨습니까?\n\"예\" 버튼 클릭 시 로그인 화면으로 돌아갑니다.");

        ad.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(ResetGmailActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        ad.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        ad.show();
    }
}