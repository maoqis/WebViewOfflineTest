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
    private WebView mWbContent;


    long startLoadUrlTime;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        assignViews();

        WebSettings webSettings = mWbContent.getSettings();
//支持缩放，默认为true。
        webSettings .setSupportZoom(false);
//调整图片至适合webview的大小
        webSettings .setUseWideViewPort(true);
// 缩放至屏幕的大小
        webSettings .setLoadWithOverviewMode(true);
//设置默认编码
        webSettings .setDefaultTextEncodingName("utf-8");
////设置自动加载图片
        webSettings .setLoadsImagesAutomatically(true);


        webSettings.supportMultipleWindows();
//获取触摸焦点
        mWbContent.requestFocusFromTouch();
//允许访问文件
        webSettings.setAllowFileAccess(true);
//开启javascript
        webSettings.setJavaScriptEnabled(true);
        //支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//提高渲染的优先级
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //支持内容重新布局
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//关闭webview中缓存
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        String url = getIntent().getStringExtra("url");

        final Boolean isIntercept = getIntent().getBooleanExtra("isIntercept", false);


        mWbContent.setWebViewClient(new WebViewClient() {
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (!isIntercept) {//不做拦截
                    return super.shouldInterceptRequest(view,url);
                }


                WebResourceResponse response = null;

                //转换 为assets目录下路径，前缀为 https://xxx.xxx.xxx/时做拦截
                String localPath = getLocalPath(url);
                Log.d(TAG, "shouldInterceptRequest called with: " + "view = [" + view + "], url = [" + url + "]" + ", localPath = [" + localPath + "]");


                if (localPath != null) {
                    try {
                        String mimeType = "";
                        mimeType = getExtentionName(url, mimeType);
                        Log.d(TAG, "shouldInterceptRequest: mimeType=" + mimeType + " localPath=" + localPath);
                        AssetManager assets = WebViewActivity.this.getResources().getAssets();
                        //文件流，暂时使用 assets下文件，如果要加载sdk文件，输入流改为文件流即可。
                        InputStream is = assets.open(localPath);
                        response = new WebResourceResponse(mimeType, "UTF-8", is);
                        return response;

                    } catch (IOException e) {
                        e.printStackTrace();
                       return super.shouldInterceptRequest(view,url);
                    }
                }else {
                    return super.shouldInterceptRequest(view,url);
                }


            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //这里为页面加载完成时机
                Log.d(TAG, "onPageFinished() called with: view = [" + view + "], url = [" + url + "]");
                long end = System.currentTimeMillis();
                boolean isPreLoad = SharedPreferenceUtils.getInstance().getBoolean(WebViewPreloadHolder.PRELOAD_ON_OFF, false);
                Log.d(TAG, "onPageFinished: "+
                        " 是否开启了预创建:"+ isPreLoad +
                        " 开始加载时间毫秒数:"+ startLoadUrlTime +
                        " 页面加载完成毫秒数："+ end +
                        " 间隔时间:"+(end -startLoadUrlTime));
            }
        });

        mWbContent.loadUrl(url);
        startLoadUrlTime = System.currentTimeMillis();


    }

    /**
     * 获取本地路径
     * @param url
     * @return 为null时候，本地没有匹配资源
     */
    private String getLocalPath(String url) {
        if (url == null) {
            return null;
        }
        if (url.startsWith("https://www.163.com")) {
            url = url.replace("https://www.163.com/", "");
        } else {
           return null;
        }

        return url;
    }
//    <type extension=".png" mimetype="image/png" />
//    <type extension=".gif" mimetype="image/gif" />
//    <type extension=".jpg" mimetype="image/jpeg" />
//    <type extension=".jpeg" mimetype="image/jpeg" />
    private String getExtentionName(String localPath, String mimeType) {
        String extentionName = "";
        if (localPath.length() > 0 && localPath.lastIndexOf(".") > -1) {
            extentionName = localPath.substring(localPath.lastIndexOf("."));
        }
        switch (extentionName) {
            case ".html":
                mimeType = "text/html";
                break;
            case ".js":
                mimeType = "application/javascript";
                break;
            case ".css":
                mimeType = "text/css";
                break;
            case ".png":
                mimeType = "image/png";
                break;
            case ".gif":
                mimeType = "image/gif";
                break;
            case ".jpg":
                mimeType = "image/jpg";
                break;
            case ".jpeg":
                mimeType = "image/jpeg";
                break;

        }
        return mimeType;
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
