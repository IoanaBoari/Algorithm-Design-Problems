import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.StringTokenizer;

public class Drumuri {

	// Clasa Edge reprezinta o muchie din graf,
	// avand un nod destinatie si un cost asociat
	static class Edge implements Comparable<Edge> {
		int node;
		long cost;

		Edge(int node, long cost) {
			this.node = node;
			this.cost = cost;
		}

		@Override
		public int compareTo(Edge other) {
			return Long.compare(this.cost, other.cost);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			Edge edge = (Edge) o;
			return node == edge.node && cost == edge.cost;
		}

		@Override
		public int hashCode() {
			return Objects.hash(node, cost);
		}
	}

	static class Task {
		public static final String INPUT_FILE = "drumuri.in";
		public static final String OUTPUT_FILE = "drumuri.out";

		int n, m, x, y, z;
		ArrayList<Edge>[] adj;
		long[] distFromX, distFromY;
		int[] prevX, prevY;

		public void solve() {
			readInput();
			writeOutput(getResult());
		}

		private void readInput() {
			try {
				MyScanner scanner = new MyScanner(new BufferedReader(new FileReader(INPUT_FILE)));
				n = scanner.nextInt(); // Numarul de noduri
				m = scanner.nextInt(); // Numarul de muchii
				adj = new ArrayList[n + 1]; // Lista de adiacenta a grafului
				for (int i = 1; i <= n; i++) {
					adj[i] = new ArrayList<>();
				}
				for (int i = 0; i < m; i++) {
					int a = scanner.nextInt();
					int b = scanner.nextInt();
					int c = scanner.nextInt();
					adj[a].add(new Edge(b, c)); // Adaugam muchia in lista de adiacenta
				}
				x = scanner.nextInt(); // Nodul de start pentru primul drum
				y = scanner.nextInt(); // Nodul de start pentru al doilea drum
				z = scanner.nextInt(); // Nodul de sfarsit pentru ambele drumuri
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(long result) {
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FILE)));
				pw.println(result);
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private long getResult() {
			distFromX = new long[n + 1]; // Distantele de la nodul x la toate celelalte noduri
			distFromY = new long[n + 1]; // Distantele de la nodul y la toate celelalte noduri
			prevX = new int[n + 1]; // Vectorul de parinti pentru drumul de la x la z
			prevY = new int[n + 1]; // Vectorul de parinti pentru drumul de la y la z

			// Initializam distantele cu o valoare mare
			Arrays.fill(distFromX, Long.MAX_VALUE);
			Arrays.fill(distFromY, Long.MAX_VALUE);

			// Costul drumului minim de la x si y la z
			long costFromXToZ = dijkstra(x, z, distFromX, prevX);
			long costFromYToZ = dijkstra(y, z, distFromY, prevY);

			// Costul total al celor doua drumuri
			long totalCost = costFromXToZ + costFromYToZ;

			// Determinam multimea muchiilor comune celor doua drumuri
			Set<Edge> commonEdges = getCommonEdges();

			// Scadem costul muchiilor comune din costul total pentru a nu se aduna de 2 ori
			for (Edge edge : commonEdges) {
				totalCost = totalCost - edge.cost;
			}
			return totalCost;
		}

		private long dijkstra(int start, int end, long[] dist, int[] prev) {
			// Coada de prioritati pentru nodurile nevizitate
			PriorityQueue<Edge> queue = new PriorityQueue<>();
			dist[start] = 0; // Distanta de la nodul de start la el insusi este 0
			queue.add(new Edge(start, 0));

			while (!queue.isEmpty()) {
				Edge edge = queue.poll();
				if (edge.node == end) {
					return dist[end]; // Daca am ajuns la nodul de sfarsit, returnam distanta
				}

				// Daca distanta curenta este mai mare decat distanta calculata,
				// trecem la urmatorul nod
				if (edge.cost > dist[edge.node]) {
					continue;
				}
				for (Edge neighbor : adj[edge.node]) {
					long newDist = dist[edge.node] + neighbor.cost;
					if (newDist < dist[neighbor.node]) {
						// Daca noua distanta este mai mica decat cea veche, o actualizam
						dist[neighbor.node] = newDist;
						queue.add(new Edge(neighbor.node, newDist)); // Adaugam nodul in coada
						if (prev != null) {
							prev[neighbor.node] = edge.node; // Actualizam parintele nodului
						}
					}
				}
			}
			return Long.MAX_VALUE;
		}

		/**
		 * Această metodă este utilizată pentru a obține muchiile comune
		 * dintre cele mai scurte căi
		 * de la x la z și de la y la z.
		 * Folosește metoda 'reconstructPath' pentru a reconstitui căile
		 * de la x la z și de la y la z.
		 * Apoi, păstrează doar muchiile care sunt comune ambelor căi.
		 *
		 * @return Un set de muchii care sunt comune celor mai scurte căi
		 */
		private Set<Edge> getCommonEdges() {
			Set<Edge> pathFromX = reconstructPath(x, z, prevX);
			Set<Edge> pathFromY = reconstructPath(y, z, prevY);
			pathFromX.retainAll(pathFromY); // Retinem doar muchiile comune
			return pathFromX;
		}

		/**
		 * Această metodă este utilizată pentru a reconstitui drumul
		 * de la un nod de start la un nod de sfârșit.
		 * Parcurge drumul de la nodul de sfârșit la nodul de start,
		 * adăugând fiecare muchie într-un set.
		 * Costul fiecărei muchii este determinat prin căutarea muchiei
		 * în lista de adiacență a nodului părinte.
		 *
		 * @param start Nodul de start al drumului.
		 * @param end Nodul de sfârșit al drumului.
		 * @param prev Vectorul de părinți, unde prev[i] este nodul părinte al nodului i.
		 * @return Un set de muchii care reprezintă drumul de la nodul de start la nodul de sfârșit.
		 */
		private Set<Edge> reconstructPath(int start, int end, int[] prev) {
			Set<Edge> path = new HashSet<>();
			for (int current = end; current != start; current = prev[current]) {
				int prevNode = prev[current];
				int edgeCost = 0;
				for (Edge edge : adj[prevNode]) {
					if (edge.node == current) {
						edgeCost = (int) edge.cost;
						break;
					}
				}
				path.add(new Edge(prevNode, edgeCost));
			}
			return path;
		}

	}

	public static void main(String[] args) {
		new Task().solve();
	}

	private static class MyScanner {
		private BufferedReader br;
		private StringTokenizer st;

		public MyScanner(Reader reader) {
			br = new BufferedReader(reader);
		}

		public String next() {
			while (st == null || !st.hasMoreElements()) {
				try {
					st = new StringTokenizer(br.readLine());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return st.nextToken();
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}
	}
}
