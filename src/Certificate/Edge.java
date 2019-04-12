package Certificate;

import java.util.Objects;

public class Edge {

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
}
