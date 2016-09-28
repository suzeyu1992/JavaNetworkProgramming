package secure;

import sun.net.www.protocol.https.DefaultHostnameVerifier;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Author :  suzeyu
 * Time   :  2016-09-28  下午8:38
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription : 一个可以连接安全HTTP服务器, 发送简单的GET请求的简单程序
 */
public class HTTPSClient {

    private static final String DEFAULT_HOST = "www.usps.com";
    private static final int DEFAULT_PORT = 443;     //默认 https端口


    public static void main(String arg[]){

        // 获取 secure socket的构建工厂类
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket socket = null;

        try {

            socket = (SSLSocket) factory.createSocket(DEFAULT_HOST, DEFAULT_PORT);

            // 启用所有密码组
            String[] supported = socket.getSupportedCipherSuites();
            socket.setEnabledCipherSuites(supported);

            // 获得写入流, 进行数据写入并发送
            OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
            // https在get行中需要完全URL
            out.write("GET http://" + DEFAULT_HOST + "/ HTTP/1.1\r\n");
            out.write("Host: "+ DEFAULT_HOST + "\r\n");
            out.write("\r\n");
            out.flush();

            // 读取响应
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 读取首部
            String s;
            while (!(s = in.readLine()).equals("")){
                System.out.println(s);
            }
            System.out.println();

            // 读取长度
            String contentLength = in.readLine();
            int length = Integer.MAX_VALUE;
            try {
                length = Integer.parseInt(contentLength.trim(), 16);
            }catch (NumberFormatException ex){
                // 这个服务器在响应体的第一行没有发送content-length
            }
            System.out.println(contentLength);

            int c;
            int i = 0;  // 用于标记当前读到的内容在全部内容的位置, 作为判断条件不允许超出响应体中的内容大小
            while ((c = in.read())!=-1 && i++ < length){
                System.out.print((char)c);
            }

            System.out.println();



        }catch (IOException ex){
            System.err.println(ex);
        }finally {
            if (socket != null) try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
