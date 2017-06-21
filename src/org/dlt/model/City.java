package org.dlt.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

public class City {
    @Getter @Setter private int id;
    @Getter @Setter private String name;
    @Getter @Setter private double x, y;

    public City(
            @JsonProperty(value = "nombre",required = true) String name,
            @JsonProperty(value = "x",required = true) double x,
            @JsonProperty(value = "y",required = true) double y
    ) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.id = 0;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.
                    writerWithDefaultPrettyPrinter().
                    writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
