package minCut;

import util.Const;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Graph {
    private ArrayList<Node> nodes;

    public Graph(String dataFileName) throws FileNotFoundException {
        Scanner in = new Scanner(new BufferedReader(new FileReader(Const.OUTPUT_DIR + dataFileName)));
        nodes = new ArrayList<>();

        int size = in.nextInt();
        for (int i = 0; i < size; i++) {
            int numNeighbours = in.nextInt();
            Node node = new Node(i, numNeighbours);
            for (int j = 0; j < numNeighbours; j++) {
                node.getAdjList().add(in.nextInt());
            }
            nodes.add(node);
        }
    }

    public Graph(Graph toCopy) {
        nodes = new ArrayList<>();

        for (int i = 0; i < toCopy.getNodes().size(); i++) {
            int numNeighbours = toCopy.nodes.get(i).getAdjList().size();
            Node node = new Node(i, numNeighbours, toCopy.getNodes().get(i).getAdjList());
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
