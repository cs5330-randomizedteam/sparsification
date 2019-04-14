package minCut;

import util.Const;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Graph {
    private ArrayList<Node> nodes;

    public Graph(String dataFileName, boolean isWeighted) throws FileNotFoundException {
        Scanner in;
        if (isWeighted) {
            in = new Scanner(new BufferedReader(new FileReader(Const.SAMPLED_DIR + dataFileName)));
        } else {
            in = new Scanner(new BufferedReader(new FileReader(Const.OUTPUT_DIR + dataFileName)));
        }
        nodes = new ArrayList<>();

        int size = in.nextInt();
        for (int i = 0; i < size; i++) {
            int numNeighbours = in.nextInt();
            Node node = new Node(i, numNeighbours);
            for (int j = 0; j < numNeighbours; j++) {
                node.getAdjList().add(in.nextInt());
                if (isWeighted) {
                    int weight = in.nextInt();
                    node.getWeights().set(j, weight);
                    node.getCapacity().set(j, weight);
                }
            }
            nodes.add(node);
        }
        System.out.println("FINISHED LOADING");
    }

    public Graph(ArrayList<ArrayList<Integer>> adjLst, ArrayList<ArrayList<Integer>> weights) {
        nodes = new ArrayList<>();

        for (int i = 0; i < adjLst.size(); i++) {
            int numNeighbours = adjLst.get(i).size();
            Node node = new Node(i, numNeighbours, adjLst.get(i), weights.get(i));
            nodes.add(node);
        }
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void resetGraph() {
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).resetWeights();
        }
    }
}
