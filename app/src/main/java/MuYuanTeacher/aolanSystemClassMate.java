package MuYuanTeacher;

import android.graphics.Bitmap;

import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            data = "__VIEWSTATE="+EncodeStr(logininfo._viewstate)+"&__VIEWSTATEGENERATOR="+ EncodeStr(logininfo._viewStategenerator) +"&__EVENTTARGET=tree1&__EVENTARGUMENT=onselectedindexchange%2C0%2C0.0%3Bonexpand%2C0.0&km=sy_jbgr&dcbz=&xqbz=&ndbz=&tindex=&tindex2=&tindex3=&tindex4=&tindex5=&tindex6=&cxtj=&f_km=&nj=&xzbz=&sbbz=&msie=&qx_s=&stbz=";
            ResponseData res = HttpUntils.POST(new URL(url), data, HttpUntils.Cookie, "application/x-www-form-urlencoded");
            UpdataViewState(res.ResponseText);
            Pattern p = Pattern.compile("onclick\\(&quot;xdm=&#39;(.*?)&#39;&quot;,&quot;(.*?)&quot;\\)");
            Matcher m = p.matcher(res.ResponseText);
            while (m.find()) {
                list.add(m.group(2)+"|"+m.group(1));
            }
        } else {
            UpdataViewState(HttpUntils.Get(url, HttpUntils.Cookie));
            data = "__VIEWSTATE=" + EncodeStr(logininfo._viewstate) +
                    "&__VIEWSTATEGENERATOR=" + EncodeStr(logininfo._viewStategenerator) +
                    "&__EVENTTARGET=&__EVENTARGUMENT=&km=sy_jbgr&dcbz=&xqbz=&ndbz=&tindex=&tindex2=&tindex3=&tindex4=&tindex5=&tindex6=&cxtj=&f_km=&nj=&xzbz=&sbbz=&msie=&qx_s=&stbz=";
            ResponseData res = HttpUntils.POST(new URL(url), data, HttpUntils.Cookie, "application/x-www-form-urlencoded");
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
     * @param nd      该学生所在年级
     * @param CardsId 该学生所在身份证ID
     * @return 返回图片
     */
    public Bitmap getThisStudentCardIDPic(String nd, String CardsId) {
        String u = "http://xgsl.jsahvc.edu.cn/student/tpxs.aspx?id=/" + nd + "/" + CardsId + ".jpg";
        return HttpUntils.getImageBitmap(u);
    }

    public void getClassMate_CLASS() {


    }

}
