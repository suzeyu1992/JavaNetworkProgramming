package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Author :  suzeyu
 * Time   :  2016-10-12  下午10:22
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :  一个基于通道的chargen客户端
 */
public class ChargenClient {

    public static int DEFAULT_PORT = 19;

    public static void main(String args[]){

        try {
            InetSocketAddress address = new InetSocketAddress("rama.poly.edu", DEFAULT_PORT);
            SocketChannel client = SocketChannel.open(address);

            // 使用通道支持的数据载体
            ByteBuffer buffer = ByteBuffer.allocate(74);
            WritableByteChannel out = Channels.newChannel(System.out);

            while ( client.read(buffer) != -1){
                buffer.flip();       // 缓冲区回绕起始读取
                out.write(buffer);
                buffer.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
