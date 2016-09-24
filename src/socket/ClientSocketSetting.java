package socket;

import java.net.InetAddress;
import java.net.Socket;

/**
 * Author :  suzeyu
 * Time   :  2016-09-24  下午10:01
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription : 针对客户端Socket的对象的一些使用和配置
 */
public class ClientSocketSetting {

    public static void main (String arg[]){

    }

    /**
     *  获取连接的Socket的基本信息
     * @param socket 要获取的连接Socket对象
     */
    private void getSocketInfo(Socket socket){
        // 远程连接地址 和端口
        InetAddress remoteAddress = socket.getInetAddress();
        int remotePort = socket.getPort();

        // 本地发起地址 和端口
        InetAddress localAddress = socket.getLocalAddress();
        int localPort = socket.getLocalPort();

        System.out.println("连接的远程地址: "+remoteAddress
        +"/r/n连接的的远程端口: "+remotePort
        +"/r/n发起连接的本地地址: "+localAddress
        +"/r/n发起连接的本地端口:"+localPort);
    }


    /**
     *  判断连接是否已经关闭
     * @return 返回是否已经关闭
     */
    public boolean isClose(Socket socket){
        // 判断socket主要有两个方法
        // isClose() 但是如果socket从未连接 也会返回false, 所以无法严格的判断
        // isConnected() 指出Socket是否从未连接过一个远程主机(如果为true代表连接过).
        // 所以两个方法可以组合使用判断开启后的具体状态
        return socket.isConnected() && (!socket.isClosed());
    }

}
