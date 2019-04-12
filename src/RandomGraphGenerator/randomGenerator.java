package RandomGraphGenerator;

import util.Const;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class randomGenerator {

    public static void main(String[] args) throws IOException {
        new randomGenerator().generate(7, 12, "sample");
    }


    public void generate(int size, int numEdges, String outputFile) throws IOException {
        ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
        ArrayList<Integer> collected = new ArrayList<>();
        ArrayList<Integer> uncollected = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            graph.add(new ArrayList<>());
            uncollected.add(i);
        }

        Random rand = new Random();
        while (!uncollected.isEmpty()) {
            int nextNodeIndex = rand.nextInt(uncollected.size());
            int nextNode = uncollected.get(nextNodeIndex);
            if (!collected.isEmpty()) {
                int toConnectNode = collected.get(rand.nextInt(collected.size()));
                graph.get(nextNode).add(toConnectNode);
                graph.get(toConnectNode).add(nextNode);
            }
            uncollected.remove(nextNodeIndex);
            collected.add(nextNode);
        }

        int count = 0;
        while (count < numEdges - size + 1) {
            int first = rand.nextInt(size);
            int second = rand.nextInt(size);
            if (first != second) {
                if (!graph.get(first).contains(second)) {
                    graph.get(first).add(second);
                    graph.get(second).add(first);
                }
                count++;
            }
        }

        for (int i = 0; i < size; i++) {
            System.out.print(i + ":");
            for (int j = 0; j < graph.get(i).size(); j++) {
                System.out.print(graph.get(i).get(j) + " ");
            }
            System.out.println();
        }

        FileOutputStream outputStream = new FileOutputStream(Const.OUTPUT_DIR + outputFile);
        outputStream.write(String.valueOf(size).getBytes());
        for (int i = 0; i < size; i++) {
            StringBuilder line = new StringBuilder(" " + graph.get(i).size());
            for (int j = 0; j < graph.get(i).size(); j++) {
                line.append(" ").append(graph.get(i).get(j));
            }
            outputStream.write(line.toString().getBytes());
        }
    }
}
