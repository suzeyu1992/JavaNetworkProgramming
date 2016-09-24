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


    /**
     *  此方法只对java 中的socket进行简单说明
     */
    public void socketSettingExplanation(){

        // Socket选项指定了Java Socket类所依赖的原声socket如何发送和接收数据
        // 对于客户端socket, java支持9中选项

        // TCP_NODELAY
        /*
         *  设置此属性为 true可确保会尽可能快地发送, 无论包的大小. 正常情况下, 小数据包(1字节)会在发送前组合为更大的包.
         *  并且在发送一个包之前, 本地主机会等待远程系统对前一个包的确认. 这称为Nagle算法.
         *  而通过setTcpNoDelay(true)会关闭了Socket的缓冲, 这样所有的包一旦就绪那么就立即发送,
         *  当底层Socket实现不支持TCP_NODELAY选项则会抛出异常
         */


        // SO_LINGER
        /*
         *  此属性指定了Socket关闭时如何处理尚未发送的数据报.
         *  默认情况下, close()方法会立即返回, 但系统仍会尝试发送剩余的数据. 如果延迟时间设置为0, 那么当Socket关闭时,
         *  所有为发送的数据包都将会被丢弃. 相反如果时间设置为一个正数, 那么close()方法会阻塞, 阻塞的时间就是设定的时间.
         *  在设定的时间内会继续发送未发送的数据知道设定时间到达.
         */

        // SO_TIMEOUT
        /*
         *  正常情况下, 尝试从Socket读取数据是, read()调用会阻塞尽可能长的事件来得到足够的字节.
         *  不过通过设置此属性可以确保这次调用的事件不会超过某个固定的毫秒数. 当超过会抛出InterruptedIOException,
         *  记住此时Socket仍然是连接的. 虽然此次调用失败, 但是可以再次尝试读取该Socket
         */

        // SO_RCVBUF和SO_SNDBUF
        /*
         *  由于TCP使用缓冲区提升网络性能.
         *  recbuf: 控制用于网络输入的建议的接收缓冲区大小
         *  sndbuf: 控制用于网络输入的建议的发送缓冲区大小
         *
         *  虽然看上去可以单独的对一个socket设置发送和接收的缓冲区, 但实际上缓冲区通常会设置为两者中的最低的一个.
         *  例如如果将发送缓冲区设置为16kb, 而接收缓冲区为64kb. 那么发送和接收缓冲区的大小都见识16kb, java会返回
         *  接收区缓存大小为64kb, 但底层TCP栈实际上会使用16kb
         *
         *  并且这两个方法实际上更偏向于建议设置, 底层的实现可能会忽略我们所设置的值, 因为UNIX和Linux系统通常指定一个
         *  最大缓冲区大小, 一般是64kb或者256kb, 而且不允许有更大的缓冲区. 所以有可能你设置一个很大的值, 但Java可能会
         *  在后台自动帮你设置为所支持的最大值.
         */


        // SO_KEEPALIVE
        /*
         *  如果打开此项, 客户端会偶尔通过一个空闲连接发送一个数据包(一般两个小时一次), 以确保服务器没有崩溃.
         *  如果服务器没有响应这个包, 客户端会持续尝试11分钟多的时间, 知道接收到响应为止, 如果一直没有响应 ,
         *  那么客户端就会关闭socket
         *
         *  默认是关闭的, 这样可能会导致不活动的客户端可能会永远存在下去
         */

        // OOBINLINE
        /*
         * TCP包括一个可以发送单字节带外(Out Of Band, OOB)"紧急"数据的特性.
         * 这个数据会立即发送. 当接收方收到紧急数据时会得到通知, 在处理其他已收到的数据之前可以
         * 选择先处理这个紧急数据. Java支持这种发送和接收这种紧急数据
         * setUrgentData()发送 -- 客户端
         * setOOBInline(true) -- 服务端开启接收紧急数据
         */

        // SO_REUSEADDR
        /*
         * = =放弃说明
         */

        // IP_TOS服务类型
        /*
         * 由于不同类型的Internet服务有不同的特性需求, 例如视频需要较高的带宽和较短的延迟, 而电子邮件可以通过
         * 低带宽的连接传递.
         *
         * 服务类型存储在 IP首部中一个名为IP_TOS的8位字段. java通过setTrafficClass()和get来设置/取得这个值
         *
         * 在TCP栈中, 这个8位字节的高6位包含一个差分服务代码点值; 低2位包含一个显示拥塞通知值.
         */

    }


}
