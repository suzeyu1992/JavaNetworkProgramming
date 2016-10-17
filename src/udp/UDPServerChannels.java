package udp;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * Author :  suzeyu
 * Time   :  2016-10-17  下午10:41
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription : 基于通道的UDP 非阻塞程序
 */
public class UDPServerChannels {

    public final static int PORT = 9989;
    public final static int MAX_PACKET_SIZE = 65507;

    public static void main(String args[]){

        try {

            DatagramChannel channel = DatagramChannel.open();
            DatagramSocket socket = channel.socket();
            InetSocketAddress address = new InetSocketAddress(PORT);
            socket.bind(address);

            // 使用一个足够大的缓冲区确保可以保存任何UDP包, 并在再次使用前清空, 避免潜在的数据问题
            ByteBuffer buffer = ByteBuffer.allocateDirect(MAX_PACKET_SIZE);
            while (true){
                SocketAddress client = channel.receive(buffer);
                buffer.flip();
                System.out.println(client + " says ");
                while (buffer.hasRemaining()) System.out.println(buffer.get());
                System.out.println();
                buffer.clear();
            }

        }catch (IOException ex){
            System.err.println(ex);
        }

    }

}
