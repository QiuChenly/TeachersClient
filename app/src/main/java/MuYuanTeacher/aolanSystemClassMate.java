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

    /**
     * 获取学生详细信息
     *
     * @return 返回查询到的数据
     * @throws IOException IO抛出异常  一般不会抛异常
     * @stu 传入数据
     */
    public studentInfoClass getThisStudentMoreInfomation(studentInfoClass stu) throws IOException {
        String url = "http://xgsl.jsahvc.edu.cn/student/rsbulid/r_3_3_sy_jbgr.aspx";
        Map<String, String> m = new HashMap<>();
        m.put("Connection", "keep-alive");
        m.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        m.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        m.put("Referer", "http://xgsl.jsahvc.edu.cn/student/rsbulid/r_3_3_sy_jbgr.aspx");
        m.put("Accept-Language", "zh-CN,zh;q=0.8");
        m.put("Cookie", HttpUtils.Cookie);
        String re = HttpUtils.Get(url, "UTF-8", m);
        UpdataViewState(re);
        String data = "__EVENTTARGET=" +
                "&__EVENTARGUMENT=" +
                "&__VIEWSTATE=" + EncodeStr(logininfo._viewstate) +
                "&__VIEWSTATEGENERATOR=" + logininfo._viewStategenerator +
                "&xb=&xbdm=&x2_xbdm=&zy=&zydm=&x2_zydm=&sfzhm=&x2_sfzhm=&cell=&x2_cell=&yhzh=&x2_yhzh=&sshm=&x2_sshm=&cwh=&x2_cwh=&qq=&x2_qq=&email=&x2_email=&mz=&mzdm=&x2_mzdm=&csrq=&x2_csrq=&zzmm=&zzmmdm=&x2_zzmmdm=&zzmmsj=&x2_zzmmsj=&xl=&xldm=&x2_xldm=&rxsj=&x2_rxsj=&syszd=&syszddm=&x2_syszddm=&xz=&x2_xz=&xxsj=&x2_xxsj=&jtdz=&x2_jtdz=&jtdh=&x2_jtdh=&jtlxr=&x2_jtlxr=&jtyzbm=&x2_jtyzbm=&xmpy=&x2_xmpy=&ksh=&x2_ksh=&byzx=&x2_byzx=&zxyzbm=&x2_zxyzbm=&cym=&x2_cym=&jg=&x2_jg=&sbkh=&x2_sbkh=&hkxz=&hkxzdm=&x2_hkxzdm=&ptbz=&x2_ptbz=&ck_xsqr=&x2_xsqr=&km=sy_jbgr&pzd=xbdm%2Czydm%2Csfzhm%2Ccell%2Cyhzh%2Csshm%2Ccwh%2Cqq%2Cemail%2Cmzdm%2Ccsrq%2Czzmmdm%2Czzmmsj%2Cxldm%2Crxsj%2Csyszddm%2Cxz%2Cxxsj%2Cjtdz%2Cjtdh%2Cjtlxr%2Cjtyzbm%2Cxmpy%2Cksh%2Cbyzx%2Czxyzbm%2Ccym%2Cjg%2Csbkh%2Chkxzdm%2Cptbz%2Cxsqr&pzd_c=xsqr%2C&pzd_lock=xh%2Cxm%2Cxbdm%2Czydm%2Crxsj%2Cksh%2Csfzhm%2Ccsrq%2Csshm%2C&pzd_lock2=" +
                "&xdm=" + stu.Student_xdm +
                "&bjhm=" + EncodeStr(stu.Student_bjhm_Str) +
                "&xh=" + stu.studentId +
                "&xm=" + EncodeStr(stu.studentName) +
                "&qx_i=0&qx_u=1&qx_d=0&databcxs=&databcdel=&xzbz=1&pkey=&pkey4=&xs_bj=&bdbz=&cw=&fjmf=&sb=&pzd_xg=&stuop=1&mc=&tkey=&tkey4=&zjdy=&pczsj=&xp_pmc=&xp_pval=&xp_plx=&xp_pkm=&xp_pzd=&xp_pjxjdm=&xp_ipbz=&xp_pjxjdm2=";
        ResponseData res = HttpUtils.POST(new URL(url), data, HttpUtils.Cookie, "application/x-www-form-urlencoded");
        stu.Student_ssbh = GetSubText(res.ResponseText, "<input name=\"sshm\" type=\"text\" id=\"sshm\" size=\"20\" class=\"TD1\" maxlength=\"20\" Onchange=\"tchang1(&#39;sshm&#39;)\" onkeydown=\"onkeydown1(&#39;yhzh&#39;,&#39;sshm&#39;,event)\" onfocus=\"this.select()\" value=\"", "\" />", 0);
        if(stu.Student_ssbh.length()>100){
            stu.Student_ssbh="";
        }
        stu.Student_cwh = GetSubText(res.ResponseText, "<input name=\"cwh\" type=\"text\" id=\"cwh\" size=\"2\" class=\"TD1\" maxlength=\"2\" onkeydown=\"onkeydown1(&#39;sshm&#39;,&#39;cwh&#39;,event)\" onfocus=\"this.select()\" value=\"", "\" />", 0);
        if(stu.Student_cwh.length()>100){
            stu.Student_cwh="";
        }
        stu.Student_QQ = GetSubText(res.ResponseText, "<input name=\"qq\" type=\"text\" id=\"qq\" size=\"20\" class=\"TD1\" maxlength=\"20\" onkeydown=\"onkeydown1(&#39;cwh&#39;,&#39;qq&#39;,event)\" onfocus=\"this.select()\" value=\"", "\" />", 0);
        if(stu.Student_QQ.length()>100){
            stu.Student_QQ="";
        }
        stu.Student_Email = GetSubText(res.ResponseText, "<input name=\"email\" type=\"text\" id=\"email\" size=\"30\" class=\"TD1\" maxlength=\"30\" onkeydown=\"onkeydown1(&#39;qq&#39;,&#39;email&#39;,event)\" onfocus=\"this.select()\" value=\"", "\" />", 0);
        if(stu.Student_Email.length()>100){
            stu.Student_Email="";
        }
        stu.Student_MZ = GetSubText(res.ResponseText, "<input name=\"mz\" type=\"text\" id=\"mz\" size=\"20\" class=\"TD1\" maxlength=\"20\" Onchange=\"tchang1(&#39;mz&#39;)\" onkeydown=\"onkeydown1(&#39;email&#39;,&#39;mzdm&#39;,event)\" onfocus=\"this.select()\" value=\"", "\" />", 0);
        if(stu.Student_MZ.length()>100){
            stu.Student_MZ="";
        }
        stu.Student_CSRQ = GetSubText(res.ResponseText, "<input name=\"csrq\" type=\"text\" id=\"csrq\" size=\"8\" class=\"TD1\" maxlength=\"8\" onkeydown=\"onkeydown1(&#39;mzdm&#39;,&#39;csrq&#39;,event)\" onfocus=\"this.select()\" value=\"", "\" />", 0);
        if(stu.Student_CSRQ.length()>100){
            stu.Student_CSRQ="";
        }
        stu.Student_ZZMM = GetSubText(res.ResponseText, "<input name=\"zzmm\" type=\"text\" id=\"zzmm\" size=\"20\" class=\"TD1\" maxlength=\"20\" Onchange=\"tchang1(&#39;zzmm&#39;)\" onkeydown=\"onkeydown1(&#39;csrq&#39;,&#39;zzmmdm&#39;,event)\" onfocus=\"this.select()\" value=\"", "\" />", 0);
        if(stu.Student_ZZMM.length()>100){
            stu.Student_ZZMM="";
        }
        stu.Student_ZZMMJRSJ = GetSubText(res.ResponseText, "<input name=\"zzmmsj\" type=\"text\" id=\"zzmmsj\" title=\"标准样式:20090910 可以只到月,不要写年月字样\" size=\"8\" class=\"TD1\" maxlength=\"8\" onkeydown=\"onkeydown1(&#39;zzmmdm&#39;,&#39;zzmmsj&#39;,event)\" onfocus=\"this.select()\" value=\"", "\" />", 0);
        if(stu.Student_ZZMMJRSJ.length()>100){
            stu.Student_ZZMMJRSJ="";
        }
        stu.Student_SYDQ = GetSubText(res.ResponseText, "<input name=\"syszd\" type=\"text\" id=\"syszd\" size=\"30\" class=\"TD1\" maxlength=\"36\" Onchange=\"tchang1(&#39;syszd&#39;)\" onkeydown=\"onkeydown1(&#39;rxsj&#39;,&#39;syszddm&#39;,event)\" onfocus=\"this.select()\" value=\"", "\" />", 0);
        if(stu.Student_SYDQ.length()>100){
            stu.Student_SYDQ="";
        }
        stu.Student_XZ = GetSubText(res.ResponseText, "<input name=\"xz\" type=\"text\" id=\"xz\" size=\"3\" class=\"TD1\" maxlength=\"3\" onkeydown=\"onkeydown1(&#39;syszddm&#39;,&#39;xz&#39;,event)\" onfocus=\"this.select()\" value=\"", "\" />", 0);
        if(stu.Student_XZ.length()>100){
            stu.Student_XZ="";
        }
        stu.Student_JTDZ = GetSubText(res.ResponseText, "<input name=\"jtdz\" type=\"text\" id=\"jtdz\" size=\"30\" class=\"TD1\" maxlength=\"200\" onkeydown=\"onkeydown1(&#39;xxsj&#39;,&#39;jtdz&#39;,event)\" onfocus=\"this.select()\" value=\"", "\" />", 0);
        if(stu.Student_JTDZ.length()>100){
            stu.Student_JTDZ="";
        }
        stu.Student_JTDH = GetSubText(res.ResponseText, "<input name=\"jtdh\" type=\"text\" id=\"jtdh\" size=\"30\" class=\"TD1\" maxlength=\"30\" onkeydown=\"onkeydown1(&#39;jtdz&#39;,&#39;jtdh&#39;,event)\" onfocus=\"this.select()\" value=\"", "\" />", 0);
        if(stu.Student_JTDH.length()>100){
            stu.Student_JTDH="";
        }
        stu.Student_LXR = GetSubText(res.ResponseText, "<input name=\"jtlxr\" type=\"text\" id=\"jtlxr\" size=\"24\" class=\"TD1\" maxlength=\"24\" onkeydown=\"onkeydown1(&#39;jtdh&#39;,&#39;jtlxr&#39;,event)\" onfocus=\"this.select()\" value=\"", "\" />", 0);
        if(stu.Student_LXR.length()>100){
            stu.Student_LXR="";
        }
        stu.Student_JTYB = GetSubText(res.ResponseText, "<input name=\"jtyzbm\" type=\"text\" id=\"jtyzbm\" size=\"6\" class=\"TD1\" maxlength=\"6\" onkeydown=\"onkeydown1(&#39;jtlxr&#39;,&#39;jtyzbm&#39;,event)\" onfocus=\"this.select()\" value=\"", "\" />", 0);
        if(stu.Student_JTYB.length()>100){
            stu.Student_JTYB="";
        }
        stu.Student_XMPY = GetSubText(res.ResponseText, "<input name=\"xmpy\" type=\"text\" id=\"xmpy\" size=\"20\" class=\"TD1\" maxlength=\"20\" onkeydown=\"onkeydown1(&#39;jtyzbm&#39;,&#39;xmpy&#39;,event)\" onfocus=\"this.select()\" value=\"", "\" />", 0);
        if(stu.Student_XMPY.length()>100){
            stu.Student_XMPY="";
        }
        stu.Student_KSH = GetSubText(res.ResponseText, "<input name=\"ksh\" type=\"text\" id=\"ksh\" size=\"16\" class=\"TD1\" maxlength=\"16\" onkeydown=\"onkeydown1(&#39;xmpy&#39;,&#39;ksh&#39;,event)\" onfocus=\"this.select()\" value=\"", "\" />", 0);
        if(stu.Student_KSH.length()>100){
            stu.Student_KSH="";
        }
        stu.Student_BYZX = GetSubText(res.ResponseText, "<input name=\"byzx\" type=\"text\" id=\"byzx\" size=\"30\" class=\"TD1\" maxlength=\"100\" onkeydown=\"onkeydown1(&#39;ksh&#39;,&#39;byzx&#39;,event)\" onfocus=\"this.select()\" value=\"", "\" />", 0);
        if(stu.Student_BYZX.length()>100){
            stu.Student_BYZX="";
        }
        stu.Student_CYM = GetSubText(res.ResponseText, "<input name=\"cym\" type=\"text\" id=\"cym\" size=\"24\" class=\"TD1\" maxlength=\"24\" onkeydown=\"onkeydown1(&#39;zxyzbm&#39;,&#39;cym&#39;,event)\" onfocus=\"this.select()\" value=\"", "\" />", 0);
        if(stu.Student_CYM.length()>100){
            stu.Student_CYM="";
        }
        stu.Student_JG = GetSubText(res.ResponseText, "<input name=\"jg\" type=\"text\" id=\"jg\" title=\"省份+市县（例如：江苏东台）\" size=\"30\" class=\"TD1\" maxlength=\"50\" onkeydown=\"onkeydown1(&#39;cym&#39;,&#39;jg&#39;,event)\" onfocus=\"this.select()\" value=\"", "\" />", 0);
        if(stu.Student_JG.length()>100){
            stu.Student_JG="";
        }
        stu.Student_HKXZ = GetSubText(res.ResponseText, "<input name=\"hkxz\" type=\"text\" id=\"hkxz\" size=\"20\" class=\"TD1\" maxlength=\"20\" Onchange=\"tchang1(&#39;hkxz&#39;)\" onkeydown=\"onkeydown1(&#39;sbkh&#39;,&#39;hkxzdm&#39;,event)\" onfocus=\"this.select()\" value=\"", "\" />", 0);
        if(stu.Student_HKXZ.length()>100){
            stu.Student_HKXZ="";
        }
        stu.Student_Confirm = GetSubText(res.ResponseText, "<input name=\"xsqr\" type=\"checkbox\" id=\"xsqr\" class=\"ck\" value=\"", "\" checked=", 0);
        if (stu.Student_Confirm.equals("ON")) {
            stu.Student_Confirm = "已确认";
        } else {
            stu.Student_Confirm = "未确认";
        }
        return stu;
    }

}
