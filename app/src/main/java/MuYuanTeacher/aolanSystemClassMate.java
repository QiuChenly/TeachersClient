package MuYuanTeacher;

import android.graphics.Bitmap;

import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by QiuChenly on 2017/6/4.
 */

public class aolanSystemClassMate extends aolanTeacherSystem {
    /**
     * 获取本教师所有的班级
     *
     * @return
     * @throws IOException
     */
    public List<String> getAllClassMate(Boolean isCLASS) throws IOException {
        String url = "http://xgsl.jsahvc.edu.cn/student/left_2.aspx";
        String data = "";
        List<String> list = new ArrayList<>();
        if (isCLASS) {
            data = "__VIEWSTATE=" + EncodeStr(logininfo._viewstate) + "&__VIEWSTATEGENERATOR=" + EncodeStr(logininfo._viewStategenerator) + "&__EVENTTARGET=tree1&__EVENTARGUMENT=onselectedindexchange%2C0%2C0.0%3Bonexpand%2C0.0&km=sy_jbgr&dcbz=&xqbz=&ndbz=&tindex=&tindex2=&tindex3=&tindex4=&tindex5=&tindex6=&cxtj=&f_km=&nj=&xzbz=&sbbz=&msie=&qx_s=&stbz=";
            ResponseData res = HttpUtils.POST(new URL(url), data, HttpUtils.Cookie, "application/x-www-form-urlencoded");
            UpdataViewState(res.ResponseText);
            Pattern p = Pattern.compile("onclick\\(&quot;xdm=&#39;(.*?)&#39;&quot;,&quot;(.*?)&quot;\\)");
            Matcher m = p.matcher(res.ResponseText);
            while (m.find()) {
                list.add(m.group(2) + "|" + m.group(1));
            }
        } else {
            UpdataViewState(HttpUtils.Get(url, HttpUtils.Cookie));
            data = "__VIEWSTATE=" + EncodeStr(logininfo._viewstate) +
                    "&__VIEWSTATEGENERATOR=" + EncodeStr(logininfo._viewStategenerator) +
                    "&__EVENTTARGET=&__EVENTARGUMENT=&km=sy_jbgr&dcbz=&xqbz=&ndbz=&tindex=&tindex2=&tindex3=&tindex4=&tindex5=&tindex6=&cxtj=&f_km=&nj=&xzbz=&sbbz=&msie=&qx_s=&stbz=";
            ResponseData res = HttpUtils.POST(new URL(url), data, HttpUtils.Cookie, "application/x-www-form-urlencoded");
            UpdataViewState(res.ResponseText);
            Pattern p = Pattern.compile("white-space:nowrap;\">&nbsp;(.*?)</font>");
            Matcher m = p.matcher(res.ResponseText);
            while (m.find()) {
                list.add(m.group(1).replace("&nbsp;", ""));
            }
        }
        return list;
    }

    /**
     * 获取该学生的个人照片
     *
     * @param nd      该学生所在年级 2015年入学就写2015 以此类推
     * @param CardsId 该学生的身份证ID
     * @return 返回图片
     */
    public Bitmap getThisStudentCardIDPic(String nd, String CardsId) {
        String u = "http://xgsl.jsahvc.edu.cn/student/tpxs.aspx?id=/" + nd + "/" + CardsId + ".jpg";
        return HttpUtils.getImageBitmap(u);
    }

