import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class WeakLearner {

    private int k; // k
    private int dimensionPredictor; // optimal dp
    private int valuePredictor;     // optimal vp
    private int signPredictor;      // optimal sp

    // constructor + training the weakLearner
    public WeakLearner(int[][] input, double[] weights, int[] labels) {
        // throw exceptions
        if (input == null || weights == null || labels == null)
            throw new IllegalArgumentException("calling constructor with null.");
        if (input.length != weights.length || weights.length != labels.length)
            throw new IllegalArgumentException(
                    "incompatible lenghth for input/weights/labels array.");
        for (int i = 0; i < labels.length; i++) {
            if (labels[i] != 0 && labels[i] != 1)
                throw new IllegalArgumentException("illegal labels.");
            if (weights[i] < 0)
                throw new IllegalArgumentException("weights not all non-negative.");
        }

        int n = input.length; // number of pts
        this.k = input[0].length; // dimension of each pt
        int[] dp = new int[k];
        int[] vp = new int[n];
        int[] predictedLabels = new int[n];
        double maxSumOfWeights = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < k; i++) {
            dp[i] = i;
            for (int j = 0; j < n; j++) {
                for (int sp = 0; sp <= 1; sp++) {
                    vp[j] = input[j][i];

                    double sumOfWeights = 0;
                    for (int m = 0; m < n; m++) {
                        if (sp == 0) {
                            if (input[m][dp[i]] <= vp[j]) predictedLabels[m] = 0;
                            else predictedLabels[m] = 1;
                        }
                        if (sp == 1) {
                            if (input[m][dp[i]] <= vp[j]) predictedLabels[m] = 1;
                            else predictedLabels[m] = 0;
                        }
                        if (predictedLabels[m] == labels[m]) {
                            sumOfWeights += weights[m];
                        }
                        if (sumOfWeights > maxSumOfWeights) {
                            maxSumOfWeights = sumOfWeights;
                            dimensionPredictor = i;
                            valuePredictor = vp[j];
                            signPredictor = sp;
                        }
                    }
                }
            }
        }
    }

    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {
        if (sample == null || sample.length != k)
            throw new IllegalArgumentException("invalid sample.");

        if (sample[dimensionPredictor] <= valuePredictor) return signPredictor;
        else return 1 - signPredictor;
    }


    // return the dimension the learner uses to separate the data
    public int dimensionPredictor() {
        return dimensionPredictor;
    }

    // return the value the learner uses to separate the data
    public int valuePredictor() {
        return valuePredictor;
    }

    // return the sign the learner uses to separate the data
    public int signPredictor() {
        return signPredictor;
    }

    // unit testing (required) must call all methods.
    public static void main(String[] args) {

        In datafile = new In(args[0]);
        int n = datafile.readInt();
        int k = datafile.readInt();

        int[][] input = new int[n][k];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                input[i][j] = datafile.readInt();
            }
        }
        int[] labels = new int[n];
        for (int i = 0; i < n; i++) {
            labels[i] = datafile.readInt();
        }
        double[] weights = new double[n];
        for (int i = 0; i < n; i++) {
            weights[i] = datafile.readDouble();
        }

        // print predictors
        WeakLearner weakLearner = new WeakLearner(input, weights, labels);
        StdOut.printf("vp = %d, dp = %d, sp = %d\n", weakLearner.valuePredictor(),
                      weakLearner.dimensionPredictor(), weakLearner.signPredictor());

    }
}


