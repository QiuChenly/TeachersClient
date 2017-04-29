package com.myapplication.qiuchen.teachersclient;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import MuYuanTeacher.logininfo;

public class News_Info extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_news__info);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //如果需要透明导航栏，请加入标记

            getWindow ().getDecorView ().setSystemUiVisibility (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        WebView webView=(WebView)findViewById (R.id.mWebViews);
        webView.loadData (logininfo.ErrorMessage,"text/html; charset=UTF-8",null);
    }
}
