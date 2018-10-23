package com.snow.storeapi;

import java.util.Arrays;
import java.util.HashMap;

public class test {
    public static void main(String[] args) {
        System.out.println(new HashMap<String,String>(){{put("msg", "dadasa");}});
        String separator = ",";
        Arrays.asList( "a", "b", "d" ).forEach(
                ( String e ) -> System.out.print( e + separator ) );
        System.out.println("=====");

    }
}
