package MuYuanTeacher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
    public List<String> getAllClassMate() throws IOException {
        String url = "http://xgsl.jsahvc.edu.cn/student/left_2.aspx";
        String data = "__VIEWSTATE=" + EncodeStr(_viewstate) +
                "&__VIEWSTATEGENERATOR=" + EncodeStr(_viewStategenerator) +
                "&__EVENTTARGET=&__EVENTARGUMENT=&km=sy_jbgr&dcbz=&xqbz=&ndbz=&tindex=&tindex2=&tindex3=&tindex4=&tindex5=&tindex6=&cxtj=&f_km=&nj=&xzbz=&sbbz=&msie=&qx_s=&stbz=";
        ResponseData res = HttpUntils.POST(url, data, new HashMap<String, String>(), false);
        Pattern p = Pattern.compile("style=\"display:inline;font-face:Times;color:black;text-decoration:none;cursor:hand;overflow:hidden;font-size:9pt;font-family:SimSun;word-break:keep-all;white-space:nowrap;\">(.*?)</font>");
        Matcher m = p.matcher(res.ResponseText);
        List<String> list = new ArrayList<>();
        while (m.find()) {
            list.add(m.group(1).replace("&nbsp;", ""));
        }
        return list;
    }
}
