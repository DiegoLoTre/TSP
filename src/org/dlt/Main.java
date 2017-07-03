package org.dlt;

import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.dlt.model.Path;
import org.dlt.service.Database;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

class Main {

    private final JTextArea textArea;

    private JPanel infoPanel() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Dirección de Lista:");
        JTextField tfURL = new JTextField();
        JButton bStart = new JButton("Iniciar búsqueda");

        bStart.addActionListener(actionEvent -> {
            try {
                Database database = new Database(tfURL.getText());

                List<Integer> list = new ArrayList<>();
                for (int i = 1; i < database.getCities().size(); i++) {
                    list.add(database.getCities().get(i).getId());
                }
                System.out.println(list.size());
                System.out.println("Empezar a buscar el más óptimo");

                long start = System.currentTimeMillis();
                Path path;
                if (list.size() == 3)
                    path = new Combinations(database).get2Best(new Path(), list.get(0), list.get(1));
                else if (list.size() == 4)
                    path = new Combinations(database).get3Best(new Path(), list.get(0), list.get(1), list.get(2));
                else if (list.size() == 5)
                    path = new Combinations(database).get4Best(new Path(), list.get(0), list.get(1), list.get(2), list.get(3));
                else if (list.size() == 6)
                    path = new Combinations(database).get5Best(new Path(), list.get(0), list.get(1), list.get(2), list.get(3), list.get(4));
                else if (list.size() == 7)
                    path = new Combinations(database).get6Best(new Path(), list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5));
                else if (list.size() == 8)
                    path = new Combinations(database).get7Best(new Path(), list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6));
                else if (list.size() < 12)
                    path = new Combinations(database).getBest(new Path(), list);
                else {
                    path = null;
                    try {
                        ThreadPoolExecutor executor = (ThreadPoolExecutor)
                                Executors.newFixedThreadPool(list.size());

                        Combinations cm = new Combinations(database,
                                7920,
                                list,
                                new Path()
                        );
                        Future<Path> result = executor.submit(cm);
                        path = result.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                long end = System.currentTimeMillis();

                System.out.println("Tiempo de ejecución " + ((end - start) / 1000));
                String route = database.getRoute(path);
                textArea.append(route);

            } catch (IllegalArgumentException e) {
                textArea.append("La lista debe de contener al menos 3 puntos");
            } catch (ArrayIndexOutOfBoundsException e) {
                textArea.append("Archivo no seleccionado");
            } catch (FileNotFoundException e) {
                textArea.append("No se encontró el archivo");
            } catch (JsonMappingException | JsonEOFException e) {
                textArea.append("Archivo mal guardado");
            } catch (UnknownHostException e) {
                textArea.append("No esta conectado a internet");
            } catch (Exception e) {
                textArea.append("Error no contemplado");
                e.printStackTrace();
            }
            textArea.append("\n\n\n");
        });

        JButton bSearch = new JButton("Buscar Archivo");
        bSearch.addActionListener(actionEvent -> {

            JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int returnValue = jfc.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = jfc.getSelectedFile();
                tfURL.setText(selectedFile.getAbsolutePath());
            }
        });

        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(label);
        panel.add(tfURL);
        panel.add(bStart);
        panel.add(bSearch);

        return panel;
    }

    private Main() {
        this.textArea = new JTextArea (16, 54);

        textArea.setEditable(false);

        JFrame frame = new JFrame("Viajes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxlayout);

        JScrollPane sp = new JScrollPane(textArea);

        panel.add(infoPanel());
        panel.add(sp);

        frame.add(panel);
        frame.pack();
        frame.setSize(600, 300);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
