import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Oferta {
	static class Task {
		public static final String INPUT_FILE = "oferta.in";
		public static final String OUTPUT_FILE = "oferta.out";

		int n; // Numarul de produse
		int k;
		int[] products;
		public void solve() {
			readInput();
			writeOutput(getResult());
		}

		private void readInput() {
			try {
				Scanner scanner = new Scanner(new BufferedReader(new FileReader(INPUT_FILE)));
				n = scanner.nextInt();
				k = scanner.nextInt();
				products = new int[n];
				for (int i = 0; i < n; i++) {
					products[i] = scanner.nextInt();
				}
				scanner.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(double minPrice) {
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FILE)));
				// Scriem pretul minim in fisierul de iesire
				pw.printf(String.format("%.1f", minPrice));
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private double getResult() {
			double[] dp = new double[n];
			dp[0] = products[0];
			if (n > 1) {
				dp[1] = Math.min(dp[0] + products[1], reduceTwo(products[0], products[1]));
			}
			if (n > 2) {
				dp[2] = Math.min(dp[1] + products[2], Math.min(dp[0]
						+ reduceTwo(products[1], products[2]),
					reduceThree(products[0], products[1], products[2])));
			}
			for (int i = 3; i < n; i++) {
				// pretul pentru i produse daca nu s-ar aplica nicio reducere cu ultimul produs
				double sum = dp[i - 1] + products[i];
				// pretul pentru i produse daca se aplica reducerea pentru ultimele 2
				double r2 = dp[i - 2] + reduceTwo(products[i - 1], products[i]);
				// pretul pentru i produse daca se aplica reducerea pentru ultimele 3
				double r3 = dp[i - 3] + reduceThree(products[i - 2], products[i - 1], products[i]);
				// dintre cele 3 variante de preturi se alege cea mai ieftina
				dp[i] = Math.min(sum, Math.min(r2, r3));
			}
			// Returnam pretul cel mai bun pentru cele n produse
			return dp[n - 1];
		}

		// Functie ce aplica reducerea pentru gruparea a 2 produse
		private double reduceTwo(int price1, int price2) {
			if (price1 < price2) {
				return (price2 + price1 / 2.0);
			} else {
				return (price1 + price2 / 2.0);
			}
		}

		// Functie ce aplica reducerea pentru gruparea a 3 produse
		private double reduceThree(int price1, int price2, int price3) {
			if (price1 < price2 && price1 < price3) {
				return price2 + price3;
			} else if (price2 < price1 && price2 < price3) {
				return price1 + price3;
			} else {
				return price1 + price2;
			}
		}
	}

	public static void main(String[] args) {
		new Oferta.Task().solve();
	}
}
