package org.dlt.model;

import lombok.Getter;
import lombok.Setter;

public class Vertex {
    @Getter @Setter private int from,to;
    @Getter @Setter private double cost;

    public Vertex(int from, int to,double cost) {
        this.from = from;
        this.to = to;
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vertex)) return false;
        Vertex other = (Vertex) o;
        return from == other.getFrom() && to == other.getTo();
    }

    @Override
    public String toString() {
        return "De "+from+" A "+to+":"+cost;
    }
}
