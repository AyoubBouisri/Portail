package niveau;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import composants.ComposantAnimation;

import geometrie.Vecteur;

/**
 * Classe qui sauvegarde tout les niveaux creer dans une ArrayList. Un objet de
 * type ArrayNiveau sera sauvegarder dans un composant d 'animation qui
 * imprimera le niveau courant. De plus cette classe permet de se sauvegarder
 * dans un fichier.
 * 
 * @author Ayoub
 *
 */
public class ArrayNiveau {
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	private ArrayList<Niveau> niveaux = new ArrayList<Niveau>();
	private Niveau currentNiv;
	private int maxSize = 20;
	private String[] mois = { "janvier", "février", "mars", "avril", "mai", "juin", "juillet", "aout", "septembre",
			"octobre", "novembre", "décembre" };

	public ArrayNiveau(ComposantAnimation parent) {

		// lire Fichier puis ajouter les niveaux lus dans la arrayList Niveaux.
		Niveau nivOriginal = new Niveau(new Vecteur(0, 9.8), parent.getModele(), parent);
		add(nivOriginal, "Niveau Original", "Ayoub Bouisri");

		
		lireFichierBinaire();
		for (Niveau n : niveaux) {
			n.resetApresLecture(parent);
			
		}

		currentNiv = niveaux.get(0);
	}

	/**
	 * Methode qui ajoute un niveau dans la liste des niveaux. Si il n'y a plus de
	 * place pour des noueaux niveau. Une fenetre demandera a l'utilisateur de
	 * supprimer un niveau.
	 * 
	 * @param newNiv
	 *            le nouveau niveau a ajouter
	 * @param nomNiveau
	 *            Le nom du niveau
	 * @param authorName
	 *            Le nom de l'auteur du niveau.
	 * @return Vrai si le niveau a ete ajouter faux sinon.
	 */
	public void add(Niveau newNiv, String nomNiveau, String authorName) {
		if (niveaux.size() < maxSize) {
			newNiv.setNomNiveau(nomNiveau);
			newNiv.setAuthorName(authorName);

			String date = getDate();
			newNiv.setDateCreation(date);
			niveaux.add(newNiv);
			// ajouter le dernier ajouter
			// currentNiv = niveaux.get(niveaux.size()-1);
			currentNiv = newNiv;

		} else {
			// lancer une fenetre indiquant que le niveau ne peut pas etre sauvegarder.
			JOptionPane.showMessageDialog(null,
					"Oops. Il n'y a plus de place disponible pour votre niveau. \nVeuillez supprimer un autre niveau pour créer de l'espace.",
					"Manque d'espace", JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * Methode qui remplace un niveau dans la liste par un autre
	 * 
	 * @param nivARemplacer
	 *            Le niveau qui sera remplace
	 * @param nouveauNiveau
	 *            Le nouveau niveau qui prendera la place du niveau a remplacer
	 * @param nomNiv
	 *            Le nom du nouveau niveau
	 * @param authorName
	 *            Le nom de l'auteur du nouveau niveau
	 */
	public void replace(int indexNivARemplacer, Niveau nouveauNiveau, String nomNiv, String authorName) {

		int index = indexNivARemplacer;
		nouveauNiveau.setNomNiveau(nomNiv);
		nouveauNiveau.setAuthorName(authorName);
		String date = getDate();
		nouveauNiveau.setDateCreation(date);
		niveaux.set(index, nouveauNiveau);
		currentNiv = niveaux.get(index);

	}

	/**
	 * Méthode qui supprime un niveau donné. Si il ne reste qu'un niveau dans la
	 * liste des niveaux. Le niveau ne sera pas supprimer.
	 * 
	 * @param nivASupprimer
	 *            Le niveau a supprimer de la liste.
	 */
	public void supprimer(Niveau nivASupprimer) {
		if (niveaux.size() > 1) {
			int index = niveaux.indexOf(nivASupprimer);
			if (index != -1) {
				niveaux.remove(nivASupprimer);

				if (index == 0) {
					currentNiv = niveaux.get(index);

				} else {
					currentNiv = niveaux.get(index - 1);
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "Il ne reste qu'un seul niveau. \nImpossible de le supprimer.",
					"Informations", JOptionPane.INFORMATION_MESSAGE);
		}

	}

	/**
	 * Methode qui trouve la date et l'heure du moment ou elle a ete appellee.
	 * 
	 * @return La date et l'heure du moment.
	 */
	public String getDate() {
		LocalDate localDate = LocalDate.now();
		String dateNum = dtf.format(localDate);

		String jour = dateNum.substring(3, 5);
		String moisMot = mois[Integer.parseInt(dateNum.substring(0, 2)) - 1];
		String annee = dateNum.substring(6, 10);

		return jour + " " + moisMot + " " + annee;

	}

	/**
	 * 
	 * @return Le niveau courant de la liste.
	 */
	public Niveau getCurrentNiveau() {
		return currentNiv;
	}

	/**
	 * Methode qui change le niveau courant pour le prochain.
	 */
	public void next() {
		int index = niveaux.indexOf(currentNiv);
		if (index < niveaux.size() - 1) {
			currentNiv = niveaux.get(index + 1);
			currentNiv.reset();
		}

	}

	/**
	 * Methode qui change le niveau courant pour le precedant.
	 */
	public void previous() {
		int index = niveaux.indexOf(currentNiv);
		if (index > 0) {
			currentNiv = niveaux.get(index - 1);
			currentNiv.reset();
		}
	}

	/**
	 * 
	 * @return La grandeur de la liste des niveaux.
	 */
	public int getSize() {
		return niveaux.size();
	}

	/**
	 * 
	 * @return L'index du niveau courant dans la liste.
	 */
	public int indexOfCurrent() {
		return niveaux.indexOf(currentNiv);
	}

	/**
	 * 
	 * @param i
	 *            L'index du niveau voulu.
	 * @return Le niveau a l'index entre.
	 */
	public Niveau get(int i) {
		return niveaux.get(i);
	}

	/**
	 * Methode qui imprime les informations de chaque niveau dans la ArrayList
	 * niveaux.
	 */
	public void print() {
		for (Niveau n : niveaux) {
			n.print();
		}

	}

	/**
	 * Methode qui permet de sauvegarder la liste des niveaux dans un fichier
	 * binaire.
	 * 
	 * @throws IOException
	 */
	public void ecrireFichierBinaire() {

		try {
			FileOutputStream arrayNivFichier;

			ObjectOutputStream fluxArrayNiv;

			arrayNivFichier = new FileOutputStream("arrayNivObj.dat");

			fluxArrayNiv = new ObjectOutputStream(arrayNivFichier);
			fluxArrayNiv.writeObject(niveaux);

			fluxArrayNiv.close();

		} catch (IOException e) {
			System.out.println("Erreur à l'écriture:" + e.toString());
		}

	}

	/**
	 * Methode qui permet de lire une liste de niveaux a partir d'un fichier
	 * binaire.
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	public void lireFichierBinaire() {
		try {
			FileInputStream arrayNivFichier;
			ObjectInputStream fluxArrayNiv;

			arrayNivFichier = new FileInputStream("arrayNivObj.dat");

			fluxArrayNiv = new ObjectInputStream(arrayNivFichier);

			niveaux = (ArrayList<Niveau>) (fluxArrayNiv.readObject());

			fluxArrayNiv.close();

		} catch (ClassNotFoundException e) {
			System.out.println("Incohérence de classe" + e.toString());
		} catch (IOException e) {
			System.out.println("Erreur à la lecture!" + e.toString());
		}

	}

}
