package Certificate;

public class Node implements Comparable<Node> {

    public int node;
    public int rValue;

    public Node(int node) {
        this.node = node;
        this.rValue = 0;
    }

    public Node(int node, int rValue) {
        this.node = node;
        this.rValue = rValue;
    }

    @Override
    public int compareTo(Node o) {
        if (this.rValue == o.rValue) {
            return this.node - o.node;
        }
        return this.rValue - o.rValue;
    }

}
