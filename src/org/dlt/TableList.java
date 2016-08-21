package org.dlt;

import org.dlt.model.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class TableList {
    private List<Vertex> list = new ArrayList<>();

    TableList(int tope) {
        int cant=0;

        double costs[] = {32.0,83.0,75.0,38.0,77.0,45.0,86.0,20.0,40.0,52.0,18.0,78.0,89.0,15.0,3.0,62.0,19.0,80.0,47.0,84.0,19.0};

        for (int i=0;i<tope;i++)
            for (int j = i+1;j<tope;j++) {
                try {list.add(new Vertex(i, j, costs[cant++]));} catch (Exception e) {list.add(new Vertex(i,j,new Random().nextInt(100)));}
            }

        /*
        for (Vertex aList : list) {
            String input = "De ";
            input += aList.getFrom();
            input += " A ";
            input += aList.getTo();
            input += " CUESTA ";
            input += aList.getCost();
            System.out.println(input);
        }
        */
    }
    Vertex searchCost(int from, int to) {
        Vertex vertex = new Vertex(from,to);
        if(to<from) vertex = new Vertex(to,from);

        int index = list.indexOf(vertex);
        if (index == -1) return new Vertex(from,to,Double.MAX_VALUE);
        else return list.get(list.indexOf(vertex));
    }
}
