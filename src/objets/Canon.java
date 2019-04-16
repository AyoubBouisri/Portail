package objets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

import javax.imageio.ImageIO;

import geometrie.SerializableArea;
import geometrie.Vecteur;
import niveau.Niveau;

/**
 * Un objet canon permettant de lancer une balle dans le niveau.
 * 
 * @author Duy
 *
 */
public class Canon extends SuperObjets implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4730352696939169049L;
	private Niveau parent;
	private final int ANGLE_MIN = 20;
	private final int ANGLE_MAX = 160;
	private double posX, posY;
	private double posXInit;
	private double diametre = 15;
	private double largeur = 10, longueur = 10;
	private double angle = 90;
	private double rayon = 1;
	private int puissance = 20;
	private int distanceMire = 15;
	private Rectangle2D.Double rect;
	private Arc2D.Double arc;
	private Ellipse2D.Double ronde;
	private SerializableArea canon = new SerializableArea();
	private SerializableArea mire = new SerializableArea();
	private transient Image etiquetteFlecheG = null;
	private transient Image etiquetteFlecheD = null;
	double posXMire, posYMire, widthMire;
	private Color colorCreatif = new Color(255, 255, 255);
	private Color colorCreatifSel = Color.ORANGE;

	private boolean selectionne = false;
	private Vecteur angleVit;
	private Vecteur posCentreArc;
	private Vecteur posCentreMire;

	public Canon(Vecteur posInit, Niveau parent) {
		super(posInit);
		posX = posInit.getX();
		posY = posInit.getY();
		setPosInit();
		this.parent = parent;
		arc = new Arc2D.Double(posX, posY - longueur, diametre, diametre, -180, 180, 1);
		rect = new Rectangle2D.Double(posX + (diametre - largeur) / 2, posY, largeur, longueur + diametre / 2);
		ronde = new Ellipse2D.Double(posX + (diametre - rayon * 2) / 2, posY + distanceMire, rayon * 2, rayon * 2);

		posCentreArc = new Vecteur(posX + diametre / 2, posY - longueur + diametre / 2);
		posCentreMire = new Vecteur(posX + (diametre - rayon * 2) / 2 + rayon, posY + distanceMire + rayon);
		angleVit = posCentreMire.soustrait(posCentreArc);

		loadImages();
	}

	@Override
	public void dessinerModeCreatif(Graphics2D g2d, AffineTransform matMc) {

		arc = new Arc2D.Double(posX, posY - longueur, diametre, diametre, -180, 180, 1);
		rect = new Rectangle2D.Double(posX + (diametre - largeur) / 2, posY, largeur, longueur);
		ronde = new Ellipse2D.Double(posX + (diametre - rayon * 2) / 2, posY + distanceMire, rayon * 2, rayon * 2);

		AffineTransform matTemp = new AffineTransform(matMc);
		matTemp.rotate(Math.toRadians(angle - 90), rect.getX() + rect.width / 2, rect.getY());

		canon = new SerializableArea(matTemp.createTransformedShape(rect));
		canon.add(new SerializableArea(matMc.createTransformedShape(arc)));
		mire = new SerializableArea(matTemp.createTransformedShape(ronde));
		g2d.setStroke(new BasicStroke(3.0f));
		g2d.setColor(new Color(117, 169, 249));
		g2d.fill(matTemp.createTransformedShape(rect));

		if (!selectionne) {
			g2d.setColor(colorCreatif);
		} else {
			g2d.setColor(colorCreatifSel);
		}

		g2d.draw(matTemp.createTransformedShape(rect));
		g2d.setColor(new Color(117, 169, 249));
		g2d.fill(matMc.createTransformedShape(arc));
		if (!selectionne) {
			g2d.setColor(colorCreatif);
		} else {
			g2d.setColor(colorCreatifSel);
		}
		g2d.draw(matMc.createTransformedShape(arc));

	}

	@Override
	public void dessiner(Graphics2D g2d, AffineTransform matMc) {

		g2d.setStroke((new BasicStroke(4.0f)));
		arc = new Arc2D.Double(posX, posY - longueur, diametre, diametre, -180, 180, 1);
		rect = new Rectangle2D.Double(posX + (diametre - largeur) / 2, posY, largeur, longueur);
		ronde = new Ellipse2D.Double(posX + (diametre - rayon * 2) / 2, posY + distanceMire, rayon * 2, rayon * 2);

		AffineTransform matTemp = new AffineTransform(matMc);
		matTemp.rotate(Math.toRadians(angle - 90), rect.getX() + rect.width / 2, rect.getY());
		// dessiner ligne pointillee
		g2d.setColor(Color.green);
		float dash1[] = { 4.0f };
		BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
		g2d.setStroke(dashed);

		Line2D.Double lignePointillee = new Line2D.Double(posX + diametre / 2, posY, posX + (diametre) / 2,
				posY + distanceMire);
		g2d.draw(matTemp.createTransformedShape(lignePointillee));
		// fin du dessin de la ligne pointillee

		canon = new SerializableArea(matTemp.createTransformedShape(rect));
		canon.add(new SerializableArea(matMc.createTransformedShape(arc)));
		mire = new SerializableArea(matTemp.createTransformedShape(ronde));

		// Dessiner etiquette (Les variables sont en pixels)

		if (parent.getSurMire()) {
			Rectangle2D mireBounds = mire.getBounds2D();
			
			posXMire = mireBounds.getX();
			posYMire = mireBounds.getY();
			widthMire = mireBounds.getWidth();

			if (angle >= 90) {
				// DESSINER L'ETIQUETTE A GAUCHE DE LA MIRE
				dessinerEtiquette(posXMire, posYMire, widthMire, 1, -(angle - 90), g2d);
			} else {
				// DESSINER L'ETIQUETTE A DROITE DE LA MIRE
				dessinerEtiquette(posXMire, posYMire, widthMire, 0, -(angle - 90), g2d);
			}

		}

		g2d.setColor(Color.orange);

		g2d.fill(matTemp.createTransformedShape(rect));
		g2d.setColor(Color.black);
		g2d.fill(matMc.createTransformedShape(arc));

		g2d.setColor(Color.green);
		g2d.setStroke((new BasicStroke(1.0f)));
		g2d.draw(matTemp.createTransformedShape(ronde));

	}

	/**
	 * Dessiner une etiquette d'apres un objet original.
	 * 
	 * @param posOrigineX
	 *            Position x en pixels de l'objet original.
	 * @param posOrigineY
	 *            Position y en pixels de l'objet original.
	 * @param widthOrigine
	 *            La largeur de l'objet original
	 * @param dir
	 *            La directon a la quelle fera face l'etiquette 0 == etiquette vers
	 *            la gauche : <[] 1 == etiquette vers la droite : []>
	 * 
	 * @param valeur
	 *            La valeur double qui sera afficher sur l'etiquette
	 * 
	 * @param g2d
	 *            Graphics sur le quel sera dessiner l'etiquette
	 */
	public void dessinerEtiquette(double posOrigineX, double posOrigineY, double widthOrigine, int dir, double valeur,
			Graphics2D g2d) {
		
		double heightEtiquette = 25;
		double widthEtiquette = 50;
		double marge = 10;

		int widthImg = 9;
		int heightImg = 16;

		double xEtiquette;
		double yEtiquette = posOrigineY + widthOrigine / 2 - heightEtiquette / 2;

		Image fleche;
		int xFleche;
		int yFleche = (int) (posOrigineY + widthOrigine / 2 - heightImg / 2);

		if (dir == 0) {
			// <[]

			xEtiquette = posOrigineX + widthOrigine + marge;
			xFleche = (int) (xEtiquette - widthImg + 4);
			fleche = etiquetteFlecheG;

		} else {
			// []>

			xEtiquette = posOrigineX - widthEtiquette - marge;
			xFleche = (int) (xEtiquette + widthEtiquette - 4);
			fleche = etiquetteFlecheD;

		}
		RoundRectangle2D.Double etiquette = new RoundRectangle2D.Double(xEtiquette, yEtiquette, widthEtiquette,
				heightEtiquette, 5, 5);

		// dessiner fleche

		g2d.drawImage(fleche, xFleche, yFleche, widthImg, heightImg, null);
		g2d.setColor(new Color(247, 214, 84));
		g2d.fill(etiquette);

		// Dessiner String angle
		Font font = new Font("Ariel", Font.BOLD, 18);
		g2d.setColor(Color.DARK_GRAY);
		g2d.setFont(font);
		String strAngle = "" + (int) valeur + "\u00b0";
		// TROUVER LA LONGUEUR DU STRING
		double longString = g2d.getFontMetrics().stringWidth(strAngle);
		// TROUVER LA HAUTEUR DU STRING
		double hautString = g2d.getFontMetrics().getMaxAscent();

		g2d.drawString(strAngle, (int) (xEtiquette + widthEtiquette / 2 - longString / 2),
				(int) (yEtiquette + hautString));
	}

	/**
	 * Methode qui cree une balle et lui donne une vitesse initiale d'apres les
	 * variables du canon
	 * 
	 * @return Une nouvelle balle
	 */
	public Balle shootBall() {

		Vecteur pos = new Vecteur(rect.getX() + rect.width / 2, rect.getY());
		Vecteur acc = parent.getGrav();

		return new Balle(pos, angleVit.copy(), acc);
	}

	/**
	 * Methode pour ajuster la vitesse initiale de la balle
	 * 
	 * @param v
	 *            permettre de rentrer une nouvelle vitesse
	 */
	public void changerVit(int v) {
		puissance = v;
	}

	/**
	 * Methode pour ajuster l'angle de tire du canon
	 * 
	 * @param mouse
	 *            position de la souris en unites reelles
	 */
	public void changerAngle(Vecteur mouse) {
		double newAngle = 0;
		posCentreArc = new Vecteur(rect.getX() + rect.width / 2, rect.getY());

		angleVit = mouse.soustrait(posCentreArc);
		angleVit.setModule(puissance);
		newAngle = Math.toDegrees(-((Math.atan2(angleVit.getX(), angleVit.getY())) - Math.PI / 2));

		this.angle = newAngle;

		if (angle < ANGLE_MIN) {
			angle = 20;
		} else if (angle > ANGLE_MAX) {
			angle = 160;
		}

	}

	/**
	 * Methode qui donne une valeur a l'angle du canon.
	 * 
	 * @param angle
	 *            Nouvel angle
	 */
	public void setAngleInit() {
		this.angle = 90;

		angleVit = new Vecteur(0, 1, 0);
		angleVit.setModule(puissance);

	}

	/**
	 * 
	 * @return Un objet de type SerializableArea qui represente le canon.
	 */
	public SerializableArea getAireCanon() {
		return canon;
	}

	/**
	 * 
	 * @return Un objet de type SerializableArea qui represente la mire.
	 */
	public SerializableArea getAireMire() {
		return mire;
	}

	/**
	 * Selectionne le canon ou le deselectionne
	 * 
	 * @param b
	 *            Valeur de la selection
	 */
	public void setSelected(boolean b) {
		selectionne = b;
	}

	/**
	 * Methode qui prepare les images de la classe
	 */
	public void loadImages() {
		URL etiquetteFlG = null;
		URL etiquetteFlD = null;

		etiquetteFlG = getClass().getClassLoader().getResource("etiquetteIcon.png");
		etiquetteFlD = getClass().getClassLoader().getResource("etiquetteIconD.png");

		// creer image de licone de rotation
		if (etiquetteFlG != null) {
			try {
				etiquetteFlecheG = ImageIO.read(etiquetteFlG);

			} catch (IOException e) {
				System.out.println("Erreur pendant la lecture de l'image");
			}
		} else {
			System.out.println("Erreur pendant la lecture du URL");
		}

		if (etiquetteFlD != null) {
			try {
				etiquetteFlecheD = ImageIO.read(etiquetteFlD);

			} catch (IOException e) {
				System.out.println("Erreur pendant la lecture de l'image");
			}
		} else {
			System.out.println("Erreur pendant la lecture du URL");
		}
	}

	/**
	 * Methode qui bouge le canon d'apres la souris
	 * 
	 * @param x
	 *            Position x de la souris reel
	 * @param startDrag
	 *            Position ou la motion du drag a commence
	 */
	public void move(double x, Vecteur startDrag) {

		double offSet = startDrag.getX() - posXInit;
		posX = x - offSet;

		position = new Vecteur(posX, posY);

	}

	/**
	 * Methode qui rend la position courante du canon la position initiale du canon.
	 */
	public void setPosInit() {
		posXInit = posX;
		
	}

	/**
	 * retourne un canon identique a celui-ci;
	 */
	public Canon clone() {
		return new Canon(new Vecteur(posX, posY), parent);
	}
	
	public void setParent(Niveau niv) {
		this.parent = niv;
	}

	@Override
	public void setPeutPlacer(boolean b) {
		
		
	}
	
	

}
