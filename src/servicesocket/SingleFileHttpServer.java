package servicesocket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Author :  suzeyu
 * Time   :  2016-09-26  下午11:09
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * <p>
 * ClassDescription : 对本地的某个文件提供访问返回数据的服务
 */
public class SingleFileHttpServer {

    private final byte[] content;
    private final int port;
    private final String encoding;
    private final byte[] header;

    private static final Logger logger = Logger.getLogger(SingleFileHttpServer.class.getSimpleName());

    public SingleFileHttpServer(byte[] data, String encoding, String mimeType, int port) {
        this.content = data;
        this.port = port;
        this.encoding = encoding;

        // 创建一个当客户端访问时, 返回的响应头信息
        String head = "HTTP/1.0 200 OK\r\n" +
                "Server: oneFile 2.0\r\n" +
                "Content-lenght: " + this.content.length + "\r\n" +
                "Content-type:" + mimeType + "; charset=" + encoding + "\r\n\r\n";
        this.header = head.getBytes();
    }

    public void start() {
        ExecutorService pool = Executors.newFixedThreadPool(100);
        try (ServerSocket server = new ServerSocket(this.port)) {

            logger.info("Accepting connection on port " + server.getLocalPort());
            logger.info("Data to be send:");
            logger.info(new String(this.content, encoding));

            while (true) {
                try {
                    Socket accept = server.accept();
                    pool.submit(new HTTPHandler(accept));
                } catch (IOException e) {
                    logger.log(Level.WARNING, "accept Exception");
                } catch (RuntimeException e) {
                    logger.log(Level.WARNING, "未知错误", e);
                }

            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "不能开启远程服务");
        }
    }


    /**
     * 利用线程池Executors来创建线程, 防止因为某一个客户端的运行缓慢而导致其他客户端的饥饿状态
     */
    private class HTTPHandler implements Callable<Void> {

        private final Socket connection;

        HTTPHandler(Socket connection) {
            this.connection = connection;
        }

        @Override
        public Void call() throws IOException {

            try {

                BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream());
                BufferedInputStream in = new BufferedInputStream(connection.getInputStream());

                // 只读取一行即可, 因为这是需要的全部内容
                StringBuilder requestSb = new StringBuilder(80);

                // 读写客户端发送的数据 只读取一行
                // 例如 HTTP/1.0 GET 
                while (true) {
                    int read = in.read();
                    if (read == '\r' || read == '\n' || read == -1) break;
                    requestSb.append((char) read);
                }

                // 模拟场景如果是HTTP/1.0或以后的版本, 则发送一个MIME首部
                if (requestSb.toString().indexOf("HTTP/") != -1) {
                    out.write(header);
                }
                // 发送内容并关闭
                out.write(content);
                out.flush();
            } catch (IOException e) {

                logger.log(Level.WARNING, "写入到客户端错误!!");
            } finally {
                connection.close();
            }


            return null;
        }
    }

    public static void main(String args[]) {
        try {
            // 设置开启服务要传递的参数
            int port = 6767;
            String encoding = "UTF-8";
            String contentType = "text/html";

            // 使用Java7 引入的Path和Files类 可直接将内容以字节数组返回
            Path path = Paths.get("/Users/suzeyu/Documents/workspace/notes/JavaNetworkProgramming/out/production/JavaNetworkProgramming/servicesocket/test.txt");
            byte[] bytes = Files.readAllBytes(path);

            SingleFileHttpServer singleFileHttpServer = new SingleFileHttpServer(bytes, encoding, contentType, port);
            singleFileHttpServer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
