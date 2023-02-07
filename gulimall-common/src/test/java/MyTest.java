import com.zzq.common.exception.BizCodeEnum;
import org.junit.Test;

public class MyTest {
    static int s;
    MyTest(){

    }
    static{
//        this();
    }

    @Test
    public void test1() {
        /**
         * 局部变量可以和成员变量一样
         */
        String s;
        System.out.println(BizCodeEnum.UNKNOW_EXCEPTION);
    }


    public void add(Byte b)
    {
        b = b++;
    }

    @Test
    public void test()
    {
        Byte a = 127;
        Byte b = 127;
        add(++a);
        System.out.print(a + " ");
        add(b);
        System.out.print(b + "");
    }
}

/**
 * 可以有构造方法
 */
abstract class Abstract{
    Abstract() {

    }

    Abstract(String a) {

    }
}





enum AccountType
{
    SAVING, FIXED, CURRENT;
    private AccountType()
    {
        System.out.println("It is a account type");
    }
}
class EnumOne
{
    public static void main(String[]args)
    {
        System.out.println(AccountType.SAVING);
    }
}




