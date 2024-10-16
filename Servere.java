import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Servere {
	static class Task {
		public static final String INPUT_FILE = "servere.in";
		public static final String OUTPUT_FILE = "servere.out";

		int n; // Numarul de servere
		double min_current = Double.MAX_VALUE;
		double max_current = 0;
		ArrayList<Integer> powers = new ArrayList<>(); // Puterile serverelor
		ArrayList<Integer> currents = new ArrayList<>(); // Curentii serverelor

		public void solve() {
			readInput();
			writeOutput(getResult());
		}

		private void readInput() {
			try {
				MyScanner scanner = new MyScanner(new BufferedReader(new FileReader(INPUT_FILE)));
				n = scanner.nextInt();
				for (int i = 0; i < n; i++) {
					powers.add(scanner.nextInt());
				}
				// Citim curentii serverelor si actualizam curentul minim si maxim
				for (int i = 0; i < n; i++) {
					currents.add(scanner.nextInt());
					if (currents.get(i) < min_current) {
						min_current = currents.get(i);
					}
					if (currents.get(i) > max_current) {
						max_current = currents.get(i);
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(double maxPower) {
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FILE)));
				// Scriem puterea maxima în fisierul de ieșire
				pw.printf(String.format("%.1f", maxPower));
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private double getResult() {
			double max_power = Double.NEGATIVE_INFINITY;
			double min_power = Double.NEGATIVE_INFINITY;
			double[] min_results = new double[n];
			double[] max_results = new double[n];
			double result = Double.POSITIVE_INFINITY;
			// Implementam algoritmul de cautare binara pentru a gasi curentul optim
			while (min_current - max_current < 0.1) {
				double min_power_min_results = Double.POSITIVE_INFINITY;
				double min_power_max_results = Double.POSITIVE_INFINITY;
				// Calculam media curentului minim și maxim
				double mean = (max_current + min_current) / 2;
				for (int i = 0; i < n; i++) {
					// Aplicam formula pentru toate serverele folosind curentul minim
					double power = powers.get(i) - Math.abs(min_current - currents.get(i));
					min_results[i] = power;
					if (power < min_power_min_results) {
						min_power_min_results = power;
					}
					power = powers.get(i) - Math.abs(max_current - currents.get(i));
					// Aplicam formula pentru toate serverele folosind curentul maxim
					max_results[i] = power;
					if (power < min_power_max_results) {
						min_power_max_results = power;
					}
				}
				// Cautam cea mai mare putere posibilia a sistemului,
				// aceasta fiind cea mai mica putere individuala a curentului ales
				// prin cautarea binara
				if (min_power < min_power_min_results) {
					min_power = min_power_min_results;
				}
				if (max_power < min_power_max_results) {
					max_power = min_power_max_results;
				}
				// Actualizam limitele curentului în funcție de puterile minime și maxime
				if (max_power > min_power) {
					min_current = mean;
				} else if (min_power > max_power) {
					max_current = mean;
				} else {
					// Daca puterile sunt egale inseamna ca am gasit curentul potrivit
					min_current = mean;
					break;
				}
			}
			// Calculam rezultatele pentru curentul optim
			for (int i = 0; i < n; i++) {
				double power = powers.get(i) - Math.abs(min_current - currents.get(i));
				if (power < result) {
					result = power;
				}
			}
			return result;
		}
	}

	public static void main(String[] args) {
		new Servere.Task().solve();
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
