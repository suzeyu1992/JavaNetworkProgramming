package urlconnection;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

/**
 * Author :  suzeyu
 * Time   :  2016-09-21  下午10:47
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription : 设置ULRConnection#ifModifiedSince. 并打印其值, 然后下载文件并输出
 *                    但只是在最后设置的指定时间内所修改的才会下载并显示.
 *
 *                    注意这里使用的是 http://www.baidu.com/
 *                    有些Web服务器不会考虑ifModifiedSince字段, 这就导致服务器无论文档是否修改过都会发送,
 *                    而百度就是忽略了此属性, 所以这里提供了另外一个网站会匹配字段的.
 */
public class SetModifiedSince {

    public static void main (String arg[]){

        // 用当前时间来创建一个Date
        Date today = new Date();
        long millisecondsPerDay = 24 * 60 * 60 * 1000;

        // 开始创建网络连接
        try {
            URL url = new URL("http://www.elharo.com");     // 此服务器会考虑ifModifiedSince字段
//            URL url = new URL(ConstantFiled.sBaiduUrl);   // 这个服务器不会考虑!!
            URLConnection uc = url.openConnection();

            // 先打印一下 ifModifiedSince 的默认值
            System.out.println(" 默认是ifModifiedSince值: "+new Date(uc.getIfModifiedSince()));

            // 设置自定义的时间, 这里为当前时间的前24个小时的时间点
            uc.setIfModifiedSince(new Date(today.getTime()-millisecondsPerDay).getTime());
            System.out.println(" 修改后的ifModifiedSince值: "+new Date(uc.getIfModifiedSince()));

            // 准备打开连接获取响应流
            try (InputStream in = new BufferedInputStream(uc.getInputStream())){
                Reader reader = new InputStreamReader(in);

                int temp = 0;
                while ((temp = reader.read()) != -1){
                    System.out.print((char) temp);
                }

                System.out.println();

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
