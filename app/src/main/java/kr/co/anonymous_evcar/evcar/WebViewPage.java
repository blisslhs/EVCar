package kr.co.anonymous_evcar.evcar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewPage extends AppCompatActivity {

    WebView webviewpage_webview;

    String page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_page);

        webviewpage_webview = findViewById(R.id.webviewpage_webview);

        Intent intent = getIntent();

        page = intent.getStringExtra("page");
        webviewpage_webview.setWebViewClient(new WebViewClient());

        WebSettings webSettings = webviewpage_webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webviewpage_webview.loadUrl(page);

    }
}
