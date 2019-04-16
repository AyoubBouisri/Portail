package backgroundanimation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import geometrie.Vecteur;

/**
 * Une particule est un objet qui lorsqu'il se trouve pres d'une autre particule
 * cree un lien avec celle-ci. De plus, la particule essaiera toujours de
 * s'eloigner de la souris si celle-ci est proche.
 * 
 * @author Ayoub
 *
 */
public class Particule {

	private double rayon;
	private int FORCE_SOURIS = 75;
	private final double COEFF_FRICTION = 0.2;
	private final double STROKE_MIN = 0.2;
	private final double STROKE_MAX = 2;
	private final double DIST_MAX = 100; // la distance maximal pour dessiner une ligne entre 2 particules du systeme

	private ArrayList<Line2D.Double> lignes = new ArrayList<Line2D.Double>();

	private Vecteur pos;
	private Vecteur vit;
	private Vecteur vitInit;
	private Vecteur acc = new Vecteur(0, 0);

	/**
	 * Permet de creer une particule avec des valeurs initiales
	 * 
	 * @param pos
	 *            Vecteur position en pixels
	 * @param vit
	 *            Vecteur vit en pixels par s
	 * @param ray
	 *            Rayon de la particule
	 */
	public Particule(Vecteur pos, Vecteur vit, double ray) {
		this.pos = pos;
		this.vit = vit;
		vitInit = vit.copy();
		this.rayon = ray;
	}

	/**
	 * Methode qui dessiner un cercle pour la particule et toutes les lignes
	 * connectees aux autres particules.
	 * 
	 * @param g2d
	 *            Les graphiques ou sera dessiner la particule
	 */
	public void dessiner(Graphics2D g2d) {
		// dessiner un cercle avec une certaine taille
		Ellipse2D.Double ellipse = new Ellipse2D.Double(pos.getX() - rayon, pos.getY() - rayon, rayon * 2, rayon * 2);
		g2d.setColor(Color.white);
		g2d.fill(ellipse);
		ArrayList<Line2D.Double> lTemp = new ArrayList<Line2D.Double>();
		lTemp.addAll(lignes);

		for (Line2D.Double l : lTemp) {
			// d'apres la distance trouver la largeur de la stroke (minStroke = 0.2
			// maxStroke = 2)
			Vecteur posInit = new Vecteur(l.getX1(), l.getY1());
			Vecteur fin = new Vecteur(l.getX2(), l.getY2());
			double length = fin.soustrait(posInit).module();

			double strokeLength = map(length, 5, DIST_MAX, STROKE_MIN, STROKE_MAX);
			strokeLength = 2 - strokeLength;
			// System.out.println(strokeLength);
			g2d.setStroke(new BasicStroke((float) strokeLength));
			g2d.draw(l);
		}

		lignes.clear();
	}

	/**
	 * Methode qui update la position et creer les lignes qui connectent chaque
	 * particule
	 * 
	 * @param sysParticules
	 *            LE systeme de particules
	 * @param deltaT
	 *            Le pas
	 * @param width
	 *            LA largeur du composant parent
	 * @param height
	 *            La longueur du composant parent
	 * @param posSouris
	 */
	public void update(ArrayList<Particule> sysParticules, double deltaT, double width, double height,
			Vecteur posSouris) {

		// Faire que les particules se fassent repousser par la souris et leur donner
		// une friction
		if (posSouris != null) {

			Vecteur runAway = pos.soustrait(posSouris);
			if (runAway.module() < DIST_MAX) {
				runAway.setModule(FORCE_SOURIS / runAway.module());
				acc = runAway.copy();

			} else {

				if (vit.module() > vitInit.module()) {
					// friction
					Vecteur friction = new Vecteur(-vit.getX(), -vit.getY());
					friction.setModule(COEFF_FRICTION);
					acc = acc.additionne(friction);

				} else {
					acc = new Vecteur(0, 0);

				}

			}

		}

		//
		Vecteur deltaVitesse = Vecteur.multiplie(acc, deltaT);

		Vecteur resultV = vit.additionne(deltaVitesse);

		vit.setX(resultV.getX());
		vit.setY(resultV.getY());

		Vecteur deltaPosition = Vecteur.multiplie(vit, deltaT);
		Vecteur resultP = pos.additionne(deltaPosition);

		if (resultP.getX() < 0) {
			pos.setX(width);
		} else if (resultP.getX() > width) {
			pos.setX(0);
		} else {
			pos.setX(resultP.getX());
		}

		if (resultP.getY() < 0) {
			pos.setY(height);
		} else if (resultP.getY() > height) {
			pos.setY(0);
		} else {
			pos.setY(resultP.getY());
		}

		// Dessiner une ligne si une particule est proche d'une autre entre les deux
		// particules.s

		for (Particule p : sysParticules) {
			if (p != this) {
				double dist = p.getPosition().soustrait(this.pos).module();

				if (dist <= DIST_MAX) {
					lignes.add(new Line2D.Double(this.pos.getX(), this.pos.getY(), p.getPosition().getX(),
							p.getPosition().getY()));
				}
			}
		}

	}

	/**
	 * 
	 * @return La position de la particule
	 */
	private Vecteur getPosition() {
		return pos;
	}

	/**
	 * Methode qui donne une nouvelle valeur a une entree dapres un certain range
	 * 
	 * @param x
	 *            Valeur initiale
	 * @param min1
	 *            La valeur minimale du range initial
	 * @param max1
	 *            La valeur maximale du range initial
	 * @param min2
	 *            La valeur minimale du range voulu
	 * @param max2
	 *            La valeur maximale du range voulu
	 * @return La nouvelle valeur par rapport au range voulu
	 */
	public double map(double x, double min1, double max1, double min2, double max2) {
		return (x - min1) * (max2 - min2) / (max1 - min1) + min2;
	}
}
