package urlconnection;

import javax.xml.ws.spi.http.HttpContext;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

/**
 * Author :  suzeyu
 * Time   :  2016-09-23  下午10:01
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription : 测试java中除了get和post, 还支持的另外五种类型请求
 *                    head, put, delete, options, trace
 */
public class TestRequestMethod {

    public static void  main (String args[]){
        // 测试head请求
        setHeadRequest();
    }

    /**
     *  设置 HEAD 请求
     *  虽然用 GET请求同样可以得到相同的结果, 不过如果使用get就会通过网络返回整个文件,
     *  但这里我们只关心首部中某一行, 所以这样会更高效
     */
    private static void setHeadRequest(){
        System.out.println("**************开始测试 HEAD请求*******************");
        try {
            URL url = new URL(ConstantFiled.sBaiduUrl);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            // 设置HEAD请求
            http.setRequestMethod("HEAD");
            System.out.println("最后修改时间为: "+new Date(http.getLastModified()));


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
