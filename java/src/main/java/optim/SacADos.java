package optim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Dynamique en n*taille du sac, mémoire idem
// Récursive exponentielle mais moins limitée par la mémoire
public class SacADos {

	// pour max = 100
	// dynamique en n, 	// recursive exponentielle (2^n)
	// tel que 30 = 10ms / 20s
	// idem max = 1000
	// max = 100000, n=25: 200/700 ms
	// max = 1000000 (heap doit être monté): 1000/800
	public static void main(String[] args) {
		int[] objets = init(25,1000000);
		int poids = 10000000;
		System.out.println("Taille du sac: "+poids);
		System.out.println("Objets: "+ Arrays.toString(objets));
		
		long time = System.currentTimeMillis();
		int[] contenu = sacADosDynamique(objets, poids);
		long time2 = System.currentTimeMillis();
		Arrays.sort(contenu);
		System.out.println("Solution dynamique (en "+(time2-time)+" ms): "+poidsVecteur(contenu)+" "+Arrays.toString(contenu)+".");
		
		time = System.currentTimeMillis();
		contenu = sacADos2(objets, poids);
		time2 = System.currentTimeMillis();
		Arrays.sort(contenu);
		System.out.println("Solution récursive (en "+(time2-time)+" ms): "+poidsVecteur(contenu)+" "+Arrays.toString(contenu)+".");
	}
	
	// solution programmation dynamique (n*poids)
	public static int[] sacADosDynamique(int[] poidsObjets, int poidsSac) {
		
		int[][] solution = new int [poidsObjets.length+1][poidsSac+1];
		// solution[i][j] = optimum du problème à i elements pour un sac de poids j
		
		for (int i = 1; i <= poidsObjets.length ; i++) {	
			for (int p = 0 ; p <= poidsSac; p++) {		
				if (poidsObjets[i-1] <= p) {
					// l'objet rentre: 
					solution[i][p] = Math.max(poidsObjets[i-1]+solution[i-1][p-poidsObjets[i-1]], // soit on le prends
											  solution[i-1][p]);							  // soit non
				} else {
					// l'objet ne rentre pas
					solution[i][p] = solution[i-1][p];	// donc on ne le prends pas
				}
			}
		}
		
		List<Integer> liste = new ArrayList<>();
		int poidsRestant = poidsSac;
				// est ce que j'ai pris l'objet i?
		for (int i = poidsObjets.length; i > 0; i--) {
			
			if ((poidsRestant - poidsObjets[i-1] )<0)
				continue;
			
			if ( (poidsObjets[i-1]+solution[i-1][poidsRestant-poidsObjets[i-1]]) == 
					solution[i][poidsRestant] ) {
				liste.add(poidsObjets[i-1]);
				poidsRestant-=poidsObjets[i-1];
			}	
		}
		int[] result = new int[liste.size()];
		for (int kk = 0 ; kk < liste.size(); kk++) {
			result[kk] = liste.get(kk).intValue();
		}
		return result;
	}
	
	// solution recursive 2 (on suppose >= 1 objets)
	public static int[] sacADos2(int[] poidsObjets, int poidsSac) {
				
		if (poidsObjets.length == 1) {
			if (poidsObjets[0] <= poidsSac)
				return poidsObjets;
			else
				return new int[0];
		}	
	
		int[] objetsCandidats = supprime(poidsObjets, 0);
		int[] sans = sacADos2(objetsCandidats, poidsSac);
		
		if (poidsObjets[0] > poidsSac) 
			return sans;
		int[] avec = sacADos2(objetsCandidats, poidsSac-poidsObjets[0]);
		
		if (poidsVecteur(sans) >= (poidsVecteur(avec)+poidsObjets[0]) )
			return sans;
		else 
			return ajoute(avec, poidsObjets[0]);
	}
	
	// solution recursive
	// pour nombre d'appel à la méthode: n*n-1*...*1 = n!
	public static int[] sacADos(int[] poidsObjets, int poidsSac) {
		
		if (poidsObjets.length == 0) {
			return new int[0];
		}
		
		if (poidsObjets.length == 1) {
			if (poidsObjets[0] < poidsSac)
				return poidsObjets;
			else
				return new int[0];
		}
		
		if (poidsVecteur(poidsObjets) < poidsSac) 
			return poidsObjets;
		
		int poidsMaximal = -1;
		int[] meilleurSousEnsemble = null;
		for (int i = 0 ; i < poidsObjets.length; i++) {
		
			if (poidsObjets[i] == poidsSac) 
				return Arrays.copyOfRange(poidsObjets, i, i+1);
			
			int[] objetsCandidats = supprime(poidsObjets, i);
			
			if (poidsObjets[i] < poidsSac) {
				int [] sousEnsemble = sacADos(objetsCandidats,poidsSac - poidsObjets[i]);
				int poidsSousEnsemble = poidsVecteur(sousEnsemble);
				if ( poidsSousEnsemble + poidsObjets[i] > poidsMaximal) {
					poidsMaximal = poidsSousEnsemble + poidsObjets[i];
					meilleurSousEnsemble = ajoute(sousEnsemble, poidsObjets[i]);
				}
			} else {
				int [] sousEnsemble = sacADos(objetsCandidats,poidsSac);
				int poidsSousEnsemble = poidsVecteur(sousEnsemble);
				if ( poidsSousEnsemble > poidsMaximal) {
					poidsMaximal = poidsSousEnsemble;
					meilleurSousEnsemble = sousEnsemble;
				}
			}
		}
		return meilleurSousEnsemble;
	}
	
	private static int poidsVecteur(int[] objets) {
		
		if (objets == null)
			return 0;
		
		int poids = 0;
		for (int i : objets) {
			poids += i;
		}
		return poids;
	}
	
	
	private static int[] supprime(int[] vecteur, int indexASupprimer) {		
		
		int[] objetsCandidats = new int[vecteur.length-1];
		for (int j = 0 ; j < vecteur.length - 1 ; j++) {
			if (j < indexASupprimer)
				objetsCandidats[j] = vecteur[j];
			else
				objetsCandidats[j] = vecteur[j+1];
		}	
		return objetsCandidats;
	}
	
	private static int[] ajoute(int[] vecteur, int nouveau) {		
		
		if (vecteur == null)
			vecteur = new int[0];
		
		int[] retour = new int[vecteur.length+1];
		
		if (vecteur.length > 0) {
			for (int k = 0; k < vecteur.length ; k++)
				retour[k] = vecteur[k];
			retour[retour.length-1] = nouveau;
		} else {
			retour[0] = nouveau;
		}
		return retour;
	}
	
	public static int[] init(int taille, int max) {
		int[] i = new int[taille];
		for (int j= 0; j < taille; j++)
			i[j] = (int)Math.round(Math.random()*max);
		return i;
	}
}
