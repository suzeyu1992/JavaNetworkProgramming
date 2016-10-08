package servicesocket;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Arrays;

/**
 * Author :  suzeyu
 * Time   :  2016-10-08  下午10:25
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription : 由于SSLServerSocketFactory构建的服务socket只支持服务器认证. 不支持加密,
 *                    所以需要更多的初始化和设置,   这里主要展示这个过程.
 */
public class SecureOrderTaker {

    public final static int PORT = 7000;
    public final static String algorithm = "SSL";

    public static void main(String args[]){

        try {
            // 为使用的算法创建一个SSLContext
            SSLContext context = SSLContext.getInstance(algorithm);

            // 参考实现只支持 X.509密钥
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");

            // Oracle的默认密钥类型
            KeyStore ks = KeyStore.getInstance("JKS");

            // 处于安全考虑, 每个密钥都必须用口令短语加密, 在从磁盘加载前必须提供这个口令. 口令短语以char[]
            // 形式存储, 所以可以很快地从内存中擦除, 而不是等待垃圾回收
            char[] password = System.console().readPassword();
            ks.load(new FileInputStream("jnp4.keys"), password);
            kmf.init(ks, password);
            context.init(kmf.getKeyManagers(), null, null);

            // 擦除口令
            Arrays.fill(password, '0');

            SSLServerSocketFactory factory = context.getServerSocketFactory();
            SSLServerSocket server = (SSLServerSocket) factory.createServerSocket(PORT);

            // 增加匿名(未认证)密码组
            String[] supported = server.getSupportedCipherSuites();
            String[] anonCipherSuitesSupported = new String[supported.length];
            int numAnonCipherSuitesSupported = 0;
            for (int i = 0; i < supported.length; i++) {
                if (supported[i].indexOf("_anon_") > 0){
                    anonCipherSuitesSupported[numAnonCipherSuitesSupported++] = supported[i];
                }
            }

            String[] oldEnabled = server.getEnabledCipherSuites();
            String[] newEnabled = new String[oldEnabled.length + numAnonCipherSuitesSupported];

            System.arraycopy(oldEnabled, 0, newEnabled, 0, oldEnabled.length);
            System.arraycopy(anonCipherSuitesSupported, 0, newEnabled, oldEnabled.length ,numAnonCipherSuitesSupported);

            server.setEnabledCipherSuites(newEnabled);

            // 现在所有设置工作已经完成, 可以集中进行实际通信
            while (true){
                // 这个socket是安全的 但是代码中是无法看出迹象的
                try (Socket theConnection = server.accept()){
                    InputStream in = theConnection.getInputStream();
                    int c;
                    while ((c = in.read()) != -1){
                        System.out.write(c);
                    }
                }catch(IOException ex){
                    ex.printStackTrace();
                }
            }


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
