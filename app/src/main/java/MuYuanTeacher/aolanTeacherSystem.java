package MuYuanTeacher;

import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cookie.store.MemoryCookieStore;
import com.lzy.okgo.cookie.store.PersistentCookieStore;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.Response;

/**
 * This Code Is Created by cheny on 2017/4/26 23:55.
 */

public class aolanTeacherSystem {
    public String _viewstate = "";
    public String _viewStategenerator = "";
    private PersistentCookieStore CookieStore = new PersistentCookieStore();
    Context mContext = null;

    public void aolanTeacherSystem()
    {
        OkGo.getInstance()
                //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                //可以全局统一设置超时重连次数,默认为三次,那么最差的情况会请求4次(一次原始请求,三次重连请求),不需要可以设置为0
                .setRetryCount(3)
                //如果不想让框架管理cookie（或者叫session的保持）,以下不需要
                .setCookieStore(CookieStore)        //cookie持久化存储，如果cookie不过期，则一直有效
                //可以设置https的证书,以下几种方案根据需要自己设置
                .setCertificates();
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
    public String GetSubText(String AllString, String left, String Right, int StartIndex) {
        int index = AllString.indexOf(left, StartIndex) + left.length();
        return AllString.substring(index, AllString.indexOf(Right, index));
    }

    /**
     * 更新必须参数
     *
     * @param ResponseBody 返回的网页数据
     */
    public void UpdataViewState(String ResponseBody) {
        _viewstate = GetSubText(ResponseBody, "id=\"__VIEWSTATE\" value=\"", "\"", 0);
        _viewStategenerator = GetSubText(ResponseBody, "id=\"__VIEWSTATEGENERATOR\" value=\"", "\"", 0);
    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 序列化Cookie为文本型数据
     *
     * @return
     */
    private String getCookies() {
        List<Cookie> Cookielist = CookieStore.getAllCookie();
        String Cook = "";
        for (Cookie m : Cookielist) {
            Cook = Cook + m.name() + "=" + m.value() + ";";
        }
        return Cook;
    }

    public void mLoginSystem(String UserID, String Pass) throws IOException {
        String url = "http://xgsl.jsahvc.edu.cn/login.aspx";
        Response res = OkGo.get(url).execute();
        url = res.body().string();
        UpdataViewState(url);
        //__VIEWSTATE=&__VIEWSTATEGENERATOR=C2EE9ABB&userbh=23333&pass=0BA7BC92FCD57E337EBB9E74308C811F&cw=&xzbz=1
        res = OkGo.post(url).params("__VIEWSTATE", _viewstate).params("__VIEWSTATEGENERATOR", _viewStategenerator).params("userbh", UserID).params("pass", md5(UserID)).params("xzbz", "1").execute();
        return;
    }


}


