package geometrie;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.io.Serializable;

import objets.Balle;

/**
 * La classe Vecteur permet de realiser les operations de base sur un vecteur
 * Euclidien en trois dimensions (x,y,z), où x, y et z sont les composantes du
 * vecteur.
 * 
 * Cette classe est une version 3d modifiée de la classe SVector3d ecrite par
 * Simon Vezina.
 * 
 * @author Simon Vézina
 * @author Caroline Houle
 * @author Ayoub
 */
public class Vecteur implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1536986234596088459L;
	// champs de base
	private static final double EPSILON = 0.01; // tolerance utilisee dans les comparaisons reeles avec zero
	private static final double EPSILON_DRAW = 0.05;
	protected double x; // composante x du vecteur 3d
	protected double y; // composante y du vecteur 3d
	protected double z; // composante z du vecteur 3d

	/**
	 * Constructeur representant un vecteur 3d aux composantes nulles
	 */
	public Vecteur() {
		x = 0;
		y = 0;
		z = 0;
	}

	/**
	 * Constructeur avec composantes x,y;
	 * 
	 * @param x
	 *            La composante x du vecteur.
	 * @param y
	 *            La composante y du vecteur.
	 */
	public Vecteur(double x, double y) {
		this.x = x;
		this.y = y;
		this.z = 0;
	}

	/**
	 * Constructeur avec composantes x,y et z;
	 * 
	 * @param x
	 *            la composante x du vecteur
	 * @param y
	 *            la composante y du vecteur
	 * @param z
	 *            la composante z du vecteur
	 */
	public Vecteur(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Creer un vecteur a partir d'un angle en radian.
	 * 
	 * @param A
	 *            L'angle en radian
	 * @param module
	 *            Le module du vecteur
	 */
	public Vecteur(double A) {
		this.x = Math.cos(A);
		this.y = Math.sin(A);
		this.z = 0;

	}

	/**
	 * Methode qui donne acces a la composante x du vecteur.
	 * 
	 * @return La composante x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Methode qui donne acces a la composante y du vecteur.
	 * 
	 * @return La composante y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Methode qui donne acces a la composant z du vecteur.
	 * 
	 * @return
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Methode qui permet de modifier la composante x du vecteur.
	 * 
	 * @param x
	 *            La nouvelle composante x
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Methode qui permet de modifier la composante y du vecteur.
	 * 
	 * @param y
	 *            La nouvelle composante y
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * MEthode qui permet de modifier la composante z du vecteur.
	 * 
	 * @param z
	 *            la nouvelle composante z
	 */
	public void setZ(double z) {
		this.z = z;
	}

	/**
	 * Methode qui permet de modifier les composantes du vecteur.
	 * 
	 * @param x
	 *            La nouvelle composante x
	 * @param y
	 *            La nouvelle composante y
	 */
	public void setXYZ(double x, double y) {
		this.x = x;
		this.y = y;

	}

	/**
	 * Methode qui permet de modifier les composantes du vecteur
	 * 
	 * @param x
	 *            la nouvelle composante x
	 * @param y
	 *            la nouvelle composante y
	 * @param z
	 *            la nouvelle composante z
	 */
	public void setXYZ(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Genere une chaine de caractere avec les informations du vecteur
	 */
	@Override
	public String toString() {
		return "[ " + x + ", " + y + " , " + z + " ]";
	}

	/**
	 * Determine si le vecteur courant est egal ou non a un autre vecteur, a EPSILON
	 * pres
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (!(obj instanceof Vecteur))
			return false;

		Vecteur other = (Vecteur) obj;

		// Comparaison des valeurs x,y et z en utilisant la precision de EPSILON modulee
		// par la valeur a comparer
		if (Math.abs(x - other.x) > Math.abs(x) * EPSILON)
			return false;

		if (Math.abs(y - other.y) > Math.abs(y) * EPSILON)
			return false;

		if (Math.abs(z - other.z) > Math.abs(z) * EPSILON)
			return false;
		return true;
	}

	/**
	 * Methode qui calcule et retourner l'addition du vecteur courant et d'un autre
	 * vecteur. Le vecteur courant reste inchangé.
	 * 
	 * @param v
	 *            Le vecteur a ajouter au vecteur courant
	 * @return La somme des deux vecteurs
	 */
	public Vecteur additionne(Vecteur v) {
		return new Vecteur(x + v.x, y + v.y, z + v.z);
	}

	/**
	 * Methode de classe qui retourne l'addition de deux vecteurs quelconques.
	 * 
	 * @param v1
	 *            Le premier vecteur
	 * @param v2
	 *            Le deuxieme vecteur
	 * @return La somme des deux vecteurs
	 */
	public static Vecteur additionne(Vecteur v1, Vecteur v2) {
		return v1.additionne(v2);
	}

	/**
	 * Methode qui calcule et retourne le vecteur resultant de la soustraction d'un
	 * vecteur quelconque du vecteur courant. Le vecteur courant reste inchangé.
	 * 
	 * @param v
	 *            Le vecteur a soustraire au vecteur courant.
	 * @return La soustraction des deux vecteurs.
	 */
	public Vecteur soustrait(Vecteur v) {
		return new Vecteur(x - v.x, y - v.y, z - v.z);
	}

	/**
	 * Methode de classe qui retourne la soustraction entre deux vecteurs
	 * quelconques.
	 * 
	 * @param v1
	 *            Le premier vecteur
	 * @param v2
	 *            Le deuxieme vecteur, a soustraire du premier
	 * @return La diffrence entre les deux vecteurs
	 */
	public static Vecteur soustrait(Vecteur v1, Vecteur v2) {
		return v1.soustrait(v2);
	}

	/**
	 * Methode qui effectue la multiplication du vecteur courant par une scalaire.Le
	 * vecteur courant reste inchangé.
	 * 
	 * @param m
	 *            - Le muliplicateur
	 * @return Le resultat de la multiplication par un scalaire m sur le vecteur.
	 */
	public Vecteur multiplie(double m) {
		return new Vecteur(m * x, m * y, m * z);
	}

	/**
	 * Methode de classe qui effectue la multiplication d'un vecteur quelconque par
	 * une scalaire.
	 * 
	 * @param v
	 *            Le vecteur
	 * @param m
	 *            Le muliplicateur
	 * @return Le resultat de la multiplication par un scalaire m sur le vecteur.
	 */
	public static Vecteur multiplie(Vecteur v, double m) {
		return v.multiplie(m);
	}

	/**
	 * Methode pour obtenir le module du vecteur courant.
	 * 
	 * @return Le module du vecteur courant.
	 */
	public double module() {
		return Math.sqrt((x * x) + (y * y) + (z * z));
	}

	/**
	 * Methode pour obtenir le module du vecteur courant au carre.
	 * 
	 * @return Le module du vecteur courant au carre.
	 */
	public double moduleSq() {
		return (x * x) + (y * y) + (z * z);
	}

	/**
	 * Methode de classe pour obtenir le module d'un vecteur quelconque.
	 * 
	 * @param v
	 *            Le vecteur
	 * @return Le module du vecteur.
	 */
	public static double module(Vecteur v) {
		return v.module();
	}

	/**
	 * Methode pour normaliser le vecteur courant. Un vecteur normalise possede la
	 * meme orientation, mais possede une longeur unitaire. Si le module du vecteur
	 * est nul, le vecteur normalise sera le vecteur nul (0.0, 0.0).
	 * 
	 * @return Le vecteur normalise.
	 * @throws Exception
	 *             Si le vecteur ne peut pas etre normalise etant trop petit ou de
	 *             longueur nulle.
	 */
	public Vecteur normalise() {
		double mod = module(); // obtenir le module du vecteur

		// Verification du module. S'il est trop petit, nous ne pouvons pas
		// numeriquement normaliser ce vecteur
		if (mod < EPSILON) {

			return new Vecteur(0, 0);
		} else {
			return new Vecteur(x / mod, y / mod);
		}
	}

	/**
	 * Methode de classe pour normaliser un vecteur quelconque. Un vecteur normalise
	 * possede la meme orientation, mais possede une longeur unitaire. Si le module
	 * du vecteur est nul, le vecteur normalise sera le vecteur nul (0.0, 0.0).
	 * 
	 * @param v
	 *            Le vecteur
	 * @return Le vecteur normalise.
	 * @throws Exception
	 *             Si le vecteur ne peut pas etre normalise etant trop petit ou de
	 *             longueur nulle.
	 */
	public static Vecteur normalise(Vecteur v) throws Exception {
		return v.normalise();
	}

	/**
	 * Methode pour effectuer le produit scalaire du vecteur courant avec un autre
	 * vecteur.
	 * 
	 * @param v
	 *            L'autre vecteur.
	 * @return Le produit scalaire entre les deux vecteurs.
	 */
	public double prodScalaire(Vecteur v) {
		return (x * v.x + y * v.y + z * v.z);
	}

	/**
	 * Methode de classe pour effectuer le produit scalaire entre deux vecteurs
	 * quelconques.
	 * 
	 * @param v1
	 *            Le premier vecteur
	 * @param v2
	 *            Le deuxieme vecteur
	 * @return Le produit scalaire entre les deux vecteurs.
	 */
	public static double prodScalaire(Vecteur v1, Vecteur v2) {
		return (v1.prodScalaire(v2));
	}

	// Methode cree par Ayoub Bouisri
	/**
	 * Methode pour trouver la distance entre deux vecteur position
	 * 
	 * @param v
	 *            L'autre vecteur.
	 * @return La distance entre les deux vecteurs.
	 */
	public double dist(Vecteur v) {
		double resul = Math.pow(this.x - v.getX(), 2) + Math.pow(this.y - v.getY(), 2);
		return Math.sqrt(resul);
	}

	/**
	 * Methode de la classe pour effectuer un produit vectoriel entre deux vecteurs.
	 * 
	 * @param v
	 *            L'autre vecteur.
	 * @return retourne le produit vectoriel
	 */
	public Vecteur cross(Vecteur v) {
		double[][] matrice = new double[2][3];
		Vecteur vTemp = this;
		for (int i = 0; i < 2; i++) {
			matrice[i][0] = vTemp.getX();
			matrice[i][1] = vTemp.getY();
			matrice[i][2] = vTemp.getZ();
			vTemp = v;
		}

		double d1 = matrice[0][1] * matrice[1][2] - matrice[0][2] * matrice[1][1];
		double d2 = -1 * (matrice[0][0] * matrice[1][2] - matrice[0][2] * matrice[1][0]);
		double d3 = matrice[0][0] * matrice[1][1] - matrice[0][1] * matrice[1][0];

		return new Vecteur(d1, d2, d3);
	}

	/**
	 * Calcul un angle entre deux vecteur
	 * 
	 * @param v1
	 *            premier vecteur
	 * @param v2
	 *            deuxieme vecteur
	 * @return l'angle entre le permier et deuxieme vecteur
	 */
	public static double angleEntre(Vecteur v1, Vecteur v2) {
		double num = v1.prodScalaire(v2);
		double denom = v1.module() * v2.module();

		return Math.acos(num / denom);
	}

	/**
	 * Calcul la projection de deux vecteurs
	 * 
	 * @return
	 */
	public static Vecteur projection(Vecteur u, Vecteur v) {
		double ratio = u.prodScalaire(v) / Math.pow(v.module(), 2);
		return v.multiplie(ratio);
	}

	/**
	 * Donne un nouveau module a un vecteur
	 */

	public void setModule(double newMod) {
		double currentMod = this.module();
		double ratioMod;
		if (currentMod == 0) {
			ratioMod = 0;
		} else {
			ratioMod = newMod / currentMod;
		}

		this.setXYZ(this.x * ratioMod, this.y * ratioMod, this.z * ratioMod);
	}

	/**
	 * Methode qui renvoie un vecteur identique a celui copié
	 * 
	 * @return un vecteur identique a celui ci
	 */
	public Vecteur copy() {
		return new Vecteur(this.x, this.y, this.z);
	}

	/**
	 * Methode qui dessine le vecteur et map la couleur du vecteur d'apres son
	 * module. (Auteur Duy)
	 * 
	 * @param g2d
	 *            Graphics
	 * @param matMc
	 *            La matrice de transformation
	 * @param position
	 *            La position a partir de la quelle le vecteur est dessine
	 */
	public void dessiner(Graphics2D g2d, AffineTransform matMc, Vecteur position, Color cl) {
		double moduleVecteur = this.module();
		// Une liste contenant {vert,jaune,orange pale,orange fonce,rouge}
		
			Color[] colors = { new Color(117, 239, 40), new Color(252, 245, 55), new Color(255, 194, 40),
					new Color(255, 94, 45), new Color(255, 39, 20) };
		
		if (moduleVecteur > EPSILON_DRAW) {
			if (moduleVecteur < Balle.rayon) {
				// LOCK LA LONGUEUR DU VECTEUR AU RAYON DE LA BALLE
				moduleVecteur = Balle.rayon;
			}
			if (cl == null) {
				// MAP LE MODULE DE VITESSE ENTRE UNE VALEUR 0 - 3.99

				double mappedVal = (moduleVecteur - Balle.rayon) / ((Balle.vitMax - 100) - Balle.rayon) * (3.99 - 0)
						+ 0;

				// LOCK LA VALEUR A LA LENGTH DES COULEURS
				if ((int) mappedVal > colors.length - 1) {
					mappedVal = colors.length - 1;
				}
				g2d.setColor(colors[(int) mappedVal]);
			}else {
				g2d.setColor(cl);
			}
			AffineTransform matTemp = (AffineTransform) matMc.clone();
			g2d.setStroke(new BasicStroke(2.0f));
			double angle = -((Math.atan2(x, y)) - Math.PI / 2);
			
			Line2D.Double vecteurDessin = new Line2D.Double(position.getX(), position.getY(),
					position.getX() + moduleVecteur, position.getY());
			double longFleche = 1;
			Line2D.Double fleche1 = new Line2D.Double(position.getX() + moduleVecteur, position.getY(),
					position.getX() + moduleVecteur - longFleche, position.getY() + longFleche);
			Line2D.Double fleche2 = new Line2D.Double(position.getX() + moduleVecteur, position.getY(),
					position.getX() + moduleVecteur - longFleche, position.getY() - longFleche);

			matTemp.rotate(angle, position.getX(), position.getY());

			// DESSINER LES LIGNES
			g2d.draw(matTemp.createTransformedShape(vecteurDessin));
			g2d.draw(matTemp.createTransformedShape(fleche1));
			g2d.draw(matTemp.createTransformedShape(fleche2));
		}

	}

	/**
	 * Si le vecteur est proche de nul. le rendre nul.
	 */
	public void clean() {
		if (this.x >= -EPSILON && this.x <= 0 || this.x <= EPSILON && this.x >= 0) {
			this.x = 0;
		}
		if (this.y >= -EPSILON && this.y <= 0 || this.y <= EPSILON && this.y >= 0) {
			this.y = 0;
		}

	}

}// fin classe Vecteur
