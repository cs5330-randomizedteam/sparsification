package Certificate;

import java.util.*;

public class UnitTest {

    public static void main(String[] args) throws Exception {
//        testRemoveNode();
//        testHashEdge();
//        testUnionEdge();
//        testActualGraph();
        testGraphFromFile();
    }

    public static void testGraphFromFile() throws Exception {
        Certificate c = new Certificate("sample");
        Set<Edge> cert = c.generateCert(1);
        cert.forEach(ct -> {
            System.out.printf("%d, %d\n", ct.src, ct.des);
        });
    }

    public static void testActualGraph() {
        int N = 13;
        ArrayList<ArrayList<Integer>> adjList = new ArrayList<>();
        int[][] a = new int[13][];
        a[0] = new int[] {1,2,4,6,7,8,9,10};
        a[1] = new int[] {0,2,3,8,11};
        a[2] = new int[] {0,1,3,4};
        a[3] = new int[] {1,2,4,5};
        a[4] = new int[] {0,2,3,5,6};
        a[5] = new int[] {3,4,6,7};
        a[6] = new int[] {0,4,5,7};
        a[7] = new int[] {0,5,6,8,9,10,12};
        a[8] = new int[] {0,1,7,9,10,11};
        a[9] = new int[] {0,7,8,10,11,12};
        a[10] = new int[] {0,7,8,9,11,12};
        a[11] = new int[] {1,8,9,10,12};
        a[12] = new int[] {7,9,10,11};
        for (int i = 0; i < N; i++) {
            adjList.add(new ArrayList<>());
            for (int j = 0; j < a[i].length; j++) {
                adjList.get(i).add(a[i][j]);
            }
        }
    }

    public static void testUnionEdge() {
        ArrayList<Edge> a = new ArrayList<>();
        a.add(new Edge(123,234));
        a.add(new Edge(5,3));
        ArrayList<Edge> b = new ArrayList<>();
        b.add(new Edge(3,5));
        b.add(new Edge(123,239));
        TreeSet<Edge> tr = new TreeSet<>();
        tr.addAll(a);
        tr.addAll(b);
        tr.forEach(edge -> {
            System.out.printf("%d, %d\n", edge.src, edge.des);
        });
    }

    public static void testHashEdge() {
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        map.put(new Edge(123, 456).hashCode(), 0);
        map.put(new Edge(234, 567).hashCode(), 1);
        map.put(new Edge(123, 123).hashCode(), 4);
        System.out.println(map.size());
        System.out.println(map.get(new Edge(123, 234).hashCode()));

    }

    public static void testRemoveNode() {
        TreeSet<Node> tr = new TreeSet<>();
        tr.add(new Node(3, 78));
        tr.add(new Node(9, 15));
        tr.add(new Node(5));

        tr.remove(new Node(3));
        tr.forEach(s -> {
            System.out.printf("%d, %d\n", s.node, s.rValue);
        });
    }
}
