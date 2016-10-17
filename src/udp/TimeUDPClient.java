package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Author :  suzeyu
 * Time   :  2016-10-17  下午9:54
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription : 一个daytime协议客户端
 */
public class TimeUDPClient {

    private final static int PORT = 13;
    private static final String HOSTNAME = "time.nist.gov";

    public static void main(String args[]){

        // 传入0可以让系统自动找寻一个本地空闲端口
        try (DatagramSocket socket = new DatagramSocket(0)){

            socket.setSoTimeout(10000);

            // 通过InetAddress配置连接地址
            InetAddress host = InetAddress.getByName(HOSTNAME);
            DatagramPacket request = new DatagramPacket(new byte[1], 1, host, PORT);
            DatagramPacket response = new DatagramPacket(new byte[1024], 1024);

            socket.send(request);
            socket.receive(response);

            String result = new String(response.getData(), 0, response.getLength(), "US-ASCII");
            System.out.println(result);

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

}
