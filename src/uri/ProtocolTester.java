package uri;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Author :  suzeyu
 * Time   :  2016-09-18  下午7:33
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription :   输出一个虚拟机支持哪些协议, 根据构建函数是否抛出异常来得出结果
 */
public class ProtocolTester {

    public static void main(String args[]){
        // 超文本传输协议
        testProtocol("http://www.qq.com");

        // 安全http
        testProtocol("https://www.qq.com");

        // 文件传出协议
        testProtocol("ftp://ibiblio.org/pub/languages/java/javafaq");

        // 邮件传输协议
        testProtocol("mailto:elharo@ibibilio.org");

        // telnet
        testProtocol("telnet://dibner.poly.edu/");

        // 本地文件访问
        testProtocol("file:///etc/test.txt");

        // gopher
        testProtocol("gopher://gopher.anc.org.za/");

        // 轻量组目录访问协议
        testProtocol("ldap://ldap.itd.umich.edu/...");

        // JAR
        testProtocol("jar:http://test.class");

        // NFS, 网络文件系统
        testProtocol("nfs://utopia.poly.edu/usr/tmp/");

        // JDBC的定制协议
        testProtocol("jdbc:mysql://luna.ibiblio.org:3306/news");

        // rmi, 远程方法调用的定制协议
        testProtocol("rmi://ibiblio.org/RenderEngine");

        // HotJava的定制协议
        testProtocol("doc://UsersGuide/release.html");
        testProtocol("netdoc://UsersGuide/release.html");
        testProtocol("systemresource://www.abc.org/+/index.html");
        testProtocol("verbatim:http://www.adc.org/");

    }



    /**
     *  编写一个测试方法
     */
    private static void testProtocol(String url){
        try {
            URL genUrl = new URL(url);
            System.out.println(genUrl.getProtocol()+"  is supported");
        } catch (MalformedURLException e) {
            // 构建失败, 截取出字符串中的协议, 并输出显示
            String protocol = url.substring(0, url.indexOf(':'));
            System.out.println(protocol+"  is not supported!!");
        }
    }
}


