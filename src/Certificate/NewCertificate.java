package Certificate;

import java.util.*;
import java.io.*;

import util.Const;

public class NewCertificate {

    //    private ArrayList<ArrayList<Edge>> E = new ArrayList<>();
    private HashMap<Integer, ArrayList<Edge>> E2 = new HashMap<>();
    private ArrayList<HashSet<Integer>> adjacentList = new ArrayList<>();
    private Set<Edge> edgeList = new TreeSet<Edge>();
    private UFDS uf;
    private int maxCount = 0;

    public NewCertificate(String dataFileName) throws FileNotFoundException {
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

    public NewCertificate(ArrayList<HashSet<Integer>> nbrList) {
        uf = new UFDS(nbrList.size());
        adjacentList = nbrList;
        for (int i = 0; i < nbrList.size(); i++) {
            for (int nbr: nbrList.get(i)) {
                if (i < nbr) {
                    edgeList.add(new Edge(i, nbr));
                }
            }
        }
    }

    public NewCertificate() {}

    public Set<Edge> generateCert(int k) throws Exception {
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
        clear();
        return edgeList;
    }

    /**
     * For undirected graph, need (src, des) and (des, src) both in adjList.
     */
    public void solve(ArrayList<HashSet<Integer>> adjList) throws Exception {
        int M = edgeList.size();
        System.out.println("Number of edge:" + M);
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
                int cur = r[rooty] + 1;
                maxCount = cur > maxCount ? cur : maxCount;
                if (!E2.containsKey(cur)) {
                    E2.put(cur, new ArrayList<>());
                }
                E2.get(cur).add(new Edge(x, y));
                if (E2.get(cur).size() >= uf.size() - 1) {
                    writeToFile(cur, E2.get(cur));
                    E2.remove(cur);
                }

//                if (r[rooty] + 1 >= E.size()) {
//                    for (int a = E.size(); a <= r[rooty] + 1; a++) {
//                        E.add(new ArrayList<>());
//                    }
//                }
//                E.get(r[rooty] + 1).add(new Edge(x, y));

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

    private void writeToFile(int id, ArrayList<Edge> edge) throws Exception {
        String fileName = Const.TEMPORARY_CERT_DIR + Const.TEMPORARY_CERT_FILE + id;
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(edge);
        oos.close();
        fos.close();
    }

    public Set<Edge> query(int k) throws Exception {
//        int mink = Math.min(k, E.size() - 1);
//        TreeSet<Edge> ans = new TreeSet<Edge>();
//        for (int i = 1; i <= mink; i++) {
//            ans.addAll(E.get(i));
//        }
//        return ans;
        int mink = Math.min(k, maxCount);
        TreeSet<Edge> ans = new TreeSet<Edge>();
        for (int i = 1; i <= mink; i++) {
            if (E2.containsKey(i)) {
                ans.addAll(E2.get(i));
            } else {
                ans.addAll(readEdges(i));
            }
        }
        return ans;
    }

    public ArrayList<Edge> readEdges(int id) throws Exception {
        String fileName = Const.TEMPORARY_CERT_DIR + Const.TEMPORARY_CERT_FILE + id;
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);

        ArrayList<Edge> edges = (ArrayList) ois.readObject();

        ois.close();
        fis.close();
        return edges;
    }

    public void clear() {
//        System.out.println("Start to clear");
        maxCount = 0;
        deleteAll();
        E2 = new HashMap<>();
    }

    private void deleteAll() {
        File folder = new File(Const.TEMPORARY_CERT_DIR);
        for (File file: folder.listFiles()) {
            if (!file.delete()) {
                System.out.println("ERROR\n");
            }
        }
    }
}


