import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.StringTokenizer;


public class Colorare {
	static class Task {
		public static final String INPUT_FILE = "colorare.in";
		public static final String OUTPUT_FILE = "colorare.out";

		public static final long MOD = 1000000007L;

		int k; // nr de zone din tablou
		char last_direction; // directia ultimei zone
		long result;

		public void solve() {
			readInput();
			writeOutput(result);
		}

		private void readInput() {
			try {
				MyScanner scanner = new MyScanner(new BufferedReader(new FileReader(INPUT_FILE)));
				k = scanner.nextInt();
				// Citim primul numar si prima directie
				int number = scanner.nextInt();
				char direction = scanner.next().charAt(0);
				// Calculam rezultatul pentru prima zona
				if (direction == 'H') {
					// Daca prima zona este orizontala acesta se poate colora in 6 moduri diferite
					// (Aranjamente de 3 luate cate 2), apoi urmatoarele zone orizontale
					// pot genera fiecare doar cate 3 moduri dfierite de colorare,
					// deoarece sunt restrictionate de zona anterioara
					result = 6L * montgomery(3, number - 1);
					last_direction = 'H';
				} else {
					// Daca prima zona este verticala acesta se poate colora in 3 moduri diferite
					// (Aranjamente de 3 luate cate 1), apoi urmatoarele zone verticale
					// pot genera fiecare doar cate 2 moduri dfierite de colorare,
					// deoarece sunt restrictionate de zona anterioara
					result = 3L * montgomery(2, number - 1);
					last_direction = 'V';
				}
				for (int i = 1; i < k; i++) {
					number = scanner.nextInt();
					direction = scanner.next().charAt(0);
					if (last_direction == 'H') {
						if (direction == 'H') {
							result *= montgomery(3, number);
						} else {
							// Prima zona verticala va avea un singur mod de colorare,
							// fiind langa o zona orizontala, apoi urmatoarele vor avea cate 2
							result *= montgomery(2, number - 1);
							last_direction = 'V';
						}
					} else {
						if (direction == 'H') {
							// Prima zona orizontala va avea doar 2 moduri de colorare,
							// fiind langa o zona verticala, apoi urmatoarele vor avea cate 3
							result *= 2L * montgomery(3, number - 1);
							last_direction = 'H';
						} else {
							result *= montgomery(2, number);
						}
					}
					// Verificăm depășirea limitei maxime a long-ului
					if (result > Long.MAX_VALUE / MOD) {
						result %= MOD;
					}
				}
				result = result % MOD;

			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(long result) {
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FILE)));
				pw.printf(String.format("%d", result));
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		// Functie pentru ridicarea la putere modulara ce aplica metoda Montgomery
		private long montgomery(long base, long power) {
			long result = 1;
			while (power > 0) {
				if ((power & 1) != 0) {
					result = montgomeryMultiply(result, base);
				}
				base = montgomeryMultiply(base, base);
				power >>= 1;
			}
			return result;
		}

		// Functie pentru inmultirea modulara Montgomery
		private long montgomeryMultiply(long a, long b) {
			long result = 0;
			a = a % MOD;
			while (b > 0) {
				if ((b & 1) != 0) {
					result = (result + a) % MOD;
				}
				a = (2 * a) % MOD;
				b >>= 1;
			}
			return result;
		}
	}

	public static void main(String[] args) {
		new Colorare.Task().solve();
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
