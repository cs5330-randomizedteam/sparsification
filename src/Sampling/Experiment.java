package Sampling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;

import minCut.MinCutSolver;
import util.Const;

public class Experiment {

    private static final double[] epsilons = new double[] {0.05, 0.1, 0.15, 0.2, 0.25, 0.3};
    private static final int accuracySampleSize = 50;

    public static void main(String[] args) throws Exception {
        new Experiment().doExperiment("clique");
    }

    public void doExperiment(String file) throws Exception {
        Scanner in = new Scanner(new BufferedReader(new FileReader(Const.SAMPLED_DIR + file)));
        int nNodes = in.nextInt();
        in.close();
        System.out.println(file + " has " + nNodes + " nodes");
        for (int i = 0; i < epsilons.length; i++) {
            new EstimateSampler().sample(file, epsilons[i]);
            MinCutSolver origin = new MinCutSolver(file, false);
            MinCutSolver sampled = new MinCutSolver(file, true);
            double avgAccuracy = 0;
            for (int t = 0; t < accuracySampleSize; t++) {
                Random randomGenerator = new Random();
                int src = randomGenerator.nextInt() % nNodes;
                int des = randomGenerator.nextInt() % nNodes;
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
