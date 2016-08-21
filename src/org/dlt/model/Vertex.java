package org.dlt.model;

public class Vertex {
    private int from,to;
    private double cost;

    public Vertex(int from, int to) {
        this.from = from;
        this.to = to;
    }
    public Vertex(int from, int to,double cost) {
        this.from = from;
        this.to = to;
        this.cost = cost;
    }

    public int getFrom() {
        return from;
    }
    public int getTo() {
        return to;
    }
    public double getCost() {
        return cost;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vertex)) return false;
        Vertex other = (Vertex) o;
        return from == other.getFrom() && to == other.getTo();
    }
}
