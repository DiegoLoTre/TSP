package org.dlt;

import org.dlt.model.Path;
import org.dlt.service.Database;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Combinations {
    private List<Integer> list = new ArrayList<>();
    private Database dataBase;
    private Path best;

    Combinations(String url) throws Exception {
        dataBase = new Database(url);
        best = new Path(Double.POSITIVE_INFINITY);

        for (int i =1;i<dataBase.getCities().size();i++) {
            list.add(dataBase.getCities().get(i).getId());
        }
    }

    Path getBest() {
        combination(new Path(), list);

        return best;
    }

    private Path addLast(Path original,
                         Integer first,
                         Integer second,
                         Integer third) {
        double cost;
        Integer last;

        Path option = new Path(original);

        last = option.getLast();
        cost = dataBase.getCost(last, first);
        option.add(first,cost);

        last = option.getLast();
        cost = dataBase.getCost(last, second);
        option.add(second,cost);

        last = option.getLast();
        cost = dataBase.getCost(last, third);
        option.add(third,cost);

        last = option.getLast();
        cost = dataBase.getCost(last, 0);
        option.add(0,cost);

        return option;
    }

    private Path getCheaper(Path list, List<Integer> header) {
        List<Path> list1 = new ArrayList<>();

        Path option1 = addLast(list,
                header.get(0),
                header.get(1),
                header.get(2));
        list1.add(option1);

        Path option2 = addLast(list,
                header.get(0),
                header.get(2),
                header.get(1));
        list1.add(option2);

        Path option3 = addLast(list,
                header.get(1),
                header.get(0),
                header.get(2));
        list1.add(option3);

        Path option4 = addLast(list,
                header.get(1),
                header.get(2),
                header.get(0));
        list1.add(option4);

        Path option5 = addLast(list,
                header.get(2),
                header.get(0),
                header.get(1));
        list1.add(option5);

        Path option6 = addLast(list,
                header.get(2),
                header.get(1),
                header.get(0));
        list1.add(option6);

        /*System.out.println(option1);
        System.out.println(option2);
        System.out.println(option3);
        System.out.println(option4);
        System.out.println(option5);
        System.out.println(option6);

        System.out.println(Collections.min(list1));*/
        return Collections.min(list1);
    }

    private void combination(Path list, List<Integer> header) {
        if (header.size() == 3) {
            if (list.getCost() > best.getCost()) return;

            Path cheap = getCheaper(list, header);

            if (cheap.getCost()<best.getCost())
                best = cheap;

            return;
        }
        for (int i=0;i<header.size();i++) {

            Integer actual = header.get(i);
            header.remove(i);

            Path path = new Path(list);
            double cost = dataBase.getCost(path.getLast(), actual);
            path.add(actual, cost);

            //System.out.println(path);

            combination(path, header);

            header.add(i,actual);
        }
    }

    String getRoute(Path path) {
        StringBuilder string = new StringBuilder("Ruta más optima:");
        string.append("\n->")
                .append(dataBase.getName(0));
        for (int i: path.getRoute()) {
            string.append("\n->");
            string.append(dataBase.getName(i));
        }
        string.append("\nCon costo de:")
                .append(path.getCost());

        return string.toString();
    }
}
