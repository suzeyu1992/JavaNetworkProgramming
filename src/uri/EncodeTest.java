package uri;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Author :  suzeyu
 * Time   :  2016-09-18  下午11:34
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription :   使用URLEncoder.encode()显示各种编码字符串
 */
public class EncodeTest {

    public static void main (String arg[]){

        try {
            System.out.println(URLEncoder.encode("This String has spaces" , "UTF-8"));

            System.out.println(URLEncoder.encode("This*String*has*asterisks" , "UTF-8"));

            System.out.println(URLEncoder.encode("This%String%has%percent%signs" , "UTF-8"));

            System.out.println(URLEncoder.encode("This+String+has+pluses" , "UTF-8"));

            System.out.println(URLEncoder.encode("This/String/has/slashes" , "UTF-8"));

            System.out.println(URLEncoder.encode("This\"String\"has\"quote\"marks" , "UTF-8"));

            System.out.println(URLEncoder.encode("This:String:has:colons" , "UTF-8"));

            System.out.println(URLEncoder.encode("This~String~has~tildes" , "UTF-8"));

            System.out.println(URLEncoder.encode("This(String)has(parentheses)" , "UTF-8"));

            System.out.println(URLEncoder.encode("This.String.has.periods" , "UTF-8"));

            System.out.println(URLEncoder.encode("This=String=has=equals=signs" , "UTF-8"));

            System.out.println(URLEncoder.encode("This&String&has&ampersands" , "UTF-8"));

            System.out.println(URLEncoder.encode("ThisÉStringÉhasÉnon-ASCII characters" , "UTF-8"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
