package MuYuanTeacher;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.myapplication.qiuchen.teachersclient.LoginActivity;
import com.myapplication.qiuchen.teachersclient.MainPage;
import com.myapplication.qiuchen.teachersclient.R;

import java.io.IOException;
import java.util.Random;

public class RequestServers extends Service {
    aolanTeacherSystem aolan;
    int oldcount;
    int now;
    Notification notification;
    NotificationManager notificationManager;


    public RequestServers () {
    }

    @Override
    public IBinder onBind (Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException ("Not yet implemented");
    }

    @Override
    public void onCreate () {
        SharedPreferences Share = this.getSharedPreferences ("QiuChenTeachersSet", MODE_PRIVATE);
        final String user = Share.getString ("user", "");
        final String pass = Share.getString ("pass", "");
        aolan = new aolanTeacherSystem ();
        final int[] a = {0};
        final Handler hand = new Handler () {
            @Override
            public void handleMessage (Message msg) {
                super.handleMessage (msg);
                notificationManager = (NotificationManager) getSystemService (Context.NOTIFICATION_SERVICE);
                // 创建一个启动其他Activity的Intent
                Intent intent = new Intent (RequestServers.this, LoginActivity.class);
                PendingIntent pi = PendingIntent.getActivity (RequestServers.this, 0, intent, 0);
                Notification notify = new Notification.Builder (RequestServers.this)
                        // 设置打开该通知，该通知自动消失
                        .setAutoCancel (true)
                        // 设置显示在状态栏的通知提示信息
                        .setTicker ("学生请假信息")
                        // 设置通知的图标
                        .setSmallIcon (R.mipmap.coffee)
                        // 设置通知内容的标题
                        .setContentTitle ("学生请假信息")
                        // 设置通知内容
                        .setContentText ("总共有" + String.valueOf (now) + "个学生提交了新的请假请求!快点我去看看吧~")
                        // // 设置使用系统默认的声音、默认LED灯
                        .setDefaults (Notification.DEFAULT_SOUND)
                        // |Notification.DEFAULT_LIGHTS)
                        // 设置通知的自定义声音
                        //.setSound ()
                        .setWhen (System.currentTimeMillis ())
                        // 设改通知将要启动程序的Intent
                        .setContentIntent (pi).getNotification ();
                notificationManager.notify (1, notify);
                //Toast.makeText (RequestServers.this, "我tm打死你!" + String.valueOf (a[0]), Toast.LENGTH_SHORT).show ();
            }
        };
        new Thread () {
            @Override
            public void run () {
                try {
                    aolan.mLoginSystem (user, pass);
                } catch (IOException e) {
                    e.printStackTrace ();
                }
                while (true) {
                    a[0]++;
                    try {
                        aolan.getAllLeaveState ();
                        now = logininfo.mlogininfo.LeavesPerson.size ();
//                        now =(new Random (System.currentTimeMillis ())).nextInt (100);
//                        logininfo.PrintInfo (String.valueOf (now));
//                        logininfo.PrintInfo (String.valueOf (oldcount));
                        if (now != oldcount) {
                            hand.sendEmptyMessage (0);
                            oldcount = now;
                        }
                        Thread.sleep (20000);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace ();
                        logininfo.PrintInfo ("服务出错了Orz~");
                    }
                    if (a[0] % 15 == 0) {
                        try {
                            aolan.mLoginSystem (user, pass);
                        } catch (IOException e) {
                            e.printStackTrace ();
                        }
                        logininfo.PrintInfo ("重新登录哦呵呵~");
                        a[0] = 0;
                    }
                }
            }
        }.start ();
    }

    @Override
    public void onDestroy () {

    }
}
