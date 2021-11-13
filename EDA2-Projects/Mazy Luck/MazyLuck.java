import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MazyLuck {
    public static class Graph {
        int numberOfVertexes;
        Vertex[] V;

        public static class Vertex {
            int number, distance;
            ArrayList<Edge> edges;
            Vertex predecessor;

            public Vertex(int number) {
                this.number = number;
                this.edges = new ArrayList<>();
            }
        }

        public static class Edge {
            int weight;
            int destination;

            public Edge(int weight, int destination) {
                this.weight = weight;
                this.destination = destination;
            }
        }

        public Graph(int numberOfVertexes) {
            this.V = new Vertex[numberOfVertexes];
            this.numberOfVertexes = numberOfVertexes;

            for (int i = 0; i < numberOfVertexes; i++) {
                this.V[i] = new Vertex(i);
            }
        }

        void addAdj(int origin, Edge edge) {
            this.V[origin].edges.add(edge);
        }

        private static void initializeSingleSource(Graph G, Vertex s) {
            for (Vertex v : G.V) {
                v.distance = Integer.MAX_VALUE;
                v.predecessor = null;
            }
            s.distance = 0;
        }

        private static void relax(Vertex u, Vertex v, int w) {
            if (u.distance != Integer.MAX_VALUE && u.distance + w < v.distance) {
                v.distance = u.distance + w;
                v.predecessor = u;
            }
        }

        public boolean bellmanFord(Graph G, Vertex s) {
            initializeSingleSource(G, s);
            for (int i = 1; i <= G.V.length - 1; i++)
                for (Vertex u : G.V)
                    for (Edge e : u.edges)
                        relax(u, G.V[e.destination], e.weight);
            for (Vertex u : G.V)
                for (Edge e : u.edges)
                    if (u.distance + e.weight < G.V[e.destination].distance)
                        return false;
            return true;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        int weight, R, C, origin, destination;
        boolean a;
        char c;
        String[] line;
        Graph G;

        line = input.readLine().split(" ");

        R = Integer.parseInt(line[0]);
        C = Integer.parseInt(line[1]);

        G = new Graph(R);

        for (int i = 0; i < C; i++) {
            line = input.readLine().split(" ");

            origin = Integer.parseInt(line[0]);
            destination = Integer.parseInt(line[1]);
            c = line[2].charAt(0);
            weight = Integer.parseInt(line[3]);

            if (c == 'C')
                weight *= -1;

            G.addAdj(origin, new Graph.Edge(weight, destination));
        }

        a = G.bellmanFord(G, G.V[0]);

        if (G.V[R - 1].distance < 0 || !a)
            System.out.println("yes");
        else
            System.out.println("no");
    }
}
