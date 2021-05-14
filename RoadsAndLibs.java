import java.io.*;
import java.util.*;
import java.util.stream.*;
import static java.util.stream.Collectors.toList;


/**
 * Roads and Libraries
 * https://www.hackerrank.com/challenges/torque-and-development/problem?h_l=interview&playlist_slugs%5B%5D=interview-preparation-kit&playlist_slugs%5B%5D=graphs
 */
public class RoadsAndLibs {


    /**
     * Utility function to add edge in directed graph.
     */
    static void addEdge(ArrayList<ArrayList<Integer>> adj, int u, int v) {
        adj.get(u).add(v);
        adj.get(v).add(u);
    }


    /**
     * Print adjacency list.
     * 
     * NOT PART OF SOLUTION
     */
    static void printGraph(ArrayList<ArrayList<Integer>> adj) {
        for (int i = 0; i < adj.size(); i++) {

            // **** ****
            System.out.print("<<< city: " + i);

            // **** print adjacent nodes ****
            for (int j = 0; j < adj.get(i).size(); j++) {
                System.out.print(" -> " + adj.get(i).get(j));
            }

            // **** end of line ****
            System.out.println();
        }
    }


    /**
     * Compute the cost for a connected library for a city..
     */
    static int dfs(ArrayList<ArrayList<Integer>> adj, int city, boolean[] visited) {

        // **** initialization ****
        int cost = 1;

        // **** flag as visited ****
        visited[city] = true;

        // **** visit adjacent nodes (if not visited) ****
        for (int i = 0; i < adj.get(city).size(); i++) {
            if (!visited[adj.get(city).get(i)]) {
                cost += dfs(adj, adj.get(city).get(i), visited);
            }
        }

        // ???? ????
        System.out.println("<<< city: " + city + " cost: " + cost);

        // **** return cost ****
        return cost;
    }


    /**
     * Determine the minimum cost to provide library access to all citizens of HackerLand.
     * Nodes start with 0.
     */
    public static long roadsAndLibraries(int n, int c_lib, int c_road, List<List<Integer>> cities) {

        // **** initialization ****
        boolean[] visited           = new boolean[n + 1];
        ArrayList<Integer> comps    = new ArrayList<>();
        long minCost                = 0;

        // ???? ????
        System.out.println("<<< visited: " + Arrays.toString(visited));

        // **** greate graph adjacency list ****
        ArrayList<ArrayList<Integer>> adj = new ArrayList<ArrayList<Integer>>(n + 1);
        for (int i = 0; i < (n + 1); i++) {
            adj.add(new ArrayList<Integer>());
        }

        // **** populate adjacency list ****
        for (int i = 0; i < cities.size(); i++) {

            // **** for eade of use ****
            List<Integer> city = cities.get(i);

            // **** add bidirectional edge ****
            addEdge(adj, city.get(0), city.get(1));
        }

        // ???? ????
        printGraph(adj);

        // **** update cost of components (uses dfs) ****
        for (int i = 1; i <= n; i++) {
            if (adj.get(i).size() >= 0 && !visited[i]) {
                comps.add(dfs(adj, i, visited));
            } else {
                if (adj.get(i).size() == 0) {
                    comps.add(1);
                }
            }
        }

        // **** compute minimal cost (road + library) ****
        for (int i = 0; i < comps.size(); i++) {
            minCost += Math.min((comps.get(i) - 1) * c_road + c_lib, comps.get(i) * c_lib);
        }

        // **** return minimum cost ****
        return minCost;
    }


    /**
     * Test scaffold.
     * 
     * NOT PART OF SOLUTION
     * 
     * @throws IOException
     * @throws NumberFormatException
     */
    public static void main(String[] args) throws NumberFormatException, IOException {
      
        // **** open buffered reader and writer ****
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        // **** get the number of queries ****
        int q = Integer.parseInt(bufferedReader.readLine().trim());

        // **** ****
        IntStream.range(0, q).forEach(qItr -> {
            try {
                String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

                int n = Integer.parseInt(firstMultipleInput[0]);

                int m = Integer.parseInt(firstMultipleInput[1]);

                int c_lib = Integer.parseInt(firstMultipleInput[2]);

                int c_road = Integer.parseInt(firstMultipleInput[3]);

                List<List<Integer>> cities = new ArrayList<>();

                IntStream.range(0, m).forEach(i -> {
                    try {
                        cities.add(
                            Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                .map(Integer::parseInt)
                                .collect(toList())
                        );
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                // **** compute result ****
                long result = roadsAndLibraries(n, c_lib, c_road, cities);

                // **** display result ****
                bufferedWriter.write("main <<< minCost: " + String.valueOf(result));
                bufferedWriter.newLine();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // **** close buffered reader and writter  ****
        bufferedReader.close();
        bufferedWriter.close();
    }
}