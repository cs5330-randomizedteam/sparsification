package Certificate;

public class UFDS {

    private int[] arr;
    private int count;

    public UFDS(int n) {
        arr = new int[n];
        count = n;
        for (int i = 0; i < n; i++) {
            arr[i] = i;
        }
    }

    public void union(int x, int y) {
        int a = find(x);
        int b = find(y);
        if (a != b) {
            arr[a] = b;
            count--;
        }
    }

    public int find(int x) {
        if (arr[x] == x) {
            return x;
        }
        arr[x] = find(arr[x]);
        return arr[x];
    }

    public int size() {
        return count;
    }
}
