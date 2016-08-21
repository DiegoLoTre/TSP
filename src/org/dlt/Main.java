package org.dlt;

import org.dlt.model.Option;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        long tStart, tEnd,time;
        tStart = System.currentTimeMillis();
        List<Option> options = new ArrayList<>();


        Thread thread = new Thread() {
            @Override
            public void run() {
                List<Integer> lista = new ArrayList<>();
                for (int i = 1;i < 14;i++)
                    lista.add(i);
                options.add(new Calculos().getBetterOnce(lista,lista.size()+2));
            }
        };
        thread.start();

        Thread thread1 = new Thread() {
            @Override
            public void run() {
                List<Integer> lista = new ArrayList<>();
                lista.add(1);lista.add(2);lista.add(3);lista.add(4);
                options.add(new Calculos().getBetterOnce(lista,lista.size()+2));

                /*try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
        };
        thread1.start();

        try {
            thread.join();
            thread1.join();
        } catch (InterruptedException e) {e.printStackTrace();}

        for (Option option : options) System.out.print(option.toString());
        Option option = options.get(options.indexOf(Collections.min(options)));
        System.out.println("De los 2 el más pequeño es: "+option);

        for (int i=0;i<option.getOrder().size()-1;i++)
            System.out.print(option.getOrder().get(i)+"->");
        System.out.print(option.getOrder().get(option.getOrder().size()-1));

        tEnd = System.currentTimeMillis();
        time = tEnd-tStart;
        System.out.println("\nTiempo en mili: "+time);
    }
}
