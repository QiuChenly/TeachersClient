package com.myapplication.qiuchen.teachersclient;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.itemanimators.ScaleUpAnimator;
import com.mikepenz.itemanimators.SlideInOutLeftAnimator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import MuYuanTeacher.HttpUtils;
import MuYuanTeacher.aolanTeacherSystem;
import MuYuanTeacher.logininfo;
import MuYuanTeacher.mLoginsData;
import MuYuanTeacher.studentInfoClass;

public class MainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    long BackTime = 0;
    Toolbar toolbar = null;
    Boolean isRefreshData = false;
    private static final String TAG = "首页";


    /*
     * 新闻数据抓取适配类
     */
    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        private List<Map<String, String>> item;

        public void SetAdapterListData(List<Map<String, String>> list) {
            item = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(MainPage.this).inflate(R.layout.index_publicinfo_item, parent, false));
            return holder;
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Map<String, String> map = item.get(position);
            holder.News_index.setText(map.get("News_index"));
            holder.News_Title.setText(map.get("News_Title"));
            holder.News_Author.setText(map.get("News_Author"));
            holder.News_ReportTime.setText(map.get("News_ReportTime"));
        }

        @Override
        public int getItemCount() {
            return item.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView News_index;
            TextView News_Title;
            TextView News_Author;
            TextView News_ReportTime;

            public MyViewHolder(View view) {
                super(view);
                News_index = (TextView) view.findViewById(R.id.News_index);
                News_Title = (TextView) view.findViewById(R.id.News_Title);
                News_Author = (TextView) view.findViewById(R.id.News_Author);
                News_ReportTime = (TextView) view.findViewById(R.id.News_ReportTime);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(findViewById(R.id.m_ContentView), News_Title.getText().toString() + "\n" + News_Author.getText().toString() + " " +
                                News_ReportTime.getText().toString
                                        (), Snackbar.LENGTH_LONG).setAction("Action", null).show();

                        new Thread() {
                            @Override
                            public void run() {
                                //TODO 这里写获取网页源码的方法,并加载在WebView里显示
                                try {
                                    logininfo.aolan.getHtmlCode((item.get(Integer.valueOf
                                            (News_index.getText().toString()) - 1)).get("News_Url"), News_Title.getText().toString(), News_ReportTime.getText().toString(), (item.get(Integer.valueOf
                                            (News_index.getText().toString()) - 1).get("News_ID")));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (logininfo.ErrorMessage.length() > 0) {
                                    Intent i = new Intent(MainPage.this, News_Info.class);
                                    startActivity(i);
                                }
                            }
                        }.start();

//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            if (view_old != null) {
//                                view_old.setBackground (null);
//                            }
//                            v.setBackground (getDrawable (R.mipmap.itemback));
//                            view_old = v;
//                        }
                    }
                });
            }
        }
    }

    /*
     * 学生信息Recycler适配器
     */
    class mAllClassAdapter extends RecyclerView.Adapter<mAllClassAdapter.MyViewHolder> {


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(MainPage.this).inflate(R.layout.mallcalssmate_view, parent, false));
            return holder;
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();//得到item的LayoutParams布局参数
            params.height = logininfo.getRandom();//把随机的高度赋予itemView布局
            holder.itemView.setLayoutParams(params);//把params设置给itemView布局
            final studentInfoClass stu = logininfo.studentInfo.get(position);
            holder.m_TextView_mAllClassMate_studentId.setText(stu.studentId);
            holder.m_TextView_mAllClassMate_studentMobileNum.setText(stu.studentMobileNumber);
            holder.m_TextView_mAllClassMate_studentName.setText(stu.studentName);
            holder.m_TextView_mAllClassMate_studentNo.setText(String.valueOf(position + 1));
            final Handler h = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    Bitmap b = msg.getData().getParcelable("image");
                    holder.m_LinearLayout_mAllClassMateView.setImageBitmap(b);
                }
            };
            new Thread() {
                @Override
                public void run() {
                    if (Objects.equals(stu.student_xdm_RealName, "") || Objects.equals(stu.Student_bjhm_Str, "")) {
                        int a = ((Spinner) findViewById(R.id.m_Spinner_AllClassMate)).getSelectedItemPosition();
                        int b = ((Spinner) findViewById(R.id.m_Spinner_AllClassMate_CLASS)).getSelectedItemPosition();
                        String[] str = logininfo.classMate_CLASS.get(b).split("\\(");
                        stu.student_xdm_RealName = logininfo.classMate.get(a).split("\\(")[0];
                        stu.Student_bjhm_Str = str[0];
                        stu.Student_xdm = str[1].split("\\|")[1];
                    }

                    /*
                     * 考虑性能,降低服务器压力,只需要初始化一次
                     */
                    if (Objects.equals(stu.Student_rxsj, "")) {

                        try {
                            stu.Student_rxsj = logininfo.aolanClassMate.getThisStudentRxsj(stu.student_xdm_RealName, stu.Student_bjhm_Str, stu.Student_xdm, stu.studentId);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    /*
                      缓存图像以便后面调用,降低服务器压力
                     */
                    if (stu.me == null) {
                        stu.me = logininfo.aolanClassMate.getThisStudentCardIDPic(stu.Student_rxsj, stu.studentCardId);
                        if (isRefreshData == false) {
                            logininfo.studentInfo.set(position, stu);
                        } else {
                            return;
                        }
                    }
                    Bundle s = new Bundle();
                    s.putParcelable("image", stu.me);
                    Message msg = new Message();
                    msg.setData(s);
                    h.sendMessage(msg);
                }
            }.start();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                studentInfoClass s = logininfo.aolanClassMate.getThisStudentMoreInfomation(stu);
                                logininfo.studentInfo.set(position, s);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Intent i = new Intent(MainPage.this, StudentInfomationActivity.class);
                            i.putExtra("studentID", position);
                            startActivity(i);
                        }
                    }.start();
                }
            });
        }

        @Override
        public int getItemCount() {
            return logininfo.studentInfo.size();
        }

        /**
         * 内部适配类,分别对应不同的Recycler控件
         */
        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView m_TextView_mAllClassMate_studentName;
            TextView m_TextView_mAllClassMate_studentId;
            TextView m_TextView_mAllClassMate_studentMobileNum;
            TextView m_TextView_mAllClassMate_studentNo;
            ImageView m_LinearLayout_mAllClassMateView;

            MyViewHolder(View view) {
                super(view);
                m_TextView_mAllClassMate_studentNo = (TextView) view.findViewById(R.id.m_TextView_mAllClassMate_studentNo);
                m_TextView_mAllClassMate_studentName = (TextView) view.findViewById(R.id.m_TextView_mAllClassMate_studentName);
                m_TextView_mAllClassMate_studentId = (TextView) view.findViewById(R.id.m_TextView_mAllClassMate_studentId);
                m_TextView_mAllClassMate_studentMobileNum = (TextView) view.findViewById(R.id.m_TextView_mAllClassMate_studentMobileNum);
                m_LinearLayout_mAllClassMateView = (ImageView) view.findViewById(R.id.m_LinearLayout_mAllClassMateView);
            }
        }
    }

    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = 10; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = 10; // (column + 1) * ((1f / spanCount) * spacing)
            outRect.top = 10;
            outRect.bottom = 10; // item bottom
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (logininfo.MainBackground != null) {
            ImageView i = (ImageView) findViewById(R.id.HomePageBackGround);
            i.setImageBitmap(logininfo.MainBackground);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //如果需要透明导航栏，请加入标记
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        logininfo.Dialog = new ProgressDialog(this);
        logininfo.Dialog.setTitle("页面加载中...");
        logininfo.Dialog.setMessage("初始化页面数据中...");
        logininfo.Dialog.setCancelable(false);

        toolbar.setTitle("学院公告");
        SwitchViewHandler.sendMessage(BundleMessage(1));


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ((TextView) findViewById(R.id.m_NickName)).setText(logininfo.mlogininfo.mName);
        ((TextView) findViewById(R.id.m_NickNick)).setText("本次登录IP:" + logininfo.mlogininfo.m_fip);
        ((ImageView) findViewById(R.id.mPic)).setImageDrawable(getResources().getDrawable(R.mipmap.coffee));
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mMainPage_logout) {
            //init All State
            logininfo.mlogininfo = new mLoginsData();
            logininfo.aolan = new aolanTeacherSystem();
            logininfo.ErrorMessage = null;
            logininfo.ErrorMsgCode = 0;
            logininfo.edit.putString("pass", "");
            logininfo.edit.apply();
            logininfo.LoginState = 2;
            Intent i = new Intent(MainPage.this, LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.mMainPage_Main) {
            toolbar.setTitle("学院公告");
            SwitchViewHandler.sendMessage(BundleMessage(1));
        } else if (id == R.id.nav_gallery) {
            toolbar.setTitle("学生请假");
            SwitchViewHandler.sendMessage(BundleMessage(2));
        }  else if (id == R.id.mAllClassMate) {
            toolbar.setTitle("我的班级");
            SwitchViewHandler.sendMessage(BundleMessage(3));
        } else if (id == R.id.mMainPage_Author) {
            Toast.makeText(this, "你好,我是秋城落叶,有问题想问我?", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.mMainPage_AuthorEmail) {
            Toast.makeText(this, "我的QQ:963084062,有问题可以加我垂询哦.", Toast.LENGTH_SHORT).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Message BundleMessage(int Page) {
        Bundle b = new Bundle();
        b.putInt("page", Page);
        Message msg = new Message();
        msg.setData(b);
        return msg;
    }

    Handler SwitchViewHandler = new Handler() {
        @SuppressLint("InflateParams")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            logininfo.Dialog.show();
            LinearLayout i;
            LinearLayout linearLayout;
            final LayoutInflater inflater = LayoutInflater.from(MainPage.this);
            switch (msg.getData().getInt("page")) {
                case 1:
                    initView1Page(inflater);
                    break;
                case 2:
                    initView2Page(inflater);
                    break;
                case 3:
                    initmClassMateView(inflater);
                    break;
            }
        }
    };
    SwipeRefreshLayout swipeRefreshLayout;

    public void initmClassMateView(LayoutInflater inflater) {
        LinearLayout i = (LinearLayout) inflater.inflate(R.layout.activity_mallclassmate, null).findViewById(R.id.m_View_mAllClassMate);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.m_ContentView);
        linearLayout.removeAllViews();
        linearLayout.addView(i);

        final Handler SwipeRefreshLayoutHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mAllClassAdapter mallclass = new mAllClassAdapter();
                RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.m_RecyclerView_mAllClassmate);
                mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//这里用线性显示 类似于listview
                mRecyclerView.setHasFixedSize(false);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.setAdapter(mallclass);
                swipeRefreshLayout.setRefreshing(false);
                isRefreshData = false;
            }
        };
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mSwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshData = true;
                logininfo.studentInfo = new ArrayList<>();
                Spinner spin = (Spinner) findViewById(R.id.m_Spinner_AllClassMate_CLASS);
                final String[] s = logininfo.classMate_CLASS.get(spin.getSelectedItemPosition()).split("\\|");
                final String count = logininfo.aolan.GetSubText(s[0], "(", ")", 0);
                final String[] className = s[0].split("\\(");
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            logininfo.studentInfo = logininfo.aolanClassMate.getThisClassStudent(className[0], s[1], count);
                        } catch (IOException e) {
                            Log.d(TAG, "发现异常:来自MainActivity第453行: " + e.getMessage());
                        }
                        SwipeRefreshLayoutHandle.sendEmptyMessage(0);
                    }
                }.start();


            }
        });


        final Spinner mAllClass = (Spinner) findViewById(R.id.m_Spinner_AllClassMate);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                ArrayAdapter arrayAdapter = new ArrayAdapter<String>(MainPage.this, android.R.layout.simple_spinner_item, logininfo.classMate);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mAllClass.setAdapter(arrayAdapter);
                mAllClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        String str=logininfo.classMate.get(position);
                        //TODO:以后备用方法

                        final Handler classMate = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                if (logininfo.studentInfo == null) {
                                    logininfo.studentInfo = new ArrayList<>();
                                }
                                mAllClassAdapter mallclass = new mAllClassAdapter();
                                RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.m_RecyclerView_mAllClassmate);
                                mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//这里用线性显示 类似于listview
                                mRecyclerView.setHasFixedSize(false);
                                mRecyclerView.addItemDecoration(new GridSpacingItemDecoration());
                                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                                mRecyclerView.setAdapter(mallclass);
                            }
                        };
                        final Handler h = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                ArrayAdapter arrayAdapter = new ArrayAdapter<String>(MainPage.this, android.R.layout.simple_spinner_item, logininfo.classMate_CLASS);
                                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ((Spinner) findViewById(R.id.m_Spinner_AllClassMate_CLASS)).setAdapter(arrayAdapter);
                                ((Spinner) findViewById(R.id.m_Spinner_AllClassMate_CLASS)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                                        new Thread() {
                                            @Override
                                            public void run() {

                                                //优化加载机制,实现只加载一次数据
                                                if (logininfo.studentInfo == null || logininfo.studentInfo.size() <= 0) {
                                                    String[] s = logininfo.classMate_CLASS.get(position).split("\\|");
                                                    String count = logininfo.aolan.GetSubText(s[0], "(", ")", 0);
                                                    String[] className = s[0].split("\\(");
                                                    try {
                                                        logininfo.studentInfo = logininfo.aolanClassMate.getThisClassStudent(className[0], s[1], count);
                                                    } catch (IOException e) {
                                                        Log.d(TAG, "发现异常:来自MainActivity第464行: " + e.getMessage());
                                                    }
                                                }
                                                classMate.sendEmptyMessage(0);
                                            }
                                        }.start();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        };
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    logininfo.classMate_CLASS = logininfo.aolanClassMate.getAllClassMate(true);
                                    h.sendEmptyMessage(0);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        };
        new Thread() {
            @Override
            public void run() {
                try {
                    logininfo.classMate = logininfo.aolanClassMate.getAllClassMate(false);
                    handler.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();


        logininfo.Dialog.cancel();
    }


    private Boolean isRefreshNews=false;

    public void initView1Page(LayoutInflater inflater) {
        LinearLayout i = (LinearLayout) inflater.inflate(R.layout.index_page, null).findViewById(R.id.index_page_pageview);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.m_ContentView);
        linearLayout.removeAllViews();
        linearLayout.addView(i);
        final SwipeRefreshLayout news_swipeRefreshLayouts = (SwipeRefreshLayout) findViewById(R.id.mNewsRefresh);


        final Handler hand = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (logininfo.mlogininfo.News == null) {
                    logininfo.mlogininfo.News = new ArrayList<>();
                }
                HomeAdapter homeAdapter = new HomeAdapter();
                homeAdapter.SetAdapterListData(logininfo.mlogininfo.News);
                RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(MainPage.this));//这里用线性显示 类似于listview
                mRecyclerView.setHasFixedSize(false);
                if (!isRefreshNews) {
                    mRecyclerView.addItemDecoration(new GridSpacingItemDecoration());
                }
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.setAdapter(homeAdapter);

                if (logininfo.mlogininfo.News_WorkPaln == null) {
                    logininfo.mlogininfo.News_WorkPaln = new ArrayList<>();
                }
                homeAdapter = new HomeAdapter();
                homeAdapter.SetAdapterListData(logininfo.mlogininfo.News_WorkPaln);
                mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView_WorkPlan);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(MainPage.this));//这里用线性显示 类似于listview
                mRecyclerView.setHasFixedSize(false);
                if (!isRefreshNews) {
                    mRecyclerView.addItemDecoration(new GridSpacingItemDecoration());
                }
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.setAdapter(homeAdapter);

                // mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));//这里用线性宫格显示 类似于grid view
                // mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager (2, OrientationHelper.VERTICAL));//这里用线性宫格显示 类似于瀑布流
                logininfo.Dialog.cancel();
                if (isRefreshNews) {
                    news_swipeRefreshLayouts.setRefreshing(false);
                    isRefreshNews = false;
                }
            }
        };
        final Thread s = new Thread() {
            @Override
            public void run() {
                try {
                    logininfo.aolan.NewsGet();
                    logininfo.aolan.NewsGet_WorkPlan();
                    hand.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        s.start();

        news_swipeRefreshLayouts.setColorSchemeColors(Color.BLUE);
        news_swipeRefreshLayouts.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(isRefreshNews){
                    news_swipeRefreshLayouts.setRefreshing(false);
                    return;
                }
                logininfo.mlogininfo.News = new ArrayList<>();
                logininfo.mlogininfo.News_WorkPaln = new ArrayList<>();
                ((RecyclerView) findViewById(R.id.mRecyclerView)).removeAllViews();
                ((RecyclerView) findViewById(R.id.mRecyclerView_WorkPlan)).removeAllViews();
                isRefreshNews = true;
                s.start();
            }
        });


    }

    public void initView2Page(LayoutInflater inflater) {
        LinearLayout i = (LinearLayout) inflater.inflate(R.layout.mmian_mian, null).findViewById(R.id.m_LeavesView);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.m_ContentView);
        linearLayout.removeAllViews();
        linearLayout.addView(i);
        findViewById(R.id.m_LeavesView_WorkView).setVisibility(View.GONE);
        findViewById(R.id.m_LeavesView_NoWorkView).setVisibility(View.VISIBLE);
        final List<String> list = new ArrayList<>();
        final Spinner spinner = (Spinner) findViewById(R.id.m_LeavesSpinner);
        final Handler hand = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                findViewById(R.id.m_LeavesView_WorkView).setVisibility(View.VISIBLE);
                findViewById(R.id.m_LeavesView_NoWorkView).setVisibility(View.GONE);

                ArrayAdapter arrayAdapter = new ArrayAdapter<String>(MainPage.this, android.R.layout.simple_spinner_item, logininfo.mlogininfo.Holidays_StudentInfo.OpinionSelection);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ((Spinner) findViewById(R.id.m_OpinionSpinner)).setAdapter(arrayAdapter);

                arrayAdapter = new ArrayAdapter<String>(MainPage.this, android.R.layout.simple_spinner_item, list);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                        Snackbar.make(findViewById(R.id.m_ContentView), logininfo.mlogininfo.LeavesPerson.get(position).get("BanJi") + " " + logininfo.mlogininfo.LeavesPerson.get(position).get
                                ("XinMing") + "\n提交时间:" + logininfo.mlogininfo.LeavesPerson.get(position).get("RequestTime"), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        final Handler hand = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                //判断数据可用
                                if (logininfo.mlogininfo.Holidays_StudentInfo.m_imageurl_Bitmap != null) {
                                    ((ImageView) findViewById(R.id.m_LeavesView_ImageView_mPicture)).setImageBitmap(logininfo.mlogininfo.Holidays_StudentInfo.m_imageurl_Bitmap);

                                    //设置学生姓名
                                    ((TextView) findViewById(R.id.m_LeavesView_TextView_mName)).setText("姓名:" + logininfo.mlogininfo.Holidays_StudentInfo.m_Name);
                                    //设置学号
                                    ((TextView) findViewById(R.id.m_LeavesView_TextView_mStudentID)).setText("学号:" + logininfo.mlogininfo.Holidays_StudentInfo.m_xh);
                                    //设置院系名
                                    ((TextView) findViewById(R.id.m_LeavesView_TextView_Department)).setText("所属院系:" + logininfo.mlogininfo.Holidays_StudentInfo.m_DepartmentName);
                                    //设置班级名
                                    ((TextView) findViewById(R.id.m_LeavesView_TextView_ClassName)).setText("所属班级:" + logininfo.mlogininfo.Holidays_StudentInfo.m_ClassName);
                                    //设置请假时间
                                    ((TextView) findViewById(R.id.m_LeavesView_TextView_RequestHolidaysTime)).setText("请假时间:" + logininfo.mlogininfo.Holidays_StudentInfo.m_RequestTime);
                                    //设置请假具体原因
                                    ((TextView) findViewById(R.id.m_LeavesView_TextView_HolidaysBecause)).setText("请假原因:" + logininfo.mlogininfo.Holidays_StudentInfo.m_qjjtyy);
                                    //设置拟回校时间
                                    ((TextView) findViewById(R.id.m_LeavesView_TextView_WillComeBackTime)).setText("拟回校时间:" + logininfo.mlogininfo.Holidays_StudentInfo.m_nhxsj);
                                    //设置请假天数
                                    ((TextView) findViewById(R.id.m_LeavesView_TextView_Days)).setText("请假天数:" + logininfo.mlogininfo.Holidays_StudentInfo.m_qjts);
                                    //设置外出地址
                                    ((TextView) findViewById(R.id.m_LeavesView_TextView_OutAdress)).setText("外出地址:" + logininfo.mlogininfo.Holidays_StudentInfo.m_wcdz);
                                    //设置联系方式
                                    ((TextView) findViewById(R.id.m_LeavesView_TextView_ContactInfomation)).setText("联系方式:" + logininfo.mlogininfo.Holidays_StudentInfo.m_jzxmlxfs);
                                }
                            }
                        };

                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    logininfo.aolan.QueryStudentLeaveInfomation(logininfo.mlogininfo.LeavesPerson.get(position).get("YuanXi"), logininfo.mlogininfo.LeavesPerson.get(position)
                                            .get("BanJi"), logininfo.mlogininfo.LeavesPerson.get(position).get("XueHao"), logininfo.mlogininfo.LeavesPerson.get(position).get("RequestTime"));
                                    logininfo.mlogininfo.Holidays_StudentInfo.m_imageurl_Bitmap = null;
                                    if (logininfo.mlogininfo.Holidays_StudentInfo.m_imageurl.length() > 0) {
                                        logininfo.mlogininfo.Holidays_StudentInfo.m_imageurl_Bitmap = HttpUtils.getImageBitmap(logininfo.mlogininfo.Holidays_StudentInfo.m_imageurl);
                                    }
                                    hand.sendEmptyMessage(0);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                //设置AllowButton
                findViewById(R.id.m_LeavesView_Button_AllowRequest).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        findViewById(R.id.m_LeavesView_Button_AllowRequest).setVisibility(View.GONE);
                        findViewById(R.id.m_LeavesView_ProgressBar_AllowRequest).setVisibility(View.VISIBLE);
                        final String[] Result = logininfo.mlogininfo.Holidays_StudentInfo.OpinionSelection.get(((Spinner) findViewById(R.id.m_OpinionSpinner)).getSelectedItemPosition()).split
                                ("\\|");
                        final Handler hand = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                if (logininfo.ErrorMsgCode == 1) {
                                    Toast.makeText(MainPage.this, "修改成功!", Toast.LENGTH_SHORT).show();
                                    Snackbar.make(findViewById(R.id.m_ContentView), "修改成功!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    SwitchViewHandler.sendMessage(BundleMessage(2));
                                } else {
                                    Snackbar.make(findViewById(R.id.m_ContentView), logininfo.ErrorMessage, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    findViewById(R.id.m_LeavesView_Button_AllowRequest).setVisibility(View.VISIBLE);
                                    findViewById(R.id.m_LeavesView_ProgressBar_AllowRequest).setVisibility(View.GONE);
                                }
                            }
                        };
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    logininfo.ErrorMsgCode = logininfo.aolan.AllowRequestHolidays(Result[1], Result[0]);
                                    hand.sendEmptyMessage(0);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                });
            }
        };
        new Thread() {
            @Override
            public void run() {
                try {
                    logininfo.aolan.getAllLeaveState();//同步线程处理
                    logininfo.aolan.initOpinionSelection();
                    logininfo.Dialog.cancel();
                    if (logininfo.mlogininfo.LeavesPerson.size() > 0) {
                        for (Map<String, String> map : logininfo.mlogininfo.LeavesPerson) {
                            list.add(map.get("BanJi") + " " + map.get("XinMing"));
                        }
                        hand.sendEmptyMessage(0);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
//                if (LoginInfo.IsRequestViews) {
//                    SwitchViews.sendMessage(SwitchView(2));
//                    LoginInfo.IsRequestViews = false;
//                } else if (LoginInfo.IsLongRequestViews) {
//                    SwitchViews.sendMessage(SwitchView(3));
//                    LoginInfo.IsLongRequestViews = false;
//                } else {
                long secondT = System.currentTimeMillis();
                if (secondT - BackTime > 2000) {
                    Toast.makeText(this, "再次点击返回键退出", Toast.LENGTH_SHORT).show();
                    BackTime = secondT;
                } else {
                    finish();

                    /*
                     * todo:下面服务启动不需要,关闭BUG功能
                     */
                    //启动后台服务
//                    Intent i=new Intent (MainPage.this, RequestServers.class);
//                    startService (i);
                    System.exit(0);
                }
                return true;
            default:
                return true;
        }
    }
}
