package uri;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Author :  suzeyu
 * Time   :  2016-09-18  下午8:28
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription : 下载一个Web页面的原始HTML代码
 */
public class LoadSourceViewer {

    /**
     *  需要下载HTML代码的地址
     */
    public static String sLoadAddress = "http://www.baidu.com";

    public static void main (String args[]){

        // 练习 URL#openStream()
        //userOpenStream();

        // 练习URL#openConnection()
        userOpenConnection(sLoadAddress);



    }

    /**
     *  使用{@link URL} openConnection()方法练习
     */
    public static void userOpenConnection(String loadStr){
        try {
            // 创建URL并获取流读取内容

            URL url = new URL(sLoadAddress);
            // 通过使用嵌套的 try-with-resources 自动关闭流
            URLConnection in = url.openConnection();
             // 将inputStream串联到一个Reader
             InputStreamReader reader = new InputStreamReader(new BufferedInputStream(in.getInputStream()));


            int c;
            while ((c = reader.read())!= -1){
                System.out.print((char) c);
            }



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 使用{@link URL} openStream()方法下载页面源码
     */
    private static void userOpenStream() {
        try {
            // 创建URL并获取流读取内容

            URL url = new URL(sLoadAddress);
            // 通过使用嵌套的 try-with-resources 自动关闭流
            try (InputStream in = url.openStream();
                 // 将inputStream串联到一个Reader
                 InputStreamReader reader = new InputStreamReader(new BufferedInputStream(in)))
            {

                int c;
                while ((c = reader.read())!= -1){
                    System.out.print((char) c);
                }

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
