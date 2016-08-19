package org.dlt;

import org.dlt.model.Option;
import org.dlt.model.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class Calculos {
    private List<Vertex> list = new ArrayList<>();
    private int cantLine=0;
    private final Object lock = new Object();

    Calculos() {
        int tope = 13,cant=0;

        double costs[] = {32.0,83.0,75.0,38.0,77.0,45.0,86.0,20.0,40.0,52.0,18.0,78.0,89.0,15.0,3.0,62.0,19.0,80.0,47.0,84.0,19.0};

        for (int i=0;i<tope;i++)
            for (int j = i+1;j<tope;j++) {
            try {
                list.add(new Vertex(i, j, costs[cant++]));
            } catch (Exception e) {
                list.add(new Vertex(i,j,new Random().nextInt(100)));
            }
        }
    }

    private String printSubList(List<Integer> subList,int i,double cost) {
        String input = (i+1)+": ";
        //String input = "";

        for (int j = 0; j < subList.size() - 1; j++)
            input += subList.get(j) + ",";
        input += subList.get(subList.size() - 1).toString();
        input += " con costo: "+cost+"\n";

        //System.out.print(input);
        return input;
    }
    private Vertex searchCost(List<Vertex>list, int from, int to) {
        Vertex vertex = new Vertex(from,to);
        if(to<from) vertex = new Vertex(to,from);

        int index = list.indexOf(vertex);
        if (index == -1) return new Vertex(from,to,Double.MAX_VALUE);
        else return list.get(list.indexOf(vertex));
    }
    private Option completeListTurn(List<Vertex>listVertex, List<Integer> lista, double previousSum, int... num) {

        double preCost = previousSum +  searchCost(listVertex,lista.get(0),lista.get(1)).getCost();
        double cost1 = preCost + searchCost(listVertex,num[num.length-1],lista.get(0)).getCost() + searchCost(listVertex,lista.get(1),0).getCost();
        double cost2 = preCost + searchCost(listVertex,num[num.length-1],lista.get(1)).getCost() + searchCost(listVertex,lista.get(0),0).getCost();

        for (int i = num.length-1;i>=0;i--)
            lista.add(0,num[i]);
        lista.add(0,0);lista.add(0);

        Option option1 = new Option(new ArrayList<>(lista),cost1);
        Collections.swap(lista,lista.size()-2,lista.size()-3);
        Option option2 = new Option(new ArrayList<>(lista),cost2);

        synchronized (lock) {
            cantLine += 2;
        }

        if(option1.getCost() > option2.getCost())
            return option2;
        else return option1;
    }
    private Option combination(List<Vertex>listVertex, int amount, double previousSum, List<Integer> lista, int... headers) {
        List<Option> opciones = new ArrayList<>();

        for (int i =0;i<lista.size();i++) {
            List<Integer> lista2 = new ArrayList<>(lista);
            int num = lista2.get(i);
            lista2.remove(i);

            int[] newArray = new int[headers.length+1];

            //copy values
            System.arraycopy(headers, 0, newArray, 0, headers.length);
            newArray[headers.length] = num;
            double cost = 0.0;
            if(headers.length<1)
                cost = searchCost(listVertex,0,num).getCost();
            else
                cost = previousSum + searchCost(listVertex,headers[headers.length-1],num).getCost();
            if (amount <= 3)
                opciones.add(completeListTurn(listVertex,new ArrayList<>(lista2), cost,newArray));
            else
                opciones.add(combination(listVertex,amount-1, cost,new ArrayList<>(lista2),newArray));
        }
        return Collections.min(opciones);
    }
    private Option combination3(final List<Vertex>listVertex, final int amount, final double previousSum, final List<Integer> lista, final int... headers) {
        final List<Option> opciones = new ArrayList<>();
        List<Thread> arrayThread = new ArrayList<>();

        for (int i = 0; i<lista.size(); i++) {
            final int finalI = i;
            Thread thread = new Thread() {
                @Override
                public void run() {

                    List<Integer> lista2 = new ArrayList<>(lista);
                    int num = lista2.get(finalI);
                    lista2.remove(finalI);

                    int[] newArray = new int[headers.length+1];

                    //copy values
                    System.arraycopy(headers, 0, newArray, 0, headers.length);
                    newArray[headers.length] = num;

                    double cost = previousSum + searchCost(listVertex, 0, num).getCost();
                    if (amount <= 8)
                        opciones.add(combination(listVertex, amount - 1, cost, new ArrayList<>(lista2), newArray));
                    else
                        opciones.add(combination3(listVertex,amount-1,cost, new ArrayList<>(lista2),newArray));
                }
            };
            thread.start();
            arrayThread.add(thread);
        }

        for (int i = 0; i<lista.size(); i++) {
            try {
                arrayThread.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return Collections.min(opciones);
    }

    void getBetterOnce() {

        List<Integer> lista = new ArrayList<>();
        for (int i = 1;i < 11;i++)
            lista.add(i);

        Option options = combination(list,lista.size(),0,lista);

        /*System.out.println("\nMás pequeño: "+printSubList(options.getOrder(),0,options.getCost()));
        System.out.println("Combinaciones:"+cantLine);*/

    }

    void getBetter() {
        List<Integer> lista = new ArrayList<>();
        for (int i = 1;i < 8;i++)
            lista.add(i);

        Option options = combination3(list,lista.size(),0,lista);
        /*for (Vertex1 vertex : list) {
            String input ="";
            input += "De: "+vertex.getFrom();
            input += " a: "+vertex.getTo();
            input += " cuesta: "+vertex.getCost();

            System.out.println(input);
        }*/

        System.out.println("\nMás pequeño: ");
        System.out.println("Combinaciones:"+cantLine);
        //for (int i=0;i<options.size();i++) {
            printSubList(options.getOrder(),0,options.getCost());
        //}

    }
}
