package com.example.movieapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.movieapp.R;

public class MoviePlayerActivity extends AppCompatActivity {

    private WebView webView;
    private String USERAGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_player);

        iniPlayer();
        hideActionBar();
    }
    private void hideActionBar() {
        getSupportActionBar().hide();
    }

    private void iniPlayer() {
        String VIDEO_URL = getIntent().getExtras().getString("episode");
//        String videoIf = "<html><body style=\"margin: 0; padding: 0\"><iframe width=\"100%\" height=\"100%\" " +
//                "src=\""+VIDEO_URL+"\" type=\"text/html\" frameborder=\"0\"></iframe><body><html>";
//        Log.v("abc",""+videoIf);
        webView = findViewById(R.id.movie_player);
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 16) {
            webSettings.setMediaPlaybackRequiresUserGesture(false);
        }

        webSettings.setUserAgentString(USERAGENT);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setLoadWithOverviewMode(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
            webView.loadUrl("javascript:(function() { document.getElementsByTagName(\"video\")[0].play(); })()");
            }
        });

        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(VIDEO_URL);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        }else{
            super.onBackPressed();
        }
    }
}