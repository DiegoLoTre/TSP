package org.dlt.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
