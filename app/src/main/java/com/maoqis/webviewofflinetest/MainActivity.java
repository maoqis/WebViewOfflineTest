package com.maoqis.webviewofflinetest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.maoqis.webviewofflinetest.tools.SharedPreferenceUtils;
import com.maoqis.webviewofflinetest.tools.WebViewPreloadHolder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private CheckBox mBox1;
    private Button mBtnRelative;
    private Button mBtnAbsolute;

    private void assignViews() {
        mBox1 = (CheckBox) findViewById(R.id.box1);
        mBtnRelative = (Button) findViewById(R.id.btn_relative);
        mBtnAbsolute = (Button) findViewById(R.id.btn_absolute);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();

        mBtnRelative.setOnClickListener(this);
        mBtnAbsolute.setOnClickListener(this);

        preloadConfig();


    }

    /**
     * 与加载处理
     */
    private void preloadConfig() {
        boolean isPreLoad = SharedPreferenceUtils.getInstance().getBoolean(WebViewPreloadHolder.PRELOAD_ON_OFF, false);
        mBox1.setChecked(isPreLoad);
        mBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged() called with: buttonView = [" + buttonView + "], isChecked = [" + isChecked + "]");
                SharedPreferenceUtils.getInstance().putBoolean(WebViewPreloadHolder.PRELOAD_ON_OFF,isChecked);
            }
        });
        //根据与加载开关，判断是否提前创建WebView。页面使用之前创建的的webView后会再创建一个新的webView。

        WebViewPreloadHolder.getInstance().newNextWebView();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_relative:
                startWebActivity("file:///android_asset/testRelative/ExampleApp.html",false);
                break;
            case R.id.btn_absolute:
//                startWebActivity("file:///android_asset/testAbsolute/ExampleApp.html",true);
                startWebActivity("http://live.ent.163.com/special/firstcharge",true);

                break;
            default:
                break;
        }
    }

    /**
     *
     * @param url
     * @param isIntercept true，拦截url转换本地处理，false，本地webview直接加载页面中的本地css，js文件
     */
    private void startWebActivity(String url,boolean isIntercept) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("isIntercept",isIntercept);
        startActivity(intent);
    }

}
