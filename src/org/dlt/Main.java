package org.dlt;

import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.dlt.model.Path;
import org.dlt.service.Database;

import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("No se escogió la lista");
            System.exit(1);
        }
        try {
            Database database = new Database(args[0]);

            List<Integer> list = new ArrayList<>();
            for (int i =1;i< database.getCities().size();i++) {
                list.add(database.getCities().get(i).getId());
            }
            System.out.println(list.size());
            System.out.println("Empezar a buscar el más optimo");

            long start = System.currentTimeMillis();
            Path path = new Combinations(database).getBest(new Path(), list);
            long end = System.currentTimeMillis();

            System.out.println("Tiempo de ejecucion"+((end-start)/1000));
            String route = database.getRoute(path);
            System.out.println(route);

        } catch (FileNotFoundException e) {
            System.out.println("No se encontro el archivo");
        } catch (JsonMappingException | JsonEOFException e) {
            System.out.println("Archivo mal guardado");
        } catch (UnknownHostException e) {
            System.out.println("No esta conectado a internet");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
