import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MyTest {

    /**
     * 密码加密器：多个用户过来存 123456 用这个Util存的值都不一样，但是都能match 123456
     * 加了个算法生成盐
     */
    @Test
    public void testBCryptPasswordEncoder(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //$2a$10$Vu.YcPJ5LOY.pIXhqxvgGuO/haGTVe5g/bpXCYYasSgS9sF2vxauC
        //$2a$10$C/7Ur.ScOTYkYIOKChppD.TwGZfMyl3QbgWtbiYU1VIYWcNJ.dNH.
        String s = encoder.encode("123456");
        System.out.println(s);
        //两个都是 true
        System.out.println(encoder.matches("123456", "$2a$10$Vu.YcPJ5LOY.pIXhqxvgGuO/haGTVe5g/bpXCYYasSgS9sF2vxauC"));
        System.out.println(encoder.matches("123456", "$2a$10$C/7Ur.ScOTYkYIOKChppD.TwGZfMyl3QbgWtbiYU1VIYWcNJ.dNH."));
    }


    @Test
    public void testApachAPI(){
        //Apache commons包 DigestUtils类，【简单的MD5】
        DigestUtils.md5Hex("123456");
        //Apache commons包 Md5Crypt类，这个方法的颜值有正则限定，需要以$1$开头，【MD5+salt】
        Md5Crypt.md5Crypt("123456".getBytes(), "$1$1");
    }
}
