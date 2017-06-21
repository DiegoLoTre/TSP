package org.dlt;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.dlt.model.Path;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;

class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("No se escogió la lista");
            System.exit(1);
        }
        try {
            Combinations combinations =
                    new Combinations(args[0]);

            System.out.println("Empezar a buscar el más optimo");

            Path path = combinations.getBest();

            String route = combinations.getRoute(path);

            System.out.println(path);
            System.out.println(route);
        } catch (FileNotFoundException fne){
            System.out.println("Archivo no encontrado");
        } catch (JsonMappingException jme) {
            System.out.println("Los datos no estan guardados correctamente en el archivo");
        } catch (IOException e) {
            e.printStackTrace();
            //System.out.println("");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
