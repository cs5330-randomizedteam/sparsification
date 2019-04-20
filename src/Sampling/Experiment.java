package Sampling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import minCut.MinCutSolver;
import util.Const;

public class Experiment {

    private static final double[] epsilons = new double[] {0.2, 0.25, 0.3, 0.35, 0.4};
    private static final int accuracySampleSize = 10;

    public static void main(String[] args) throws Exception {
        new Experiment().doExperiment("random");
    }

    public void doExperiment(String file) throws Exception {
        Scanner in = new Scanner(new BufferedReader(new FileReader(Const.OUTPUT_DIR + file)));
        int nNodes = in.nextInt();
        in.close();
        System.out.println(file + " has " + nNodes + " nodes");

        ArrayList<Integer> samples = new ArrayList<>();

        for (int i = 0; i < accuracySampleSize; i++) {
            Random randomGenerator = new Random();
            samples.add(randomGenerator.nextInt(nNodes) % nNodes);
            samples.add(randomGenerator.nextInt(nNodes) % nNodes);
        }

        for (int i = 0; i < epsilons.length; i++) {
            new EstimateSampler().sample(file, epsilons[i]);
            MinCutSolver origin = new MinCutSolver(file, false);
            MinCutSolver sampled = new MinCutSolver(file, true);
            double avgAccuracy = 0;
            for (int t = 0; t < accuracySampleSize; t++) {
                int src = samples.get(2 * t);
                int des = samples.get(2 * t + 1);
                System.out.println(t + ": (" + src + ", " + des + ")");
                int actualMincut = origin.IterativeSolve(src, des);
                int sampledMincut = sampled.IterativeSolve(src, des);
                double accuracy = 1.0 * Math.abs(actualMincut - sampledMincut) / actualMincut;
                avgAccuracy += accuracy;
            }
            avgAccuracy /= accuracySampleSize;
            System.out.printf("(%f, %f),", epsilons[i], avgAccuracy);
        }
    }
}
