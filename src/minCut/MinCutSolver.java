package minCut;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class MinCutSolver {

    public static void main(String[] args) throws FileNotFoundException {
        MinCutSolver solver = new MinCutSolver("sample");
        System.out.println("Min cut is " + solver.solve(1, 2));

    }

    private Graph graph;

    public MinCutSolver(String graphFile) throws FileNotFoundException {
        this.graph = new Graph(graphFile);
    }

    public int solve(int source, int dest) {
        int improvement = findResidual(source, dest, new HashMap<>());
        while (improvement != Integer.MAX_VALUE) {
            improvement = findResidual(source, dest, new HashMap<>());
        }

        int totalFlow = 0;
        Node destNode = this.graph.getNodes().get(dest);
        for (int i = 0; i < destNode.getAdjList().size(); i++) {
            Node neighbourNode = this.graph.getNodes().get(destNode.getAdjList().get(i));
            for (int j = 0; j < neighbourNode.getAdjList().size(); j++) {
                if (neighbourNode.getAdjList().get(j) == dest) {
                    totalFlow += neighbourNode.getCapacity().get(j) - neighbourNode.getWeights().get(j);
                }
            }
        }
        return totalFlow;
    }

    private int findResidual(int source, int dest, HashMap<Integer, Boolean> visited) {
        Node node = graph.getNodes().get(source);
        visited.put(source, true);

        ArrayList<Integer> neighbours = node.getAdjList();
        for (int i = 0; i < neighbours.size(); i++) {
            int neighbour = neighbours.get(i);
            int weight = node.getWeights().get(i);
            if (!visited.getOrDefault(neighbour, false)) {
                if (weight > 0) {
                    int bottleNeck;
                    if (neighbour != dest) {
                        bottleNeck = findResidual(neighbour, dest, visited);
                    } else {
                        bottleNeck = weight;
                    }
                    if (bottleNeck != Integer.MAX_VALUE) {
                        bottleNeck = Math.min(bottleNeck, weight);
                        node.getWeights().set(i, weight - bottleNeck);
                        return bottleNeck;
                    }
                }

                Node neighbourNode = graph.getNodes().get(neighbour);
                for (int j = 0; j < neighbourNode.getAdjList().size(); j++) {
                    if (neighbourNode.getAdjList().get(j) == source) {
                        if (neighbourNode.getWeights().get(j) < neighbourNode.getCapacity().get(j)) {
                            int bottleNeck = findResidual(neighbours.get(i), dest, visited);
                            if (bottleNeck != Integer.MAX_VALUE) {
                                bottleNeck = Math.min(bottleNeck, neighbourNode.getCapacity().get(j) - neighbourNode.getWeights().get(j));
                                neighbourNode.getWeights().set(j, neighbourNode.getWeights().get(j) + bottleNeck);
                                return bottleNeck;
                            }
                        }
                    }
                }
            }
        }

        return Integer.MAX_VALUE;
    }

}
