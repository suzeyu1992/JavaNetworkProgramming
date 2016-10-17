package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Date;

/**
 * Author :  suzeyu
 * Time   :  2016-10-17  下午10:05
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription : 时间协议服务器
 */
public class TimeUDPServer {

    private final static int PORT = 9989;

    public static void main(String arg[]){

        try (DatagramSocket socket = new DatagramSocket(PORT)){

            while (true){
                // 可以防止一个入站链接发生错误的时候可以继续处理其余的入站请求
                try {
                    DatagramPacket request = new DatagramPacket(new byte[1024], 1024);
                    socket.receive(request);

                    String datetime = new Date().toString();
                    byte[] data = datetime.getBytes("US-ASCII");

                    DatagramPacket response = new DatagramPacket(data, data.length, request.getAddress(), request.getPort());
                    socket.send(response);

                    System.out.println("服务端发送数据成功: 发送地址为--"+request.getAddress());
                }catch (IOException | RuntimeException ex){
                    System.err.println("入站连接发生错误 ");
                }

            }


        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
