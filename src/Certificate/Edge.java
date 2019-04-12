package Certificate;

import java.util.Objects;

public class Edge implements Comparable<Edge> {

    public int src;
    public int des;

    public Edge(int src, int des) {
        this.src = src;
        this.des = des;
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, des);
    }

    @Override
    public int compareTo(Edge o) {
        if ((this.src == o.src && this.des == o.des)
                || (this.src == o.des && this.des == o.src)) {
            return 0;
        }
        return -1;
    }
}
