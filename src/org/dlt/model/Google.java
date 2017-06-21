package org.dlt.model;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Google {
    @Getter @Setter private List<Elements> list;

    public class Elements {
        @Getter @Setter private Element duration, distance;

        Elements(JSONObject rec) {
            try {
                duration = new Element(rec.getJSONObject("duration"));
                distance = new Element(rec.getJSONObject("distance"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String toString() {
            return distance.toString();
        }
    }

    public class Element {
        @Getter @Setter private String text;
        @Getter @Setter private double value;

        Element(JSONObject rec) {
            try {
                text = rec.getString("text");
                value = rec.getDouble("value");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String toString() {
            return "Value:"+value+"\nText:"+text;
        }
    }

    public Google(JSONObject request) {
        try {
            JSONArray rows = request.getJSONArray("rows");
            JSONObject elements = rows.getJSONObject(0);
            JSONArray elementsArray = elements.getJSONArray("elements");

            list = new ArrayList<>();
            for (int i = 0; i < elementsArray.length(); ++i) {
                JSONObject rec = elementsArray.getJSONObject(i);
                list.add(new Elements(rec));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}