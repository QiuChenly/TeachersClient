package MuYuanTeacher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by QiuChenly on 2017/4/22.
 */

public class HttpUntils {
    public static String Cookie;

    /**
     * 提交自定义数据到服务器
     *
     * @param url         网址
     * @param Datas       数据,文本
     * @param Cookie      附带的Cookie,有时候服务器会需要你提供这个
     * @param ContentType 协议头
     * @return 返回网页数据
     * @throws IOException IO异常捕捉,请在外部调用Try
     */
    public static ResponseData submitPostData (URL url, String Datas, String Cookie, String ContentType) throws IOException {
        return submitPostData (url, Datas.getBytes (), Cookie, ContentType, false);
    }

    public static ResponseData submitPostData (URL url, String Datas, String Cookie, String ContentType, boolean Direct) throws IOException {
        return submitPostData (url, Datas.getBytes (), Cookie, ContentType, Direct);
    }


    /**
     * 提交自定义数据到服务器
     *
     * @param url         网址
     * @param Datas       数据,s字节
     * @param Cookies     附带的Cookie,有时候服务器会需要你提供这个
     * @param ContentType 协议头
     * @return 返回网页数据
     * @throws IOException IO异常捕捉,请在外部调用Try
     */
    public static ResponseData submitPostData (URL url, byte[] Datas, String Cookies, String ContentType, Boolean Redirct) throws IOException {

        byte[] data = Datas;//获得请求体
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection ();
        httpURLConnection.setConnectTimeout (10000);     //设置连接超时时间
        httpURLConnection.setDoInput (true);                  //打开输入流，以便从服务器获取数据
        httpURLConnection.setDoOutput (true);                 //打开输出流，以便向服务器提交数据
        httpURLConnection.setRequestMethod ("POST");     //设置以Post方式提交数据
        httpURLConnection.setUseCaches (false);               //使用Post方式不能使用缓存
        httpURLConnection.setInstanceFollowRedirects (Redirct);
        //设置请求体的类型是文本类型
        httpURLConnection.setRequestProperty ("Content-Type", ContentType);
        httpURLConnection.setRequestProperty ("Cookie", Cookies);
        //设置请求体的长度
        httpURLConnection.setRequestProperty ("Content-Length", String.valueOf (data.length));
        //获得输出流，向服务器写入数据
        OutputStream outputStream = httpURLConnection.getOutputStream ();
        outputStream.write (data);
        ResponseData res = new ResponseData ();
        int response = 0;
        try {
            response = httpURLConnection.getResponseCode ();            //获得服务器的响应码

            String cookieskey = "Set-Cookie";
            Map<String, List<String>> maps = httpURLConnection.getHeaderFields ();
            List<String> coolist = maps.get (cookieskey);
            if (coolist != null) {
                Iterator<String> it = coolist.iterator ();
                StringBuffer sbu = new StringBuffer ();
                while (it.hasNext ()) {
                    sbu.append (it.next ());
                }
                Cookie = sbu.toString ();
            }

            if (response == HttpURLConnection.HTTP_OK) {

                InputStream inptStream = httpURLConnection.getInputStream ();
                res.ResponseCode = response;
                res.ResponseText = dealResponseResult (inptStream);                     //处理服务器的响应结果
            } else {
                //如果涉及到非200的跳转,这里直接将其抛出
                String urls = httpURLConnection.getHeaderField ("Location");
                res.ResponseCode = response;
                res.RedirctUrl = urls;
                res.ResponseText = "";
            }
        } catch (IOException e) {
            e.printStackTrace ();
        }
        //获得服务器的响应码
        Log.d ("QiuChen", String.valueOf (response));
        return res;
    }

