package com.myapplication.qiuchen.teachersclient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import MuYuanTeacher.HttpUntils;
import MuYuanTeacher.aolanTeacherSystem;
import MuYuanTeacher.logininfo;
import MuYuanTeacher.mLoginsData;

public class MainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    long BackTime = 0;
    Toolbar toolbar = null;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main_page);
        toolbar = (Toolbar) findViewById (R.id.toolbar);
        setSupportActionBar (toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById (R.id.fab);
        fab.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Snackbar.make (view, "我就是提示.....", Snackbar.LENGTH_LONG).setAction ("Action", null).show ();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById (R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle (this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener (toggle);
        toggle.syncState ();

        NavigationView navigationView = (NavigationView) findViewById (R.id.nav_view);
        navigationView.setNavigationItemSelectedListener (this);

        logininfo.Dialog = new ProgressDialog (this);
        logininfo.Dialog.setTitle ("页面加载中...");
        logininfo.Dialog.setMessage ("初始化页面数据中...");
        logininfo.Dialog.setCancelable (false);
    }

    @Override
    public void onBackPressed () {
        DrawerLayout drawer = (DrawerLayout) findViewById (R.id.drawer_layout);
        if (drawer.isDrawerOpen (GravityCompat.START)) {
            drawer.closeDrawer (GravityCompat.START);
        } else {
            super.onBackPressed ();
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        ((TextView) findViewById (R.id.m_NickName)).setText (logininfo.mlogininfo.mName);
        ((TextView) findViewById (R.id.m_NickNick)).setText ("本次登录IP:"+logininfo.mlogininfo.m_fip);
        ((ImageView) findViewById (R.id.mPic)).setImageDrawable (getResources ().getDrawable (R.mipmap.coffee));

        getMenuInflater ().inflate (R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId ();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mMainPage_logout) {
            //init All State
            logininfo.mlogininfo = new mLoginsData ();
            logininfo.aolan = new aolanTeacherSystem ();
            logininfo.ErrorMessage = null;
            logininfo.ErrorMsgCode = 0;
            logininfo.edit.putString ("pass", "");
            logininfo.edit.apply ();
            Intent i = new Intent (MainPage.this, LoginActivity.class);
            startActivity (i);
            finish ();
            return true;
        }

        return super.onOptionsItemSelected (item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected (MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId ();
        if (id == R.id.mMainPage_Main) {
            toolbar.setTitle ("首页");
        } else if (id == R.id.nav_gallery) {
            toolbar.setTitle ("学生请假");
            SwitchViewHandler.sendMessage (BundleMessage (2));
        } else if (id == R.id.mMainPage_Author) {
            Toast.makeText (this, "你好,我是秋城落叶,有问题想问我?", Toast.LENGTH_SHORT).show ();
        } else if (id == R.id.mMainPage_AuthorEmail) {
            Toast.makeText (this, "我的QQ:963084062,有问题可以加我垂询哦.", Toast.LENGTH_SHORT).show ();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById (R.id.drawer_layout);
        drawer.closeDrawer (GravityCompat.START);
        return true;
    }

    private Message BundleMessage (int Page) {
        Bundle b = new Bundle ();
        b.putInt ("page", Page);
        Message msg = new Message ();
        msg.setData (b);
        return msg;
    }

    Handler SwitchViewHandler = new Handler () {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage (msg);
            logininfo.Dialog.show ();
            final LayoutInflater inflater = LayoutInflater.from (MainPage.this);
            switch (msg.getData ().getInt ("page")) {
                case 1:
                    LinearLayout linearLayout = (LinearLayout) findViewById (R.id.m_ContentView);
                    linearLayout.removeAllViews ();
                    logininfo.Dialog.cancel ();
                    break;
                case 2:
                    initView1Page (inflater);
                    logininfo.Dialog.cancel ();
                    break;
            }
        }
    };

    public void initView1Page (LayoutInflater inflater) {
        LinearLayout i = (LinearLayout) inflater.inflate (R.layout.mmian_mian, null).findViewById (R.id.m_LeavesView);
        LinearLayout linearLayout = (LinearLayout) findViewById (R.id.m_ContentView);
        linearLayout.removeAllViews ();
        linearLayout.addView (i);
        findViewById (R.id.m_LeavesView_WorkView).setVisibility (View.GONE);
        findViewById (R.id.m_LeavesView_NoWorkView).setVisibility (View.VISIBLE);
        final List<String> list = new ArrayList<> ();
        final Spinner spinner = (Spinner) findViewById (R.id.m_LeavesSpinner);
        final Handler hand = new Handler () {
            @Override
            public void handleMessage (Message msg) {
                super.handleMessage (msg);
                findViewById (R.id.m_LeavesView_WorkView).setVisibility (View.VISIBLE);
                findViewById (R.id.m_LeavesView_NoWorkView).setVisibility (View.GONE);

                ArrayAdapter arrayAdapter = new ArrayAdapter<String> (MainPage.this, android.R.layout.simple_spinner_item, logininfo.mlogininfo.Holidays_StudentInfo.OpinionSelection);
                arrayAdapter.setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item);
                ((Spinner) findViewById (R.id.m_OpinionSpinner)).setAdapter (arrayAdapter);

                arrayAdapter = new ArrayAdapter<String> (MainPage.this, android.R.layout.simple_spinner_item, list);
                arrayAdapter.setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter (arrayAdapter);
                spinner.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
                    @Override
                    public void onItemSelected (AdapterView<?> parent, View view, final int position, long id) {
                        Snackbar.make (findViewById (R.id.m_ContentView), logininfo.mlogininfo.LeavesPerson.get (position).get ("BanJi") + " " + logininfo.mlogininfo.LeavesPerson.get (position).get
                                ("XinMing") + "\n提交时间:" + logininfo.mlogininfo.LeavesPerson.get (position).get ("RequestTime"), Snackbar.LENGTH_LONG).setAction ("Action", null).show ();
                        final Handler hand = new Handler () {
                            @Override
                            public void handleMessage (Message msg) {
                                super.handleMessage (msg);
                                //判断数据可用
                                if (logininfo.mlogininfo.Holidays_StudentInfo.m_imageurl_Bitmap != null) {
                                    ((ImageView) findViewById (R.id.m_LeavesView_ImageView_mPicture)).setImageBitmap (logininfo.mlogininfo.Holidays_StudentInfo.m_imageurl_Bitmap);

                                    //设置学生姓名
                                    ((TextView) findViewById (R.id.m_LeavesView_TextView_mName)).setText ("姓名:" + logininfo.mlogininfo.Holidays_StudentInfo.m_Name);
                                    //设置学号
                                    ((TextView) findViewById (R.id.m_LeavesView_TextView_mStudentID)).setText ("学号:" + logininfo.mlogininfo.Holidays_StudentInfo.m_xh);
                                    //设置院系名
                                    ((TextView) findViewById (R.id.m_LeavesView_TextView_Department)).setText ("所属院系:" + logininfo.mlogininfo.Holidays_StudentInfo.m_DepartmentName);
                                    //设置班级名
                                    ((TextView) findViewById (R.id.m_LeavesView_TextView_ClassName)).setText ("所属班级:" + logininfo.mlogininfo.Holidays_StudentInfo.m_ClassName);
                                    //设置请假时间
                                    ((TextView) findViewById (R.id.m_LeavesView_TextView_RequestHolidaysTime)).setText ("请假时间:" + logininfo.mlogininfo.Holidays_StudentInfo.m_RequestTime);
                                    //设置请假具体原因
                                    ((TextView) findViewById (R.id.m_LeavesView_TextView_HolidaysBecause)).setText ("请假原因:" + logininfo.mlogininfo.Holidays_StudentInfo.m_qjjtyy);
                                    //设置拟回校时间
                                    ((TextView) findViewById (R.id.m_LeavesView_TextView_WillComeBackTime)).setText ("拟回校时间:" + logininfo.mlogininfo.Holidays_StudentInfo.m_nhxsj);
                                    //设置请假天数
                                    ((TextView) findViewById (R.id.m_LeavesView_TextView_Days)).setText ("请假天数:" + logininfo.mlogininfo.Holidays_StudentInfo.m_qjts);
                                    //设置外出地址
                                    ((TextView) findViewById (R.id.m_LeavesView_TextView_OutAdress)).setText ("外出地址:" + logininfo.mlogininfo.Holidays_StudentInfo.m_wcdz);
                                    //设置联系方式
                                    ((TextView) findViewById (R.id.m_LeavesView_TextView_ContactInfomation)).setText ("联系方式:" + logininfo.mlogininfo.Holidays_StudentInfo.m_jzxmlxfs);
                                }
                            }
                        };

                        new Thread () {
                            @Override
                            public void run () {
                                try {
                                    logininfo.aolan.QueryStudentLeaveInfomation (logininfo.mlogininfo.LeavesPerson.get (position).get ("YuanXi"), logininfo.mlogininfo.LeavesPerson.get (position)
                                            .get ("BanJi"), logininfo.mlogininfo.LeavesPerson.get (position).get ("XueHao"), logininfo.mlogininfo.LeavesPerson.get (position).get ("RequestTime"));
                                    logininfo.mlogininfo.Holidays_StudentInfo.m_imageurl_Bitmap = null;
                                    if (logininfo.mlogininfo.Holidays_StudentInfo.m_imageurl.length () > 0) {
                                        logininfo.mlogininfo.Holidays_StudentInfo.m_imageurl_Bitmap = HttpUntils.getImageBitmap (logininfo.mlogininfo.Holidays_StudentInfo.m_imageurl);
                                    }
                                    hand.sendEmptyMessage (0);
                                } catch (IOException e) {
                                    e.printStackTrace ();
                                }
                            }
                        }.start ();
                    }

                    @Override
                    public void onNothingSelected (AdapterView<?> parent) {

                    }
                });

                //设置AllowButton
                findViewById (R.id.m_LeavesView_Button_AllowRequest).setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick (View v) {
                        findViewById (R.id.m_LeavesView_Button_AllowRequest).setVisibility (View.GONE);
                        findViewById (R.id.m_LeavesView_ProgressBar_AllowRequest).setVisibility (View.VISIBLE);
                        final String[] Result = logininfo.mlogininfo.Holidays_StudentInfo.OpinionSelection.get (((Spinner) findViewById (R.id.m_OpinionSpinner)).getSelectedItemPosition ()).split
                                ("\\|");
                        final Handler hand = new Handler () {
                            @Override
                            public void handleMessage (Message msg) {
                                super.handleMessage (msg);
                                if (logininfo.ErrorMsgCode == 1) {
                                    Toast.makeText (MainPage.this, "修改成功!", Toast.LENGTH_SHORT).show ();
                                    Snackbar.make (findViewById (R.id.m_ContentView), "修改成功!", Snackbar.LENGTH_LONG).setAction ("Action", null).show ();
                                    SwitchViewHandler.sendMessage (BundleMessage (2));
                                } else {
                                    Snackbar.make (findViewById (R.id.m_ContentView), logininfo.ErrorMessage, Snackbar.LENGTH_LONG).setAction ("Action", null).show ();
                                    findViewById (R.id.m_LeavesView_Button_AllowRequest).setVisibility (View.VISIBLE);
                                    findViewById (R.id.m_LeavesView_ProgressBar_AllowRequest).setVisibility (View.GONE);
                                }
                            }
                        };
                        new Thread () {
                            @Override
                            public void run () {
                                try {
                                    logininfo.ErrorMsgCode = logininfo.aolan.AllowRequestHolidays (Result[1], Result[0]);
                                    hand.sendEmptyMessage (0);
                                } catch (IOException e) {
                                    e.printStackTrace ();
                                }
                            }
                        }.start ();
                    }
                });
            }
        };
        new Thread () {
            @Override
            public void run () {
                try {
                    logininfo.aolan.getAllLeaveState ();//同步线程处理
                    logininfo.aolan.initOpinionSelection ();
                    if (logininfo.mlogininfo.LeavesPerson.size () > 0) {
                        for (Map<String, String> map : logininfo.mlogininfo.LeavesPerson) {
                            list.add (map.get ("BanJi") + " " + map.get ("XinMing"));
                        }
                        hand.sendEmptyMessage (0);
                    }
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }
        }.start ();
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
//                if (LoginInfo.IsRequestViews) {
//                    SwitchViews.sendMessage(SwitchView(2));
//                    LoginInfo.IsRequestViews = false;
//                } else if (LoginInfo.IsLongRequestViews) {
//                    SwitchViews.sendMessage(SwitchView(3));
//                    LoginInfo.IsLongRequestViews = false;
//                } else {
                long secondT = System.currentTimeMillis ();
                if (secondT - BackTime > 2000) {
                    Toast.makeText (this, "再次点击返回键退出", Toast.LENGTH_SHORT).show ();
                    BackTime = secondT;
                } else {
                    finish ();
                    System.exit (0);
                }
                return true;
            default:
                return true;
        }
    }
}
