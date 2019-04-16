package objets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import geometrie.Vecteur;

/**
 * Un objet portail qui permet de teleporter la balle.
 * 
 * @author Ayoub
 *
 */
public class Portail extends SuperObjets {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private int type; // 0 - ORANGE 1 - BLEU


	private Vecteur direction;
	private Rectangle2D.Double rect;
	private Shape rectShape;
	private Rectangle2D.Double rectHider;
	private Shape hider;
	private double hautHider = Balle.rayon;
	private double offSetHider;

	private Color color;
	private int dirNormale;

	private double positionYTrans;
	private double diam;
	private double haut;
	private Plans parent;
	private double angleRotation;

	public Portail(Vecteur position, int type, Vecteur direction, int dirNormale, Plans parent) {
		super(position);
		this.type = type;
		if (type == 0) {
			color = Color.orange;

		} else {
			color = new Color(102, 179, 255);
		}
		this.direction = direction;
		this.dirNormale = dirNormale;

		this.parent = parent;
		// set la normale du portail d'apres la direction de sa normale et la normale du
		// parent

		diam = Portails.getDiam();
		haut = Portails.getHaut();

		// Pour fix le bug du nullPointer lors du placage du deuxieme portail.
		offSetHider = 0;
		if (dirNormale == 1) {
			positionYTrans = 0;
			offSetHider = -hautHider;

		} else {
			positionYTrans = -haut / 2;
		}
		angleRotation = -Math.atan2(direction.getX(), direction.getY()) - Math.PI / 2;
		rect = new Rectangle2D.Double(position.getX() - diam / 2, position.getY() + positionYTrans, diam, haut / 2);
		rectShape = rect;
		
		rectHider = new Rectangle2D.Double(position.getX() - diam / 2, position.getY() + offSetHider, diam, hautHider);
		hider = rectHider;
	}

	@Override
	public void dessinerModeCreatif(Graphics2D g2d, AffineTransform matMc) {

	}

	@Override
	public void dessiner(Graphics2D g2d, AffineTransform matMc) {
		g2d.setColor(color);

		matMc.rotate(angleRotation, position.getX(), position.getY());
		double offSetHider = 0;

		if (dirNormale == 1) {
			positionYTrans = 0;
			offSetHider = -hautHider;

		} else {

			positionYTrans = -haut / 2;
		}

		rect = new Rectangle2D.Double(position.getX() - diam / 2, position.getY() + positionYTrans, diam, haut / 2);
		rectHider = new Rectangle2D.Double(position.getX() - diam / 2, position.getY() + offSetHider, diam, hautHider);

		double hautTemp = 2 * dirNormale;
		if (dirNormale == 1) {
			positionYTrans = haut / 2;
		} else {
			positionYTrans = -haut / 2;
		}
		Line2D.Double line1 = new Line2D.Double(position.getX() - diam / 2 + 0.4, position.getY() + positionYTrans,
				position.getX() - diam / 2 - 0.1, position.getY() + hautTemp);
		Line2D.Double line2 = new Line2D.Double(position.getX() + diam / 2 - 0.4, position.getY() + positionYTrans,
				position.getX() + diam / 2 + 0.1, position.getY() + hautTemp);
		// creation des shapes
		rectShape = matMc.createTransformedShape(rect);
		hider = matMc.createTransformedShape(rectHider);
		g2d.fill(rectShape);
		

		g2d.setStroke(new BasicStroke(4.0f));
		g2d.draw(matMc.createTransformedShape(line1));
		g2d.draw(matMc.createTransformedShape(line2));

		
	

		
		matMc.rotate(-angleRotation, position.getX(), position.getY());

	}



	/**
	 * Si l'utilisateur place un portail alors qu'il existe un autre portail du meme
	 * type on update sa direction et sa position;
	 * 
	 * @param newPos
	 *            nouvelle position du portail
	 * @param newDir
	 *            nouvelle direction du portail qui depend du plan
	 */
	public void updatePosition(Vecteur newPos, Vecteur newDir, int dirNormale, Plans parent) {
		this.position = newPos;
		this.direction = newDir;
		this.dirNormale = dirNormale;
		this.parent = parent;
		angleRotation = -Math.atan2(direction.getX(), direction.getY()) - Math.PI / 2;

	}

	/**
	 * 
	 * @return La Shape de l'objet
	 */
	public Shape getShape() {

		return rectShape;
	}

	/**
	 * 
	 * @return Shape du hider qui se trouve en dessous du portail.
	 */
	public Shape getHider() {
		return hider;
	}

	public Rectangle2D.Double getRect() {

		return rect;
	}

	/**
	 * 
	 * @return La direction de la normae (1 ou -1)
	 */
	public int getDirNormale() {

		return dirNormale;
	}

	/**
	 * 
	 * @return le plan parent du portail, le plan sur lequel le plan est situe.
	 */
	public Plans getBigParent() {

		return parent;
	}

	/**
	 * 
	 * @return le type du portail ( 0 ou 1)
	 */
	public int getType() {
		return type;
	}

	/**
	 * Verifie si un portail est egale a celui ci.
	 * 
	 * @param other
	 *            L'autre portail.
	 * @return Vrai si c'est le meme portail (DU meme type, car il ne peut n'y avoir
	 *         que deux portails en meme temps) false sinon.
	 * 
	 */
	public boolean equals(Portail other) {
		if (this.type == other.getType()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @return La normale du portail
	 */
	public Vecteur getNormale() {

		return parent.getNormale().multiplie(dirNormale);
	}

	public boolean peutPlacerPortail() {
		return parent.peutPlacerPortail();
	}

	@Override
	public void setPeutPlacer(boolean b) {
		// TODO Auto-generated method stub
		
	}

}
