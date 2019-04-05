package tri;

import java.util.ArrayList;
import java.util.List;

public class Quicksort {

	public static void main(String[] args) {
		
		int[] tableau = { 1 , 9 , 2 , 6 , 5, 7 , 4, 8, 3};
		List<Comparable<Object>> l = new ArrayList<Comparable<Object>>();
		for (Integer t : tableau) {
			l.add((Comparable)t);
		}
		System.out.println(l);
		l = sort(l);
		System.out.println(l);
	}
	
	public static List<Comparable<Object>> sort(List<Comparable<Object>> liste ) {
		
		// liste triviale est toujours triée
		int size = liste.size();
		if (size <= 1)
			return liste;
		
		// Pivot au milieu de la liste
		int indexPivot = size / 2;
		Comparable<Object> pivot = liste.get(indexPivot);
		liste.remove(indexPivot);
			
		// tri récursif
		List<Comparable<Object>> inferieur = new ArrayList<Comparable<Object>>();
		List<Comparable<Object>> superieur = new ArrayList<Comparable<Object>>();
		
		for (Comparable<Object> c : liste) {
			if (c.compareTo(pivot) > 0)		
				superieur.add(c);
			else
				inferieur.add(c);
		}
		
		List<Comparable<Object>> tri = sort(inferieur);
		tri.add(pivot);
		tri.addAll(sort(superieur));
		return tri;
	}
}