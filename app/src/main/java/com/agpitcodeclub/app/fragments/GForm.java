package com.agpitcodeclub.app.fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.agpitcodeclub.app.R;

public class GForm extends AppCompatActivity {
    private WebView webView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gform);


        findViewById(R.id.btn_gform_back).setOnClickListener((view -> {
            finish();
        }));

        webView = findViewById(R.id.web_gform);

        // Enable JavaScript (optional, depending on your requirements)
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Load the URL
        String url = "https://www.google.com";

        url=getIntent().getExtras().get("url").toString().trim();


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Toast.makeText(GForm.this, "Error loading URL: " + error.getDescription(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        webView.loadUrl(url);

    }
}