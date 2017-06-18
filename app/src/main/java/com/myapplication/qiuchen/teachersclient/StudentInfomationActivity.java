package com.myapplication.qiuchen.teachersclient;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

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
        toolbar.setTitle(stu.studentName);
        setSupportActionBar(toolbar);

        Drawable d=new BitmapDrawable(stu.me);

        findViewById(R.id.app_bar).setBackground(d);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("tel:"+stu.studentMobileNumber));
               startActivity(i);
            }
        });
    }
}