/******
 * efficient implementation, not passing test1 and test3 of weakLearner.java
 *
 * import edu.princeton.cs.algs4.In;
 * import edu.princeton.cs.algs4.StdOut;
 *
 * import java.util.Arrays;
 *
 * public class WeakLearner {
 *
 *     private int n; // number of sample pts
 *     private int k; // k-dimensional each
 *
 *     private int dimensionPredictor; // optimal dp
 *     private int valuePredictor; // optimal vp
 *     private int signPredictor; // optimal sp
 *
 *     // private class for coordinates
 *     private class Coordinate implements Comparable<Coordinate> {
 *         double index;  // compare using this index
 *         double weight; // weight
 *         int label;     // label
 *
 *         // coordinate data type
 *         private Coordinate(double index, double weight, int label) {
 *             this.index = index;
 *             this.weight = weight;
 *             this.label = label;
 *         }
 *
 *         // compareTo() for Coordinate
 *         public int compareTo(Coordinate coordinate) {
 *             return Double.compare(coordinate.index, index);
 *         }
 *     }
 *
 *     // constructor + training the weakLearner
 *     public WeakLearner(int[][] input, double[] weights, int[] labels) {
 *         // throw exceptions
 *         if (input == null || weights == null || labels == null)
 *             throw new IllegalArgumentException("calling constructor with null.");
 *         if (input.length != weights.length || weights.length != labels.length)
 *             throw new IllegalArgumentException(
 *                     "incompatible lenghth for input/weights/labels array.");
 *         for (int i = 0; i < labels.length; i++) {
 *             if (labels[i] != 0 && labels[i] != 1)
 *                 throw new IllegalArgumentException("illegal labels.");
 *             if (weights[i] < 0)
 *                 throw new IllegalArgumentException("weights not all non-negative.");
 *         }
 *
 *         this.n = input.length; // number of pts
 *         this.k = input[0].length; // dimension of each pt
 *
 *         Coordinate[] coordinates = new Coordinate[n * k]; // array of coordinates
 *         for (int i = 0; i < n; i++) {
 *             for (int j = 0; j < k; j++) {
 *                 coordinates[i * k + j] =
 *                         new Coordinate(input[i][j], weights[i], labels[i]);
 *             }
 *         }
 *
 *         Arrays.sort(coordinates); // sort based on index
 *         double accuracy = 0;
 *         double sumOfWeights = 0;
 *
 *         for (int i = 0; i < n; i++) {
 *             sumOfWeights += coordinates[i].weight;
 *             if (coordinates[i].label == 1) {
 *                 accuracy += coordinates[i].weight;
 *             }
 *         }
 *
 *         valuePredictor = (int) coordinates[0].index;
 *         dimensionPredictor = 0;
 *         signPredictor = 1;
 *
 *         // flipping signs
 *         if (accuracy > sumOfWeights - accuracy) {
 *             signPredictor = 0;
 *         }
 *
 *         double maxWeightedAccuracy = Math.max(accuracy, sumOfWeights - accuracy);
 *
 *         for (int i = 1; i < n; i++) {
 *             double currentAccuracy = accuracy;
 *             double currentSumOfWeights = sumOfWeights;
 *
 *             if (coordinates[i].index != coordinates[i - 1].index) {
 *                 if (coordinates[i - 1].label == 1) {
 *                     currentAccuracy -= coordinates[i - 1].weight;
 *                 }
 *                 currentSumOfWeights -= coordinates[i - 1].weight;
 *
 *                 if (currentAccuracy > maxWeightedAccuracy) {
 *                     maxWeightedAccuracy = currentAccuracy;
 *                     valuePredictor = (int) coordinates[i].index;
 *                     dimensionPredictor = i % k;
 *                     signPredictor = 1;
 *                 }
 *
 *                 if (currentSumOfWeights - currentAccuracy > maxWeightedAccuracy) {
 *                     maxWeightedAccuracy = currentSumOfWeights - currentAccuracy;
 *                     valuePredictor = (int) coordinates[i].index;
 *                     dimensionPredictor = i % k;
 *                     signPredictor = 0;
 *                 }
 *             }
 *
 *             accuracy = currentAccuracy;
 *             sumOfWeights = currentSumOfWeights;
 *         }
 *     }
 *
 *     // return the prediction of the learner for a new sample
 *     public int predict(int[] sample) {
 *         if (sample == null || sample.length != k)
 *             throw new IllegalArgumentException("invalid sample.");
 *
 *         if (sample[dimensionPredictor] <= valuePredictor) return signPredictor;
 *         else return 1 - signPredictor;
 *     }
 *
 *
 *     // return the dimension the learner uses to separate the data
 *     public int dimensionPredictor() {
 *         return dimensionPredictor;
 *     }
 *
 *     // return the value the learner uses to separate the data
 *     public int valuePredictor() {
 *         return valuePredictor;
 *     }
 *
 *     // return the sign the learner uses to separate the data
 *     public int signPredictor() {
 *         return signPredictor;
 *     }
 *
 *     // unit testing (required) must call all methods.
 *     public static void main(String[] args) {
 *
 *         In datafile = new In(args[0]);
 *         int n = datafile.readInt();
 *         int k = datafile.readInt();
 *
 *         int[][] input = new int[n][k];
 *         for (int i = 0; i < n; i++) {
 *             for (int j = 0; j < k; j++) {
 *                 input[i][j] = datafile.readInt();
 *             }
 *         }
 *         int[] labels = new int[n];
 *         for (int i = 0; i < n; i++) {
 *             labels[i] = datafile.readInt();
 *         }
 *         double[] weights = new double[n];
 *         for (int i = 0; i < n; i++) {
 *             weights[i] = datafile.readDouble();
 *         }
 *
 *         // print predictors
 *         WeakLearner weakLearner = new WeakLearner(input, weights, labels);
 *         StdOut.printf("vp = %d, dp = %d, sp = %d\n", weakLearner.valuePredictor(),
 *                       weakLearner.dimensionPredictor(),
 *                       weakLearner.signPredictor());
 *     }
 * }
 *
 * *******/
