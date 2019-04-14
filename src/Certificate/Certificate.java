package Certificate;

import java.util.*;
import java.io.*;

import util.Const;

public class Certificate {

    private ArrayList<ArrayList<Edge>> E = new ArrayList<>();
    private ArrayList<Set<Integer>> adjacentList = new ArrayList<>();
    private Set<Edge> edgeList = new TreeSet<Edge>();
    private UFDS uf;

    public Certificate(String dataFileName) throws FileNotFoundException {
        Scanner in = new Scanner(new BufferedReader(new FileReader(Const.OUTPUT_DIR + dataFileName)));
        int N = in.nextInt();
        uf = new UFDS(N);
        for (int i = 0; i < N; i++) {
            int M = in.nextInt();
            adjacentList.add(new HashSet<>());
            for(int j = 0; j < M; j++) {
                int nbr = in.nextInt();
                adjacentList.get(i).add(nbr);
                if (i < nbr) {
                    edgeList.add(new Edge(i, nbr));
                }
            }
        }
    }

    public Certificate(ArrayList<HashSet<Integer>> nbrList) {
        uf = new UFDS(nbrList.size());
        for (int i = 0; i < nbrList.size(); i++) {
            adjacentList.add(nbrList.get(i));
            for (int nbr: nbrList.get(i)) {
                if (i < nbr) {
                    edgeList.add(new Edge(i, nbr));
                }
            }
        }
    }

    public Certificate() {}

    public Set<Edge> generateCert(int k) {
        while (uf.size() > 1 && edgeList.size() > k * (uf.size() - 1)) {
            System.out.println("number of node: "+uf.size());
            clear();
            solve(this.adjacentList);
//            System.out.println("=====Edge List========");
//            edgeList.forEach(ct -> {
//                System.out.printf("%d, %d\n", ct.src, ct.des);
//            });
//            System.out.println("======Certificate=======");
            Set<Edge> cert = query(k);
//            cert.forEach(ct -> {
//                System.out.printf("%d, %d\n", ct.src, ct.des);
//            });
//            System.out.println("======UFDS=======");

            edgeList.removeAll(cert);

//            edgeList.forEach(ct -> {
//                System.out.printf("%d, %d\n", ct.src, ct.des);
//            });

            for (Edge e : edgeList) {
                uf.union(e.src, e.des);
            }

//            for(int i = 0; i < 7; i++) {
//                System.out.print(uf.find(i));
//            }
//            System.out.println();
//            System.out.println("=============");

            edgeList = cert;
        }
        return edgeList;
    }

    /**
     * For undirected graph, need (src, des) and (des, src) both in adjList.
     */
    public void solve(ArrayList<Set<Integer>> adjList) {
        int M = edgeList.size();
        System.out.println("Number of edge:" + M);
        for (int i = 0; i <= M; i++) {
            E.add(new ArrayList<>());
        }
        int[] r = new int[adjList.size()];
        HashMap<Edge, Integer> visited = new HashMap<>();

        TreeSet<Node> pq = new TreeSet<Node>();
        for (int i = 0; i < adjList.size(); i++) {
            pq.add(new Node(i));
        }
        while (!pq.isEmpty()) {
            Node nodex = pq.pollLast();
            int x = nodex.node;
            Set<Integer> nbrs = adjList.get(x);
            for (int y: nbrs) {
                int rootx = uf.find(x);
                int rooty = uf.find(y);
                if (rootx == rooty || visited.containsKey(new Edge(x, y))) {
                    continue;
                }
                E.get(r[rooty] + 1).add(new Edge(x, y));
                if (r[x] == r[rooty]) r[x]++;
//                pq.remove(new Node(rooty, r[rooty]));
                for (int i = 0; i < adjList.size(); i++) {
                    if (uf.find(i) == rooty) {
                        pq.remove(new Node(i, r[i]));
                        r[i]++;
                        pq.add(new Node(i, r[i]));
                    }
                }
//                pq.add(new Node(rooty, r[rooty]));
                visited.put(new Edge(x, y), 1);
//                visited.put(new Edge(y, x).hashCode(), 1);
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


