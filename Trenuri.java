import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class Trenuri {
	static class Task {
		public static final String INPUT_FILE = "trenuri.in";
		public static final String OUTPUT_FILE = "trenuri.out";

		int m;
		String startCity, endCity;
		Map<String, Integer> cityIndices = new HashMap<>();
		int nextIndex = 0;
		// Inițializăm lista de adiacență
		ArrayList<ArrayList<Integer>> graph = new ArrayList<>();

		public void solve() {
			readInput();
			writeOutput(getResult());
		}

		private int getCityIndex(String city) {
			// Obținem indexul unui oraș, dacă nu există îl adăugăm în Map
			if (!cityIndices.containsKey(city)) {
				cityIndices.put(city, nextIndex++);
			}
			return cityIndices.get(city);
		}

		private void readInput() {
			try {
				MyScanner scanner = new MyScanner(new BufferedReader(new FileReader(INPUT_FILE)));
				startCity = scanner.next();
				endCity = scanner.next();
				m = scanner.nextInt();

				for (int i = 0; i < m; i++) {
					String fromCity = scanner.next();
					String toCity = scanner.next();

					// Adăugăm orașele în Map-ul cityIndices și obținem indecșii lor
					int fromIndex = getCityIndex(fromCity);
					int toIndex = getCityIndex(toCity);

					// Adăugăm relația între orașe în lista de adiacență
					while (graph.size() <= fromIndex) {
						graph.add(new ArrayList<>());
					}
					graph.get(fromIndex).add(toIndex);

				}

			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(int result) {
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FILE)));
				pw.printf("%d", result); // Scriem numarul maxim de orase distincte
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private int getResult() {
			// Obținem indexul orașului de start și al celui de sfârșit
			final int startCityIndex = getCityIndex(startCity);
			final int endCityIndex = getCityIndex(endCity);

			// Sortare topologică
			List<Integer> topologicalOrder = topologicalSort();

			// Initializează distanțele
			int[] dist = new int[nextIndex];
			Arrays.fill(dist, Integer.MIN_VALUE);
			dist[startCityIndex] = 0;

			// Actualizează distanțele folosind sortarea topologică
			for (int node : topologicalOrder) {
				for (int neighbor : graph.get(node)) {
					dist[neighbor] = Math.max(dist[neighbor], dist[node] + 1);
				}
			}
			dist[endCityIndex]++; // Adăugăm orașul final
			return dist[endCityIndex];
		}

		private List<Integer> topologicalSort() {
			// Sortam topologic folosind DFS
			boolean[] visited = new boolean[nextIndex];
			List<Integer> order = new ArrayList<>();

			for (int i = 0; i < nextIndex; i++) {
				if (!visited[i]) {
					dfs(i, visited, order);
				}
			}
			Collections.reverse(order);
			return order;
		}

		private void dfs(int node, boolean[] visited, List<Integer> order) {
			// Parcurgem în adâncime graful
			visited[node] = true;
			for (int neighbour : graph.get(node)) {
				if (!visited[neighbour]) {
					dfs(neighbour, visited, order);
				}
			}
			order.add(node);
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}

	/**
	 * A class for buffering read operations, inspired from here:
	 * https://pastebin.com/XGUjEyMN.
	 */
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

		public long nextLong() {
			return Long.parseLong(next());
		}

		public double nextDouble() {
			return Double.parseDouble(next());
		}

		public String nextLine() {
			String str = "";
			try {
				str = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return str;
		}
	}
}
