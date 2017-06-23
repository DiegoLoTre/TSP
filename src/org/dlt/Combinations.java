package org.dlt;

import lombok.Getter;
import org.dlt.model.Path;
import org.dlt.service.Database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Combinations {
    @Getter private final Database database;
    private Path best;

    Combinations(Database database) {
        this.database = database;
        best = new Path(Double.POSITIVE_INFINITY);
    }

    Path getBest(Path path, List<Integer> list) {
        combination(path, new ArrayList<>(list));

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
        cost = database.getCost(last, first);
        option.add(first,cost);

        cost = database.getCost(first, second);
        option.add(second,cost);

        cost = database.getCost(second, third);
        option.add(third,cost);

        cost = database.getCost(third, 0);
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

        return Collections.min(list1);
    }

    private void combination(Path list, List<Integer> header) {
        if (header.size() == 3) {
            if (list.getCost() > best.getCost()) return;

            Path cheap = getCheaper(list, header);

            if (cheap.getCost() < best.getCost()) best = cheap;

            return;
        }
        for (int i=0;i<header.size();i++) {

            Integer actual = header.get(i);
            header.remove(i);

            Path path = new Path(list);
            double cost = database.getCost(path.getLast(), actual);
            path.add(actual, cost);

            combination(path, header);

            header.add(i,actual);
        }
    }
}
