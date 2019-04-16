package objets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import composants.Jeu;
import geometrie.Vecteur;


/**
 * Objet principal de l'application. Une balle affecte par l'acceleration.
 * 
 * @author Ayoub
 *
 */
public class Balle extends SuperObjets {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double EPSILON = 0.01;
	public static double rayon = 3;// metres
	public static double vitMax = 150; // calculee avec des test pour eviter que les balles passent a travers les
										// objets
	private Vecteur vitesse;
	private Vecteur acc;
	private Vecteur normaleCollision;
	private Vecteur friction;
	private Vecteur ptCollision;
	private AffineTransform matTemp;
	private double masse = 10; // kg

	private Shape shapeBall = new Ellipse2D.Double();
	private Shape cutBall = null;

	private boolean dying = false;
	private int opacity = 250;
	private double dyingRate = 10;
	private boolean dead = false;

	/**
	 * Constructeur Balle
	 * 
	 * @param posInit
	 *            position initiale du centre de la balle
	 * @param vitInit
	 *            vitesse initiale de la balle
	 * @param acc
	 *            acceleration initiale appliquee sur la balle
	 */
	public Balle(Vecteur posInit, Vecteur vitInit, Vecteur acc) {
		super(posInit);
		vitesse = vitInit;
		this.acc = acc;

	}

	@Override
	/**
	 * Dessine une ellipse sur la position
	 */
	public void dessiner(Graphics2D g2d, AffineTransform matMc) {
		if (dying) {
			g2d.setColor(new Color(255, 255, 255, opacity));
			Ellipse2D.Double balle = new Ellipse2D.Double(position.getX() - rayon, position.getY() - rayon, rayon * 2,
					rayon * 2);
			shapeBall = matMc.createTransformedShape(balle);
			matTemp = matMc;

			if (cutBall != null) {
				// dessiner la balle coupee

				g2d.fill(cutBall);
			} else {
				// Si la balle n'est pas dans un portail et n'a pas besoin d'etre coupee

				g2d.fill(shapeBall);

			}
			
			opacity-=dyingRate;
			if(opacity<=0) {
				dead = true;
			}
		} else {
			g2d.setColor(Color.white);
			Ellipse2D.Double balle = new Ellipse2D.Double(position.getX() - rayon, position.getY() - rayon, rayon * 2,
					rayon * 2);
			shapeBall = matMc.createTransformedShape(balle);
			matTemp = matMc;

			if (cutBall != null) {
				// dessiner la balle coupee

				g2d.fill(cutBall);
			} else {
				// Si la balle n'est pas dans un portail et n'a pas besoin d'etre coupee

				g2d.fill(shapeBall);

			}

			// Dessiner les vecteurs
			if (Jeu.afficherVecteur) {
				vitesse.dessiner(g2d, matMc, position, null);
				if (friction != null) {
					friction.dessiner(g2d, matMc, ptCollision, new Color(0, 0, 0, 150));
				}

			}
		}
	}

	/**
	 * Calcul une nouvelle position d'apres la vitesse et l'acceleration
	 * 
	 * @param deltaT
	 */
	public void update(double deltaT) {
		Vecteur deltaVitesse = Vecteur.multiplie(acc, deltaT);

		Vecteur resultV = vitesse.additionne(deltaVitesse);

		vitesse.setX(resultV.getX());
		vitesse.setY(resultV.getY());

		// rendre la vitesse egale a 0 si proche de epsilon
		if (vitesse.getX() < EPSILON && vitesse.getX() >= 0 || vitesse.getX() > -EPSILON && vitesse.getX() <= 0) {
			vitesse.setX(0);
		}
		if (vitesse.getY() < EPSILON && vitesse.getY() >= 0 || vitesse.getY() > -EPSILON && vitesse.getY() <= 0) {
			vitesse.setY(0);
		}

		if (vitesse.module() >= vitMax) {
			vitesse.setModule(vitMax);
		}
		Vecteur deltaPosition = Vecteur.multiplie(vitesse, deltaT);
		Vecteur resultP = position.additionne(deltaPosition);
		position.setX(resultP.getX());
		position.setY(resultP.getY());

		// update la shape pour pouvoir coupee la balle

		Ellipse2D.Double balle = new Ellipse2D.Double(position.getX() - rayon, position.getY() - rayon, rayon * 2,
				rayon * 2);
		if (matTemp != null) {
			shapeBall = matTemp.createTransformedShape(balle);
		} else {
			shapeBall = balle;
		}

	}

