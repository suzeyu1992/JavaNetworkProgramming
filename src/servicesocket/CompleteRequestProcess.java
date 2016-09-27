package servicesocket;

import java.io.*;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Author :  suzeyu
 * Time   :  2016-09-27  下午11:07
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription : 通过内部的线程池来接收 Socket并进行客户端的请求处理和响应返回逻辑
 *                    主要逻辑复写在 Runnable#run()中
 */
public class CompleteRequestProcess implements Runnable{

    private final static Logger mLogger = Logger.getLogger(CompleteRequestProcess.class.getSimpleName());

    /**
     *  要处理的根目录文件夹
     */
    private File mRootDirectory;

    private String indexFileName = "index.html";

    /**
     *  服务端和某一个客户端连接的 socket流
     */
    private Socket mConnection;

    public CompleteRequestProcess(File rootDirectory, String indexFileName, Socket connection){

        // 首先对传入参数进行验证
        if (rootDirectory.isFile()){
            throw new IllegalArgumentException("rootDirectory must be a directory, not a file");
        }

        try {
            rootDirectory = rootDirectory.getCanonicalFile();
        }catch (IOException e){}

        mRootDirectory = rootDirectory;

        if (indexFileName != null) this.indexFileName = indexFileName;

        mConnection = connection;

    }


    @Override
    public void run() {
        // 进行安全检查
        String root = mRootDirectory.getPath();
        try {

            // 获得连接的 socket的输入输出流
            BufferedOutputStream raw = new BufferedOutputStream(mConnection.getOutputStream());
            OutputStreamWriter out = new OutputStreamWriter(raw);
            InputStreamReader in = new InputStreamReader(new BufferedInputStream(mConnection.getInputStream()));

            // 创建字符串缓冲池 来存储客户端传入的数据
            StringBuilder sb = new StringBuilder();
            while (true){
                int read = in.read();
                if (read == '\r' || read == '\n' || read == -1) break;
                sb.append((char) read);
            }

            // 先输出客户端的第一行进行一些比对处理
            String requestLineStr = sb.toString();
            mLogger.info("远程连接的的地址为: "+mConnection.getRemoteSocketAddress() +"  客户端第一行请求的数据"+requestLineStr);

            String[] tokens = requestLineStr.split("\\s+");
            String method = tokens[0];
            String version = "";
            if (method.equals("GET")){
                String fileName = tokens[1];
                if (fileName.endsWith("/")) fileName += indexFileName;

                // 通过内部方法传入文件, 通过文件扩展名映射为MIME类型
                String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName);
                if (tokens.length > 2) version = tokens[2];

                File theFile = new File(mRootDirectory, fileName.substring(1, fileName.length()));

                if (theFile.canRead()
                        // 判断文件路径是否和根目录开头一致 防止客户端访问超出文档根之外的文件
                        && theFile.getCanonicalPath().startsWith(root)){

                    // 使用Java 7 提供的Files类直接将文件内容以二进制的格式返回
                    byte[] theData = Files.readAllBytes(theFile.toPath());
                    if (version.startsWith("HTTP/")){
                        // 发送一个MIME首部
                        sendHeader(out, "HTTP/1.0 200 OK", contentType, theData.length);
                    }
                    // 开始发送文件, 文件可能是图片或者二进制, 所以要使用底层的输入流
                    raw.write(theData);
                    raw.flush();
                }else {
                    // 无法找到文件的场景
                    StringBuilder body = new StringBuilder("<html lang=\"en\">\n" +
                            "<head>\n" +
                            "    <meta charset=\"UTF-8\">\n" +
                            "    <title>Title</title>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "404, File not Found!\n" +
                            "</body>\n" +
                            "</html>");

                    String bodyStr = body.toString();

                    // 判读是否需要发送MIME首部
                    if (version.startsWith("HTTP/")){
                        sendHeader(out, "HTTP/1.0 404 File not found", "text/html", body.length());
                    }

                    out.write(bodyStr);
                    out.flush();

                }
            }else {
                // 此种场景访问方式不是Get请求
                // 无法找到文件的场景
                StringBuilder body = new StringBuilder("<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>Title</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "501: request method not implemented !\n" +
                        "</body>\n" +
                        "</html>");

                String bodyStr = body.toString();

                // 判读是否需要发送MIME首部
                if (version.startsWith("HTTP/")){
                    sendHeader(out, "HTTP/1.0 404 File not found", "text/html", body.length());
                }

                out.write(bodyStr);
                out.flush();
            }


        }catch (IOException e){
            mLogger.log(Level.WARNING, "Error talking to "+mConnection.getRemoteSocketAddress(), e);
        }finally {
            try {
                mConnection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  接收响应头部字段的值, 对socket返回的流进行响应头的写入
     */
    private void sendHeader(OutputStreamWriter out, String responseCode, String contentType, int length) throws IOException {

        out.write(responseCode+"\r\n");
        out.write("Data: " + new Date() + "\r\n");
        out.write("Server: JHTTP 2.0\r\n");
        out.write("Content-length: " + length + "\r\n");
        out.write("Content-type: "+ contentType + "\r\n");
        out.flush();
    }
}
