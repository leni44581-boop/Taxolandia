package com.example.taxolandia;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private GameBridge gameBridge;
    private String currentPage = "index.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        gameBridge = new GameBridge(this);
        webView.addJavascriptInterface(gameBridge, "Android");

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        setContentView(webView);

        webView.loadUrl("file:///android_asset/index.html");
    }

    public void loadPage(String url) {
        webView.loadUrl(url);
        if (url.contains("index.html")) currentPage = "index.html";
        else if (url.contains("quiz.html")) currentPage = "quiz.html";
        else if (url.contains("story.html")) currentPage = "story.html";
        else if (url.contains("ending.html")) currentPage = "ending.html";
    }

    public WebView getWebView() {
        return webView;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    @Override
    public void onBackPressed() {
        if (currentPage.equals("index.html")) {
            super.onBackPressed();
        } else {
            webView.evaluateJavascript("window.onpopstate()", null);
        }
    }
}