package com.myapplication.qiuchen.teachersclient;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import MuYuanTeacher.logininfo;
import MuYuanTeacher.studentInfoClass;

public class StudentInfomationActivity extends AppCompatActivity {
    studentInfoClass stu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_infomation);
        Intent i = getIntent();
        int position = i.getExtras().getInt("studentID");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        stu= logininfo.studentInfo.get(position);

        //先设置标题,随后让系统处理显示
        //若在setSupportActionBar()   后面定义则无效显示
        toolbar.setTitle(stu.Student_bjhm_Str+" "+stu.studentName);
        setSupportActionBar(toolbar);

        //设置学生图片
        ImageView mStudentImage=(ImageView)findViewById(R.id.mStudentImage);
        mStudentImage.setImageBitmap(stu.me);

        //拨打电话快捷方式
        FloatingActionButton mCallStudent = (FloatingActionButton) findViewById(R.id.mCallStudent);
        mCallStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("tel:"+stu.studentMobileNumber));
                startActivity(i);
            }
        });
        FloatingActionButton mSendMessageStudent=(FloatingActionButton)findViewById(R.id.mSendMessageStudent);
        mSendMessageStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:+86"+stu.studentMobileNumber));
                startActivity(intent);
            }
        });
    }
}