	@Override
	/**
	 * Dessiner l'objet en mode scienfique avec ses vecteurs
	 * 
	 * @param g2d
	 *            graphics ou sera dessiner l'objet
	 * @param matMC
	 *            matrice de transformation du monde reelle vers le monde pixelise
	 */
	public void dessinerModeCreatif(Graphics2D g2d, AffineTransform matMC) {

	}

	/**
	 * 
	 * @return La position de la balle
	 */
	public Vecteur getPosition() {

		return position;
	}

	/**
	 * Donne une nouvelle valeur a la position de la balle
	 * 
	 * @param position
	 *            La nouvelle position voulue
	 */
	public void setPosition(Vecteur position) {
		this.position = position;

	}

	/**
	 * 
	 * @return retourne le rayon de la balle
	 */
	public double getRayon() {

		return rayon;
	}

	/**
	 * Donne une nouvelle valeur au vecteur de vitesse de la balle
	 * 
	 * @param vitTemp
	 *            Nouvelle vitesse desiree
	 */
	public void setVitesse(Vecteur vitTemp) {
		this.vitesse = vitTemp;

	}

	/**
	 * Donne la vitesse de la balle
	 * 
	 * @return La vitesse de la balle
	 */
	public Vecteur getVitesse() {
		return vitesse;
	}
	
	/**
	 *	Donne l'accélération de la balle
	 * @return l'accélération de la balle
	 */
	public Vecteur getAcceleration() {
		return acc;
	}

	/**
	 * Donner une direction a la normale lors de la collision
	 * 
	 * @param norm
	 *            nouvelle normale de collision de la balle
	 */
	public void setNormaleCollision(Vecteur norm) {
		normaleCollision = norm;
	}

	/**
	 * 
	 * @return La normale de collision lors d'une collision
	 */
	public Vecteur getNormaleCollision() {
		return normaleCollision;
	}

	/**
	 * 
	 * @return la masse de la balle
	 */
	public double getMasse() {
		return masse;
	}

	/**
	 * 
	 * @return La shape de la balle complete.
	 */
	public Shape getShapeBall() {
		return shapeBall;
	}

	/**
	 * Méthode qui permet de set le vecteur de friction
	 * 
	 * @param v
	 *            vecteur de friction
	 */
	public void setFriction(Vecteur v) {

		this.friction = v;

	}

	/**
	 * Assigne un vecteur au point de collision de la balle si la balle est en
	 * collision
	 */
	public void setPtCollision(Vecteur pt) {

		ptCollision = pt;
	}

	/**
	 * Donner une shape a la shape Balle coupee pour dessiner celle ci.
	 * 
	 * @param cutBall
	 *            La forme de la balle coupee par le portail.
	 */
	public void setCutBallShape(Shape cutBall) {
		this.cutBall = cutBall;

	}
	/**
	 * 
	 * @return Si la balle a ete comlpetement supprime du niveau.
	 */
	public boolean isDead() {
		return dead;
	}
	/**
	 * Rend la balle mourante, donc une animation de fading.
	 * @param dying Vrai si la balle va etre supprime completement du niveau. Permet de commencer une animation
	 */
	public void setDying(boolean dying) {
		this.dying = dying;
	}

	@Override
	public void setPeutPlacer(boolean b) {
		// TODO Auto-generated method stub
		
	}
}
