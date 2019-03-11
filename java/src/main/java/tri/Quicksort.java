package tri;

import java.util.ArrayList;
import java.util.List;

public class Quicksort {

	public static void main(String[] args) {
		
		int[] tableau = { 1 , 9 , 2 , 6 , 5, 7 , 4, 8, 3};
		List l = new ArrayList<>();
		for (int t : tableau) {
			l.add(t);
		}
		System.out.println(l);
		l =sort (l);
		System.out.println(l);
	}
	
	public static List<Comparable> sort(List<Comparable> liste ) {
		
		// liste triviale est toujours triée
		int size = liste.size();
		if (size <= 1)
			return liste;
		
		// Pivot au milieu de la liste
		int indexPivot = size / 2;
		Comparable pivot = liste.get(indexPivot);
		liste.remove(indexPivot);
			
		// tri récursif
		List<Comparable> inferieur = new ArrayList<>();
		List<Comparable> superieur = new ArrayList<>();
		
		for (Comparable c : liste) {
			if (c.compareTo(pivot) > 0)		
				superieur.add(c);
			else
				inferieur.add(c);
		}
		
		List<Comparable> tri = sort(inferieur);
		tri.add(pivot);
		tri.addAll(sort(superieur));
		return tri;
	}
}
