package com.tyrone.threads;

import java.util.concurrent.ThreadLocalRandom;

public class event extends Thread {
    @Override
    public void run(){
        for(int i = 0; i<=5; i++){
             int action = ThreadLocalRandom.current().nextInt(1000);

             System.out.println("=============================\n" + action);
             if(action <= 960 && action >= 890){
                System.out.println("WAR");
             } else if(action <= 580 && action >= 650){
                System.out.println("Earfqueake");
             } else if(action <= 480 && action >= 540){
                System.out.println("Pandemic");
             }
             try{
                Thread.sleep(2000);
             } catch (InterruptedException e){
                e.printStackTrace();
             }
        }
    }
}
