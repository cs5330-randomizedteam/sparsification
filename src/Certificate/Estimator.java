package Certificate;

import java.io.*;
import java.util.*;

import util.Const;

public class Estimator {

    private String fileName;

    public Estimator(String fileName) {
        this.fileName = fileName;
    }

    public static void main(String[] args) throws Exception {
        String inputFile = "sample";
        Estimator e = new Estimator(inputFile);
        e.readEstimation(inputFile);
    }

    public HashMap<String, Integer> estimateGraph() throws Exception {
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

        HashMap<String, Integer> edges = new HashMap<>();
        for (int i = 2; i <= nEdges; i *= 2) {
            if (edges.size() >= nEdges) break;
            Set<Edge> cert = new Certificate(adjacentList).generateCert(i);
            for (Edge ct: cert) {
                String str = ct.toString();
                if (!edges.containsKey(str)) {
                    edges.put(str, i / 2);
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

        edges.forEach((k, v) -> {
//            System.out.printf("Estimation of edge (%s) is %d\n", k, v);
            if (k.equals("1-3")) {
                System.out.printf("Estimation of edge (%s) is %d\n", k, v);
            }
            if (k.charAt(0) == '0') {
                System.out.printf("Estimation of edge (%s) is %d\n", k, v);
            }
        });
        return edges;
    }

    public void saveToFile(String file, HashMap<String, Integer> estimation) throws Exception {
        String saveFileName = Const.ESTIMATE_DIR + file;
        FileOutputStream fos = new FileOutputStream(saveFileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(estimation);
        oos.close();
        fos.close();
    }

    public HashMap<String, Integer> readEstimation(String file) throws IOException, ClassNotFoundException {
        String targetFile = Const.ESTIMATE_DIR + file;
        try {
            FileInputStream fis = new FileInputStream(targetFile);
            ObjectInputStream ois = new ObjectInputStream(fis);

            HashMap<String, Integer> edges = (HashMap<String, Integer>) ois.readObject();

            ois.close();
            fis.close();
            return edges;
        } catch (FileNotFoundException e) {
            try {
                HashMap<String, Integer> estimator = estimateGraph();
                saveToFile(file, estimator);
                return estimator;
            } catch (Exception ee) {
                ee.printStackTrace();
                return null;
            }
        }
    }
}
