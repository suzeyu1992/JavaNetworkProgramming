package servicesocket;

import javafx.util.Callback;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author :  suzeyu
 * Time   :  2016-09-25  上午1:20
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription :  使用线程池来创建一个本地socket服务并监听9999端口,
 *                      当有socket请求进来的时候返回一个当前时间
 */
public class PooledDaytiemServer {

    /**
     *  定义监听的端口个
     */
    private final static int PORT = 9999;

    public static void main(String args[]){

        // 创建一个固定数量的线程池Executor
        ExecutorService pool = Executors.newFixedThreadPool(50);

        // 开始创建ServerSocket并监听
        try (ServerSocket server = new ServerSocket(PORT)){

            while (true){
                // 为了捕获客户端可能发生的异常
                try {
                    Socket connection = server.accept();
                    DaytimeTask task = new DaytimeTask(connection);
                    pool.submit(task);
                }catch (IOException ex){
                    System.err.println("客户端发生了异常");
                }

            }

        } catch (IOException e) {
            System.err.println("Couldn't start server");
        }
    }

    /**
     *  派生一个Callable的子类, 用来替换Thread的子类, 并且不再使用新建线程,
     *  而是会把这些callable提交给executor服务
     */
    private static class DaytimeTask implements Callable<Void>{

        private final Socket connection;

        public DaytimeTask(Socket connection){
            this.connection = connection;
        }


        @Override
        public Void call()  {

            try {
                OutputStreamWriter write = new OutputStreamWriter(connection.getOutputStream());
                Date date = new Date();

                write.write(date.toString()+"\r\n");
                write.write("我真棒, 嘻嘻嘻!\r\n");
                write.flush();
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }
}
