package MuYuanTeacher;

import android.content.Context;
import android.icu.text.LocaleDisplayNames;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This Code Is Created by cheny on 2017/4/26 23:55.
 */

public class aolanTeacherSystem {
    public String _viewstate = "";
    public String _viewStategenerator = "";
    Context mContext = null;

    public void aolanTeacherSystem (Context c) {
        mContext = c;
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
        logininfo.ErrorMessage = HttpUntils.getURLResponse (url, HttpUntils.Cookie, "http://xgsl.jsahvc.edu.cn/top_1.aspx");
        UpdataViewState (logininfo.ErrorMessage);
        logininfo.mlogininfo.m_YXDM = GetSubText (logininfo.ErrorMessage, "type=\"hidden\" id=\"yxdm\" value=\"", "\"", 0);
        logininfo.mlogininfo.m_bh = GetSubText (logininfo.ErrorMessage, "type=\"hidden\" id=\"bh\" value=\"", "\"", 0);
        logininfo.mlogininfo.m_fip = GetSubText (logininfo.ErrorMessage, "type=\"hidden\" id=\"fip\" value=\"", "\"", 0);
        logininfo.mlogininfo.m_xzbz = GetSubText (logininfo.ErrorMessage, "type=\"hidden\" id=\"xzbz\" value=\"", "\"", 0);
        logininfo.mlogininfo.m_zxrs = GetSubText (logininfo.ErrorMessage, "type=\"hidden\" id=\"zxrs\" value=\"", "\"", 0);
    }

