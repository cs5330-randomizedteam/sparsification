package Sampling;

import util.Const;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class StartCliqueSampler {

    public static void main(String[] args) throws IOException {
        new StartCliqueSampler().sample("starClique1");
    }

    public void sample(String inputFile) throws IOException {
        Scanner in = new Scanner(new BufferedReader(new FileReader(Const.OUTPUT_DIR + inputFile)));
        ArrayList<ArrayList<Integer>> adjLst = new ArrayList<>();
        ArrayList<ArrayList<Integer>> weights = new ArrayList<>();

        int size = in.nextInt();

        int d = 1;
        double epsilon = 0.3;
        double p = d * Math.log(size) / (epsilon * epsilon);
        double originalSize = 0, sampledSize = 0;


        for (int i = 0; i < size; i++) {
            adjLst.add(new ArrayList<>());
            weights.add(new ArrayList<>());
        }

        for (int i = 0; i < size; i++) {
            if (i % 1000 == 0) {
                System.out.println(i);
            }
            int numNeighbours = in.nextInt();
            double pe = p;

            if (i != 0) {
                pe = p / numNeighbours;
            }

            pe = Math.min(1, pe);

            for (int j = 0; j < numNeighbours; j++) {
                int nextNeighbour = in.nextInt();
                if (i < nextNeighbour) {
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

        System.out.println("Compression rate = " + (sampledSize / originalSize));

        FileOutputStream outputStream = new FileOutputStream(Const.SAMPLED_DIR + "sample");
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

}
