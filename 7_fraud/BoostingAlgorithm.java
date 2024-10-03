import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.LinkedList;

public class BoostingAlgorithm {
    // list of decision stumps
    private LinkedList<WeakLearner> weakLearnersList = new LinkedList<>();
    private Clustering clustering; // clustering object for all pts
    private double[] weights; // array of n weights
    private int[] labels; // array of n labels
    private int[][] reducedInput; // array of n inputs of k dimensions

    // private int n; // number of points
    // private int k; // dimension of pt after reduction

    // create the clusters and initialize your data structures
    public BoostingAlgorithm(int[][] input, int[] labels, Point2D[] locations, int k) {

        if (input == null || labels == null || locations == null)
            throw new IllegalArgumentException("calling constructor with null.");
        if (input.length != labels.length)
            throw new IllegalArgumentException("incompatible input/weights/labels array.");
        for (int i = 0; i < labels.length; i++) {
            if (labels[i] != 0 && labels[i] != 1)
                throw new IllegalArgumentException("illegal labels.");
        }
        if (k < 1 || k > input[0].length)
            throw new IllegalArgumentException("illegal k.");

        int n = input.length;
        // this.k = k;
        this.labels = labels;
        clustering = new Clustering(locations, k);
        weights = new double[n];
        double initWeight = 1.0 / n;

        // reduce dimensions from n-by-n to n-by-k
        reducedInput = new int[n][k];
        for (int i = 0; i < n; i++) {
            reducedInput[i] = clustering.reduceDimensions(input[i]);
        }

        // initialize weights[] array
        for (int i = 0; i < n; i++) {
            weights[i] = initWeight;
        }
    }

    // return the current weight of the ith point
    public double weightOf(int i) {
        return weights[i];
    }

    // apply one step of the boosting algorithm
    public void iterate() {
        WeakLearner weakLearner = new WeakLearner(reducedInput, weights, labels);
        weakLearnersList.add(weakLearner);

        double newTotalWeight = 0;

        // update weights[] array
        for (int i = 0; i < weights.length; i++) {
            // consider the ith input pt
            int prediction = weakLearner.predict(reducedInput[i]);
            if (prediction != labels[i]) {
                weights[i] *= 2; // double weight of misclassified pt
            }
            newTotalWeight += weights[i];
        }

        // re-normalizing the weights
        for (int i = 0; i < weights.length; i++) {
            weights[i] /= newTotalWeight;
        }
    }

    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {

        int size = weakLearnersList.size();
        int[] predictions = new int[size];
        int[] reducedSample = clustering.reduceDimensions(sample);

        // get all predictions
        for (int i = 0; i < size; i++) {
            WeakLearner learner = weakLearnersList.get(i);
            predictions[i] = learner.predict(reducedSample);
        }

        int count1 = 0;
        int count0 = 0;
        for (int i = 0; i < size; i++) {
            if (predictions[i] == 0) count0++;
            if (predictions[i] == 1) count1++;
        }

        // return the majority prediction
        if (count0 >= count1) return 0;
        else return 1;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // read in the terms from a file
        DataSet training = new DataSet(args[0]);
        DataSet testing = new DataSet(args[1]);
        int k = Integer.parseInt(args[2]);
        int T = Integer.parseInt(args[3]);

        Stopwatch stopwatch = new Stopwatch();

        int[][] trainingInput = training.getInput();
        int[][] testingInput = testing.getInput();
        int[] trainingLabels = training.getLabels();
        int[] testingLabels = testing.getLabels();
        Point2D[] trainingLocations = training.getLocations();

        // train the model
        BoostingAlgorithm model = new BoostingAlgorithm(trainingInput, trainingLabels,
                                                        trainingLocations, k);
        for (int t = 0; t < T; t++)
            model.iterate();

        // calculate the training data set accuracy
        double trainingAccuracy = 0;
        for (int i = 0; i < training.getN(); i++)
            if (model.predict(trainingInput[i]) == trainingLabels[i])
                trainingAccuracy += 1;
        trainingAccuracy /= training.getN();

        // calculate the test data set accuracy
        double testAccuracy = 0;
        for (int i = 0; i < testing.getN(); i++)
            if (model.predict(testingInput[i]) == testingLabels[i])
                testAccuracy += 1;
        testAccuracy /= testing.getN();

        StdOut.println("Training accuracy of model: " + trainingAccuracy);
        StdOut.println("Test accuracy of model: " + testAccuracy);

        double time = stopwatch.elapsedTime();
        StdOut.println("time: " + time);
    }
}
