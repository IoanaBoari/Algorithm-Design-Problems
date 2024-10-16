import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Numarare {
	static class Task {
		public static final String INPUT_FILE = "numarare.in";
		public static final String OUTPUT_FILE = "numarare.out";

		public static final long MOD = 1000000007L;

		int n, m;
		ArrayList<ArrayList<Integer>> graf1 = new ArrayList<>();
		ArrayList<ArrayList<Integer>> common = new ArrayList<>();

		public void solve() {
			readInput();
			writeOutput(getResult());
		}

		private void readInput() {
			try {
				MyScanner scanner = new MyScanner(new BufferedReader(new FileReader(INPUT_FILE)));
				n = scanner.nextInt();
				m = scanner.nextInt();
				for (int i = 0; i <= n; i++) {
					graf1.add(new ArrayList<>());
					common.add(new ArrayList<>());
				}
				for (int i = 0; i < m; i++) {
					int x = scanner.nextInt();
					int y = scanner.nextInt();
					graf1.get(x).add(y);
				}
				for (int i = 0; i < m; i++) {
					int x = scanner.nextInt();
					int y = scanner.nextInt();
					// Cream lista de adiacenta pentru drumurile comune
					if (graf1.get(x).contains(y)) {
						common.get(x).add(y);
					}
				}

			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(long result) {
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FILE)));
				pw.printf("%d", result); // Scriem numarul de lanturi elementare comune
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private long getResult() {
			long[] dp = new long[n + 1];
			boolean[] visited = new boolean[n + 1];
			PriorityQueue<Integer> queue = new PriorityQueue<>();

			dp[1] = 1;
			queue.add(1);
			while (!queue.isEmpty()) {
				int node = queue.poll();
				// Verificăm dacă nodul nu a fost deja vizitat
				if (!visited[node]) {
					visited[node] = true;
					// Iterăm prin vecinii nodului curent
					for (int neighbor : common.get(node)) {
						// Actualizăm numărul de lanțuri care ajung la vecin
						dp[neighbor] = (dp[neighbor] + dp[node]) % MOD;

						// Adăugăm vecinul în coadă pentru procesare ulterioară
						queue.add(neighbor);
					}
				}
			}
			// Returnăm numărul de lanțuri care ajung la nodul N
			return dp[n];
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
