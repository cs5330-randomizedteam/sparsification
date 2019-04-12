package Certificate;

import java.util.Objects;

public class Edge implements Comparable<Edge> {

    public int src;
    public int des;

    public Edge(int src, int des) {
        this.src = src < des? src : des;
        this.des = src >= des? src : des;
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, des);
    }

    @Override
    public int compareTo(Edge o) {
        if (this.src == o.src) {
            return this.des - o.des;
        }
        return this.src - o.src;
    }
}
