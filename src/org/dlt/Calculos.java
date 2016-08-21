package org.dlt;

import org.dlt.model.Option;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class Calculos {
    private TableList tableList;
    private Option option = new Option();

    private void completeListTurn(List<Integer> lista, double previousSum, int... num) {

        previousSum += tableList.searchCost(lista.get(0), lista.get(1)).getCost();
        if (this.option.getCost() < previousSum) return;

        double cost1 = previousSum + tableList.searchCost(num[num.length - 1], lista.get(0)).getCost() + tableList.searchCost(lista.get(1), 0).getCost();
        double cost2 = previousSum + tableList.searchCost(num[num.length - 1], lista.get(1)).getCost() + tableList.searchCost(lista.get(0), 0).getCost();

        if (this.option.getCost() < cost1 && this.option.getCost() < cost2) return;

        for (int i = num.length - 1; i >= 0; i--)
            lista.add(0, num[i]);

        Option option1 = new Option(new ArrayList<>(lista), cost1);
        Collections.swap(lista, lista.size() - 2, lista.size() - 3);
        Option option2 = new Option(new ArrayList<>(lista), cost2);

        if (option2.getCost() < option1.getCost()) {
            if (option2.getCost() < this.option.getCost()) this.option = option2;
        } else {
            if (option1.getCost() < this.option.getCost()) this.option = option1;
        }
    }

    private void combination(int amount, List<Integer> list, double previousSum, int... headers) {
        for (int i = 0; i < list.size(); i++) {
            List<Integer> list2 = new ArrayList<>(list);
            int num = list2.get(i);
            list2.remove(i);

            int[] newArray = Arrays.copyOf(headers, headers.length+1);
            newArray[headers.length] = num;

            double cost;

            if (headers.length < 1) cost = tableList.searchCost(0, num).getCost();
            else cost = previousSum + tableList.searchCost(headers[headers.length - 1], num).getCost();

            if (this.option.getCost() < cost) return;

            if (amount <= 3) completeListTurn(new ArrayList<>(list2), cost, newArray);
            else combination(amount - 1, new ArrayList<>(list2), cost, newArray);
        }
    }

    Option getBetterOnce(List<Integer> lista,int size) {
        tableList = new TableList(size);

        combination(lista.size(), lista, 0);
        if(size>6)
        for (int i =0 ;i<option.getOrder().size()-1;i++) {
            String input = "De ";
            input += option.getOrder().get(i);
            input += " A ";
            input += option.getOrder().get(i+1);
            input += " CUESTA ";
            input += tableList.searchCost(option.getOrder().get(i), option.getOrder().get(i+1)).getCost();
            System.out.println(input);
        }

        return option;
    }
}
