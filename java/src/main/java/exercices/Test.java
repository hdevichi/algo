package exercices;

public class Test {

	public static void main(String[] args) {
		
		System.out.println(compress("aaakkvvd"));
	}
	
	// Find if a string has all unique characters
	
	static int a = 65;
	boolean allUnique(String s) {
		
		int[] countChar = new int[26];
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			countChar[c-a]++;
		}
		for (int i = 0 ; i < countChar.length; i++) {
			if (countChar[i] == 0) {
				return false;
			}
		}
		return true;
	}
	
	// compress
	// 2 erreurs d'indice, syntaxe ok à quelques details
	public static String compress(String s) {
		if (s.length() <= 1)
			return s;
		
		StringBuilder sb = new StringBuilder();
		StringBuilder result = new StringBuilder(s.length()); // erreur!! (manque length)
		for (int i = 0 ; i < s.length(); i++) {
			int count = 0;
			char c = sb.charAt(i);
			while (i < (s.length() - 1) && sb.charAt(i+1) == c ) { // erreur (parenthese manquante)
				i++;
				count++;
			}
			result.append(c);
			if (count > 0) {
				result.append(Integer.toString(count+1)); // stringValue to toString
			}
		}
		return result.toString();
	}
}
