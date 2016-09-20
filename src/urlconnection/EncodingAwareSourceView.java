package urlconnection;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Author :  suzeyu
 * Time   :  2016-09-20  下午11:25
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription :   用正确的字符集下载一个Web页面
 */
public class EncodingAwareSourceView {

    public static final String sWebURL= "http://www.baidu.com";

    public static void main(String arg[]){

        // 设置默认的编码集
        String encoding = "ISO-8859-1";

        try {
            URL url = new URL(sWebURL);
            URLConnection uc = url.openConnection();

            // 返回相应主体的MIME内容类型
            String contentType = uc.getContentType();

            // 获取首部字段, 查看是否有声明响应体的编码方式
            int encodingStart = contentType.indexOf("charset=");
            if (encodingStart != -1){
                encoding = contentType.substring(encodingStart + 8);
            }

            // 开始对取响应流 并输出
            BufferedInputStream in = new BufferedInputStream(uc.getInputStream());
            InputStreamReader reader = new InputStreamReader(in, encoding);

            int temp;
            while ((temp = reader.read()) != -1){
                System.out.print((char) temp);
            }



        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();

        }
    }
}
