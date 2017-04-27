package MuYuanTeacher;

import android.app.ProgressDialog;
import android.content.SharedPreferences;

/**
 * This Code Is Created by cheny on 2017/4/26 23:51.
 */

public class logininfo {
    public static aolanTeacherSystem aolan = null;
    public static String ErrorMessage;
    public static int ErrorMsgCode;
    public static mLoginsData mlogininfo = new mLoginsData ();
    public static SharedPreferences Share;
    public static SharedPreferences.Editor edit;
    public static ProgressDialog Dialog = null;
}
