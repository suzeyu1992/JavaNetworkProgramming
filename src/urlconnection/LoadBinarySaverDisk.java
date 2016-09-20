package urlconnection;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Author :  suzeyu
 * Time   :  2016-09-20  下午11:39
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * <p>
 * ClassDescription : 下载二进制文件并保存到磁盘
 */
public class LoadBinarySaverDisk {

    public static String loadUrl = "http://d.hiphotos.baidu.com/image/pic/item/562c11dfa9ec8a13f075f10cf303918fa1ecc0eb.jpg";

    public static void main(String args[]) {
        try {
            URL url = new URL(loadUrl);
            saveBinary(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收一个URL对象, 此URL只能为下载返回文件类型, 并可以保存为本地文件
     */
    private static void saveBinary(URL url) throws IOException {

        // 首先获取响应体的数据类型和长度
        URLConnection uc = url.openConnection();
        String contentType = uc.getContentType();
        int contentLength = uc.getContentLength();

        // 如果数据类型不是二进制或者没有响应体那么就抛出异常
        if (contentType.startsWith("text/") || contentLength == -1) {
            throw new IOException("这不是一个二进制文件文件");
        }


        try (InputStream raw = uc.getInputStream()) {

            // 开始读取返回数据
            BufferedInputStream in = new BufferedInputStream(raw);
            byte[] data = new byte[contentLength];
            int offset = 0;

            while (offset < contentLength) {
                int bytesRead = in.read(data, offset, data.length - offset);
                if (bytesRead == -1) {
                    break;
                }
                offset += bytesRead;

            }

            if (offset != contentLength) {
                throw new IOException("只读到了" + offset + " 字节; 总共字节为:" + contentLength);
            }

            // 写入本地磁盘
            String fileName = url.getFile();
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);

            try (FileOutputStream fout = new FileOutputStream(fileName)) {
                fout.write(data);
                fout.flush();
            }


        }


    }
}
