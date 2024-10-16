import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Criptat {
	static class Task {
		public static final String INPUT_FILE = "criptat.in";
		public static final String OUTPUT_FILE = "criptat.out";

		int n; // Nr de cuvinte
		ArrayList<String> words = new ArrayList<>();
		// Folosesc un hashmap pentru a retine literele folosite
		// si de cate ori apar acestea in toate cuvintele
		HashMap<Character, Integer> letters = new HashMap<>();

		public void solve() {
			readInput();
			writeOutput(getResult());
		}

		private void readInput() {
			try {
				Scanner scanner = new Scanner(new BufferedReader(new FileReader(INPUT_FILE)));
				n = scanner.nextInt();
				for (int i = 0; i < n; i++) {
					String str = scanner.next();
					for (char letter : str.toCharArray()) {
						// Verificam daca litera exista deja in HashMap
						if (letters.containsKey(letter)) {
							// Daca exista, incrementam numarul de aparitii
							int count = letters.get(letter);
							letters.put(letter, count + 1);
						} else {
							// Daca nu exista, adaugam litera si setam numarul de aparitii la 1
							letters.put(letter, 1);
						}
					}
					words.add(str);
				}
				scanner.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(int len) {
			try {
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FILE)));
				pw.printf(String.format("%d", len));
				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private int getResult() {
			String maxPassword = "";
			// Determinam parola maxima folosind drept litera dominanta fiecare litera posibila,
			// dar le luam in ordinea descrescatoare a numarului de apartii,
			// astfel rezultatul final se poate afla adesea din prima iteratie
			for (int k = 0; k < letters.size(); k++) {
				String password = null;
				char maxLetter = 'a';
				Integer maxValue = 0;

				for (Map.Entry<Character, Integer> entry : letters.entrySet()) {
					if (entry.getValue() >= maxValue) {
						maxLetter = entry.getKey();
						maxValue = entry.getValue();
					}
				}
				// Extragem litera dominanta si ii setam numarul de apartii la -1
				// pentru a nu o folosi de mai multe ori
				letters.put(maxLetter, -1);
				char letter = maxLetter;
				// Sortam cuvintele dupa raportul dintre litera dominanta
				// si nr de litere al cuvantului
				Collections.sort(words, new Comparator<String>() {
					@Override
					public int compare(String s1, String s2) {
						// Calculam raportul literei dominante pentru fiecare cuvant
						double ratio1 = dominantLetterRatio(letter, s1);
						double ratio2 = dominantLetterRatio(letter, s2);
						return Double.compare(ratio2, ratio1);
					}
				});
				// Verificam daca primul cuvant respecta proprietatea
				if (checkDominance(maxLetter, words.get(0))) {
					password = words.get(0);
				}
				// Adaugam fiecare cuvant pe rand si testam daca se respecta conditia
				for (int i = 1; i < n; i++) {
					String newPassword = password + words.get(i);
					if (checkDominance(maxLetter, newPassword)) {
						password += words.get(i);
					}
				}
				// Daca parola curenta este mai lunga decat maximul de pana acum,
				// inlocuim parola maxima cu parola curenta
				if (password != null && (password.length() > maxPassword.length())) {
					maxPassword = password;
				}
			}
			return maxPassword.length();
		}

		private boolean checkDominance(char letter, String password) {
			// Verificam dacă litera primita ca parametru este dominanta in password
			int count = 0;
			for (int i = 0; i < password.length(); i++) {
				if (password.charAt(i) == letter) {
					count++;
				}
			}
			return count > password.length() / 2;
		}

		// Metoda pentru calcularea raportului literei dominante
		// intr-un cuvant folosita la sortarea cuvintelor
		private static double dominantLetterRatio(char letter, String word) {
			int count = 0;
			for (int i = 0; i < word.length(); i++) {
				if (word.charAt(i) == letter) {
					count++;
				}
			}
			return (double) count / word.length();
		}
	}

	public static void main(String[] args) {
		new Criptat.Task().solve();
	}
}
