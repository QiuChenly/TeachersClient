package MuYuanTeacher;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * This Code Is Created by cheny on 2017/4/26 23:55.
 */

public class aolanTeacherSystem {
    public String _viewstate = "";
    public String _viewStategenerator = "";
    Context mContext = null;
    private AsyncHttpClient client = null;

    public void aolanTeacherSystem (Context c) {
        mContext = c;
        client = new AsyncHttpClient ();//initiation
        client.setConnectTimeout (5000);//设置超时
        client.setResponseTimeout (5000);
        client.get ("http://xgsl.jsahvc.edu.cn/login.aspx", new AsyncHttpResponseHandler () {
            @Override
            public void onSuccess (int statusCode, Header[] headers, byte[] responseBody) {
                String Temp = null;
                Temp = new String (responseBody);
                UpdataViewState (Temp);//更新数据
            }

            @Override
            public void onFailure (int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d ("QiuChen", error.getMessage ());
            }
        });
    }

    /**
     * 取文本中间方法
     *
     * @param AllString  所有文本
     * @param left       左边文本
     * @param Right      右边文本
     * @param StartIndex 可空,起始值
     * @return中间文本
     */
    public String GetSubText (String AllString, String left, String Right, int StartIndex) {
        int index = AllString.indexOf (left, StartIndex) + left.length ();
        return AllString.substring (index, AllString.indexOf (Right, index));
    }

    /**
     * 更新必须参数
     *
     * @param ResponseBody 返回的网页数据
     */
    public void UpdataViewState (String ResponseBody) {
        _viewstate = GetSubText (ResponseBody, "id=\"__VIEWSTATE\" value=\"", "\"", 0);
        _viewStategenerator = GetSubText (ResponseBody, "id=\"__VIEWSTATEGENERATOR\" value=\"", "\"", 0);
    }

    public static String md5 (String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance ("MD5").digest (string.getBytes ("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException ("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException ("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder (hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append ("0");
            hex.append (Integer.toHexString (b & 0xFF));
        }
        return hex.toString ();
    }

    /**
     * 把汉字编码为UTF-8编码,解决报错问题
     *
     * @param str 原字符
     * @return 转换后的UTF-8字符
     * @throws UnsupportedEncodingException 异常捕捉
     */
    public String EncodeStr (String str) throws UnsupportedEncodingException {
        return URLEncoder.encode (str, "UTF-8");
    }

    /**
     * 登陆学校系统
     *
     * @param UserID 账号
     * @param Pass   密码可为空
     * @return 返回3种数据 1=登陆成功,2=账号或密码错误,3=未知异常
     * @throws IOException IO异常捕捉
     */
    public int mLoginSystem (String UserID, String Pass) throws IOException {
        String url = "http://xgsl.jsahvc.edu.cn/login.aspx";
        String D = md5 (Pass.toUpperCase ()).toUpperCase ();
        String data = HttpUntils.getURLResponse (url, "");
        UpdataViewState (data);
        data = "__VIEWSTATE=" + EncodeStr (_viewstate) + "&__VIEWSTATEGENERATOR=" + _viewStategenerator + "&userbh=" + UserID + "&pass=" + D + "&cw=&xzbz=1";
        ResponseData res = HttpUntils.submitPostData (new URL (url), data, HttpUntils.Cookie, "application/x-www-form-urlencoded");
        if (res.ResponseCode != 200) {
            String ress = HttpUntils.getURLResponse ("http://xgsl.jsahvc.edu.cn" + res.RedirctUrl, HttpUntils.Cookie);
            if (ress.contains ("系统要求:显示分辨率为")) {
                url = "http://xgsl.jsahvc.edu.cn/top_1.aspx";
                ress = HttpUntils.getURLResponse (url, HttpUntils.Cookie);
                System.out.print (ress);
                logininfo.mlogininfo.mName = GetSubText (ress, "欢迎你:", "\r", 0).trim ();
                return 1;
            }
        } else {
            logininfo.ErrorMessage = GetSubText (res.ResponseText, "type=\"hidden\" id=\"cw\" value=\"", "\"", 0);
            return 2;
        }
        return 3;
    }


    public void findOnlinePersonCount () throws IOException {
        String url = "http://xgsl.jsahvc.edu.cn/online.aspx?xzbz=f";
        logininfo.ErrorMessage = HttpUntils.getURLResponse (url, HttpUntils.Cookie);
        logininfo.mlogininfo.m_YXDM=GetSubText (logininfo.ErrorMessage,"type=\"hidden\" id=\"yxdm\" value=\"","\"",0);
        logininfo.mlogininfo.m_bh=GetSubText (logininfo.ErrorMessage,"type=\"hidden\" id=\"bh\" value=\"","\"",0);
        logininfo.mlogininfo.m_fip=GetSubText (logininfo.ErrorMessage,"type=\"hidden\" id=\"fip\" value=\"","\"",0);
        logininfo.mlogininfo.m_xzbz=GetSubText (logininfo.ErrorMessage,"type=\"hidden\" id=\"xzbz\" value=\"","\"",0);
        logininfo.mlogininfo.m_zxrs=GetSubText (logininfo.ErrorMessage,"type=\"hidden\" id=\"zxrs\" value=\"","\"",0);
    }
}