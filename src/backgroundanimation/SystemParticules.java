package backgroundanimation;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import geometrie.Vecteur;

/**
 * 
 * Une classe qui anime un systeme de particules interactif avec la souris.
 * 
 * @author Ayoub
 */
public class SystemParticules {
	private final double MODULE_VITESSE = 3;
	private ArrayList<Particule> sysParticules = new ArrayList<Particule>();
	private Vecteur posSouris = null;

	private double width, height;

	/**
	 * Methode qui cree un systeme de particules avec un certain nombre de
	 * particules.
	 * 
	 * @param nbParticules
	 *            le nombre de particules initiales dans le systeme.
	 */
	public SystemParticules(int nbParticules, double width, double height) {
		this.width = width;
		this.height = height;
		for (int i = 0; i < nbParticules; i++) {
			double posX = ThreadLocalRandom.current().nextDouble(0, width);
			double posY = ThreadLocalRandom.current().nextDouble(0, height);
			Vecteur pos = new Vecteur(posX, posY);
			double vitX = ThreadLocalRandom.current().nextDouble(-1, 1);
			double vitY = ThreadLocalRandom.current().nextDouble(-1, 1);
			Vecteur vit = new Vecteur(vitX, vitY);
			vit.setModule(MODULE_VITESSE);

			double rayon = ThreadLocalRandom.current().nextDouble(1, 5);

			sysParticules.add(new Particule(pos, vit, rayon));

		}
	}

	/**
	 * Methode qui dessine chaque particule dans le systeme de particules
	 * 
	 * @param g2d
	 *            les graphiques
	 * 
	 */
	public void dessiner(Graphics2D g2d) {
		for (Particule p : sysParticules) {
			p.dessiner(g2d);
		}
	}

	/**
	 * Methode qui update chaque particule du systeme
	 * 
	 * @param deltaT
	 *            Le pas
	 */
	public void update(double deltaT) {
		for (Particule p : sysParticules) {
			p.update(sysParticules, deltaT, width, height, posSouris);
		}
	}

	/**
	 * Methode qui donne des nouvelles valeur a la position de la souris dans le
	 * composant
	 * 
	 * @param x
	 *            Position x en pixels de la souris
	 * @param y
	 *            Position y en pixels de la souris
	 */
	public void updateSouris(int x, int y) {
		posSouris = new Vecteur(x, y);

	}

}
