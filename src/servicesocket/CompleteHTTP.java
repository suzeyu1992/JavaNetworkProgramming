package servicesocket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Author :  suzeyu
 * Time   :  2016-09-27  下午8:36
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription : 练习HTTP重定向
 */
public class CompleteHTTP {
    private static final Logger mLogger = Logger.getLogger(CompleteHTTP.class.getSimpleName());

    private static final int NUM_THREADS = 50;
    private static final String INDEX_FILE = "index.html";

    private final File rootDirectory;
    private final int port;


    public CompleteHTTP(File rootDirectory, int port) throws IOException {

        // 首先进行参数校验
        if (! rootDirectory.isDirectory()){
            throw new IOException(rootDirectory + " 这不是一个文件夹路径");
        }

        this.rootDirectory = rootDirectory;
        this.port = port;
    }


    /**
     *  开启服务进行对应站点监听的方法
     */
    public void start() throws IOException{
        ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);

        try (ServerSocket server = new ServerSocket(port)){
            mLogger.info("Accepting connections on port "+ server.getLocalPort());
            mLogger.info("Document Root: "+rootDirectory);

            while (true){
                try {
                    // 循环监听每一个客户端的入站请求
                    Socket request = server.accept();
                    CompleteRequestProcess runnable = new CompleteRequestProcess(rootDirectory, INDEX_FILE, request);
                    pool.submit(runnable);

                }catch (IOException e){
                    mLogger.log(Level.WARNING, "Error accepting connection", e);
                }
            }
        }
    }


    public static void main (String args[]){
        File docroot = new File("/Users/suzeyu/Desktop/");
        int port = 6768;

        try {
            CompleteHTTP webServer = new CompleteHTTP(docroot, port);
            webServer.start();
        } catch (IOException e) {
            mLogger.log(Level.SEVERE, "服务器不能打开", e);
        }
    }



}
