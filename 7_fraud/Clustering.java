import edu.princeton.cs.algs4.CC;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;

public class Clustering {

    private CC cc; // list of all clusters
    private int m; // length of original array
    private int k; // number of clusters reduced to

    // run the clustering algorithm and create the clusters
    public Clustering(Point2D[] locations, int k) {

        if (locations == null)
            throw new IllegalArgumentException("null locations array.");
        if (k < 1 || k > locations.length)
            throw new IllegalArgumentException("k is out of bound.");

        m = locations.length;
        this.k = k;
        // 1. add edge between every two locations
        EdgeWeightedGraph graph = new EdgeWeightedGraph(m);
        for (int i = 0; i < m; i++) {
            for (int j = i + 1; j < m; j++) {
                double distance = locations[i].distanceTo(locations[j]);
                graph.addEdge(new Edge(i, j, distance)); // distance = edge weight
            }
        }

        // 2. compute mst of the graph
        KruskalMST mst = new KruskalMST(graph);

        // 3. form cluster graph with lowest m-k edges
        int count = m - k;
        int index = 0;
        EdgeWeightedGraph clusterGraph = new EdgeWeightedGraph(m);
        for (Edge e : mst.edges()) {
            if (index < count) {
                clusterGraph.addEdge(e);
                index++;
            }
            else break;
        }

        // 4. identity all clusters
        cc = new CC(clusterGraph);
    }

    // return the cluster of the ith point
    public int clusterOf(int i) {
        if (i < 0 || i > m - 1)
            throw new IllegalArgumentException("i is ount of bound.");
        return cc.id(i);
    }

    // use the clusters to reduce the dimensions of an input
    public int[] reduceDimensions(int[] input) {
        if (input == null)
            throw new IllegalArgumentException("null input array.");
        if (input.length != m)
            throw new IllegalArgumentException("wrong dimensions for input array.");

        int[] reduced = new int[k];
        for (int i = 0; i < m; i++) {
            reduced[clusterOf(i)] += input[i];
        }
        return reduced;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Point2D[] locations = {
                new Point2D(1, 2), new Point2D(0, 0), new Point2D(4, 3),
                new Point2D(2, 3), new Point2D(4, 5), new Point2D(2, 1)
        }; // location array

        int k = 3; // number of clusters
        Clustering clustering = new Clustering(locations, k);
        StdOut.println(clustering.clusterOf(0));
        // StdOut.println(clustering.clusterOf(4));
        int[] array = { 1, 2, 3, 4, 5, 6 };
        int[] reduced = clustering.reduceDimensions(array);
        for (int i = 0; i < k; i++) {
            StdOut.println(reduced[i]);
        }
    }
}
