package minCut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node {
    private int nodeId;
    private ArrayList<Integer> adjList;
    private ArrayList<Integer> capacity;
    ArrayList<Integer> weights;

    public Node(int id, int numNeighbours) {
        // constructor for unweighted graph
        this.nodeId = id;
        this.adjList = new ArrayList<>();
        this.weights = new ArrayList<Integer>(Collections.nCopies(numNeighbours, 1));
        this.capacity =  new ArrayList<Integer>(Collections.nCopies(numNeighbours, 1));
    }

    public Node(int id, int numNeighbours, List<Integer> adjList) {
        // constructor for unweighted graph
        this.nodeId = id;
        this.adjList = new ArrayList<>(adjList);
        this.weights = new ArrayList<Integer>(Collections.nCopies(numNeighbours, 1));
        this.capacity =  new ArrayList<Integer>(Collections.nCopies(numNeighbours, 1));
    }

    public void resetWeights() {
        this.weights = new ArrayList<Integer>(Collections.nCopies(this.weights.size(), 1));
    }

    public int getNodeId() {
        return nodeId;
    }

    public ArrayList<Integer> getAdjList() {
        return adjList;
    }

    public ArrayList<Integer> getCapacity() {
        return capacity;
    }

    public ArrayList<Integer> getWeights() {
        return weights;
    }

}
