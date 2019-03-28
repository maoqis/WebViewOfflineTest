package com.maoqis.webviewofflinetest;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.maoqis.webviewofflinetest.tools.WebViewPreloadHolder;

public class WebViewActivity extends Activity {
    private static final String TAG = "WebViewActivity";
    private FrameLayout mFlRoot;
    private MyWebView mWbContent;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        String url = getIntent().getStringExtra("url");
        String tag = getIntent().getStringExtra("tag");

        final Boolean isIntercept = getIntent().getBooleanExtra("isIntercept", false);



        assignViews(tag);


        mWbContent.startLoadUrlTime = System.currentTimeMillis();
        mWbContent.setIntercept(isIntercept);
        MyWebView mWbContent = this.mWbContent;
        if (!tag.equals("3")) {
            mWbContent.loadUrl(url);
        }


    }



    private void assignViews(String tag) {
        mFlRoot = (FrameLayout) findViewById(R.id.fl_root);
        //加载预创建的WebView，并新建一个预备WebView
        if (!tag.equals("3")) {
            mWbContent = WebViewPreloadHolder.getInstance().getWebView(this);
        } else {
            mWbContent = WebViewPreloadHolder.getInstance().getPreloadWebView(this);
        }


        mFlRoot.addView(mWbContent,
                new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
    }

}
