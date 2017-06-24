package com.myapplication.qiuchen.teachersclient;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import MuYuanTeacher.logininfo;
import MuYuanTeacher.mAdapterData;
import MuYuanTeacher.studentInfoClass;

public class StudentInfomationActivity extends AppCompatActivity {
    studentInfoClass stu = new studentInfoClass();
    RecyclerView mRecyclerView;

    class mAdapter extends RecyclerView.Adapter<mAdapter.mHolderView> {
        /*
        创建内部成员变量
         */
        List<mAdapterData> mList;

        View ItemChildView;

        public mAdapter(List<mAdapterData> list) {
            mList = list;
        }

        class mHolderView extends RecyclerView.ViewHolder {
            /*
            创建Item上的控件
             */
            TextView index;
            TextView title;

            public mHolderView(View itemView) {
                super(itemView);
                ItemChildView = itemView;
                index = (TextView) itemView.findViewById(R.id.indexs);
                title = (TextView) itemView.findViewById(R.id.titles);
            }
        }

        @Override
        public mHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
            mHolderView m = new mHolderView(LayoutInflater.from(parent.getContext()).inflate(R.layout.mcontentstudentallinfomation, parent, false));
            return m;
        }

        @Override
        public void onBindViewHolder(mHolderView holder, final int position) {
            final mAdapterData s = mList.get(position);
            holder.index.setText(s.mKey);
            holder.title.setText(s.mValue);
            /*
            下面写点击事件
             */
            ItemChildView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager mClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    mClipboard.setPrimaryClip(ClipData.newPlainText("qiuchenly", s.mValue));
                    Toast.makeText(StudentInfomationActivity.this, "已复制 " + s.mKey.replace(":", "") + " 数据.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        /*
                返回Item总数
                 */
        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_infomation);
        Intent i = getIntent();
        int position = i.getExtras().getInt("studentID");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        stu = logininfo.studentInfo.get(position);


        //先设置标题,随后让系统处理显示
        //若在setSupportActionBar()   后面定义则无效显示
        toolbar.setTitle(stu.Student_bjhm_Str + " " + stu.studentName);
        setSupportActionBar(toolbar);

        //设置学生图片
        ImageView mStudentImage = (ImageView) findViewById(R.id.mStudentImage);
        mStudentImage.setImageBitmap(stu.me);

        //拨打电话快捷方式
        FloatingActionButton mCallStudent = (FloatingActionButton) findViewById(R.id.mCallStudent);
        mCallStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("tel:" + stu.studentMobileNumber));
                startActivity(i);
            }
        });
        FloatingActionButton mSendMessageStudent = (FloatingActionButton) findViewById(R.id.mSendMessageStudent);
        mSendMessageStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:+86" + stu.studentMobileNumber));
                startActivity(intent);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.mStudentAllInfo);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(StudentInfomationActivity.this));//这里用线性显示 类似于listview
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 10;
                outRect.top = 10;
                outRect.left = 10;
                outRect.right = 10;
            }
        });
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter s = new mAdapter(sets());
        mRecyclerView.setAdapter(s);
    }

    /*
    数据适配器
     */
    List<mAdapterData> sets() {
        List<mAdapterData> a = new ArrayList<>();
        mAdapterData b;
        for (int i = 0; i < 26; i++) {
            b = new mAdapterData();
            switch (i) {
                case 0:
                    b.mKey = "姓名:";
                    b.mValue = stu.studentName + "," + stu.studentSex;
                    break;
                case 1:
                    b.mKey = "专业名称:";
                    b.mValue = stu.student_xdm_RealName + "," + stu.studentType;
                    break;
                case 2:
                    b.mKey = "身份证:";
                    b.mValue = stu.studentCardId;
                    break;
                case 3:
                    b.mKey = "手机号码:";
                    b.mValue = stu.studentMobileNumber;
                    break;
                case 4:
                    b.mKey = "银行卡号:";
                    b.mValue = stu.studentMoneyCardId;
                    break;
                case 5:
                    b.mKey = "宿舍编号:";
                    b.mValue = stu.Student_ssbh + (stu.Student_cwh == "" ? "" : "," + stu.Student_cwh + "号床.");
                    break;
                case 6:
                    b.mKey = "QQ:";
                    b.mValue = stu.Student_QQ;
                    break;
                case 7:
                    b.mKey = "电子邮件:";
                    b.mValue = stu.Student_Email;
                    break;
                case 8:
                    b.mKey = "民族:";
                    b.mValue = stu.Student_MZ;
                    break;
                case 9:
                    b.mKey = "出生日期:";
                    b.mValue = stu.Student_CSRQ;
                    break;
                case 10:
                    b.mKey = "政治面貌:";
                    b.mValue = stu.Student_ZZMM + ((Objects.equals(stu.Student_ZZMMJRSJ, "")) ? "" : "," + stu.Student_ZZMMJRSJ + "加入.");
                    break;
                case 11:
                    b.mKey = "入学时间:";
                    b.mValue = stu.Student_rxsj + "年.";
                    break;
                case 12:
                    b.mKey = "生源地区:";
                    b.mValue = stu.Student_SYDQ;
                    break;
                case 13:
                    b.mKey = "学制:";
                    b.mValue = stu.Student_XZ + "年.";
                    break;
                case 14:
                    b.mKey = "家庭地址:";
                    b.mValue = stu.Student_JTDZ;
                    break;
                case 15:
                    b.mKey = "家长/学生责任人:";
                    b.mValue = stu.Student_LXR;
                    break;
                case 16:
                    b.mKey = "家长/学生责任人联系电话:";
                    b.mValue = stu.Student_JTDH;
                    break;
                case 17:
                    b.mKey = "家庭邮编:";
                    b.mValue = stu.Student_JTYB;
                    break;
                case 18:
                    b.mKey = "姓名拼音:";
                    b.mValue = stu.Student_XMPY;
                    break;
                case 19:
                    b.mKey = "考生号";
                    b.mValue = (stu.Student_KSH == "" ? "无信息" : stu.Student_KSH);
                    break;
                case 20:
                    b.mKey = "毕业中学:";
                    b.mValue = stu.Student_BYZX;
                    break;
                case 21:
                    b.mKey = "中学邮编";
                    b.mValue = stu.Student_JTYB;
                    break;
                case 22:
                    b.mKey = "曾用名:";
                    b.mValue = (stu.Student_CYM == "" ? "无信息" : stu.Student_CYM);
                    break;
                case 23:
                    b.mKey = "籍贯:";
                    b.mValue = stu.Student_JG;
                    break;
                case 24:
                    b.mKey = "户口性质:";
                    b.mValue = stu.Student_HKXZ;
                    break;
                case 25:
                    b.mKey = "学生确认以上信息状态:";
                    b.mValue = stu.Student_Confirm;
                    break;
            }
            a.add(b);
        }
        return a;
    }
}
