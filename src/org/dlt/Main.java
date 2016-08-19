package org.dlt;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        long tStart, tEnd,time;
        tStart = System.currentTimeMillis();
        List<Thread> threads = new ArrayList<>();

        //calculos.getBetter();
        /*for (int i = 0;i<150;i++) {
            final int finalI = i;
            Thread thread = new Thread() {
                @Override
                public void run() {
                    new Calculos().getBetterOnce();
                    System.out.print(finalI);
                }
            };
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        //for (int i = 0;i<150;i++) { new Calculos().getBetterOnce();System.out.print(i);}



        Thread thread = new Thread() {
            @Override
            public void run() {
                new Calculos().getBetterOnce();System.out.print(0);
            }
        };
        thread.start();

        Thread thread1 = new Thread() {
            @Override
            public void run() {
                new Calculos().getBetterOnce();System.out.print(1);
            }
        };
        thread1.start();

        Thread thread2 = new Thread() {
            @Override
            public void run() {
                new Calculos().getBetterOnce();System.out.print(2);
            }
        };
        thread2.start();

        Thread thread3 = new Thread() {
            @Override
            public void run() {
                new Calculos().getBetterOnce();System.out.print(3);
            }
        };
        thread3.start();

        Thread thread4 = new Thread() {
            @Override
            public void run() {
                new Calculos().getBetterOnce();System.out.print(4);
            }
        };
        thread4.start();

            try {
                thread.join();
                thread1.join();
                thread2.join();
                thread3.join();
                thread4.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
        }

        tEnd = System.currentTimeMillis();
        time = tEnd-tStart;
        System.out.println("\nTiempo en mili: "+time);
    }
}
