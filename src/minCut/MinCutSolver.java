package minCut;

import RandomGraphGenerator.randomGenerator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class MinCutSolver {

    public static void main(String[] args) throws IOException {
        MinCutSolver solver = new MinCutSolver("sample", true);
        System.out.println("Min cut is " + solver.IterativeSolve(11000,  17666));
    }

    private Graph graph;

    public MinCutSolver(String graphFile, boolean isWeighted) throws FileNotFoundException {
        this.graph = new Graph(graphFile, isWeighted);
    }

    public MinCutSolver(ArrayList<ArrayList<Integer>> adjLst, ArrayList<ArrayList<Integer>> weights) {
        this.graph = new Graph(adjLst, weights);
    }


    public int solve(int source, int dest) {
        this.graph.resetGraph();
        int improvement = findResidual(source, dest, new HashMap<>());
        while (improvement != Integer.MAX_VALUE) {
            printGraph();
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

    private void printGraph() {
        System.out.println("##PRINT GRAPH##");
        for (int i = 0; i < graph.getNodes().size(); i++) {
            System.out.print(i + ": ");
            for (int j = 0; j < graph.getNodes().get(i).getAdjList().size(); j++) {
                System.out.print("(" + graph.getNodes().get(i).getAdjList().get(j) + "," + graph.getNodes().get(i).getWeights().get(j) + ")");
            }
            System.out.println();
        }
    }

    private static void verifier() throws IOException {
        int size = 2000;
        new randomGenerator().generate(size, 100000, "sample");

        long startTime = System.nanoTime();
        MinCutSolver solverIterative = new MinCutSolver("sample", false);
        ArrayList<Node> nodes = solverIterative.graph.getNodes();
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.get(i).getAdjList().size(); j++) {
                if (i < nodes.get(i).getAdjList().get(j)) {
                    solverIterative.IterativeSolve(i, nodes.get(i).getAdjList().get(j));
                }
            }
        }

        long endTime = System.nanoTime();
        System.out.println("Version 1: " + "Execution time in milliseconds : " +
                (endTime - startTime) / 1000000000);

    }

    public int IterativeSolve(int source, int dest) {
        this.graph.resetGraph();
        int total = 0;
        int improvement = findResidualIterative(source, dest, new HashMap<>());
        while (improvement != Integer.MAX_VALUE) {
            total += improvement;
            System.out.println("Now is " + total);
            improvement = findResidualIterative(source, dest, new HashMap<>());
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
            Node neighbourNode = graph.getNodes().get(neighbour);
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

                        for (int j = 0; j < neighbourNode.getAdjList().size(); j++) {
                            if (neighbourNode.getAdjList().get(j) == source) {
                                if (neighbourNode.getWeights().get(j) < neighbourNode.getCapacity().get(j)) {
                                    int diff = neighbourNode.getCapacity().get(j) - neighbourNode.getWeights().get(j);
                                    if (diff > bottleNeck) {
                                        neighbourNode.getWeights().set(j, neighbourNode.getWeights().get(j) + bottleNeck);
                                    } else {
                                        neighbourNode.getWeights().set(j, neighbourNode.getCapacity().get(j) + bottleNeck);
                                        node.getWeights().set(i, weight - bottleNeck + diff);
                                    }
                                }
                            }
                        }
                        return bottleNeck;
                    }
                }

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

    private int findResidualIterative(int source, int dest, HashMap<Integer, Boolean> visited) {
        Stack<Integer> searchStack = new Stack<>();
        Stack<Integer> bottleNeckStack = new Stack<>();
        Stack<Integer> childPointerStack = new Stack<>();
        searchStack.push(source + 1);
        childPointerStack.push(null);
        bottleNeckStack.push(Integer.MAX_VALUE);

        while(!searchStack.empty()) {
            boolean isDeeper = false;
            int next = searchStack.peek();
            if (next > 0) {
                next = next - 1;
            } else {
                next = -1 * next + 1;
            }

            int bottleNeck = bottleNeckStack.peek();
            visited.put(next, true);

            Node node = graph.getNodes().get(next);

            ArrayList<Integer> neighbours = node.getAdjList();
            for (int i = 0; i < neighbours.size(); i++) {
                int neighbour = neighbours.get(i);
                int weight = node.getWeights().get(i);
                if (!visited.getOrDefault(neighbour, false)) {
                    if (weight > 0) {
                        bottleNeck = Math.min(bottleNeck, weight);
                        if (neighbour != dest) {
                            searchStack.push(neighbour + 1);
                            bottleNeckStack.push(bottleNeck);
                            childPointerStack.push(i);
                            isDeeper = true;
                            break;
                        } else {
                            int previous = dest;
                            childPointerStack.push(i);
                            while (!searchStack.isEmpty()) {
                                int curChildPointer = childPointerStack.pop();
                                int k = searchStack.pop();
                                if (k > 0) {
                                    k = k - 1;
                                    Node cur = graph.getNodes().get(k);
                                    Node neighbourNode = graph.getNodes().get(previous);
                                    for (int i1 = 0; i1 < neighbourNode.getAdjList().size(); i1++) {
                                        if (neighbourNode.getAdjList().get(i1) == k) {
                                            int diff = neighbourNode.getCapacity().get(i1) - neighbourNode.getWeights().get(i1);
                                            if (diff > bottleNeck) {
                                                neighbourNode.getWeights().set(i1, neighbourNode.getWeights().get(i1) + bottleNeck);
                                            } else {
                                                neighbourNode.getWeights().set(i1, neighbourNode.getCapacity().get(i1));
                                                cur.getWeights().set(curChildPointer, cur.getWeights().get(curChildPointer) - bottleNeck + diff);
                                            }
                                            break;
                                        }
                                    }
                                } else {
                                    k = -1 * k - 1;
                                    Node neighbourNode = graph.getNodes().get(previous);
                                    for (int j = 0; j < neighbourNode.getAdjList().size(); j++) {
                                        if (neighbourNode.getAdjList().get(j) == k) {
                                            neighbourNode.getWeights().set(j, neighbourNode.getWeights().get(j) + bottleNeck);
                                            break;
                                        }
                                    }
                                }
                                previous = k;
                            }
                            return bottleNeck;
                        }
                    }
                    Node neighbourNode = graph.getNodes().get(neighbour);
                    for (int j = 0; j < neighbourNode.getAdjList().size(); j++) {
                        if (neighbourNode.getAdjList().get(j) == source) {
                            if (neighbourNode.getWeights().get(j) < neighbourNode.getCapacity().get(j)) {
                                searchStack.push(-1 * searchStack.pop());
                                searchStack.push(neighbour + 1);
                                childPointerStack.push(i);
                                bottleNeck = Math.min(bottleNeck,  neighbourNode.getCapacity().get(j) - neighbourNode.getWeights().get(j));
                                bottleNeckStack.push(bottleNeck);
                                isDeeper = true;
                                break;
                            }
                        }
                    }
                }
                if (isDeeper) {
                    break;
                }
            }
            if (!isDeeper) {
                searchStack.pop();
                childPointerStack.pop();
                bottleNeckStack.pop();
            }
        }
        return Integer.MAX_VALUE;
    }

}