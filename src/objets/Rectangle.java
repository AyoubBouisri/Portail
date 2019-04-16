package objets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import geometrie.Vecteur;
/**
 * 
 * @author Duy
 *
 */
public class Rectangle extends SuperObjets implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private double WIDTH_MIN = 5;
	private double HEIGHT_MIN = 5;
	private Vecteur dirPrincipal; // Vecteur qui represente le sens du rectangle. (longueur)
	private Vecteur dirSecondaire; // Vecteur qui est perpendiculaire au dirPrincipal. (largeur);

	private Vecteur[] points = new Vecteur[4];
	private Plans[] plans = new Plans[4];
	private Vecteur posInit;
	private double width;
	private double height;

	private Path2D.Double path = new Path2D.Double();
	private Shape shapeObj = new Rectangle2D.Double();
	private boolean selected = false;

	private Shape scaleG, scaleD, scaleH, scaleB;
	private Rectangle2D.Double scaleGRect, scaleDRect, scaleHRect, scaleBRect;
	private double diamScale = 2;
	private boolean scaling = false;
	private int posCurrent = 0;

	public Rectangle(Vecteur posInit, double width, double height) {

		super(posInit);
		this.width = width;
		this.height = height;

		this.position = posInit;
		this.posInit = posInit.copy();

		// trouver les vecteurs de directions
		dirPrincipal = new Vecteur(1, 0);
		dirPrincipal.setModule(width);
		dirSecondaire = new Vecteur(-dirPrincipal.getY(), dirPrincipal.getX());
		dirSecondaire.setModule(height);

		dirSecondaire = new Vecteur(-dirPrincipal.getY(), dirPrincipal.getX());
		dirSecondaire.setModule(height);
		// trouver les points du rectangles. Les points sont en ordre anti-horaire.
		// Commencant par le haut gauche.
		points[0] = position;
		points[1] = points[0].additionne(dirPrincipal);
		points[2] = points[1].additionne(dirSecondaire);
		points[3] = points[0].additionne(dirSecondaire);
		// Avec les points ajouter des plans. Sens antiHoraire commencant avec le
		// plan du haut
		plans[0] = new Plans(points[0], points[1]);
		plans[1] = new Plans(points[1], points[2]);
		plans[2] = new Plans(points[2], points[3]);
		plans[3] = new Plans(points[3], points[0]);

		path.moveTo(points[0].getX(), points[0].getY());
		path.lineTo(points[1].getX(), points[1].getY());
		path.lineTo(points[2].getX(), points[2].getY());
		path.lineTo(points[3].getX(), points[3].getY());
		path.closePath();
	}

	@Override
	/**
	 * Methode qui dessine les plans du rectangle en mode creatif
	 */
	public void dessinerModeCreatif(Graphics2D g2d, AffineTransform matMc) {
		// ajouter la shape generale de l'objet;
		shapeObj = matMc.createTransformedShape(path);

		// Trouver les positions des shape qui servent a scale l'objet

		setShapeScaling(matMc);

		g2d.setColor(new Color(117, 169, 249));
		g2d.fill(shapeObj);
		if (selected) {

			g2d.setColor(new Color(249, 179, 87, 150));
			g2d.fill(shapeObj);

			g2d.setColor(Color.ORANGE);

		} else {
			if(plans[0].peutPlacerPortail()) {
				g2d.setColor(Color.white);
			}else {
				g2d.setColor(Color.MAGENTA);
			}
			
		}

		for (int i = 0; i < plans.length; i++) {

			Plans pl = plans[i];
			Line2D.Double line = new Line2D.Double(pl.getPosition().getX(), pl.getPosition().getY(),
					pl.getPosFin().getX(), pl.getPosFin().getY());
			g2d.setStroke(new BasicStroke(3.0f));
			g2d.draw(matMc.createTransformedShape(line));

		}

		g2d.setColor(new Color(117, 169, 249));
		if (selected) {
			g2d.fill(scaleD);
			g2d.fill(scaleG);
			g2d.fill(scaleH);
			g2d.fill(scaleB);
			g2d.setColor(Color.ORANGE);
			g2d.setStroke(new BasicStroke(1.0f));
			g2d.draw(scaleD);
			g2d.draw(scaleG);
			g2d.draw(scaleH);
			g2d.draw(scaleB);
		}

	}

	@Override
	/**
	 * Methode qui dessiner les plans du rectangle
	 */
	public void dessiner(Graphics2D g2d, AffineTransform matMc) {
		shapeObj = matMc.createTransformedShape(path);

		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fill(shapeObj);
		
		for(Plans p : plans) {
			p.dessiner(g2d, matMc);
		}

	}

	/**
	 * Methode qui trouve les positions et cree les shape qui permettent de scale le
	 * rectangle
	 */
	public void setShapeScaling(AffineTransform matMc) {
		Vecteur dirSecTemp = dirSecondaire.copy();
		Vecteur dirPrincipTemp = dirPrincipal.copy();
		dirSecTemp.setModule(height / 2);
		dirPrincipTemp.setModule(width / 2);
		Vecteur posPtG = points[0].additionne(dirSecTemp);
		Vecteur posPtD = points[1].additionne(dirSecTemp);
		Vecteur posPtH = points[0].additionne(dirPrincipTemp);
		Vecteur posPtB = points[3].additionne(dirPrincipTemp);
		scaleGRect = new Rectangle2D.Double(posPtG.getX() - diamScale / 2, posPtG.getY() - diamScale / 2, diamScale,
				diamScale);
		scaleDRect = new Rectangle2D.Double(posPtD.getX() - diamScale / 2, posPtD.getY() - diamScale / 2, diamScale,
				diamScale);
		scaleHRect = new Rectangle2D.Double(posPtH.getX() - diamScale / 2, posPtH.getY() - diamScale / 2, diamScale,
				diamScale);
		scaleBRect = new Rectangle2D.Double(posPtB.getX() - diamScale / 2, posPtB.getY() - diamScale / 2, diamScale,
				diamScale);

		scaleG = matMc.createTransformedShape(scaleGRect);
		scaleD = matMc.createTransformedShape(scaleDRect);
		scaleH = matMc.createTransformedShape(scaleHRect);
		scaleB = matMc.createTransformedShape(scaleBRect);
	}

	/**
	 * Methode qui donne une valeur a la selection de l'objet
	 * 
	 * @param b
	 *            valeur de la selection de l'objet
	 */
	public void setSelected(boolean b) {
		selected = b;
	}

	/**
	 * Methode qui verifie si l'objet contient une position.
	 * 
	 * @param pos
	 *            La position qui est verifiee en pixels.
	 */
	public boolean contains(Vecteur pos) {
		if (shapeObj.contains(pos.getX(), pos.getY())) {
			return true;
		}
		return false;
	}

	/**
	 * Methode qui bouge l'objet d'apres le mouvement de la souris
	 * 
	 * @param souris
	 *            Position courante de la souris
	 * @param startDrag
	 *            Position ou le drag de la souris a commence.
	 */
	public void move(Vecteur souris, Vecteur startDrag) {
		if (!scaling) {
			Vecteur offSetPos1 = startDrag.soustrait(posInit);
			position = souris.soustrait(offSetPos1);

			calculerPositions();
		}

	}

	/**
	 * Donne une valeur a la position initiale
	 * 
	 */
	public void setPositionInit() {
		posInit = position.copy();
	}

	/**
	 * retourne une copie de ce rectangle.
	 */
	@Override
	public Rectangle clone() {
		Rectangle newRect = new Rectangle(position, width, height);
		newRect.setPeutPlacer(plans[0].peutPlacerPortail());
		return newRect;
	}

	/**
	 * Methode qui recalcul toutes les composantes du rectangle
	 * (points,plans,path...) d'apres le point initial
	 */
	public void calculerPositions() {

		dirSecondaire = new Vecteur(-dirPrincipal.getY(), dirPrincipal.getX());
		dirSecondaire.setModule(height);
		// trouver les points du rectangles. Les points sont en ordre anti-horaire.
		// Commencant par le haut gauche.
		points[0] = position;
		points[1] = points[0].additionne(dirPrincipal);
		points[2] = points[1].additionne(dirSecondaire);
		points[3] = points[0].additionne(dirSecondaire);
		// Avec les points ajouter des plans. Sens antiHoraire commencant avec le
		// plan du haut
		plans[0].setPosition(points[0]);
		plans[0].setPositionFinale(points[1]);
		plans[1].setPosition(points[1]);
		plans[1].setPositionFinale(points[2]);
		plans[2].setPosition(points[2]);
		plans[2].setPositionFinale(points[3]);
		plans[3].setPosition(points[3]);
		plans[3].setPositionFinale(points[0]);
		// creer le path pour dessiner la shape
		path.reset();
		path.moveTo(points[0].getX(), points[0].getY());
		path.lineTo(points[1].getX(), points[1].getY());
		path.lineTo(points[2].getX(), points[2].getY());
		path.lineTo(points[3].getX(), points[3].getY());
		path.closePath();
	}

	public boolean equals(Rectangle other) {
		if (this.position.equals(other.getPosition()) && this.dirPrincipal.equals(other.getDirPrincipal())) {
			if (this.width == other.getWidth() && this.height == other.getHeight()) {
				if(plans[0].peutPlacerPortail() == other.getPlans()[0].peutPlacerPortail()) {
					return true;
				}
				
			}

		}

		return false;
	}

	private double getHeight() {

		return height;
	}

	private double getWidth() {

		return width;
	}

	private Object getDirPrincipal() {

		return dirPrincipal;
	}

	public boolean movePointsContains(Vecteur mousePos) {
		if (scaleD.contains(mousePos.getX(), mousePos.getY()) || scaleG.contains(mousePos.getX(), mousePos.getY())
				|| scaleH.contains(mousePos.getX(), mousePos.getY())
				|| scaleB.contains(mousePos.getX(), mousePos.getY())) {
			return true;
		}
		return false;
	}

	public void setScaling(boolean b) {
		scaling = b;
	}

	public boolean isScaling() {

		return scaling;
	}

	public void scale(Vecteur position, Vecteur mousePos) {
		// si lutilisateur drag
		// posCurrent : 0 = G,1 = h, 2 = D,3 = B;
		if (scaleD.contains(mousePos.getX(), mousePos.getY()) || posCurrent == 2) {
			// projeter le vectgeur souris/pointscaleD sur dirPrincip
			this.width = position.getX() - points[0].getX();
			if (width < WIDTH_MIN) {
				width = WIDTH_MIN;
			}

			dirPrincipal.setModule(width);

			posCurrent = 2;

		} else if (scaleB.contains(mousePos.getX(), mousePos.getY()) || posCurrent == 3) {

			this.height = position.getY() - points[0].getY();
			if (height < HEIGHT_MIN) {
				height = HEIGHT_MIN;
			}
			dirSecondaire.setModule(height);
			posCurrent = 3;
		} else if (scaleG.contains(mousePos.getX(), mousePos.getY()) || posCurrent == 0) {
			this.width = points[1].getX() - position.getX();
			if(width < WIDTH_MIN) {
				width = WIDTH_MIN;
			}else {
				this.position.setX(position.getX());
			}
			dirPrincipal.setModule(width);
			
			
			posCurrent = 0;
		}else if(scaleH.contains(mousePos.getX(), mousePos.getY()) || posCurrent == 1) {
			
			this.height = points[3].getY() - position.getY();
			if (height < HEIGHT_MIN) {
				height = HEIGHT_MIN;
			}else {
				this.position.setY(position.getY());
			}
			dirSecondaire.setModule(height);
			posCurrent = 1;
		}
		calculerPositions();
		scaling = true;

	}

	public void setScalerNull() {
		posCurrent = 4;
	}

	public Plans[] getPlans() {
		return plans;
	}
	
	public void setPeutPlacer(boolean b) {
		for(int i = 0; i < plans.length; i++) {
			plans[i].setPeutPlacer(b);
		}
	}
}
