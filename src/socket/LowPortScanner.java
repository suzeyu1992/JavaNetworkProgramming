package socket;

import java.io.IOException;
import java.net.Socket;

/**
 * Author :  suzeyu
 * Time   :  2016-09-24  下午8:43
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription : 查看本地主机前1024个端口中哪些安装有TCP服务器
 */
public class LowPortScanner {

    public static void  main(String args[]){

        // 循环遍历本地端口 1 到 1024
        for (int i = 1; i < 1024; i++) {
            try {
                Socket localhost = new Socket("localhost", i);
                System.out.println("这个服务在端口: "+ i);
                localhost.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
