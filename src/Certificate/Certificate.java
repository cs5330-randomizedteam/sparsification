package Certificate;

import java.util.*;
import java.io.*;

public class Certificate {

    private ArrayList<ArrayList<Edge>> E = new ArrayList<>();

    /**
     * For undirected graph, need (src, des) and (des, src) both in adjList.
     */
    public void solve(ArrayList<ArrayList<Integer>> adjList) {
        int M = 0;
        for (ArrayList<Integer> nbrs: adjList) {
            M += nbrs.size();
        }
        M /= 2;
        System.out.println(M);
        for (int i = 0; i <= M; i++) {
            E.add(new ArrayList<>());
        }

        int N = adjList.size();
        int[] r = new int[N];
        HashMap<Integer, Integer> visited = new HashMap<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < adjList.get(i).size(); j++) {
                visited.put(new Edge(i, adjList.get(i).get(j)).hashCode(), 0);
            }
        }

        TreeSet<Node> pq = new TreeSet<Node>();
        for (int i = 0; i < N; i++) {
            pq.add(new Node(i));
        }
        while (!pq.isEmpty()) {
            Node nodex = pq.pollLast();
            int x = nodex.node;
            ArrayList<Integer> nbrs = adjList.get(x);
            for (int y: nbrs) {
                if (y == x || visited.get(new Edge(x, y).hashCode()) == 1) {
                    continue;
                }

                E.get(r[y] + 1).add(new Edge(x, y));
                if (r[x] == r[y]) r[x]++;
                pq.remove(new Node(y, r[y]));
                r[y]++;
                pq.add(new Node(y, r[y]));
                visited.put(new Edge(x, y).hashCode(), 1);
                visited.put(new Edge(y, x).hashCode(), 1);
            }
        }
    }

    public Set<Edge> query(int k) {
        TreeSet<Edge> ans = new TreeSet<Edge>();
        for (int i = 1; i <= k; i++) {
            ans.addAll(E.get(i));
        }
        return ans;
    }

    public void clear() {
        E = new ArrayList<>();
    }
}


