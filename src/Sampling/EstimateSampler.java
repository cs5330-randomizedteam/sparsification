package Sampling;

import Certificate.Estimator;
import util.Const;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class EstimateSampler {

    public static void main(String[] args) throws Exception {
        new EstimateSampler().sample("combined", 0.1);
    }

    public void sample(String inputFile, double epsilon) throws Exception {
        HashMap<String, Integer> strengthMap = new Estimator(inputFile).readEstimation(inputFile);
        System.out.println("finish loading map");

        Scanner in = new Scanner(new BufferedReader(new FileReader(Const.OUTPUT_DIR + inputFile)));
        ArrayList<ArrayList<Integer>> adjLst = new ArrayList<>();
        ArrayList<ArrayList<Integer>> weights = new ArrayList<>();

        int size = in.nextInt();

        int d = 1;
        double p = d * Math.log(size) / (epsilon * epsilon);
        double originalSize = 0, sampledSize = 0;


        for (int i = 0; i < size; i++) {
            adjLst.add(new ArrayList<>());
            weights.add(new ArrayList<>());
        }

        for (int i = 0; i < size; i++) {
            int numNeighbours = in.nextInt();

            for (int j = 0; j < numNeighbours; j++) {
                int nextNeighbour = in.nextInt();
                if (i < nextNeighbour) {
                    String edgeKey = constructEdgeKey(i, nextNeighbour);
                    double pe = Math.min(1, p / strengthMap.get(edgeKey));
                    //System.out.println("Sampling probability is "+pe);

                    originalSize += 1;
                    if (isSample(pe)) {
                        sampledSize += 1;
                        adjLst.get(i).add(nextNeighbour);
                        adjLst.get(nextNeighbour).add(i);
                        int weight = (int) (1 / pe);
                        weights.get(i).add(weight);
                        weights.get(nextNeighbour).add(weight);
                    }
                }
            }
        }

        System.out.println("epsilon =" + epsilon + ", Compression rate = " + (sampledSize / originalSize));

        FileOutputStream outputStream = new FileOutputStream(Const.SAMPLED_DIR + inputFile);
        outputStream.write(String.valueOf(size).getBytes());
        for (int i = 0; i < size; i++) {
            ArrayList<Integer> neighbours = adjLst.get(i);
            ArrayList<Integer> neighWeights = weights.get(i);
            StringBuilder line = new StringBuilder(" " + neighbours.size());
            for (int j = 0; j < neighbours.size(); j++) {
                line.append(" ").append(neighbours.get(j)).append(" ").append(neighWeights.get(j));
            }
            outputStream.write(line.toString().getBytes());
        }
        outputStream.flush();
        outputStream.close();

    }

    private boolean isSample(double p) {
        Random rand = new Random();
        return rand.nextDouble() <= p;
    }

    private String constructEdgeKey(int source, int dest) {
        if (source < dest) {
            return source + "-" + dest;
        } else {
            return dest + "-" + source;
        }
    }
}
