package com.maoqis.webviewofflinetest.tools;

import android.app.Activity;
import android.content.Context;
import android.content.MutableContextWrapper;
import android.os.Build;
import android.support.annotation.NonNull;
import android.webkit.WebView;


import com.maoqis.webviewofflinetest.BuildConfig;
import com.maoqis.webviewofflinetest.TestApplication;
import com.maoqis.webviewofflinetest.MyWebView;
import com.orhanobut.logger.Logger;

import java.util.concurrent.LinkedBlockingQueue;

public class WebViewPreloadHolder {
    private static final String TAG = "WebViewPreloadHolder";
    public static final int JELLY_BEAN_MR_2 = Build.VERSION_CODES.JELLY_BEAN_MR2;
    public static final String PRELOAD_ON_OFF = "preload_on_off";
    private LinkedBlockingQueue<MyWebView> mViews = new LinkedBlockingQueue<>(1);


    private static class SingletonHolder {
        private static final WebViewPreloadHolder INSTANCE = new WebViewPreloadHolder();
    }

    private WebViewPreloadHolder() {
    }

    public static final WebViewPreloadHolder getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * 创建预存的WebView
     */
    public synchronized void newNextWebView() {

        if (mViews.size() > 0) {
            return;
        }
        if (isDoPreLoad()) {
            mViews.add(instanceWebView(TestApplication.getAppContext()));
        }

    }

    public static boolean isDoPreLoad() {
        boolean ret = Build.VERSION.SDK_INT > JELLY_BEAN_MR_2;
        if (BuildConfig.DEBUG) {
            ret &= SharedPreferenceUtils.getInstance().getBoolean(PRELOAD_ON_OFF, false);
        }
        return ret;
    }

    /**
     * 外部获WebView取入口
     * @param activity
     * @return
     */
    public MyWebView getWebView(Activity activity) {
        MyWebView webView;
        if (isDoPreLoad()) { // 4.3以上采用webview预加载
            Logger.i(TAG, "getWebView() called with: " + "Webview use preload!");
            webView = WebViewPreloadHolder.getInstance().getWebViewFromCache();
        } else {//4.3 以下 系统创建webview传appclication可能会有适配问题。禁用webview预加载
            Logger.i(TAG, "getWebView() called with: " + "Webview not use preload!");
            webView = WebViewPreloadHolder.getInstance().instanceWebView(activity);
        }
        if (webView.getContext() instanceof MutableContextWrapper) {
            MutableContextWrapper mutableContextWrapper = (MutableContextWrapper) webView.getContext();
            mutableContextWrapper.setBaseContext(activity);
        }
        return webView;
    }

    /**
     * 取预存的webview，没有的话创建
     * @return
     */
    private synchronized MyWebView getWebViewFromCache() {
        if (mViews.size() == 0) {
            mViews.add(instanceWebView(TestApplication.getAppContext()));
        }
        MyWebView webView = mViews.poll();
        mViews.add(instanceWebView(TestApplication.getAppContext()));
        return webView;
    }

    /**
     * 创建webView,设置webView拦截器,以及加载空白页面
     *
     * @param context
     * @return
     */
    private MyWebView instanceWebView(Context context) {
        MyWebView webView = new MyWebView(new MutableContextWrapper(context));
        webView.setWebViewConfig("about:blank",true,webView);//设置webView配置，拦截器以及加载空白页面
        return webView;
    }


}
