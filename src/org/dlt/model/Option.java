package org.dlt.model;

import java.util.List;

public class Option implements Comparable<Option> {
    private List<Integer> order;
    private Double cost;

    public Option() {
        this.cost = Double.MAX_VALUE;
    }

    public Option(List<Integer> order, double cost) {
        this.order = order;
        this.cost = cost;
        order.add(0,0);order.add(0);
    }

    public Double getCost() {
        return cost;
    }
    public List<Integer> getOrder() {
        return order;
    }

    @Override
    public int compareTo(Option option) {
        return this.cost.compareTo(option.getCost());
    }

    public String toString() {
        String input = "";
        for (int j = 0; j < order.size() - 1; j++)
            input += order.get(j) + ",";
        input += order.get(order.size() - 1).toString();
        input += " con costo: "+cost+"\n";

        return input;
    }
}
