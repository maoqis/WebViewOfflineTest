package com.maoqis.webviewofflinetest;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.maoqis.webviewofflinetest.tools.SharedPreferenceUtils;
import com.maoqis.webviewofflinetest.tools.WebViewPreloadHolder;

import java.io.IOException;
import java.io.InputStream;

public class WebViewActivity extends Activity {
    private static final String TAG = "WebViewActivity";
    private FrameLayout mFlRoot;
    private MyWebView mWbContent;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        String url = getIntent().getStringExtra("url");

        final Boolean isIntercept = getIntent().getBooleanExtra("isIntercept", false);



        assignViews();


        mWbContent.startLoadUrlTime = System.currentTimeMillis();
        mWbContent.setIntercept(isIntercept);
        MyWebView mWbContent = this.mWbContent;
        mWbContent.loadUrl(url);




    }



    private void assignViews() {
        mFlRoot = (FrameLayout) findViewById(R.id.fl_root);
        //加载预创建的WebView，并新建一个预备WebView
        mWbContent = WebViewPreloadHolder.getInstance().getWebView(this);

        mFlRoot.addView(mWbContent,
                new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
    }

}
