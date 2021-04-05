package com.example.cyberguide2.activity;

import android.os.Bundle;
import android.webkit.WebView;
import com.example.cyberguide2.R;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WebActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        final String url = getIntent().getStringExtra("url");
        WebView webView = findViewById(R.id.activity_web_wv);
        webView.loadUrl(url);
    }
}