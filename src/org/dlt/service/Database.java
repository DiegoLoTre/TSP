package org.dlt.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.dlt.model.City;
import org.dlt.model.Google;
import org.dlt.model.Path;
import org.dlt.model.Vertex;
import org.dlt.model.google.Element;
import org.dlt.model.google.Elements;
import org.dlt.model.google.Response;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Database {
    /*Put here your API Key of google*/private final String APIKEY = "";

    @Getter @Setter private List<Vertex> vertex;
    @Getter @Setter private List<City> cities;

    public Database(String filename) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        cities = mapper.readValue(
                new File(filename),
                new TypeReference<List<City>>() {}
        );
        if (cities.size() < 3) throw new IllegalArgumentException();

        vertex = new ArrayList<>();

        getGoogleDistance();
    }

    private void getGoogleDistance() throws Exception {
        System.out.println("Obteniendo datos de Google");
        InputStream is;
        BufferedReader br;
        String line;
        int i;
        ObjectMapper mapper = new ObjectMapper();
        for (i=0;i<cities.size();i++) {
            System.out.println("Obteniendo Distancias desde el punto "+i);
            cities.get(i).setId(i);
            City origin = cities.get(i);

            is = new URL(createUrl(origin, i)).openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));

            StringBuilder data = new StringBuilder();
            while ((line = br.readLine()) != null) {
                data.append(line);
            }
            Google google = new Google(mapper.readValue(data.toString(), Response.class));
            int id = 0;
            for (Elements element : google.getList()) {
                Element distance = element.getDistance();
                Element duration = element.getDuration();

                if (id == i) id++;
                vertex.add(new Vertex(i, id++,
                        distance.getValue(),
                        duration.getValue()));
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
        if(vertex.stream()
                .anyMatch(e -> e.getFrom() == from && e.getTo() == to)) {
            Vertex vertex1 = vertex.stream()
                    .filter(e -> e.getFrom() == from && e.getTo() == to)
                    .findFirst()
                    .orElse(null);

            return vertex1.getTime();
            //return vertex1.getDistance();
        } else
            return 0;
    }

    private String getName(int id) {
        City city = cities.stream()
                .filter(e -> e.getId() == id)
                .findAny()
                .orElse(null);

        return city.getName();
    }

    public String getRoute(Path path) {
        StringBuilder string = new StringBuilder("Ruta más optima:");
        string.append("\n->")
                .append(getName(0));
        for (int i: path.getRoute()) {
            string.append("\n->");
            string.append(getName(i));
        }
        string.append("\nCon costo de: ")
                .append(path.getCost());

        return string.toString();
    }
}
