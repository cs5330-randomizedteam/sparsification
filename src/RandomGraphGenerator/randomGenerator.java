package RandomGraphGenerator;

import util.Const;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class randomGenerator {

    public static void main(String[] args) throws IOException {
//        new randomGenerator().generate(1500, 800000, "sample1");
//        new randomGenerator().generate(800, 200000, "sample2");
//        new randomGenerator().generate(2000, 900000, "sample3");
//        new randomGenerator().generate(5000, 2500000, "sample4");
//
//        new randomGenerator().generate(1500, 700000, "sample5");
//        new randomGenerator().generate(800, 150000, "sample6");
//        new randomGenerator().generate(2000, 800000, "sample7");
//        new randomGenerator().generate(10000, 20000000, "random1");
//
        new randomGenerator().generateStarClique(10,  1500, "clique");
//
//        new randomGenerator().combineGraph("sample1", "sample2", "combined12", 500);
//        new randomGenerator().combineGraph("sample3", "sample4", "combined34", 150);
//        new randomGenerator().combineGraph("combined12", "combined34", "combined1234", 1500);
//
//
//        new randomGenerator().combineGraph("sample5", "sample6", "combined56", 500);
//        new randomGenerator().combineGraph("sample7", "sample8", "combined78", 150);
//        new randomGenerator().combineGraph("combined56", "combined78", "combined5678", 1500);
//
//        new randomGenerator().combineGraph("combined1234", "combined5678", "combined12345678", 2000);
//        new randomGenerator().combineGraph("combined12345678", "sample9", "combined", 500);


//        new randomGenerator().generateStarClique(10, 1500, "clique");


    }

    public void printGraph(ArrayList<ArrayList<Integer>> graph) {
        for (int i = 0; i < graph.size(); i++) {
            System.out.print(i + ":");
            for (int j = 0; j < graph.get(i).size(); j++) {
                System.out.print(graph.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }


    public void generate(int size, int numEdges, String outputFile) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(Const.OUTPUT_DIR + outputFile);

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
                    count++;
                }
            }
        }

        writeToFile(outputFile, graph);
    }

    private void writeToFile(String outputFile, ArrayList<ArrayList<Integer>> graph) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(Const.OUTPUT_DIR + outputFile);
        outputStream.write(String.valueOf(graph.size()).getBytes());
        for (int i = 0; i < graph.size(); i++) {
            StringBuilder line = new StringBuilder(" " + graph.get(i).size());
            for (int j = 0; j < graph.get(i).size(); j++) {
                line.append(" ").append(graph.get(i).get(j));
            }
            outputStream.write(line.toString().getBytes());
        }
        outputStream.flush();
        outputStream.close();
    }

    public void generateSpecialGraph(int num, int height, String outputFile) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(Const.OUTPUT_DIR + outputFile);
        int N = num * (height + 1);
        outputStream.write(String.valueOf(N).getBytes());
        for(int i = 0; i < num; i++) {
            StringBuilder line = new StringBuilder(" " + (2*(height+1)));
            line.append(String.format(" %d %d", (i+1)%num, (i-1+num)%num));
            for (int j = i+num; j < N; j += num) {
                int first = (i == 0 ? j + num - 1 : j - 1);
                line.append(String.format(" %d %d", first, j));
            }
            outputStream.write(line.toString().getBytes());
        }

        for (int i = num; i < N; i++) {
            StringBuilder line = new StringBuilder(" 2");
            line.append(String.format(" %d %d", i%num, (i%num+1)%num));
            outputStream.write(line.toString().getBytes());
        }
        outputStream.close();
    }


    public void generateStarClique(int numClique, int cliqueSize, String outputFile) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(Const.OUTPUT_DIR + outputFile);
        outputStream.write(String.valueOf(numClique * cliqueSize + 1).getBytes());

        StringBuilder line = new StringBuilder(" " + numClique);
        for (int i = 0; i < numClique; i++) {
            line.append(" ").append(1 + i * cliqueSize);
        }
        outputStream.write(line.toString().getBytes());

        int curNode = 1;
        for (int i = 0; i < numClique; i++) {
            System.out.println("##Clique " + i + ":");
            int cliqueNodeEnd = curNode + cliqueSize;
            for (int j = curNode; j < cliqueNodeEnd; j++) {
                if (j == curNode) {
                    line = new StringBuilder(" " + cliqueSize);
                    line.append(" ").append(0);
                } else {
                    line = new StringBuilder(" " + (cliqueSize - 1));
                }
                for (int k = curNode; k < cliqueNodeEnd; k++) {
                    if (k != j) {
                        line.append(" ").append(k);
                    }
                }
                outputStream.write(line.toString().getBytes());
            }
            curNode = cliqueNodeEnd;
        }
    }

    public void combineGraph(String graphFile1, String graphFile2, String outputFile, int numLinksInBetween) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(Const.OUTPUT_DIR + outputFile);

        Scanner graph1In = new Scanner(new FileInputStream(new File(Const.OUTPUT_DIR + graphFile1)));
        Scanner graph2In = new Scanner(new FileInputStream(new File(Const.OUTPUT_DIR + graphFile2)));

        int graph1Size = graph1In.nextInt();
        int graph2Size = graph2In.nextInt();


        outputStream.write(String.valueOf(graph1Size + graph2Size).getBytes());

        HashMap<Integer, ArrayList<Integer>> newEdges = new HashMap<>();

        int count = 0;
        while (count < numLinksInBetween) {
            Random rand = new Random();
            int node1 = rand.nextInt(graph1Size);
            int node2 = rand.nextInt(graph2Size);

            ArrayList<Integer> node1Neighbours = newEdges.getOrDefault(node1, null);
            ArrayList<Integer> node2Neighbours = newEdges.getOrDefault(node2 + graph1Size, null);

            if (node1Neighbours != null && node1Neighbours.contains(node2)) {
                continue;
            }

            if (node1Neighbours == null && node2Neighbours == null) {
                node1Neighbours = new ArrayList<>();
                node2Neighbours = new ArrayList<>();
                node1Neighbours.add(node2 + graph1Size);
                node2Neighbours.add(node1);
                newEdges.put(node1, node1Neighbours);
                newEdges.put(node2 + graph1Size, node2Neighbours);
            } else if (node1Neighbours == null) {
                node1Neighbours = new ArrayList<>();
                node1Neighbours.add(node2 + graph1Size);
                node2Neighbours.add(node1);
                newEdges.put(node1, node1Neighbours);
            } else if (node2Neighbours == null) {
                node2Neighbours = new ArrayList<>();
                node2Neighbours.add(node1);
                node1Neighbours.add(node2 + graph1Size);
                newEdges.put(node2 + graph1Size, node2Neighbours);
            } else {
                node1Neighbours.add(node2 + graph1Size);
                node2Neighbours.add(node1);
            }
            count++;
        }

        for (int j = 0; j < graph1Size; j++) {
            int numNeighbours = graph1In.nextInt();

            ArrayList<Integer> additional = newEdges.getOrDefault(j, null);

            StringBuilder line;
            if (additional != null) {
                line = new StringBuilder(" " + (numNeighbours + additional.size()));
                for (int i = 0; i < additional.size(); i++) {
                    line.append(" ").append(additional.get(i));
                }
            } else {
                line = new StringBuilder(" " + numNeighbours);
            }

            for (int k = 0; k < numNeighbours; k++) {
                line.append(" ").append(graph1In.nextInt());
            }
            outputStream.write(line.toString().getBytes());
        }

        graph1In.close();

        for (int j = graph1Size; j < graph2Size + graph1Size; j++) {
            int numNeighbours = graph2In.nextInt();
            ArrayList<Integer> additional = newEdges.getOrDefault(j, null);

            StringBuilder line;
            if (additional != null) {
                line = new StringBuilder(" " + (numNeighbours + additional.size()));
                for (int i = 0; i < additional.size(); i++) {
                    line.append(" ").append(additional.get(i));
                }
            } else {
                line = new StringBuilder(" " + numNeighbours);
            }

            for (int k = 0; k < numNeighbours; k++) {
                line.append(" ").append(graph2In.nextInt() + graph1Size);
            }
            outputStream.write(line.toString().getBytes());
        }

        graph2In.close();
        outputStream.flush();
        outputStream.close();
    }
}
