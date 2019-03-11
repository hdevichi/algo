package exercices;

public class Palindromes {

	//Determine whether an integer is a palindrome. Do this without extra space.
	
	// clarification: no string, local variable ok
	// assuming the integer is < 32 bits (int), and is not in string value (otherwise compare extremities)
	
	public static void main (String[] args) {
		
		int number = 122; // Exemple value, value to test
		int number2 = number;
		int reverse = 0;		
		
		while (number >= 10) {
			int digit = number % 10;
			reverse += digit;
			reverse = reverse * 10;
			number = number - digit;
			number = number / 10;
		}
		
		reverse += number;
		
		boolean palindrome = false;
		
		if (reverse == number2) 
			palindrome = true;
		
		System.out.println(number2);
		System.out.println("Palindrome: "+palindrome);
	}

	// corrigé: algo ok
	// assumption string ok
	// aurait du demander si negatif fonctionne
	// un mini bug (manquait reverse += number), mais trouvé sans debugger
	// y a t'il des overflow dans la manipulation du reverse??
	// on peut faire un tout petit plus concis. ( number = number-digit ne sert à rien si on divise par 10 ensuite ; et multiplier rev par 10 avant de sommer
	//digit permet d'eviter le reverse += number final)
}