    public void getAllLeaveState () throws IOException {
        String url = "http://xgsl.jsahvc.edu.cn/student/r_2_5_1.aspx";
        String data = HttpUntils.getURLResponse (url, HttpUntils.Cookie);
        UpdataViewState (data);
        Map<String, String> map = new HashMap<> ();
        map.put ("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        map.put ("Accept-Language", "zh-CN,zh;q=0.8");
        map.put ("Cache-Control", "max-age=0");
        map.put ("Connection", "keep-alive");
        map.put ("Content-Type", "application/x-www-form-urlencoded");
        map.put ("Referer", "http://xgsl.jsahvc.edu.cn/student/r_2_5_1.aspx");
        map.put ("Origin", "http://xgsl.jsahvc.edu.cn");
        map.put ("Host", "xgsl.jsahvc.edu.cn");
        map.put ("Cookie", HttpUntils.Cookie);

        data = "__EVENTTARGET=&__EVENTARGUMENT=&__VIEWSTATE=" + EncodeStr (_viewstate) + "&__VIEWSTATEGENERATOR=" + _viewStategenerator +
                "&ym=1&xzbz=0&ptj=&km=&ar1_gs=0&dc_bq=&N_HT=&N_DF=&N_DF2=&ppage=0&fy=&xzclk1=xdm" +
                "&xzclk2=bjhm&xzclk3=xh&xzclk4=xm&xzgjc=xh&ii_z=0&ii_d=&dy_bz=1&dy_hz=&hei=&dc_f1=&dc_f2=&n_ni=&n_nim=&n_hj=&n_hjz=&n_hjzdx=&pgs=40&phs=25&allbz=&xq=&rzbz=1&czsj=&gjz=&cw=&mbbz" +
                "=&xfl_y=";
        ResponseData res = HttpUntils.POST (url, data, map, true);
        if (res.ResponseCode == 200) {
            UpdataViewState (res.ResponseText);
            //获取数据并封装
            String Regex = "ondblclick=\"xzst\\(&#39;.*?&#39;,&#39;st_xsqj&#39;,&#39;(.*?)&#39;\\)\">[\\s]*<td nowrap=\"nowrap\" valign=\"top\".*?>([\\u4e00-\\u9fa5]*?)[\\s]*<br>(.*?)[\\s]*<br>(" +
                    ".*?)[\\s]*<br>(.*?)[\\s]*</td><td valign=\"top\"><font color=red>(.*?)[\\s]*</font>";
            Pattern p = Pattern.compile (Regex);
            Matcher m = p.matcher (res.ResponseText);
            logininfo.mlogininfo.LeavesPerson = new ArrayList<> ();
            while (m.find ()) {
                Map<String, String> maps = new HashMap<> ();
                maps.put ("RequestTime", m.group (1));
                maps.put ("YuanXi", m.group (2));
                maps.put ("BanJi", m.group (3));
                maps.put ("XueHao", m.group (4));
                maps.put ("XinMing", m.group (5));
                maps.put ("LeiXing", m.group (6));
                logininfo.mlogininfo.LeavesPerson.add (maps);
            }

        }
    }

    public void QueryStudentLeaveInfomation (String DepartmentName, String ClassName, String StudentID, String RequestTime) throws IOException {
        String url = "http://xgsl.jsahvc.edu.cn/student/r_3_1_sy.aspx";
        String Data = HttpUntils.getURLResponse (url, HttpUntils.Cookie);
        UpdataViewState (Data);
        Data = "__EVENTTARGET=&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE" + "=" + EncodeStr (_viewstate) + "&__VIEWSTATEGENERATOR=" + _viewStategenerator + "&x=" + EncodeStr (DepartmentName) +
                "&bjhm=" + EncodeStr (ClassName) + "&gjc=xh&gjc_z=" + StudentID + "&xm=&xdm=&iud2=&pzd=xdm%2Cbjhm%2Cxh%2Cxm&xzbz=1&psrc=&pxj=&pcf=&rxsj=&km_lx=sy_&xh=" + StudentID +
                "&km=st_xsqj&czsj=" + EncodeStr (RequestTime) + "&xp_pmc" + "=&xp_pval" + "=&xp_plx=&xp_pkm=&xp_pzd=&xp_pjxjdm=&xp_ipbz=&xp_pjxjdm2=";
        Map<String, String> map = new HashMap<> ();
        map.put ("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        map.put ("Accept-Language", "zh-CN,zh;q=0.8");
        map.put ("Cache-Control", "max-age=0");
        map.put ("Connection", "keep-alive");
        map.put ("Content-Type", "application/x-www-form-urlencoded");
        map.put ("Referer", "http://xgsl.jsahvc.edu.cn/student/r_3_1_sy.aspx");
        map.put ("Origin", "http://xgsl.jsahvc.edu.cn");
        map.put ("Host", "xgsl.jsahvc.edu.cn");
        map.put ("Cookie", HttpUntils.Cookie);
        ResponseData res = HttpUntils.POST (url, Data, map, true);
        logininfo.mlogininfo.Holidays_StudentInfo = new ObjectPersonInfo ();
        logininfo.mlogininfo.Holidays_StudentInfo.m_ClassName = GetSubText (res.ResponseText, "<input name=\"bjhm\" type=\"text\" value=\"", "\"", 0);
        logininfo.mlogininfo.Holidays_StudentInfo.m_czsj = GetSubText (res.ResponseText, "<input name=\"czsj\" type=\"hidden\" id=\"czsj\" value=\"", "\"", 0);
        logininfo.mlogininfo.Holidays_StudentInfo.m_xh = GetSubText (res.ResponseText, "type=\"hidden\" id=\"xh\" value=\"", "\"", 0);
        logininfo.mlogininfo.Holidays_StudentInfo.m_DepartmentName = GetSubText (res.ResponseText, "<input name=\"x\" type=\"text\" value=\"", "\"", 0);
        logininfo.mlogininfo.Holidays_StudentInfo.m_Name = GetSubText (res.ResponseText, "<input name=\"xm\" type=\"text\" value=\"", "\"", 0);
        logininfo.mlogininfo.Holidays_StudentInfo.m_imageurl = "http://xgsl.jsahvc.edu.cn/student/" + GetSubText (res.ResponseText, "type=\"hidden\" id=\"psrc\" value=\"", "\"", 0);
        logininfo.mlogininfo.Holidays_StudentInfo.m_rxsj = GetSubText (res.ResponseText, "type=\"hidden\" id=\"rxsj\" value=\"", "\"", 0);
        logininfo.mlogininfo.Holidays_StudentInfo.m_xdm = GetSubText (res.ResponseText, "type=\"hidden\" id=\"xdm\" value=\"", "\"", 0);

        //同步获取这位同学的数据
        url = "http://xgsl.jsahvc.edu.cn/student/rsbulid/r_3_3_st_xsqj.aspx";
        Data = HttpUntils.getURLResponse (url, HttpUntils.Cookie);
        UpdataViewState (Data);
        Data = "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"__EVENTTARGET\"\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" +
                "Content-Disposition: form-data; name=\"__EVENTARGUMENT\"\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"__VIEWSTATE\"\n"
                + "\n" + _viewstate + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; " + "name=\"__VIEWSTATEGENERATOR\"\n" + "\n" + _viewStategenerator +
                "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"qjsj\"\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" +
                "Content-Disposition: form-data; name=\"qjsy\"\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"qjsydm\"\n" + "\n" + "\n" +
                "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"qjjtyy\"\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" +
                "Content-Disposition: form-data; name=\"File1\"; filename=\"\"\n" + "Content-Type: " + "application/octet-stream\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" +
                "Content-Disposition: form-data; name=\"fjm\"\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"wcdz\"\n" + "\n" + "\n" +
                "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"jzxmlxfs\"\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" +
                "Content-Disposition: form-data; name=\"nhxsj\"\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"qjts\"\n" + "\n" + "\n" +
                "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"xjrq\"\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" +
                "Content-Disposition:" + " form-data; name=\"czsj\"\n" + "\n" + logininfo.mlogininfo.Holidays_StudentInfo.m_czsj + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" +
                "Content-Disposition: form-data; " + "name=\"fdyspyj\"\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"fdyspyjdm\"\n" +
                "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"yxspyj\"\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" +
                "Content-Disposition: form-data; name=\"yxspyjdm\"\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"xjspyj\"\n" + "\n" +
                "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"xjspyjdm\"\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" +
                "Content-Disposition: form-data; name=\"km\"\n" + "\n" + "st_xsqj\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"pzd\"\n" + "\n" +
                "qjsj,qjsydm,qjjtyy,fjm,wcdz,jzxmlxfs,nhxsj,qjts,xjrq,czsj," + "fdyspyjdm,fdy,fdyspsj,yxspyjdm,yxspr,yxspsj,xjspyjdm,xjspr,xjspsj,sqpzck\n" +
                "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"pzd_c\"\n" + "\n" + "sqpzck,\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" +
                "Content-Disposition: form-data; name=\"pzd_lock\"\n" + "\n" + "nhxsj,qjsj,yxspyjdm,xjspyjdm,\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; "
                + "name=\"pzd_lock2\"\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"xdm\"\n" + "\n" + logininfo.mlogininfo
                .Holidays_StudentInfo.m_xdm + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: " + "form-data; name=\"bjhm\"\n" + "\n" + logininfo.mlogininfo
                .Holidays_StudentInfo.m_ClassName + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"xh\"\n" + "\n" + logininfo.mlogininfo
                .Holidays_StudentInfo.m_xh + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"xm\"\n" + "\n" + logininfo.mlogininfo.Holidays_StudentInfo
                .m_Name + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"qx_i\"\n" + "\n" + "0\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" +
                "Content-Disposition: form-data; name=\"qx_u\"\n" + "\n" + "1\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"qx_d\"\n" + "\n" + "0\n" +
                "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"databcxs\"\n" + "\n" + "1\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" +
                "Content-Disposition: form-data; name=\"databcdel\"\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"xzbz\"\n" + "\n" +
                "1\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"pkey\"\n" + "\n" + "2017-4-25\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" +
                "Content-Disposition: form-data; name=\"pkey4\"\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"xs_bj\"\n" + "\n" + "\n" +
                "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"bdbz\"\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition:" +
                " form-data; name=\"cw\"\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: " + "form-data; name=\"sender\"\n" + "\n" + "\n" +
                "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"fjmf\"\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition:" +
                " form-data; name=\"pczsj\"\n" + "\n" + logininfo.mlogininfo.Holidays_StudentInfo.m_czsj + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; " +
                "name=\"xp_pmc\"\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"xp_pval\"\n" + "\n" + "\n" +
                "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"xp_plx\"\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" +
                "Content-Disposition: form-data; name=\"xp_pkm\"\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"xp_pzd\"\n" + "\n" + "\n"
                + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"xp_pjxjdm\"\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" +
                "Content-Disposition: form-data; name=\"xp_ipbz\"\n" + "\n" + "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ\n" + "Content-Disposition: form-data; name=\"xp_pjxjdm2\"\n" + "\n" +
                "\n" + "------WebKitFormBoundaryB3g3OGJ1RGcsa6kZ--";
        Map<String, String> map1 = new HashMap<> ();
        map1.put ("Accept", "text / html, application / xhtml + xml, application / xml;q = 0.9, image / webp,*/*;q=0.8");
        map1.put ("Accept-Language", "zh-CN,zh;q=0.8");
        map1.put ("Cache-Control", "max-age=0");
        map1.put ("Connection", "keep-alive");
        map1.put ("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundaryB3g3OGJ1RGcsa6kZ");
        map1.put ("Host", "xgsl.jsahvc.edu.cn");
        map1.put ("Origin", "http://xgsl.jsahvc.edu.cn");
        map1.put ("Referer", "http://xgsl.jsahvc.edu.cn/student/rsbulid/r_3_3_st_xsqj.aspx");
        map1.put ("Upgrade-Insecure-Requests", "1");
        map1.put ("Cookie", HttpUntils.Cookie);
        ResponseData responseData = HttpUntils.POST (url, Data, map1, true);
        UpdataViewState (responseData.ResponseText);


    }
}