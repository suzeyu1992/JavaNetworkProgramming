package udp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * Author :  suzeyu
 * Time   :  2016-10-17  下午10:50
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription : 基于通道的UDP客户端
 */
public class UDPClientChannels {

    public final static int PORT = 9989;
    private final static int LIMIT = 100;
    private static InetSocketAddress remote;

    public static void main(String arg[]){

        try {
            remote = new InetSocketAddress("localhost", PORT);
        }catch (RuntimeException ex){
            System.err.println("Usage err");
            return;
        }

        try (DatagramChannel channel = DatagramChannel.open()){

            channel.configureBlocking(false);
            channel.connect(remote);

            Selector selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

            ByteBuffer buffer = ByteBuffer.allocate(4);
            int n = 0;
            int numbersRead = 0;
            while (true){
                if (numbersRead == LIMIT) break;
                // 为一个连接等待一分钟
                selector.select(60000);
                Set<SelectionKey> readyKeys = selector.selectedKeys();
                if (readyKeys.isEmpty() && n == LIMIT){
                    // 所有包已写入, 看起来
                    // 好像不会再有更多的数据从网络到达
                    break;
                }else {
                    Iterator<SelectionKey> iterator = readyKeys.iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        iterator.remove();

                        if (key.isReadable()){
                            buffer.clear();
                            channel.read(buffer);
                            buffer.flip();
                            int echo = buffer.getInt();
                            System.out.println("Read: "+echo);
                            numbersRead++;
                        }

                        if (key.isWritable()){
                            buffer.clear();
                            buffer.putInt(n);
                            buffer.flip();
                            channel.write(buffer);
                            System.out.println("write: "+n);
                            n++;

                            if (n == LIMIT){
                                // 所有包已写入, 切换到只读模式
                                key.interestOps(SelectionKey.OP_READ);
                            }
                        }
                    }
                }
            }

            System.out.println("Echoed "+numbersRead + " out of "+LIMIT + " sent");
            System.out.println("Success rate: "+100.0 * numbersRead/LIMIT+"%");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
