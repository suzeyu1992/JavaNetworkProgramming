package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Author :  suzeyu
 * Time   :  2016-10-12  下午11:07
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :  一个非阻塞的chargen服务器
 */
public class ChargenServer {

    public static void main(String args[]) {

        byte[] rotation = new byte[95 * 2];
        for (byte i = ' '; i < '~'; i++) {
            rotation[i - ' '] = i;
            rotation[i + 95 - ' '] = i;
        }

        ServerSocketChannel serverChannel;
        Selector selector;

        try {
            // 服务器的通道初始化创建
            serverChannel = ServerSocketChannel.open();
            ServerSocket ss = serverChannel.socket();

            InetSocketAddress address = new InetSocketAddress(19); //对于UNIX和LINUX如果非root权限, 只能打开1024以上的端口
            ss.bind(address);
            serverChannel.configureBlocking(false);

            selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            try {
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }

            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();

            while (iterator.hasNext()) {

                SelectionKey key = iterator.next();
                iterator.remove();

                try {
                    if (key.isAcceptable()) {

                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        System.out.println("Accepted connection from " + client);
                        client.configureBlocking(false);
                        SelectionKey key2 = client.register(selector, SelectionKey.OP_WRITE);

                        ByteBuffer buffer = ByteBuffer.allocate(74);
                        buffer.put(rotation, 0, 72);
                        buffer.put((byte) '\r').put((byte) '\n');
                        buffer.flip();

                        key2.attach(buffer);

                    } else if (key.isWritable()) {

                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();

                        if (!buffer.hasRemaining()) {
                            //  用下一行重新填充缓冲区
                            buffer.rewind();
                            // 得到上一次的首字符
                            byte first = buffer.get();
                            // 准备改变缓冲区中的数据
                            buffer.rewind();
                            // 寻找rotation中新的首字符位置
                            int position = first - ' ' + 1;
                            // 将数据从rotation复制到缓冲区
                            buffer.put(rotation, position, 72);
                            // 在缓冲区末尾存储一个行分隔符
                            buffer.put((byte) '\r').put((byte) '\n');
                            // 准备缓冲区进行写入
                            buffer.flip();

                        }

                        client.write(buffer);
                    }
                } catch (IOException ex) {
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException cex) {
                    }
                }
            }


        }

    }
}
