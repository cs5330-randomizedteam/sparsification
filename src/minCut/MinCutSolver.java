package minCut;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class MinCutSolver {

    public static void main(String[] args) throws FileNotFoundException {
        MinCutSolver solver = new MinCutSolver("sample");
        MinCutSolver solverIterative = new MinCutSolver("sample");

//        System.out.println("Min cut is " + solver.solve(0, 899));
//        System.out.println("Min cut is " + solverIterative.IterativeSolve(0, 899));

        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                if (i != j) {
                    if (solver.solve(i, j) != solverIterative.solve(i, j)) {
                        System.out.println("FAILED...");
                    }
                }
            }
        }
        System.out.println("PASSED");

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

    public int IterativeSolve(int source, int dest) {
        int improvement = findResidualIterative(source, dest, new HashMap<>());
        while (improvement != Integer.MAX_VALUE) {
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

    private int findResidualIterative(int source, int dest, HashMap<Integer, Boolean> visited) {
        Stack<Integer> searchStack = new Stack<>();
        Stack<Integer> bottleNeckStack = new Stack<>();
        searchStack.push(source + 1);

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
                            isDeeper = true;
                            break;
                        } else {
                            int previous = dest;
                            while (!searchStack.isEmpty()) {
                                int k = searchStack.pop();
                                if (k > 0) {
                                    k = k - 1;
                                    Node cur = graph.getNodes().get(k);
                                    for (int j = 0; j < cur.getAdjList().size(); j++) {
                                        if (cur.getAdjList().get(j) == previous) {
                                            cur.getWeights().set(j, cur.getWeights().get(j) - bottleNeck);
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
                                searchStack.push(searchStack.pop() * -1);
                                searchStack.push(neighbour + 1);
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
                bottleNeckStack.pop();
            }
        }
        return Integer.MAX_VALUE;
    }

}
