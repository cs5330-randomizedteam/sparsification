package Certificate;

import java.io.*;
import java.util.*;

import util.Const;

public class Estimate {

    public static void main(String[] args) throws Exception {
        estimateGraph("starClique1");
    }

    private static void estimateGraph(String fileName) throws Exception {
        ArrayList<HashSet<Integer>> adjacentList = new ArrayList<>();
        Scanner in = new Scanner(new BufferedReader(new FileReader(Const.OUTPUT_DIR + fileName)));
        int N = in.nextInt();
        int nEdges = 0;
        for (int i = 0; i < N; i++) {
            int M = in.nextInt();
            nEdges += M;
            HashSet<Integer> nbrs = new HashSet<>();
            for(int j = 0; j < M; j++) {
                int nbr = in.nextInt();
                nbrs.add(nbr);
            }
            adjacentList.add(nbrs);
        }
        nEdges /= 2;
        HashMap<Edge, Integer> edges = new HashMap<>();
        for (int i = 2; i <= nEdges; i *= 2) {
            if (edges.size() >= nEdges) break;
            Set<Edge> cert = new Certificate(adjacentList).generateCert(i);
            for (Edge ct: cert) {
                if (!edges.containsKey(ct)) {
                    edges.put(ct, i / 2);
                }
            }
        }

        edges.forEach((k, v) -> {
            if (k.src == 1 && k.des == 3) {
                System.out.printf("Estimation of edge (%d, %d) is %d\n", k.src, k.des, v);
            }
            if (k.src == 0) {
                System.out.printf("Estimation of edge (%d, %d) is %d\n", k.src, k.des, v);
            }
        });

    }
}
