package com.example.hp.shoppersstop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class TermsAndConditions extends AppCompatActivity {


    WebView terms_and_condition_web_view;

    ProgressBar progressBarWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        progressBarWebView = findViewById(R.id.progressBarWebView);
        if(!progressBarWebView.isShown())
        {
            progressBarWebView.setVisibility(View.VISIBLE);
        }
        terms_and_condition_web_view = findViewById(R.id.terms_condition_web_view);

        terms_and_condition_web_view.getSettings().setJavaScriptEnabled(true);

        terms_and_condition_web_view.loadUrl("https://app.termly.io/document/terms-of-use-for-website/e2168abc-7ec3-4984-9efc-e44a029321fb");
        terms_and_condition_web_view.setWebViewClient(new WebViewClient(){


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(progressBarWebView.isShown())
                {
                    progressBarWebView.setVisibility(View.GONE);
                }


            }
        });
    }



}

