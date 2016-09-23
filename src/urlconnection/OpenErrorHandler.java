package urlconnection;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Author :  suzeyu
 * Time   :  2016-09-23  下午10:50
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription : 有时如果打开url.getInputStream失败时候,
 *                    可以调用getErrorStream()展示一个搜索界面
 */
public class OpenErrorHandler {

    public static void main(String arg[]){

        try {
            URL url = new URL("http://www.baidu.com/haha.html");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            try (InputStream buffer = http.getInputStream()){
                // 打印响应流中的内容, 如果发生错误 那么就打开一个默认的错误搜索页面
                printFromStream(buffer);
            } catch (Exception e) {
                // 打开默认的错误搜索页面
                printFromStream(http.getErrorStream());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void printFromStream(InputStream raw) throws IOException{
        try (InputStream buffer = new BufferedInputStream(raw)){
            InputStreamReader reader = new InputStreamReader(buffer);

            int c;
            while ((c = reader.read())!= -1){
                System.out.print((char)c);
            }
        }
    }

}
