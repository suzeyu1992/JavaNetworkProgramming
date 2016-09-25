package servicesocket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

/**
 * Author :  suzeyu
 * Time   :  2016-09-25  下午1:42
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription : 对构造服务器Socket的函数进行说明
 */
public class StructureServerSocket {

    // 对构造函数进行说明
    private void descriptionStructure() throws IOException {
        // 在端口4444创建一个HTTP服务器使用服务器Socket
        ServerSocket serverSocket = new ServerSocket(4444);


        // 参数二限制了队列一次最多可以保存50个入站连接.
        ServerSocket serverSocket1 = new ServerSocket(4444, 50);

        // 参数三 可以要求 ServerSocket 只监听这个指定地址上的入站连接. 不会监听通过这个主机其他地址进入的连接
        // 如果端口传入0, 这样系统就会为你选择可用的端口. 像这样由系统选择的端口有时称为匿名端口.
        InetAddress local = InetAddress.getByName("192.168.21.22");
        ServerSocket serverSocket2 = new ServerSocket(4444, 50, local);


        // 最后一种就是 first structure last bind port
        ServerSocket serverSocket3 = new ServerSocket();
        serverSocket3.bind(new InetSocketAddress(4444));

    }

    // 服务器socket的相关信息说明
    private void socketMethod(){
        // 关于常用的就是获取ServerSocket获取占用本地地址和端口.
        // getInetAddress()
        // getLocalPort() // 如果没有绑定某个端口 那么就返回-1


        /*
         * Socket选项指定了ServerSocket类所依赖的原生Socket如何发送和接收数据.
         * 对于服务器, Java支持一下3个选项
         * ` SO_TIMEOUT
         * ` SO_REUSEADDR
         * ` SO_RCVBUF
         */
    }
}
