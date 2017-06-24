package com.myapplication.qiuchen.teachersclient;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import org.apache.http.cookie.Cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import MuYuanTeacher.HttpUtils;
import MuYuanTeacher.logininfo;

public class News_Info extends AppCompatActivity {
    List<DOWNFILEINFO> DOWN = new ArrayList<>();
    String DOWN_lx;
    String DOWN_ylx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news__info);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //如果需要透明导航栏，请加入标记

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        ImageView i = (ImageView) findViewById(R.id.NewsBackGround);
        i.setImageBitmap(logininfo.MainBackground);
        WebView webView = (WebView) findViewById(R.id.mWebViews);
        webView.loadData(logininfo.ErrorMessage, "text/html; charset=UTF-8", null);
        ResolveData(logininfo.ErrorMessage);


    }

    void ResolveData(String htmlData) {
        Pattern p = Pattern.compile("onclick=\"down.?\\('(.*?)'\\)\">(.*?)</a>");
        Matcher m = p.matcher(htmlData);
        DOWN_lx = logininfo.aolan.GetSubText(htmlData, "<input name=\"km\" type=\"hidden\" id=\"km\" value=\"", "\" />", 0);
        DOWN_ylx = logininfo.aolan.GetSubText(htmlData, "<input name=\"tjsj_d\" type=\"hidden\" id=\"tjsj_d\" value=\"", "\" />", 0);
        while (m.find()) {
            DOWNFILEINFO d = new DOWNFILEINFO();
            d.DOWNLOADREALURL = "http://xgsl.jsahvc.edu.cn/aldfdnf.aspx?lx=" + DOWN_lx + "&ylx=" + DOWN_ylx + "&file=" + m.group(1);
            d.DOWNLOADFILENAME = m.group(2);
            DOWN.add(d);
        }
        //down2('%e9%99%84%e4%bb%b62%ef%bc%9a%e6%b1%87%e6%80%bb%e8%a1%a8.doc')
    }

    private class DOWNFILEINFO {
        String DOWNLOADREALURL;
        String DOWNLOADFILENAME;
    }
}
