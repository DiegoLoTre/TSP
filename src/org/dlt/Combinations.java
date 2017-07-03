package org.dlt;

import lombok.Getter;
import org.dlt.model.Path;
import org.dlt.service.Database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

class Combinations implements Callable<Path> {
    @Getter private final Database database;
    private Path best;
    private int count;
    private Path path;
    private List<Integer> list;

    Combinations(Database database) {
        this.database = database;
        best = new Path(Double.POSITIVE_INFINITY);
        this.count = 0;
    }

    Combinations(Database database,
                 int count,
                 List<Integer> list,
                 Path path) {
        this.database = database;
        this.list = new ArrayList<>(list);
        this.path = path;
        this.count = count;
        best = new Path(Double.POSITIVE_INFINITY);
    }

    Path getBest(Path path, List<Integer> list) {
        combination(path, new ArrayList<>(list));

        return best;
    }

    private void combination(Path list, List<Integer> header) {
        if (header.size() == 7) {
            System.out.println("Buscando ruta #"+ ++count);
            if (list.getCost() > best.getCost()) return;
            Path cheap = get7Best(list,
                    header.get(0),
                    header.get(1),
                    header.get(2),
                    header.get(3),
                    header.get(4),
                    header.get(5),
                    header.get(6));

            if (cheap.getCost() < best.getCost()) best = cheap;

            return;
        }
        for (int i=0;i<header.size();i++) {

            Integer actual = header.get(i);
            header.remove(i);

            Path path = addNode(list, actual);
            combination(path, header);

            header.add(i,actual);
        }
    }

    private Path addNode(Path original, Integer node) {
        Path option = new Path(original);
        Integer last = option.getLast();
        double cost = database.getCost(last, node);
        option.add(node, cost);

        return option;
    }

    private Path addHeader(Path path, Integer... headers) {
        Path option = addNode(path, headers[0]);
        for (int i = 1; i< headers.length;i++) {
            option = addNode(option, headers[i]);
        }
        option = addNode(option, 0);

        return option;
    }

    Path get2Best(Path path, Integer first, Integer second) {
        Path option = addHeader(path, first, second);
        Path option1 = addHeader(path, second, first);

        if (option.getCost() < option1.getCost()) return option;
        else return option1;
    }
    Path get3Best(Path path, Integer first, Integer second, Integer third) {

        List<Path> list = new ArrayList<>();

        list.add(addHeader(path, first, second, third));
        list.add(addHeader(path, first, third, second));
        list.add(addHeader(path, second, first, third));
        list.add(addHeader(path, second, third, first));
        list.add(addHeader(path, third, first, second));
        list.add(addHeader(path, third, second, first));

        return Collections.min(list);
    }
    Path get4Best(Path path, Integer first, Integer second, Integer third, Integer fourth) {
        List<Path> list = new ArrayList<>();

        list.add(get3Best(addNode(path, first),  second, third,  fourth));
        list.add(get3Best(addNode(path, second), first,  third,  fourth));
        list.add(get3Best(addNode(path, third),  first,  second, fourth));
        list.add(get3Best(addNode(path, fourth), first,  second, third));

        return Collections.min(list);
    }
    Path get5Best(Path path, Integer first, Integer second, Integer third, Integer fourth, Integer fifth) {
        List<Path> list = new ArrayList<>();

        list.add(get4Best(addNode(path, first),  second, third,  fourth, fifth));
        list.add(get4Best(addNode(path, second), first,  third,  fourth, fifth));
        list.add(get4Best(addNode(path, third),  first,  second, fourth, fifth));
        list.add(get4Best(addNode(path, fourth), first,  second, third,  fifth));
        list.add(get4Best(addNode(path, fifth),  first,  second, third,  fourth));

        return Collections.min(list);
    }
    Path get6Best(Path path, Integer first, Integer second, Integer third, Integer fourth, Integer fifth, Integer sixth) {

        List<Path> list = new ArrayList<>();

        list.add(get5Best(addNode(path, first),  second, third,  fourth, fifth,  sixth));
        list.add(get5Best(addNode(path, second), first,  third,  fourth, fifth,  sixth));
        list.add(get5Best(addNode(path, third),  first,  second, fourth, fifth,  sixth));
        list.add(get5Best(addNode(path, fourth), first,  second, third,  fifth,  sixth));
        list.add(get5Best(addNode(path, fifth),  first,  second, third,  fourth, sixth));
        list.add(get5Best(addNode(path, sixth),  first,  second, third,  fourth, fifth));

        return Collections.min(list);
    }
    Path get7Best(Path path, Integer first, Integer second, Integer third, Integer fourth, Integer fifth, Integer sixth, Integer seventh) {
        List<Path> list = new ArrayList<>();

        list.add(get6Best(addNode(path, first),   second, third,  fourth, fifth,  sixth, seventh));
        list.add(get6Best(addNode(path, second),  first,  third,  fourth, fifth,  sixth, seventh));
        list.add(get6Best(addNode(path, third),   first,  second, fourth, fifth,  sixth, seventh));
        list.add(get6Best(addNode(path, fourth),  first,  second, third,  fifth,  sixth, seventh));
        list.add(get6Best(addNode(path, fifth),   first,  second, third,  fourth, sixth, seventh));
        list.add(get6Best(addNode(path, sixth),   first,  second, third,  fourth, fifth, seventh));
        list.add(get6Best(addNode(path, seventh), first,  second, third,  fourth, fifth, sixth));

        return Collections.min(list);
    }

    @Override
    public Path call() throws Exception {

        if (list.size() == 11) {
            combination(path, list);
            return best;
        }
        else {
            List<Path> pathList = new ArrayList<>();

            ThreadPoolExecutor executor = (ThreadPoolExecutor)
                    Executors.newFixedThreadPool(list.size());

            List<Future<Path>> resultList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                List<Integer> list1 = new ArrayList<>(list);
                Integer actual = list1.get(i);
                list1.remove(i);
                Path option = new Path();

                Integer last = option.getLast();

                double cost = database.getCost(last, actual);
                option.add(actual, cost);

                Combinations cm = new Combinations(database,
                        count * i,
                        list1,
                        option);
                Future<Path> result = executor.submit(cm);
                resultList.add(result);
            }
            for (Future<Path> future : resultList) {
                try {
                    pathList.add(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            return Collections.min(pathList);
        }
    }
}
