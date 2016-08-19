package org.dlt.model;

import java.util.List;

public class Option implements Comparable<Option> {
    private List<Integer> order;
    private Double cost;

    public Option(List<Integer> order, double cost) {
        this.order = order;
        this.cost = cost;
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
}
