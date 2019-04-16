package objets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import java.awt.Image;
import javax.imageio.ImageIO;



import java.io.IOException;
import java.io.Serializable;
import java.net.URL;


import geometrie.Vecteur;
import modeleaffichage.ModeleAffichage;
import niveau.Niveau;

/**
 * Portails permet de memoriser deux portails de differents types et gerer les
 * regles de placement entre les deux.
 * 
 * @author Ayoub
 *
 */
public class Portails extends SuperObjets implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static double placeDist = 2; // m
	private final static double diam = Balle.rayon * 4;
	private final static double haut = 1.5;

	private Vecteur mousePx = new Vecteur(0, 0);
	private boolean canPlace = false;
	private boolean inComposant = false;
	private Portail pOrange = null;
	private Portail pBleu = null;
	
	private Vecteur ptPlacementTemp;
	private Plans bigParent = null;
	
	private Shape preview;

	private transient Image iconRotation = null;
	private transient Image iconDrag = null;
	private transient Image iconMove = null;
	private transient Image iconScale = null;

	private ModeleAffichage modele;
	private Niveau nivParent;
	// facon de dessiner le crosshair icon

	private int typeCrosshair = 0;
	// si le typeCrosshair est egal a 0 dessiner normalement
	// si 1 dessiner rotation
	// si 2 dessiner vitesseInit
	private int typeCrosshairCreatif = 0;

	public Portails(Vecteur posInit, ModeleAffichage modele, Niveau nivParent) {
		super(posInit);
		this.modele = modele;
		this.nivParent = nivParent;

		loadImages();

	}

	@Override
	public void dessiner(Graphics2D g2d, AffineTransform matMc) {

		if (inComposant) {

			if (canPlace) {

				Color c = new Color(255, 255, 255, 75);
				g2d.setColor(c);
				// Creer le rectangle preview
				double angleRotation = -Math.atan2(bigParent.getDirection().getX(), bigParent.getDirection().getY())
						- Math.PI / 2;
				matMc.rotate(angleRotation, ptPlacementTemp.getX(), ptPlacementTemp.getY());
				preview = new Rectangle2D.Double(ptPlacementTemp.getX() - diam / 2, ptPlacementTemp.getY() - haut / 2,
						diam, haut);
				preview = matMc.createTransformedShape(preview);
				g2d.fill(preview);
				matMc.rotate(-angleRotation, ptPlacementTemp.getX(), ptPlacementTemp.getY());

			}
		}

		// Dessiner les portails
		if (pOrange != null) {
			pOrange.dessiner(g2d, matMc);

		}
		if (pBleu != null) {
			pBleu.dessiner(g2d, matMc);
		}

		if (inComposant) {
			if (typeCrosshair == 0) {
				dessinerCurseurNormale(g2d, matMc);
			} else if (typeCrosshair == 1) {
				dessinerCurseurRotation(g2d);
			} else if (typeCrosshair == 2) {
				dessinerCurseurFleche(g2d);
			}

		}

	}

	/**
	 * Dessine le curseur de base. Arc orange a gauche,arc bleu a droite et un point
	 * blanc au centre.
	 * 
	 * @param g2d
	 *            La composante graphique
	 * @param matMc
	 *            La matrice de transformation
	 */
	public void dessinerCurseurNormale(Graphics2D g2d, AffineTransform matMc) {
		// centre du curseur

		g2d.setColor(Color.white);
		double diamCentre = 0.5;
		g2d.setStroke(new BasicStroke(1.0f));
		if (mouseOnPortal(false)) {
			// Si la souris est sur un portail afficher un x pour supprimer le portail
			diamCentre *= 2;
			g2d.setColor(Color.RED);
			Line2D.Double hautGauche = new Line2D.Double(position.getX() - diamCentre / 2,
					position.getY() - diamCentre / 2, position.getX() + diamCentre / 2,
					position.getY() + diamCentre / 2);
			Line2D.Double hautDroite = new Line2D.Double(position.getX() + diamCentre / 2,
					position.getY() - diamCentre / 2, position.getX() - diamCentre / 2,
					position.getY() + diamCentre / 2);
			g2d.draw(matMc.createTransformedShape(hautGauche));
			g2d.draw(matMc.createTransformedShape(hautDroite));

		} else {
			Ellipse2D.Double centre = new Ellipse2D.Double(position.getX() - diamCentre / 2,
					position.getY() - diamCentre / 2, diamCentre, diamCentre);
			g2d.fill(matMc.createTransformedShape(centre));
		}

		// le contour du curseur
		g2d.setStroke(new BasicStroke(3.0f));
		// DESSINER LES ARCS
		int arcStart = 120;
		int arcLength = 125;

		Arc2D.Double arcGauche = new Arc2D.Double(position.getX() - placeDist, position.getY() - placeDist,
				placeDist * 2, placeDist * 2, arcStart, arcLength, Arc2D.OPEN);
		arcStart = -65;

		Arc2D.Double arcDroit = new Arc2D.Double(position.getX() - placeDist, position.getY() - placeDist,
				placeDist * 2, placeDist * 2, arcStart, arcLength, Arc2D.OPEN);
		if (pOrange == null) {
			g2d.setColor(new Color(255, 165, 0, 75));

		} else {
			g2d.setColor(Color.orange);
		}

		g2d.draw(matMc.createTransformedShape(arcGauche));

		if (pBleu == null) {
			g2d.setColor(new Color(102, 179, 255, 75));
		} else {
			g2d.setColor(new Color(102, 179, 255));
		}

		g2d.draw(matMc.createTransformedShape(arcDroit));
	}

	/**
	 * Dessine de rotation. Lorsque l'utilisateur peut tourner le canon dessiner une
	 * icone de rotation.
	 * 
	 * @param g2d
	 *            La composante graphique
	 * @param matMc
	 *            La matrice de transformation
	 */
	public void dessinerCurseurRotation(Graphics2D g2d) {

		// ----------------------ici dessiner le crosshair
		int diamCurseur = 15; // en pixels
		int xImg = (int) (mousePx.getX() - diamCurseur / 2);
		int yImg = (int) (mousePx.getY() - diamCurseur / 2);
		g2d.drawImage(iconRotation, xImg, yImg, diamCurseur, diamCurseur, null);

	}

	/**
	 * Dessine le curseur fleche. Lorsque l'utilisateur se trouve sur le canon
	 * dessiner une fleche comme curseur.
	 * 
	 * @param g2d
	 *            La composante graphique
	 * @param matMc
	 *            La matrice de transformation
	 */
	public void dessinerCurseurFleche(Graphics2D g2d) {

		// ----------------------ici dessiner le crosshair
		int diamCurseur = 15; // en pixels
		int xImg = (int) (mousePx.getX());
		int yImg = (int) (mousePx.getY());
		g2d.drawImage(iconDrag, xImg, yImg, diamCurseur, diamCurseur, null);

	}

	/**
	 * Dessiner le curseur avec quatres fleches qui signifie que le composant peut
	 * etre bouge.
	 * 
	 * @param g2d
	 *            La composante graphique sur lequel sera dessiner l'icone
	 */
	public void dessinerCruseurMove(Graphics2D g2d) {
		// ----------------------ici dessiner le crosshair
		int diamCurseur = 18; // en pixels
		int xImg = (int) (mousePx.getX() - diamCurseur / 2);
		int yImg = (int) (mousePx.getY() - diamCurseur / 2);
		g2d.drawImage(iconMove, xImg, yImg, diamCurseur, diamCurseur, null);
	}
	/**
	 * Dessiner le curseur avec deux fleches qui signifie que le composant peut
	 * etre modifie.
	 * 
	 * @param g2d
	 *            La composante graphique sur lequel sera dessiner l'icone
	 */
	public void dessinerCurseurScale(Graphics2D g2d) {
		// ----------------------ici dessiner le crosshair
		int diamCurseur = 18; // en pixels
		int xImg = (int) (mousePx.getX() - diamCurseur / 2);
		int yImg = (int) (mousePx.getY() - diamCurseur / 2);
		g2d.drawImage(iconScale, xImg, yImg, diamCurseur, diamCurseur, null);
	}
	@Override
	public void dessinerModeCreatif(Graphics2D g2d, AffineTransform matMC) {
		if (inComposant) {
			if (typeCrosshairCreatif == 0) {
				dessinerCurseurFleche(g2d);
			} else if (typeCrosshairCreatif == 1) {
				dessinerCruseurMove(g2d);
			} else if (typeCrosshairCreatif == 2) {
				dessinerCurseurScale(g2d);
			}

		}
	}

	/**
	 * Methode qui teleporte la balle d'un vecteur a l'autre.
	 * 
	 * @param balle
	 *            Balle qui sera teleporter si elle touche le portail.
	 * @return
	 */
	public void teleport(Balle balle) {

		Portail to = null;
		Portail from = null;
		Vecteur posPx = modele.getInPixel(balle.getPosition().getX(), balle.getPosition().getY());
		if (pOrange != null && pBleu != null) {

			if (pOrange.getShape().contains(posPx.getX(), posPx.getY())) {

				from = pOrange;
				to = pBleu;

			} else if (pBleu.getShape().contains(posPx.getX(), posPx.getY())) {

				from = pBleu;
				to = pOrange;

			}
		}

		// si les deux portails existent et la balle se trouve dans l'un des portails.
		// Teleporter la balle vers le portail "to" et ajuster sa vitesse et sa
		// position.

		if (from != null) {

			// trouver la normale du portail "to"

			Vecteur toNormale = to.getNormale();
			Vecteur fromNormale = from.getNormale();

			Vecteur nouvVit;
			Vecteur nouvPosition = null;
			Vecteur ballPort = balle.getPosition().soustrait(from.getPosition());
			ballPort.setModule((int) ballPort.module());

			if (toNormale.equals(fromNormale.multiplie(-1))) {
				nouvVit = balle.getVitesse();
				// trouver nouv Position

				nouvPosition = to.getPosition().additionne(ballPort);
			} else if (toNormale.equals(fromNormale)) {
				nouvVit = balle.getVitesse().multiplie(-1);

				// trouver nouv Position

				nouvPosition = to.getPosition().additionne(ballPort);

			} else {
				double moduleNouvVit = balle.getVitesse().module();
				nouvVit = toNormale.multiplie(moduleNouvVit);

				// trouver nouvPosition
				double moduleDist = ballPort.module();
				Vecteur projection = Vecteur.projection(ballPort, to.getBigParent().getDirection());
				projection.setModule(moduleDist);
				nouvPosition = to.getPosition().additionne(projection);

			}

			// set la nouvelle vitesse et le nouvel angle.
			nivParent.teleporting = true;

			// trouver la nouvelle position

			Vecteur n = toNormale.copy();
			n.setModule(balle.getRayon());
			balle.setPosition(nouvPosition.additionne(n));

			balle.setVitesse(nouvVit);

		}

	}

	/**
	 * Methode qui coupe la balle si elle se trouve en partie dans le portail
	 * 
	 * @param balle
	 *            La balle qui va etre coupee
	 */
	public void cutBall(Balle balle) {
		Area balleShape = new Area(balle.getShapeBall());
		Area hiderOr = new Area(pOrange.getHider());
		Area hiderBl = new Area(pBleu.getHider());

		// Trouver si la balle est dans un des deux portails

		Area nouvAreaBalle = inPortail(balle, hiderOr, hiderBl, balleShape);

		// set la nouvelle shape trouvee
		if (nouvAreaBalle != null) {
			balle.setCutBallShape(nouvAreaBalle);
		} else {
			balle.setCutBallShape(null);
		}

	}

	/**
	 * Methode qui retourne le portail dans le quel la balle se trouve
	 * 
	 * @param bl
	 *            La balle courante
	 * @return Un des deux portails qui contient la balle. Si aucun des deux
	 *         portails contiennent la balle retourne null.
	 */
	public Area inPortail(Balle bl, Area hiderOr, Area hiderBl, Area blArea) {

		Vecteur distBallPort;
		Vecteur proj;
		Area resultat;
		Area intersect = (Area) hiderOr.clone();
		intersect.intersect(blArea);

		if (!intersect.isEmpty()) {
			// la balle touche le portail orange
			distBallPort = bl.getPosition().soustrait(pOrange.getPosition());
			proj = Vecteur.projection(distBallPort, pOrange.getNormale());
			proj = proj.normalise();

			if (proj.equals(pOrange.getNormale())) {
				// la balle est dans le portail orange et devrait etre coupee

				resultat = (Area) blArea.clone();
				resultat.subtract(hiderOr);

				return resultat;
			}
		}

		intersect = (Area) hiderBl.clone();
		intersect.intersect(blArea);
		if (!intersect.isEmpty()) {
			// la balle touche le portail orange
			distBallPort = bl.getPosition().soustrait(pBleu.getPosition());
			proj = Vecteur.projection(distBallPort, pBleu.getNormale());
			proj = proj.normalise();

			if (proj.equals(pBleu.getNormale())) {
				// la balle est dans le portail orange et devrait etre coupee

				resultat = (Area) blArea.clone();
				resultat.subtract(hiderBl);

				return resultat;
			}
		}
		return null;

	}

	/**
	 * update la position de la souris
	 * 
	 * @param x
	 *            Position x en metres
	 * @param y
	 *            Position y en metres
	 * @param xPx
	 *            Position x en pixels
	 * @param yPx
	 *            Position y en pixels
	 */
	public void updateSouris(double x, double y, double xPx, double yPx) {
		position.setX(x);
		position.setY(y);

		mousePx.setX(xPx);
		mousePx.setY(yPx);

	}

	/**
	 * set la valeur de la souris par rapport au composant(vrai si dans le
	 * composant)
	 * 
	 * @param in
	 *            La valeur de mouseIn
	 */
	public void setCanPlace(boolean bool) {
		canPlace = bool;
	}

	/**
	 * set la valeur boolean de sa position par rapport au composant graphique
	 * 
	 * @param b
	 */
	public void setInComposant(boolean b) {
		inComposant = b;

	}

	/**
	 * return la distance a la quelle le programme detecte le placage de portails
	 * 
	 * @return la distance sur la quelle on peut placer un portail
	 */
	public static double getPlaceDist() {

		return placeDist;
	}

	/**
	 * 
	 * @return la position du curseur;
	 */
	public Vecteur getPosition() {

		return position;
	}

	/**
	 * set la valeur de canPlace
	 * 
	 * @param b
	 */
	public void setPlacable(boolean b) {
		canPlace = b;
	}

	/**
	 * set un plan possible
	 * 
	 * @param pl
	 *            le plan parent possible
	 */
	public void setBigParent(Plans pl) {
		bigParent = pl;
	}

	/**
	 * 
	 * @return le diametre des portails en monde reelle
	 */
	public static double getDiam() {

		return diam;
	}

	/**
	 * Set la position possible pour la position du portail qui sera place;
	 * 
	 * @param ptPlan
	 *            point dans la scene ou sera place le portail
	 */
	public void setPossiblePosition(Vecteur ptPlan) {
		ptPlacementTemp = ptPlan;

	}

	/**
	 * Place un portail orange si possible apres un click. place un portail bleu si
	 * possible apres un click. Si il existe un portail de la couleur desiree, le
	 * click supprime le current portail bleu et le remplace par le nouveau portail.
	 * Si le click est situe sur un portail deja place, ce portail sera supprime.
	 */
	public void placePortailOrange() {
		if (canPlace ) {
			if (mouseOnPortal(true)) {
				// lutilisateur a clicker sur le portail, le portail a ete enleve dans
				// clickedOnPortal
			} else {
				int normalePortail; // 1 ou -1 dependant de la normale du plan

				if (position.soustrait(ptPlacementTemp).normalise().equals(bigParent.getNormale().normalise())) {
					normalePortail = 1;

				} else if(position.soustrait(ptPlacementTemp).normalise().equals(bigParent.getNormale().normalise().multiplie(-1))){
					normalePortail = -1;
				}else {
					return;
				}

				if (pBleu != null) {
					Area prev = new Area(preview);
					Area orRect = new Area(pBleu.getShape());
					prev.intersect(orRect);
					if (!prev.isEmpty() && pBleu.getDirNormale() == normalePortail) {

						// si le portail intersect un portail d'un autre type enlever l'autre portail
						pBleu = null;

					}
				}

				if (pOrange == null) {
					// il n'existe pas de portail orange, placé le nouveau
					pOrange = new Portail(ptPlacementTemp, 0, bigParent.getDirection(), normalePortail, bigParent);

				} else {
					// le portail orange existe , remplace ce portail par un nouveau

					// supprimer et placer nouveau

					pOrange.updatePosition(ptPlacementTemp, bigParent.getDirection(), normalePortail, bigParent);

				}

			}
		}

	}

	/**
	 * place un portail bleu si possible apres un click. Si il existe un portail
	 * bleu, le click supprime le current portail bleu et le remplace par le nouveau
	 * portail. Si le click est situe sur un portail deja place, ce portail sera
	 * supprime.
	 */
	public void placePortailBleu() {
		if (canPlace) {
			if (mouseOnPortal(true)) {
				// lutilisateur a clicker sur le portail, le portail a ete enleve dans
				// clickedOnPortal
			} else {
				int normalePortail; // 1 ou -1 dependant de la normale du plan
				if (position.soustrait(ptPlacementTemp).normalise().equals(bigParent.getNormale().normalise())) {
					normalePortail = 1;
				} else if(position.soustrait(ptPlacementTemp).normalise().equals(bigParent.getNormale().normalise().multiplie(-1))){
					normalePortail = -1;
				}else {
					return;
				}
				if (pOrange != null) {

					Area prev = new Area(preview);
					Area orRect = new Area(pOrange.getShape());
					prev.intersect(orRect);
					if (!prev.isEmpty() && pOrange.getDirNormale() == normalePortail) {
						// si le portail intersect un portail d'un autre type enlever l'autre portail

						pOrange = null;

					}
				}

				if (pBleu == null) {
					// il n'existe pas de portail orange, placé le nouveau
					pBleu = new Portail(ptPlacementTemp, 1, bigParent.getDirection(), normalePortail, bigParent);

				} else {

					// supprimer et placFer nouveau

					pBleu.updatePosition(ptPlacementTemp, bigParent.getDirection(), normalePortail, bigParent);

				}

			}
		}
	}

	/**
	 * Verifier si la souris est dans un portail si la boolean entree est vrai
	 * supprimer le portail existent
	 * 
	 * @remove si Vrai supprimer le portail dans lequel la souris est.
	 * @return vrai si la souris est dans la shape d'un portail
	 */
	private boolean mouseOnPortal(boolean remove) {

		if (pOrange != null) {
			if (pOrange.getShape().contains(mousePx.getX(), mousePx.getY())) {
				if (remove) {
					pOrange = null;
				}

				return true;
			}
		}
		if (pBleu != null) {
			if (pBleu.getShape().contains(mousePx.getX(), mousePx.getY())) {
				if (remove) {
					pBleu = null;
				}

				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @return la hauteur des portails
	 */
	public static double getHaut() {

		return haut;
	}

	/**
	 * 
	 * @return le portail orange
	 */
	public Portail getPOrange() {
		return pOrange;
	}

	/**
	 * 
	 * @return le portail bleu.
	 */
	public Portail getPBleu() {
		return pBleu;
	}

	/**
	 * 
	 * 
	 * @param type
	 *            valeur int qui permet de changer de type de crosshair
	 */
	public void setTypeCrosshair(int type) {
		typeCrosshair = type;
	}

	/**
	 * Methode qui set la facon de dessiner le curseur de la souris dans le
	 * composant. Si le type == 0 Le curseur aura la forme d'une fleche Si le type
	 * == 1 Le curseur aura la forme de 4 fleches signifiant que le composant peut
	 * etre deplace Si le type == 2 le curseur aura la forme de 2 fleches signifiant
	 * que le crosshair peut etre agrandit.
	 * 
	 * @param type
	 *            le nouveau type du crosshair
	 */
	public void setTypeCrosshairCreatif(int type) {
		typeCrosshairCreatif = type;

	}

	/**
	 * Prepare les images des curseurs possibles
	 */
	public void loadImages() {
		URL urlRotation = null;
		URL urlDrag = null;
		URL urlMove = null;
		URL urlScale = null;

		urlRotation = getClass().getClassLoader().getResource("rotationIcon.png");
		urlMove = getClass().getClassLoader().getResource("moveIcon.png");
		urlDrag = getClass().getClassLoader().getResource("dragIcon.png");
		urlScale = getClass().getClassLoader().getResource("scaleIcon.png");
		// creer image de licone de rotation
		if (urlRotation != null) {
			try {
				iconRotation = ImageIO.read(urlRotation);

			} catch (IOException e) {
				System.out.println("Erreur pendant la lecture de l'image");
			}
		} else {
			System.out.println("Erreur pendant la lecture du URL");
		}
		// creer l'image de licone du drag
		if (urlDrag != null) {
			try {
				iconDrag = ImageIO.read(urlDrag);
			} catch (IOException e) {
				System.out.println("Erreur pendant la lecture de l'image");
			}
		} else {
			System.out.println("Erreur pendant la lecture du URL");
		}
		// creer l'image de licone du move
		if (urlMove != null) {
			try {
				iconMove = ImageIO.read(urlMove);
			} catch (IOException e) {
				System.out.println("Erreur pendant la lecture de l'image");
			}
		} else {
			System.out.println("Erreur pendant la lecture du URL");
		}
		// creer l'image de licone du scale
		if (urlScale != null) {
			try {
				iconScale = ImageIO.read(urlScale);
			} catch (IOException e) {
				System.out.println("Erreur pendant la lecture de l'image");
			}
		} else {
			System.out.println("Erreur pendant la lecture du URL");
		}
	}

	/**
	 * Methode qui supprime les portails existant
	 */
	public void delete() {
		pOrange = null;
		pBleu = null;

	}

	@Override
	public void setPeutPlacer(boolean b) {
		// TODO Auto-generated method stub
		
	}
}
