package socket;

import java.io.IOException;
import java.net.*;

/**
 * Author :  suzeyu
 * Time   :  2016-09-24  下午8:43
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription : 查看本地主机前1024个端口中哪些安装有TCP服务器
 *
 *          在UNIX系统中, 可以查看文件/etc/services 可以查看本机服务有哪些驻留在哪个端口.
 */
public class LowPortScanner {

    public static void  main(String args[]){

        // 循环遍历本地端口 1 到 1024
        for (int i = 1; i < 1024; i++) {
            try {
                Socket localhost = new Socket("localhost", i);
                System.out.println("这个服务在端口: "+ i);
                localhost.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     *  针对Socket的构建, 采用先构造后连接的模式而不是常用的构造就连接
     */
    public void structureNoConnect() throws IOException {

        Socket socket = new Socket();
        // 设置连接的地址
        InetSocketAddress inetSocketAddress = new InetSocketAddress("www.baidu.com", 80);

        // 参数2 为一个可选参数代表连接超时之前的等待时间(毫秒级),
        //      如果为0那么就标示永远的等下去
        socket.connect(inetSocketAddress, 0);


        // 接下来可以使用socket.....

        // 当连接成功以后可以存储连接的目标地址和发起端地址
        // 如果socket尚未连接那么这两个方法都返回null
        // 目标地址
        SocketAddress remoteSocketAddress = socket.getRemoteSocketAddress();
        // 发起地址
        SocketAddress localSocketAddress = socket.getLocalSocketAddress();

        // 虽然返回的SocketAddress 但是本质其实是InetSocketAddress. 所以可以获取一些数据
        InetSocketAddress remoteAddress = (InetSocketAddress) remoteSocketAddress;
        InetAddress address = remoteAddress.getAddress();
        String hostName = remoteAddress.getHostName();
        int port = remoteAddress.getPort();
    }

    /**
     *  构造函数可以通过传入一个 代理类Proxy, 来构建一个未连接的Socket.
     *    默认情况下: Socket使用的代理服务器由socketProxyHost和socketProxyPort系统属性控制,
     *    这些属性应用于系统中的所有socket. 但是这个构造函数创建的socket会使用指定的代理服务器.
     *
     *    当然可以为参数传入 Proxy.NO_PROXY, 完全绕过所有代理服务器, 而直接连接远程主机. 当然防火墙
     *    禁止直接连接, Java将无能无力, 连接就会失败
     *
     */
    public void structureProxy() throws IOException {

        // 要使用某个特定的代理服务器, 可以指定其地址. 如下使用一个位于myproxy.example.com的
        // Socket代理服务器来连接主机login.ibiblio.org (此例子可能无法跑通, 只是说明使用代理的步骤)
        InetSocketAddress proxyAdress = new InetSocketAddress("myproxy.example.com", 1888);
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, proxyAdress);

        Socket socket = new Socket(proxy);
        socket.connect(new InetSocketAddress("login.ibiblio.org", 99));
    }
}
