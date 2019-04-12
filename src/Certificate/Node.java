package Certificate;

public class Node implements Comparable<Node> {

    public int node;
    public int rValue;

    public Node(int node) {
        this.node = node;
        this.rValue = 0;
    }

    @Override
    public int compareTo(Node o) {
        return this.rValue - o.rValue;
    }
}
