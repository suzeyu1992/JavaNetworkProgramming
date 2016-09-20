package urlconnection;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Author :  suzeyu
 * Time   :  2016-09-21  上午12:29
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription :   利用获取响应头的键和值的方法获取整个响应头信息
 */
public class GetAllHeadersInfo {

    public static void main (String arg[]){

        try {
            URL url = new URL(ConstantFiled.sBaiduUrl);
            URLConnection uc = url.openConnection();

            for (int j = 1; ; j++){
                if (j==1){
                    // 打印第一行
                    String headerField = uc.getHeaderField(0);
                    System.out.println(headerField);
                    continue;
                }
                // 从第一个实际的响应头获取值
                String headerField = uc.getHeaderField(j);
                if (headerField == null) break;

                // 输出对应的响应头的键值对形式
                System.out.println(uc.getHeaderFieldKey(j)+ ": "+headerField);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
