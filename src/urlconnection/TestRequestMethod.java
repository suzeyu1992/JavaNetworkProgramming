package urlconnection;

import javax.xml.ws.spi.http.HttpContext;
import java.io.*;
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
     * 设置 Options 请求
     * 通常这个请求会询问某个指向的URL支持哪些选项, 如果请求URL是星号, 那么这个请求
     * 将应用整个服务器而不是服务器上的某个URL
     *
     * 设置 TRACE 请求
     * TRACE请求方法会发送HTTP首部, 服务器将从客户端接收这个HTTP首部, 之所以需要这样,
     * 主要原因是要查看服务器和客户端之前的代理服务器做了哪些修改. 因为服务器的响应首先是
     * 和其他请求方式一样的HTTP的响应头, 结束之后会空一行, 然后对客户端的请求头进行回显输出,
     * 如果这是存在代理服务器, 那么会显得就是代理服务器, 可以和原始发送的请求头进行对比
     */

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