    /**
     * 获取该班级所有学生基本信息
     *
     * @param className 班级名称
     * @param xdm       院系代码
     * @param count     学生总数
     * @return 返回学生信息集合
     * @throws IOException IO异常
     */
    public List<studentInfoClass> getThisClassStudent(String className, String xdm, String count) throws IOException {
        Map<String, String> m = new HashMap<>();
        m.put("Connection", "keep-alive");
        m.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        m.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        m.put("Referer", "http://xgsl.jsahvc.edu.cn/student/r_2_4.aspx");
        m.put("Accept-Language", "zh-CN,zh;q=0.8");
        m.put("Cookie", HttpUtils.Cookie);
        String url = "http://xgsl.jsahvc.edu.cn/student/r_2_4.aspx";
        String re = HttpUtils.Get(url, "UTF-8", m);
        UpdataViewState(re);
        String data = "__EVENTTARGET=&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE=" + EncodeStr(logininfo._viewstate) + "&__VIEWSTATEGENERATOR=" + logininfo._viewStategenerator + "&km=sy_jbgr&dcbz=&xqbz=&ndbz=&ptj=xdm%3D%27209%27%5Cand%5Cbjhm%3D%27" + EncodeStr(className) + "%27&y_ptj=&por1=&w1=0&w2=0&w3=0&pls=0&pzd=0&pzd_hj=0&pzd_x=&n_ht=0&n_df=0&n_df2=0&n_ni=&n_nim=&n_m=&n_l=&xzbz=0&v_fsxz=&v_flxz=&pkey=&xs_bj=&cxtj=&f_km=&spzd_x=&scxtj=&sf_km=&ppage=&vfl=&xm=&sbbz=&bz=3&stbz=&qx_s=&yfssbz=";
        HttpUtils.POST(url, data, m, false);
        url = "http://xgsl.jsahvc.edu.cn/student/r_2_5_2.aspx";
        re = HttpUtils.Get(url, "UTF-8", m);
        UpdataViewState(re);
        data = "__EVENTTARGET=&__EVENTARGUMENT=&__VIEWSTATE=" + EncodeStr(logininfo._viewstate) + "&__VIEWSTATEGENERATOR=" + logininfo._viewStategenerator + "&ym=1&km=sy_jbgr&ptj=xdm%3D%27" + xdm + "%27%5Cand%5Cbjhm%3D%27" + EncodeStr(className) + "%27&pzd=&pzd_hj=%27%27%5Cas%5Cxdm%2C%27%E5%90%88%E8%AE%A1%27%5Cas%5Cx%2C%27%E5%90%88%E8%AE%A1%27%5Cas%5Cbjhm%2C%27%E5%90%88%E8%AE%A1%27%5Cas%5Cxh%2C%27%27%5Cas%5Cxm%2C%27%27%5Cas%5Cxbdm%2C%27%27%5Cas%5Czydm%2C%27%27%5Cas%5Csfzhm%2C%27%27%5Cas%5Ccell%2C%27%27%5Cas%5Cyhzh&n_ht=%E9%99%A2%E7%B3%BB%2C%E7%8F%AD%E7%BA%A7%2C%E5%AD%A6%E5%8F%B7%2C%E5%A7%93%E5%90%8D%2C%E6%80%A7%E5%88%AB%2C%E4%B8%93%E4%B8%9A%2C%E8%BA%AB%E4%BB%BD%E8%AF%81%2C%E6%89%8B%E6%9C%BA%2C%E9%93%B6%E8%A1%8C%E5%B8%90%E5%8F%B7&n_df=x%2Cbjhm%2Cxh%2Cxm%2Cxbdm%2Czydm%2Csfzhm%2Ccell%2Cyhzh&n_df2=&n_ni=&n_nim=&n_m=&n_l=&ppage=0&xzbz=0&fy=&hj=&pls=4&xzclk1=&xzclk2=&xzgjc=xh&cxtj=&ii_z=0&ii_d=&dy_bz=1&dy_hz=&hei=754&dc_f1=&dc_f2=&dc_bq=&psort=&psort_ad=&psort2=&psort2_ad=&psort3=&psort3_ad=&v_fsxz=%E9%BB%98%E8%AE%A4%E6%96%B9%E5%BC%8F&v_flxz=&bjxs=&xmxs=&fxxs=&xhxs=&chxs=&xjxs=&n_hj=&n_hjz=&n_hjzdx=&pgs=40&phs=" + count + "&ckpt=&allbz=&xm=&pzd_x=&cw=&sbbz=&mbbz=&bjxs2=&yfssck=&plbz=0&stbz=&xqbz=&ndbz=&jbxs=";
        ResponseData res = HttpUtils.POST(url, data, m, false);
        String buffer = res.ResponseText;
        Pattern p = Pattern.compile("<tr nowrap=\"nowrap\" onmouseout=\"mt\\(this\\)\" onmouseover=\"mv\\(this\\)\" ondblclick=\"xzst\\(&#39;.*?&#39;,&#39;&#39;\\)\">[\\s\\S]*?<td nowrap=\"nowrap\">(.*?)\\s*?</td><td nowrap=\"nowrap\">(.*?)\\s*?</td><td nowrap=\"nowrap\">(.*?)\\s*?</td><td nowrap=\"nowrap\">(.*?)\\s*?</td><td nowrap=\"nowrap\">(.*?)\\s*?</td><td nowrap=\"nowrap\">(.*?)\\s*?</td><td nowrap=\"nowrap\">(.*?)\\s*?</td>");
        Matcher ms = p.matcher(buffer);
        List<studentInfoClass> list = new ArrayList<studentInfoClass>();
        studentInfoClass stu;
        while (ms.find()) {
            stu = new studentInfoClass();
            stu.studentId = ms.group(1);
            stu.studentName = ms.group(2);
            stu.studentSex = ms.group(3);
            stu.studentType = ms.group(4);
            stu.studentCardId = ms.group(5);
            stu.studentMobileNumber = ms.group(6);
            stu.studentMoneyCardId = ms.group(7);
            list.add(stu);
        }
        return list;
    }

    /**
     * 获取该学生所有信息
     *
     * @param x     系名称,如农业工程系
     * @param bjhm  班级名称,如数控151
     * @param xdm   系代码,如209
     * @param gjc_z 学生学号
     */
    public String getThisStudentRxsj(String x, String bjhm, String xdm, String gjc_z) throws IOException {
        String url = "http://xgsl.jsahvc.edu.cn/student/r_3_1_sy.aspx";
        Map<String, String> m = new HashMap<>();
        m.put("Connection", "keep-alive");
        m.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        m.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        m.put("Referer", "http://xgsl.jsahvc.edu.cn/student/r_3_1_sy.aspx");
        m.put("Accept-Language", "zh-CN,zh;q=0.8");
        m.put("Cookie", HttpUtils.Cookie);
        String re = HttpUtils.Get(url, "UTF-8", m);
        UpdataViewState(re);
        String data = "__EVENTTARGET=&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE=" + EncodeStr(logininfo._viewstate) + "&__VIEWSTATEGENERATOR=" + logininfo._viewStategenerator + "&x=" + EncodeStr(x) + "&bjhm=" + EncodeStr(bjhm) + "&gjc=xh&gjc_z=" + gjc_z + "&xm=&xdm=" + xdm + "&iud2=&pzd=xdm%2Cbjhm%2Cxh%2Cxm&xzbz=1&psrc=&pxj=&pcf=&rxsj=&km_lx=sy_&xh=&km=sy_jbgr&czsj=&xp_pmc=&xp_pval=&xp_plx=&xp_pkm=&xp_pzd=&xp_pjxjdm=&xp_ipbz=&xp_pjxjdm2=";
        ResponseData res = HttpUtils.POST(new URL(url), data, HttpUtils.Cookie, "application/x-www-form-urlencoded");
        return GetSubText(res.ResponseText, "name=\"rxsj\" type=\"hidden\" id=\"rxsj\" value=\"", "\"", 0);
    }
}
