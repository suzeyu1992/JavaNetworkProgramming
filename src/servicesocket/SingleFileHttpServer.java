package servicesocket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
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
 * ClassDescription : 对本地的某个文件提供服务
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
        String head = "HTTP/1.0 200 OK\r\n" +
                "Server: oneFile 2.0\r\n" +
                "Content-lenght: " + this.content.length + "\r\n" +
                "Content-type:" + mimeType + "; charset=" + encoding + "\r\n\r\n";
        this.header = head.getBytes();
    }

    public void start(){
        ExecutorService pool = Executors.newFixedThreadPool(100);
        try (ServerSocket server = new ServerSocket(this.port)){

            logger.info("Accepting connection on port "+ server.getLocalPort());
            logger.info("Data to be send:");
            logger.info(new String(this.content, encoding));

            while (true){
                try {
                    Socket accept = server.accept();
                    pool.submit(new HTTPHandler(accept));
                }catch (IOException e){
                    logger.log(Level.WARNING, "accept Exception");
                }catch (RuntimeException e){
                    logger.log(Level.WARNING, "未知错误", e);
                }

            }

        }catch (IOException e){
            logger.log(Level.SEVERE, "不能开启远程服务");
        }
    }


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
                while (true) {
                    int read = in.read();
                    if (read == '\r' || read == '\n' || read == -1) break;
                    requestSb.append(read);
                }

                // 如果是HTTP/1.0或以后的版本, 则发送一个MIME首部
                if (requestSb.toString().indexOf("HTTP/") != -1) {
                    out.write(header);
                }

                out.write(content);
                out.flush();
            }catch (IOException e){

                logger.log(Level.WARNING, "写入到客户端错误!!");
            }finally {
                connection.close();
            }


            return null;
        }
    }

    public static void main(String args[]){
        // 设置开启服务要传递的参数
        int port = 25555;
        String encoding = "utf-8";
        Path path = Paths.get("test.txt");
        String contentType = "text/html";

        try {
            byte[] bytes = Files.readAllBytes(path);
            SingleFileHttpServer singleFileHttpServer = new SingleFileHttpServer(bytes, encoding, contentType, port);
            singleFileHttpServer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
