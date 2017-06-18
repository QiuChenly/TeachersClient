package MuYuanTeacher;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * This Code Is Created by cheny on 2017/4/26 23:51.
 */

public class logininfo {
    public static aolanTeacherSystem aolan;
    public static aolanSystemClassMate aolanClassMate;
    public static List<String> classMate = new ArrayList<>();
    public static List<String> classMate_CLASS = new ArrayList<>();

    public static String ErrorMessage;
    public static int ErrorMsgCode;
    public static mLoginsData mlogininfo = new mLoginsData();
    public static SharedPreferences Share;
    public static SharedPreferences.Editor edit;
    public static ProgressDialog Dialog = null;

    public static Bitmap MainBackground=null;

    public static int LoginState = 0;
    public static String _viewstate;
    public static String _viewStategenerator;

    public static List<studentInfoClass> studentInfo=new ArrayList<studentInfoClass>();

    public static void PrintInfo(String info) {
        Log.d("QiuChen", info);
    }

    public  static int getRandom(){
        return (int)(650+Math.random()*400);
    }

}
