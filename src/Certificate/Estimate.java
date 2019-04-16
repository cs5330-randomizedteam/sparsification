package Certificate;

import java.io.*;
import java.util.*;

import minCut.MinCutSolver;
import util.Const;

public class Estimate {

    public static void main(String[] args) throws Exception {
        estimateGraph("combined");
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
        in.close();
        nEdges /= 2;
        System.out.printf("finish loading %d vertices and %d edges\n", N, nEdges);
        

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

//        int exactMinCutSum = 0;
//        int estimationSum = 0;
//        MinCutSolver solver = new MinCutSolver("combined", false);
//
//        for (Edge edge: edges.keySet()) {
//            if (edge.src % 100 == 0) {
//                int estimation = edges.get(edge);
//                int exact = solver.IterativeSolve(edge.src, edge.des);
//                System.out.println("Estimation: " + estimation + " Exact: " + exact);
//                estimationSum += estimation;
//                exactMinCutSum += exact;
//            }
//        }
//
//        System.out.println("Exact min cut sum is " + exactMinCutSum);
//        System.out.println("Estimation sum is " + estimationSum);

//        edges.forEach((k, v) -> {
//            System.out.printf("Estimation of edge (%d, %d) is %d\n", k.src, k.des, v);
//            if (k.src == 1 && k.des == 3) {
//                System.out.printf("Estimation of edge (%d, %d) is %d\n", k.src, k.des, v);
//            }
//            if (k.src == 0) {
//                System.out.printf("Estimation of edge (%d, %d) is %d\n", k.src, k.des, v);
//            }
//        });

    }
}
