import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class HardWeeks {
    public static class Graph {

        public static class Vertex {
            int number;
            ArrayList<Vertex> edges;
            int i;

            public Vertex(int number) {
                this.number = number;
                this.edges = new ArrayList<>();
                this.i = 0;
            }
        }

        Vertex[] vertexes;
        int size;

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

    static int[] hardWeekSolver(Graph G, int L) {
        int counter = 0, nHardWeeks = 0, max;
        boolean queueChanger = true;

        for (Graph.Vertex u : G.vertexes)
            for (Graph.Vertex v : u.edges)
                v.i++;

        Queue<Graph.Vertex> S = new LinkedList<>();
        Queue<Graph.Vertex> P = new LinkedList<>();

        for (Graph.Vertex u : G.vertexes)
            if (u.i == 0) {
                S.add(u);
                counter++;
            }

        max = counter;

        if (counter > L)
            nHardWeeks++;

        counter = 0;

        while (!S.isEmpty() || !P.isEmpty()) {
            Graph.Vertex u;
            if (queueChanger) {
                u = S.poll();
                for (Graph.Vertex v : u.edges) {
                    v.i--;
                    if (v.i == 0) {
                        P.add(v);
                        counter++;
                    }
                }
                if (S.isEmpty()) {
                    queueChanger = false;
                    if (counter > max)
                        max = counter;
                    if (counter > L)
                        nHardWeeks++;
                    counter = 0;

                }
            } else {
                u = P.poll();
                for (Graph.Vertex v : u.edges) {
                    v.i--;
                    if (v.i == 0) {
                        S.add(v);
                        counter++;
                    }
                }
                if (P.isEmpty()) {
                    queueChanger = true;
                    if (counter > max)
                        max = counter;
                    if (counter > L)
                        nHardWeeks++;
                    counter = 0;
                }
            }
        }
        return new int[]{max, nHardWeeks};
    }

    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        String[] line = input.readLine().split(" ");

        int T = Integer.parseInt(line[0]);
        int P = Integer.parseInt(line[1]);
        int L = Integer.parseInt(line[2]);

        Graph G = new Graph(T);

        for (int i = 0; i < P; i++) {
            line = input.readLine().split(" ");
            G.addAdj(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
        }

        int[] answer = hardWeekSolver(G, L);

        System.out.println(answer[0] + " " + answer[1]);

        input.close();
    }
}
