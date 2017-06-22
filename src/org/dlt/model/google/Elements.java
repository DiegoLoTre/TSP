package org.dlt.model.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Elements {
    @Getter @Setter private Element duration, distance;
    @Getter @Setter private String status;
}