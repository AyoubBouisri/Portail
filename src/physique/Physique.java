package physique;

import geometrie.Vecteur;
import objets.Balle;

/**
 * Classe comportant des methodes qui executes des formules physiques
 * (Friction,impulsion ...)
 * 
 * @author Ayoub
 *
 */
public class Physique {

	/**
	 * Calculer la nouvelle vitesse d'une balle a partir de son impulsion
	 * 
	 * @param impulsion
	 *            impulsion de la balle lors de l'impact
	 * @param balle
	 *            balle qui est en collision
	 */
	public static void calculerVit(double impulsion, Balle balle) {
		Vecteur newVit = balle.getVitesse()
				.additionne(balle.getNormaleCollision().multiplie(impulsion / balle.getMasse()));

		balle.setVitesse(newVit);

	}

	/**
	 * Calculer l'impulsion entre deux balles
	 * 
	 * @param balleA
	 *            Premiere balle
	 * @param balleB
	 *            Deuxieme balle
	 * @param coeffRest
	 *            Le coefficient de restitution (entre 0 et 1)
	 * @return L'impulsion de la collision entre les deux balles
	 */
	public static double calculerImpulsion(Balle balleA, Balle balleB, double coeffRest) {

		double j = -(1 + coeffRest) / (1 / balleA.getMasse() + 1 / balleB.getMasse());
		Vecteur vitTot = balleA.getVitesse().soustrait(balleB.getVitesse());

		j = j * vitTot.prodScalaire(balleA.getNormaleCollision());

		return j;
	}

	/**
	 * Calculer l'impulsion entre une balle et un objet avec une masse infinie
	 * 
	 * @param balleA
	 * @param coeffRest
	 *            Le coefficient de restitution (entre 0 et 1)
	 * @return retourne l'impulsion entre la balle et l'objet de masse infinie
	 */
	public static double calculerImpulsion(Balle balleA, double coeffRest) {
		Vecteur vitTot = balleA.getVitesse();
		double j = -(1 + coeffRest) / (1 / balleA.getMasse());
		j = j * vitTot.prodScalaire(balleA.getNormaleCollision());
		return j;
	}

	/**
	 * méthode pour calculer la force de la friction (Auteur Duy)
	 * 
	 * @param g
	 *            la gravité
	 * @param masse
	 * @param angle
	 *            l'angle du plan par rapport à l'origine en rad
	 * @param coeffFriction
	 *            le coefficient de friction
	 * @return
	 */
	public static double calculefriction(double g, double masse, double angle, double coeffFriction) {

		return coeffFriction * g * masse * Math.cos(angle);
	}

}
