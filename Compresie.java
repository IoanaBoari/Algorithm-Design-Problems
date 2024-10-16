import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class Compresie {
	static class Task {
		public static final String INPUT_FILE = "compresie.in";
		public static final String OUTPUT_FILE = "compresie.out";

		int n, m;
		ArrayList<Integer> arrayA = new ArrayList<>();
		ArrayList<Integer> arrayB = new ArrayList<>();

		public void solve() {
			readInput();
			writeOutput(getResult());
		}

		private void readInput() {
			try {

				MyScanner scanner = new MyScanner(new BufferedReader(new FileReader(INPUT_FILE)));
				n = scanner.nextInt();
				for (int i = 0; i < n; i++) {
					arrayA.add(scanner.nextInt());
				}
				m = scanner.nextInt();
				for (int i = 0; i < m; i++) {
					arrayB.add(scanner.nextInt());
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(int maxLength) {
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FILE)));
				pw.printf("%d", maxLength); // Scriem lungimea maximă în fișierul de ieșire
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private int getResult() {
			ArrayList<Integer> arrayFinal = new ArrayList<Integer>();
			int partialSumA = arrayA.get(0); // Suma partiala pentru array-ul A
			int partialSumB = arrayB.get(0); // Suma partiala pentru array-ul B
			int i = 0;
			int j = 0;
			while (i < n && j < m) {
				if (partialSumA == 0 && partialSumB == 0) {
					// Daca sumele au fost resetate le initalizam cu elementul curent
					partialSumA = arrayA.get(i);
					partialSumB = arrayB.get(j);
				}
				if (partialSumA == partialSumB) {
					// Daca sumele partiale sunt egale, adaugam elementul in array-ul final
					arrayFinal.add(partialSumA);
					// Trecem la urmatorul element din ambele array-uri
					i++;
					j++;
					// Resetam sumele parțiale
					partialSumA = 0;
					partialSumB = 0;
				} else if (partialSumA > partialSumB) {
					// Daca suma parțiala a array-ului A este mai mare decat cea a array-ului B,
					// trecem la urmatorul element din array-ul B
					// si actualizam suma partiala a acestuia
					j++;
					partialSumB += arrayB.get(j);
				} else if (partialSumA < partialSumB) {
					// Daca suma parțiala a array-ului A este mai mica decat cea a array-ului B,
					// trecem la urmatorul element din array-ul A
					// si actualizam suma parțiala a acestuia
					i++;
					partialSumA += arrayA.get(i);
				}
			}
			// Daca nu am parcurs complet ambele array-uri
			// inseamna ca nu se pot forma 2 siruri egale
			if (i < n || j < m) {
				return -1;
			} else {
				return arrayFinal.size();
			}
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
