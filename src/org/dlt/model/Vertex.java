package org.dlt.model;

import lombok.Getter;
import lombok.Setter;

public class Vertex {
    @Getter @Setter private int from,to;
    @Getter @Setter private double distance, time;

    public Vertex(int from, int to,double distance, double time) {
        this.from = from;
        this.to = to;
        this.distance = distance;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vertex)) return false;
        Vertex other = (Vertex) o;
        return from == other.getFrom() && to == other.getTo();
    }

    @Override
    public String toString() {
        return "De "+from+" A "+to+":"+distance+"/"+time;
    }
}
