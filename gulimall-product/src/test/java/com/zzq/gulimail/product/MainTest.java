package com.zzq.gulimail.product;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.*;

@Slf4j
public class MainTest {

    @Test
    public void test01(){
        byte b5 = 7;
        final byte b1 =125, b2=1;
//        byte b3 = b1+b2+b5;

    }

    @Test
    public void test02(){
        System.out.println(new ArrayList() instanceof List);
        System.out.println(new ArrayList() instanceof Collection);
        System.out.println(new LinkedHashMap() instanceof HashMap);
    }

    int a;
//    a=10;//error
    @Test
    public void test03(){
        int[] nums = {1,7,3,6,5,6};
        int sum = (int) Arrays.stream(nums).boxed().count(); //sum
        System.out.println(sum);
    }

    public static void main(String[] args) {
        List<String> list = Arrays.asList("a", "b", "c");
        System.out.println(list.size());
        /**
         * 想测数组长度的影响是什么！！！
         */
        log.info("arr: {}",list.toArray(new String[0]));
        log.info("arr111: {}",list.toArray(new String[list.size()]));
        // End

        String[] strings = list.toArray(new String[0]);
        String[] strings1 = list.toArray(new String[list.size()]);
        System.out.println(strings);
        System.out.println(strings1);

        for (String string : strings) {
            System.out.print(string);
        }
        System.out.println();
        for (String s : strings1) {
            System.out.print(s);
        }
        System.out.println();
        System.out.println(list);
        log.info("arr111: {}",Arrays.asList(list.toArray(new String[list.size()])));
    }
}
