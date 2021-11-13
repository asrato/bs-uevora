import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SpreadingTheNews {

    public enum Color {
        WHITE,
        GREY,
        BLACK
    }

    public static class Graph {

        public static class Vertex {
            int number;
            Color color;
            int distance;
            Vertex previous;
            ArrayList<Vertex> edges;
            int finished;

            public Vertex(int number) {
                this.number = number;
                this.color = Color.WHITE;
                this.distance = 0;
                this.previous = null;
                this.edges = new ArrayList<>();
            }

        }

        Vertex[] vertexes;
        int size, time;

        public Graph(int nVertex) {
            this.vertexes = new Vertex[nVertex];
            this.size = nVertex;

            for (int i = 0; i < nVertex; i++)
                this.vertexes[i] = new Vertex(i);
        }

        void addAdj(int origin, int destination) {
            this.vertexes[origin].edges.add(this.vertexes[destination]);
        }
    }

    static int getMaxIndex(int[] array) {
        int max = array[0];
        int maxIdx = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                maxIdx = i;
            }
        }
        return maxIdx;
    }

    static void BFS(Graph G, int beginning) {
        Graph.Vertex s = G.vertexes[beginning];
        Queue<Graph.Vertex> Q = new LinkedList<>();

        for (Graph.Vertex u : G.vertexes) {
            if (!u.equals(s)) {
                u.color = Color.WHITE;      // unknown vertex
                u.distance = Integer.MAX_VALUE;
                u.previous = null;
            }
        }

        s.color = Color.GREY;               // known but not explored vertex
        s.distance = 0;
        s.previous = null;

        Q.add(s);

        while (!Q.isEmpty()) {
            Graph.Vertex u = Q.poll();

            for (Graph.Vertex v : u.edges) {
                if (v.color == Color.WHITE) {
                    v.color = Color.GREY;
                    v.distance = u.distance + 1;
                    v.previous = u;
                    Q.add(v);
                }
            }

            u.color = Color.BLACK;          // explored vertex
        }
    }

    static int[] findBoom(Graph G, int beginning) {
        int[] result = new int[G.size];
        Graph.Vertex s = G.vertexes[beginning];
        Queue<Graph.Vertex> Q = new LinkedList<>();

        for (Graph.Vertex u : G.vertexes) {
            if (!u.equals(s)) {
                u.color = Color.WHITE;      // unknown vertex
                u.distance = Integer.MAX_VALUE;
                u.previous = null;
            }
        }

        s.color = Color.GREY;               // known but not explored vertex
        s.distance = 0;
        s.previous = null;

        Q.add(s);

        while (!Q.isEmpty()) {
            Graph.Vertex u = Q.poll();

            for (Graph.Vertex v : u.edges) {
                if (v.color == Color.WHITE) {
                    v.color = Color.GREY;
                    v.distance = u.distance + 1;
                    result[v.distance]++;
                    v.previous = u;
                    Q.add(v);
                }
            }

            u.color = Color.BLACK;          // explored vertex
        }


        int index = getMaxIndex(result);

        return new int[]{result[index], index};
    }

    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));


        int nEmployees = Integer.parseInt(input.readLine());
        Graph graph = new Graph(nEmployees);

        for (int i = 0; i < nEmployees; i++) {
            String[] line = input.readLine().split(" ");
            int nFriends = Integer.parseInt(line[0]);
            for (int j = 1; j <= nFriends; j++)
                graph.addAdj(i, Integer.parseInt(line[j]));

        }

        int nStarts = Integer.parseInt(input.readLine());

        for (int i = 0; i < nStarts; i++) {
            int ind = Integer.parseInt(input.readLine());
            if (graph.vertexes[ind].edges.isEmpty())
                System.out.println(0);
            else {
                int[] array = findBoom(graph, ind);
                System.out.println(array[0] + " " + array[1]);
            }
        }
    }
}
