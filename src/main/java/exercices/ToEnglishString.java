package exercices;

public class ToEnglishString {

	private static String[] unities = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"}; 
	private static String[] teens = {"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
	private static String[] tens = {"twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety" };
	private static String hundred = " hundred ";
	private static String thousand = " thousand, ";
	private static String milion = " milion, ";
	private static String bilion = " bilion, ";
	
	
	public static void main(String[] args) {
		
		System.out.println(toEnglishString(-9));
	}
	
	public static String toEnglishString( int i ) {
		
		if ( i < 0) {
			return "minus "+toEnglishString(-i);
		}
		if (i == 0)
			return "zero";
		
		if (i >= 1000000000) {
			return toEnglishString(i/1000000000)+bilion+toEnglishString(i%1000000000);
		}
		if (i >= 1000000) {
			return toEnglishString(i/1000000)+milion+toEnglishString(i%1000000);
		}
		if (i >= 1000) {
			return toEnglishString(i/1000)+thousand+toEnglishString(i%1000);
		}
		if (i >= 100) {
			return toEnglishString(i/100)+hundred+toEnglishString(i%100);
		}
		if (i < 10)
			return unities[i-1];
		if (i < 20)
			return teens[i-10];
		// 21 < i < 99
		return tens[i/10-2]+"-"+unities[i%10-1];
	}
	
}
