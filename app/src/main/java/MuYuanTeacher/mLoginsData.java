package MuYuanTeacher;

import java.util.List;
import java.util.Map;

/**
 * This Code Is Created by cheny on 2017/4/27 22:57.
 */

public class mLoginsData {
    //m_开头的参数名都是奥兰公司的人擅自定义的 一点语法都不讲  -_-!
    public String mName;
    public String m_YXDM;//院系代码
    public String m_zxrs;////当前在线人数
    public String m_xzbz;//同上
    public String m_bh;//目测是编号
    public String m_fip;//IP地址
    public List<Map<String,String>> LeavesPerson;
    public ObjectPersonInfo Holidays_StudentInfo;
    public  List<Map<String, String>> News;
    public  List<Map<String, String>> News_WorkPaln;
}
