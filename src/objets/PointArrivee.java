package objets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.Serializable;

import geometrie.SerializableArea;
import geometrie.Vecteur;

/**
 * 
 * @author Duy créer une point d'arrivée pour terminer un niveau
 */
public class PointArrivee extends SuperObjets implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double posX, posY;
	private Vecteur posInit;
	private Ellipse2D.Double rect;
	private double diam = Balle.rayon * 4;
	private boolean selected = false;

	private SerializableArea ptArrivee;

	public PointArrivee(Vecteur posInit) {
		super(posInit);
		posX = posInit.getX();
		posY = posInit.getY();
		this.posInit = new Vecteur(posX, posY);
		rect = new Ellipse2D.Double(posX, posX, diam, diam);

	}

	@Override
	public void dessinerModeCreatif(Graphics2D g2d, AffineTransform matMc) {
		g2d.setStroke(new BasicStroke(3.0f));
		rect = new Ellipse2D.Double(posX, posY, diam, diam);
		ptArrivee = new SerializableArea(matMc.createTransformedShape(rect));

		g2d.setColor(new Color(117, 169, 249));
		g2d.fill(ptArrivee);
		if (selected) {
			g2d.setColor(Color.orange);
		} else {
			g2d.setColor(Color.white);
		}
		g2d.draw(ptArrivee);
		g2d.setStroke(new BasicStroke(5.0f));
		Line2D.Double hautGauche = new Line2D.Double(posX + (diam / 3), posY + (diam / 3), posX + (2 * diam / 3),
				posY + (2 * diam / 3));
		Line2D.Double hautDroite = new Line2D.Double(posX + (diam / 3), posY + (2 * diam / 3), posX + (2 * diam / 3),
				posY + (diam / 3));
		g2d.draw(matMc.createTransformedShape(hautGauche));
		g2d.draw(matMc.createTransformedShape(hautDroite));

	}

	@Override
	public void dessiner(Graphics2D g2d, AffineTransform matMc) {
		g2d.setStroke(new BasicStroke(5.0f));
		rect = new Ellipse2D.Double(posX, posY, diam, diam);
		ptArrivee = new SerializableArea(matMc.createTransformedShape(rect));

		g2d.setColor(new Color(42, 211, 115));
		g2d.fill(ptArrivee);

		g2d.setColor(Color.WHITE);
		Line2D.Double hautGauche = new Line2D.Double(posX + (diam / 3), posY + (diam / 3), posX + (2 * diam / 3),
				posY + (2 * diam / 3));
		Line2D.Double hautDroite = new Line2D.Double(posX + (diam / 3), posY + (2 * diam / 3), posX + (2 * diam / 3),
				posY + (diam / 3));
		g2d.draw(matMc.createTransformedShape(hautGauche));
		g2d.draw(matMc.createTransformedShape(hautDroite));

	}

	/**
	 * Methode qui verifie si le point d'arrivee contient la souris
	 * 
	 * @param mousePos
	 *            La position de la souris
	 * @return Vrai si l'objet contient la position de la souris, sinon faux.
	 */
	public boolean contains(Vecteur mousePos) {
		if (getAirePtArrivee().contains(mousePos.getX(), mousePos.getY())) {
			return true;
		}
		return false;

	}

	/**
	 * 
	 * @return L'aire du point d'arrivee
	 */
	public SerializableArea getAirePtArrivee() {
		return ptArrivee;
	}

	/**
	 * @return Une copie de cet objet.
	 */
	public PointArrivee clone() {
		return new PointArrivee(new Vecteur(posX, posY));
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
	 * Methode qui bouge l'objet d'apres le mouvement de la souris
	 * 
	 * @param souris
	 *            Position courante de la souris
	 * @param startDrag
	 *            Position ou le drag de la souris a commence.
	 */
	public void move(Vecteur souris, Vecteur startDrag) {
		Vecteur offSetPos1 = startDrag.soustrait(posInit);
		position = souris.soustrait(offSetPos1);
		posX = position.getX();
		posY = position.getY();

	}

	/**
	 * Donne une valeur a la position initiale
	 * 
	 */
	public void setPositionsInit() {
		posInit = new Vecteur(posX, posY);
	}
	
	public boolean enCollisionPointsArrive(Balle balle) {
		Area intersect = new Area(ptArrivee);
		intersect.intersect(new Area(balle.getShapeBall()));
		
		return !intersect.isEmpty();
	}

	@Override
	public void setPeutPlacer(boolean b) {
		// TODO Auto-generated method stub
		
	}
	
}
