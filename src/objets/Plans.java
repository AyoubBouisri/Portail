package objets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import geometrie.Vecteur;

/**
 * Une droite avec une direction et une position initiale permettant de composer
 * les autres objets de l'application.
 * 
 * @author Ayoub
 *
 */
public class Plans extends SuperObjets implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vecteur posFinale;
	private Vecteur positionInit;
	private Vecteur posFinaleInit;
	private Vecteur directionPlan;

	private Vecteur normale;

	private Shape bounds;
	private Shape shapePosInit;
	private Shape shapePosFin;
	private double diamShapePos = 2;
	private double boundsH = 3;

	private boolean selectionne = false;
	private boolean peutPlacer = true;
	private boolean scaling = false;
	private int posCurrent;

	private double longueur;

	/**
	 * Constructeur qui calcul la longueur,la direction et la normale d'apres les
	 * deux points dune droite finie
	 * 
	 * @param posInit
	 *            position initiale du plan
	 * @param posFinale
	 *            position finale du plan
	 * 
	 */
	public Plans(Vecteur posInit, Vecteur posFinale) {
		super(posInit);

		this.posFinale = posFinale;
		setPositionsInit();
		longueur = posFinale.dist(position);
		// trouverle vecteur direction du plan et le normaliser
		directionPlan = posFinale.soustrait(position);
		directionPlan = directionPlan.normalise();
		// trouver la normale perpendiculaire a la direction du vecteur
		normale = directionPlan.cross(new Vecteur(0, 0, 1));
		normale = normale.normalise();

	}

	@Override
	/**
	 * Dessine une ligne representant le plan en 2d
	 */
	public void dessiner(Graphics2D g2d, AffineTransform matMc) {
		g2d.setStroke(new BasicStroke(1.5f));
		g2d.setColor(Color.white);
		if (!peutPlacer) {
			g2d.setColor(Color.black);
		}
		Line2D.Double line = new Line2D.Double(position.getX(), position.getY(), posFinale.getX(), posFinale.getY());

		g2d.draw(matMc.createTransformedShape(line));

	}

	/**
	 * 
	 * @return La position Initiale du plan
	 */
	public Vecteur getPositionInit() {
		return position;
	}

	/**
	 * retourne le vecteur plan
	 * 
	 * @return La direction du plan
	 */
	public Vecteur getDirection() {
		return directionPlan;
	}

	/**
	 * 
	 * @return La longueur du plan en metres
	 */
	public double getLongueur() {
		return longueur;
	}

	/**
	 * 
	 * @return la position finale du plan
	 */
	public Vecteur getPosFin() {
		return posFinale;
	}

	@Override
	public void dessinerModeCreatif(Graphics2D g2d, AffineTransform matMc) {

		g2d.setStroke(new BasicStroke(1.0f));
		Line2D.Double line = new Line2D.Double(position.getX(), position.getY(), posFinale.getX(), posFinale.getY());

		bounds = new Rectangle2D.Double(position.getX() - diamShapePos / 2, position.getY() - boundsH / 2,
				longueur + diamShapePos, boundsH);
		shapePosInit = new Ellipse2D.Double(position.getX() - diamShapePos / 2, position.getY() - diamShapePos / 2,
				diamShapePos, diamShapePos);
		shapePosFin = new Ellipse2D.Double(posFinale.getX() - diamShapePos / 2, posFinale.getY() - diamShapePos / 2,
				diamShapePos, diamShapePos);

		double angle = -(Math.atan2(directionPlan.getX(), directionPlan.getY()) - Math.PI / 2);
		AffineTransform matTemp = new AffineTransform(matMc);
		matTemp.rotate(angle, position.getX(), position.getY());

		bounds = matTemp.createTransformedShape(bounds);
		shapePosInit = matMc.createTransformedShape(shapePosInit);
		shapePosFin = matMc.createTransformedShape(shapePosFin);

		g2d.setStroke(new BasicStroke(3.0f));
		if (selectionne) {
			g2d.setColor(Color.ORANGE);
		} else if (!peutPlacer) {
			g2d.setColor(Color.MAGENTA);
		} else {
			g2d.setColor(Color.white);
		}
		g2d.draw(matMc.createTransformedShape(line));

		if (selectionne) {
			g2d.setColor(new Color(117, 169, 249));
			g2d.setStroke(new BasicStroke(1.0f));
			g2d.fill(shapePosFin);
			g2d.fill(shapePosInit);
			g2d.setColor(Color.ORANGE);
			g2d.draw(shapePosFin);
			g2d.draw(shapePosInit);
		}

	}

	/**
	 * Selectionne le plan ou le deselectionne
	 * 
	 * @param b
	 *            Valeur de la selection
	 */
	public void setSelected(boolean b) {
		selectionne = b;
	}

	/**
	 * Methode qui verifie si le plan contient un point
	 * 
	 * @param x
	 *            Valeur x du point en pixels
	 * @param y
	 *            Valeur y du point en pixels
	 * @return Vrai si le plan contient le point sinon faux.
	 */
	public boolean contains(double x, double y) {

		if (bounds.contains(x, y)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 
	 * @return La normale du vecteur creer d'apres la regle de la main droite
	 */
	public Vecteur getNormale() {

		return normale;
	}

	/**
	 * Voir si deux plans sont egales
	 * 
	 * @return vrai si cest le meme plan et faux si ce n'est pas le meme plan
	 */
	public boolean equals(Plans plTemp) {
		if (position.equals(plTemp.getPositionInit()) && posFinale.equals(plTemp.getPosFin())) {
			if (peutPlacer == plTemp.getPeutPlacer()) {
				return true;
			}

		}
		return false;

	}
	/**
	 * 
	 * @return Retourne vrai si on peut placer un portail dessus.
	 */
	private boolean getPeutPlacer() {
	
		return peutPlacer;
	}

	/**
	 * Methode qui change l'emplacement du plan d'apres le mouvement de la souris
	 * 
	 * @param position
	 *            Position en unites reelles de la souris
	 * @param startDrag
	 *            Position en unites reelles de la souris au debut du drag
	 */
	public void move(Vecteur posSouris, Vecteur startDrag) {
		if (!scaling) {

			Vecteur offSetPos1 = startDrag.soustrait(positionInit);
			Vecteur offSetPos2 = startDrag.soustrait(posFinaleInit);

			position = posSouris.soustrait(offSetPos1);
			posFinale = posSouris.soustrait(offSetPos2);

		}
	}

	/**
	 * Methode qui set les positions initiales avant le drag de la souris
	 */
	public void setPositionsInit() {
		posFinaleInit = posFinale;
		positionInit = position;
	}

	/**
	 * Methode qui verifie si un point est contenu sur les points initiale et final
	 * du plan pour permettre de le modifier
	 * 
	 * @param x
	 *            Position x en pixels
	 * @param y
	 *            Position y en pixels
	 * @return Vrai si le point est contenu sur les extremites du plan faux sinon.
	 */
	public boolean movePointsContains(double x, double y) {
		if (shapePosInit.contains(x, y) || shapePosFin.contains(x, y)) {
			return true;
		}
		return false;
	}

	/**
	 * Methode qui modifie la forme du plan d'apres la souris sur une des extremites
	 * du plan
	 * 
	 * @param positionReel
	 *            Vecteur position de la souris en unites reelles
	 * @param positionPixels
	 *            Vecteur position de la souris en pixels
	 */
	public void scale(Vecteur positionReelle, Vecteur positionPixels) {
		if (shapePosInit.contains(positionPixels.getX(), positionPixels.getY())) {

			position = positionReelle.copy();
			posCurrent = 0;
		} else if (shapePosFin.contains(positionPixels.getX(), positionPixels.getY())) {
			posFinale = positionReelle.copy();
			posCurrent = 1;
		} else {
			if (posCurrent == 0) {
				position = positionReelle.copy();
			} else if (posCurrent == 1) {
				posFinale = positionReelle.copy();
			}
		}

		directionPlan = posFinale.soustrait(position);
		longueur = directionPlan.module();
		directionPlan = directionPlan.normalise();

		normale = directionPlan.cross(new Vecteur(0, 0, 1));
		normale = normale.normalise();

		scaling = true;

	}

	/**
	 * 
	 * @return Si le plan est entrain detre change return vrai sinon faux.
	 */

	public boolean isScaling() {
		return scaling;
	}

	/**
	 * 
	 * @param scaling
	 *            Rendre le plan changeable
	 */
	public void setScaling(boolean scaling) {
		this.scaling = scaling;
	}

	/**
	 * Retourne une copie du vecteur.
	 */
	public Plans clone() {
		Plans  pl = new Plans(position.copy(), posFinale.copy());
		pl.setPeutPlacer(peutPlacer);
		return pl;
	}

	/**
	 * Donne une nouvelle valeur a la position initiale du plan
	 * 
	 * @param pos
	 *            Nouvelle position en unites reelles
	 */
	public void setPosition(Vecteur pos) {
		this.position = pos;
		setPositionsInit();
		directionPlan = posFinale.soustrait(position);
		longueur = directionPlan.module();
		directionPlan = directionPlan.normalise();

		normale = directionPlan.cross(new Vecteur(0, 0, 1));
		normale = normale.normalise();

	}

	/**
	 * Donne une nouvelle valeur a la position finale du plan
	 * 
	 * @param pos
	 *            Nouvelle position finale en unites reelles
	 */
	public void setPositionFinale(Vecteur pos) {
		posFinale = pos;
		setPositionsInit();
		directionPlan = posFinale.soustrait(position);
		longueur = directionPlan.module();
		directionPlan = directionPlan.normalise();

		normale = directionPlan.cross(new Vecteur(0, 0, 1));
		normale = normale.normalise();

	}

	public boolean peutPlacerPortail() {
		return peutPlacer;
	}

	public void setPeutPlacer(boolean b) {
		
		peutPlacer = b;
	}

}
