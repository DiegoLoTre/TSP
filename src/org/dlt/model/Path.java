package org.dlt.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Path implements Comparable<Path> {
    @Getter @Setter private List<Integer> route;
    @Getter @Setter private double cost;

    public Path() {
        route = new ArrayList<>();
        cost = 0;
    }
    public Path(double cost) {
        this.route = new ArrayList<>();
        this.cost = cost;
    }
    public Path(Path path) {
        this.route = new ArrayList<>(path.route);
        this.cost = path.cost;
    }

    public Integer getLast() {
        if (route.size() == 0) return 0;
        return route.get(route.size()-1);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer r: route) {
            stringBuilder.append(r).append("->");
        }
        stringBuilder.append("Costo: ").append(cost);
        return stringBuilder.toString();
    }

    public void add(Integer actual, double cost) {
        route.add(actual);
        this.cost += cost;
    }

    @Override
    public int compareTo(Path that) {
        if (cost > that.getCost())      return 1;
        else if (cost < that.getCost()) return -1;
        return 0;
    }
}
