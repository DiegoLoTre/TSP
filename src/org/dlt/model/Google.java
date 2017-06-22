package org.dlt.model;

import lombok.Getter;
import lombok.Setter;
import org.dlt.model.google.Elements;
import org.dlt.model.google.Response;
import org.dlt.model.google.Row;

import java.util.ArrayList;
import java.util.List;

public class Google {
    @Getter @Setter private List<Elements> list;

    public Google(Response response) {
        list = new ArrayList<>();
        for (Row row: response.getRows()) {
            list.addAll(row.getElements());
        }
    }
}