package com.bvirtuoso.hari.scheduler;

import java.time.LocalDateTime;
import java.util.TimeZone;

public class Test {
    public static void main(String [] args){
       /* Integer count = 0;
        String name;

        for(int i = 0; i <= 100; i++){
            for(int j = 0; j <= i; j++){
                System.out.print("_");
            }
            System.out.println();
        }*/
        System.out.println("Testing the application");
        LocalDateTime rightNow = LocalDateTime.now();
        System.out.println("rightNow: "+ rightNow);
        TimeZone timeZone = TimeZone.getDefault();
        System.out.println("Time zone: "+ timeZone);
    }
}
