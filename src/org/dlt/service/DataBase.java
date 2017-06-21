package org.dlt.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.dlt.model.City;
import org.dlt.model.Google;
import org.dlt.model.Vertex;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DataBase {

    private String APIKEY = "AIzaSyAOvc6vdULyXtI0j0xrbBcNqO3_aOSSy-Q";

    @Getter @Setter private List<Vertex> vertex;
    @Getter @Setter private List<City> cities;

    public DataBase(String filename) throws IOException, JSONException {

        ObjectMapper mapper = new ObjectMapper();
        cities = mapper.readValue(
                new File(filename),
                new TypeReference<List<City>>() {}
        );
        vertex = new ArrayList<>();

        getGoogleDistance();
        vertex.add(new Vertex(0,0,0)); //Add 0->0 cost 0 to avoid crashes
    }

    private void getGoogleDistance() throws IOException, JSONException {
        System.out.println("Obteniendo datos de Google");
        InputStream is;
        BufferedReader br;
        String line;
        int i;
        for (i=0;i<cities.size();i++) {
            cities.get(i).setId(i);
            City origin = cities.get(i);

            is = new URL(createUrl(origin, i)).openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));

            StringBuilder data = new StringBuilder();
            while ((line = br.readLine()) != null) {
                data.append(line);
            }
            JSONObject request = new JSONObject(data.toString());

            Google google = new Google(request);
            int id = 0;
            for (int j=0;j<google.getList().size();j++) {
                Google.Elements element = google.getList().get(j);
                //vertex.add(new Vertex(i,j+i+1, element.getDuration().getValue()));
                if (id == i) id++;
                vertex.add(new Vertex(i,id++, element.getDistance().getValue()));
            }
        }
        System.out.println("Se termino de obtener datos de Google");
    }

    private String createUrl(City origin, int i) {
        StringBuilder urlString = new StringBuilder()
                .append("https://maps.googleapis.com/maps/api/distancematrix/json?origins=")
                .append(origin.getX())
                .append(",")
                .append(origin.getY())
                .append("&destinations=");

        for (int j=0;j<cities.size();j++) {
            if (j != i) {
                City destination = cities.get(j);
                urlString
                        .append(destination.getX())
                        .append(",")
                        .append(destination.getY());
                if (j != cities.size() - 1)
                    urlString.append("|");
            }
        }
        urlString.append("&key=")
                .append(APIKEY);

        return urlString.toString();
    }

    public double getCost(Integer from, Integer to) {
        Vertex vertex1 = vertex.stream()
                .filter(e -> e.getFrom() == from && e.getTo() == to)
                .findFirst()
                .get();

        //System.out.println(vertex1);

        return vertex1.getCost();
    }
    public String getName(int id) {
        City city = cities.stream()
                .filter(e -> e.getId() == id)
                .findAny()
                .get();

        return city.getName();
    }
}