    public static ResponseData POST (
            String urls, String Datas, Map<String, String> RequestHeader, Boolean Redirct
    ) throws IOException {

        byte[] data = Datas.getBytes ();//获得请求体
        URL url = new URL (urls);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection ();
        httpURLConnection.setConnectTimeout (10000);     //设置连接超时时间
        httpURLConnection.setDoInput (true);                  //打开输入流，以便从服务器获取数据
        httpURLConnection.setDoOutput (true);                 //打开输出流，以便向服务器提交数据
        httpURLConnection.setRequestMethod ("POST");     //设置以Post方式提交数据
        httpURLConnection.setUseCaches (false);               //使用Post方式不能使用缓存
        httpURLConnection.setInstanceFollowRedirects (Redirct);

        for (Map.Entry<String, String> vo : RequestHeader.entrySet ()) {
            httpURLConnection.setRequestProperty (vo.getKey (), vo.getValue ());
        }
        //设置请求体的长度
        httpURLConnection.setRequestProperty ("Content-Length", String.valueOf (data.length));
        //获得输出流，向服务器写入数据
        OutputStream outputStream = httpURLConnection.getOutputStream ();
        outputStream.write (data);
        ResponseData res = new ResponseData ();
        int response = 0;
        try {
            response = httpURLConnection.getResponseCode ();            //获得服务器的响应码

            String cookieskey = "Set-Cookie";
            Map<String, List<String>> maps = httpURLConnection.getHeaderFields ();
            List<String> coolist = maps.get (cookieskey);
            if (coolist != null) {
                Iterator<String> it = coolist.iterator ();
                StringBuffer sbu = new StringBuffer ();
                while (it.hasNext ()) {
                    sbu.append (it.next ());
                }
                Cookie = sbu.toString ();
            }

            if (response == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = httpURLConnection.getInputStream ();
                res.ResponseCode = response;
                res.ResponseText = dealResponseResult (inptStream);                     //处理服务器的响应结果
            } else {
                //如果涉及到非200的跳转,这里直接将其抛出
                String urlss = httpURLConnection.getHeaderField ("Location");
                res.ResponseCode = response;
                res.RedirctUrl = urlss;
                res.ResponseText = "";
            }
        } catch (IOException e) {
            e.printStackTrace ();
        }
        //获得服务器的响应码
        Log.d ("QiuChen", String.valueOf (response));
        return res;
    }




    /*
         * Function  :   处理服务器的响应结果（将输入流转化成字符串）
         * Param     :   inputStream服务器的响应输入流
         * Author    :   博客园-依旧淡然
         */

    public static String dealResponseResult (InputStream inputStream) throws UnsupportedEncodingException {
        String resultData;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream ();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read (data)) != - 1) {
                byteArrayOutputStream.write (data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace ();
        }
        resultData = new String (byteArrayOutputStream.toByteArray ());
        return resultData;
    }

    public static String dealResponseResult (InputStream inputStream, String CharSet) throws UnsupportedEncodingException {
        String resultData;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream ();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read (data)) != - 1) {
                byteArrayOutputStream.write (data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace ();
        }
        resultData = new String (byteArrayOutputStream.toByteArray (), CharSet);
        return resultData;
    }

    public static String getURLResponse (String urlString, String Cookies, String Referer) throws IOException {
        return getURLResponse (urlString, Cookies, "UTF-8", Referer);
    }

    public static String getURLResponse (String urlString, String Cookies) throws IOException {
        return getURLResponse (urlString, Cookies, "UTF-8", urlString);
    }

    /**
     * 获取指定URL的响应字符串
     *
     * @param urlString
     * @return
     */
    public static String getURLResponse (String urlString, String CookieEx, String CharSet, String Referer) throws IOException {
        String s = null;
        URL url = new URL (urlString); //URL对象
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection ();
        httpURLConnection.setConnectTimeout (3000);     //设置连接超时时间
        httpURLConnection.setDoInput (true);                  //打开输入流，以便从服务器获取数据
        //httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
        httpURLConnection.setRequestMethod ("GET");     //设置以Post方式提交数据
        httpURLConnection.setUseCaches (false);
        httpURLConnection.setRequestProperty ("Accept", "*/*");
        httpURLConnection.setRequestProperty ("Referer", Referer);
        httpURLConnection.setRequestProperty ("Accept-Language", "zh-cn");
        httpURLConnection.setRequestProperty ("Cookie", CookieEx);
        httpURLConnection.setRequestProperty ("Content-Type", "application/x-www-form-urlencoded");

        int response = httpURLConnection.getResponseCode ();            //获得服务器的响应码
        if (response == HttpURLConnection.HTTP_OK) {
            InputStream inptStream = httpURLConnection.getInputStream ();

            String cookieskey = "Set-Cookie";
            Map<String, List<String>> maps = httpURLConnection.getHeaderFields ();
            List<String> coolist = maps.get (cookieskey);
            if (coolist != null) {
                Iterator<String> it = coolist.iterator ();
                StringBuffer sbu = new StringBuffer ();
                while (it.hasNext ()) {
                    sbu.append (it.next ());
                }
                Cookie = sbu.toString ();
            }

            s = dealResponseResult (inptStream, CharSet);                     //处理服务器的响应结果
        } else {
            s = null;
        }
        return s;
    }

    /**
     * 从指定URL获取图片
     *
     * @param url
     * @return
     */
    public static Bitmap getImageBitmap (String url) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL (url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection ();
            conn.setDoInput (true);
            conn.connect ();
            InputStream is = conn.getInputStream ();
            bitmap = BitmapFactory.decodeStream (is);
            is.close ();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
        return bitmap;
    }
}
