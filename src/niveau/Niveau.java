package niveau;

import java.awt.Graphics2D;

import java.awt.geom.AffineTransform;

import java.io.Serializable;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import composants.ComposantAnimation;
import composants.Jeu;
import geometrie.Collisions;
import geometrie.Vecteur;
import interfaces.Dessinable;

import modeleaffichage.ModeleAffichage;
import objets.Balle;
import objets.Canon;
import objets.Plans;
import objets.PointArrivee;
import objets.Portails;

import objets.Rectangle;
import objets.SuperObjets;
import physique.Physique;

/**
 * Une classe niveau qui regroupe tout les objets possible d'un niveau dans des
 * arrayLists. Cette classe permet aussi de verifier les collisions entre les
 * differents objets du niveau en plus de gerer les portails.
 * 
 * @author Ayoub et Duy
 *
 */
public class Niveau implements Dessinable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vecteur grav;
	private Vecteur mousePos = new Vecteur(0, 0);
	// ARRAY D'OBJETS
	private ArrayList<Balle> balles = new ArrayList<Balle>();
	private ArrayList<Balle> ballesRemoved = new ArrayList<Balle>();
	private int nbMaxBalles = 1;
	private double coeffRest;
	private double coeffFric;
	private ArrayList<Plans> plans = new ArrayList<Plans>();
	private ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();

	private transient Portails portails;

	private Canon canon = null;
	public boolean teleporting = false;
	private boolean isSurMire = false;
	private boolean moving = false;
	private boolean gagne = false;
	private ModeleAffichage modele;
	private transient ComposantAnimation parent;
	private Plans planSelec;
	private Rectangle rectSelec;
	private String compSelec = "";
	private int nbTire;

	private PointArrivee ptArrivee = null;
	private Vecteur startDrag = null;
	private Balle currentBalle;

	// Nom et auteur pour une fonctionnalite future pas encore presente
	private String nomNiveau = "Joe Niveau";
	private String authorName = "Sloth McLazy";
	private String dateCreation = "29 février 2018";

	// Ayoub
	/**
	 * Constructeur du niveau qui set la gravite
	 * 
	 * @param grav
	 *            L'acceleration causee par la force de gravite
	 * @param modele
	 *            Modele d'affichage du composant paren
	 * @param parent
	 *            Composant d'animation ou existe le niveau
	 */
	public Niveau(Vecteur grav, ModeleAffichage modele, ComposantAnimation parent) {

		this.grav = grav;
		this.modele = modele;
		this.parent = parent;
		coeffRest = 0.6;
		coeffFric = 0.05;
		portails = new Portails(new Vecteur(1000, 1000, 0), modele, this);
		canon = new Canon(new Vecteur(50, 0), this);
		ptArrivee = new PointArrivee(new Vecteur(modele.getLargUnitesReelles() / 2, modele.getHautUnitesReelles() / 2));

		creerPlansCote();

	}

	// Ayoub
	/**
	 * Constructeur permettant de creer un niveau comme copie d'un autre.
	 * 
	 * @param niv
	 *            Niveau a copier.
	 */
	public Niveau(Niveau niv) {

		this.grav = niv.getGrav();
		this.parent = niv.getParent();
		this.modele = niv.getModele();

		this.plans = new ArrayList<Plans>();
		// copier chaque plan
		for (Plans p : niv.getPlans()) {
			plans.add(p.clone());
		}
		// copier chaque rectangle
		for (Rectangle r : niv.getRectangles()) {
			rectangles.add(r.clone());
		}

		if (niv.getCanon() != null) {
			this.canon = niv.getCanon().clone();
			this.canon.setParent(this);
		}
		if (niv.getPtArrivee() != null) {
			this.ptArrivee = niv.getPtArrivee().clone();
		}
		this.nbMaxBalles = niv.getNbMaxBalles();
		this.coeffFric = niv.getCoeffFric();
		this.coeffRest = niv.getCoeffRest();

		portails = new Portails(new Vecteur(1000, 1000, 0), modele, this);

	}

	// Ayoub
	/**
	 * 
	 * @return La liste des rectangles.
	 */
	private ArrayList<Rectangle> getRectangles() {

		return rectangles;
	}

	@Override
	// Ayoub
	/**
	 * methode qui dessine chaque objet du niveau
	 */
	public void dessiner(Graphics2D g2d, AffineTransform matMc) {
		// Dessiner les plans
		for (int i = 0; i < plans.size(); i++) {

			plans.get(i).dessiner(g2d, matMc);

		}
		// dessiner les rectangles

		for (int i = 0; i < rectangles.size(); i++) {
			rectangles.get(i).dessiner(g2d, matMc);
		}
		// dessiner les balles separement pour couper les balles dans les portails
		for (int i = 0; i < balles.size(); i++) {

			balles.get(i).dessiner(g2d, matMc);

		}

		// animer les balles supprimees
		for (int i = 0; i < ballesRemoved.size(); i++) {
			if (ballesRemoved.get(i).isDead()) {
				ballesRemoved.remove(i);
			} else {
				ballesRemoved.get(i).dessiner(g2d, matMc);
			}

		}
		if (canon != null) {
			canon.dessiner(g2d, matMc);
		}

		if (ptArrivee != null) {
			ptArrivee.dessiner(g2d, matMc);
		}
		portails.dessiner(g2d, matMc);

	}

	// Ayoub
	/**
	 * Methode qui change la position du curseur qui represente l'utilisateur et
	 * verifie si il se trouve sur un objet interactif.
	 */
	public void updateUtilisateur() {
		checkCurseurEtat();
		checkCollisionCurseur();

	}

	// Ayoub
	/**
	 * Update la position des objets (un pas euler)
	 * 
	 * @param deltaT
	 *            Temps sur lequel s'effectue le calcul
	 * @param modele
	 * 
	 */
	public void update(double deltaT) {

		checkBallesPointsArrive();
		checkBallesTeleportation();

		checkCollisionBalles();
		checkCollisionsPlans(deltaT);

		teleporting = false;

		for (int i = 0; i < balles.size(); i++) {
			balles.get(i).update(deltaT);

			if (portails.getPBleu() != null && portails.getPOrange() != null) {
				portails.cutBall(balles.get(i));
			} else {
				balles.get(i).setCutBallShape(null);
			}

		}
	}

	// Duy
	private void checkBallesPointsArrive() {
		for (int i = 0; i < balles.size(); i++) {
			if (!gagne && ptArrivee.enCollisionPointsArrive(balles.get(i))) {
				gagne = true;
				gagner();

			}
		}

	}

	// Ayoub
	/**
	 * Voir ou le curseur se trouve et lui assigne une facon de dessiner.
	 */
	private void checkCurseurEtat() {

		if (parent.getParent().isCreatif()) {

			if (compSelec.equals("canon")) {

				if (canon.getAireCanon().contains(mousePos.getX(), mousePos.getY())) {
					portails.setTypeCrosshairCreatif(1);
				} else {
					portails.setTypeCrosshairCreatif(0);
				}

			} else if (compSelec.equals("ptArrivee")) {
				if (ptArrivee.contains(mousePos)) {
					portails.setTypeCrosshairCreatif(1);
				} else {
					portails.setTypeCrosshairCreatif(0);
				}
			} else if (compSelec.equals("pl")) {
				if (planSelec.contains(mousePos.getX(), mousePos.getY())) {
					if (planSelec.movePointsContains(mousePos.getX(), mousePos.getY())) {
						portails.setTypeCrosshairCreatif(2);
					} else {
						portails.setTypeCrosshairCreatif(1);
					}
				} else {
					portails.setTypeCrosshairCreatif(0);
				}
			} else if (compSelec.equals("rect")) {

				if (rectSelec.movePointsContains(mousePos)) {
					portails.setTypeCrosshairCreatif(2);
				} else if (rectSelec.contains(mousePos)) {
					portails.setTypeCrosshairCreatif(1);

				} else {
					portails.setTypeCrosshairCreatif(0);
				}
			} else {
				portails.setTypeCrosshairCreatif(0);
			}

			if (moving) {
				portails.setTypeCrosshairCreatif(1);
			}
		} else {
			if (isSurMire || canon.getAireMire().contains(mousePos.getX(), mousePos.getY())) {
				portails.setTypeCrosshair(1);

			} else if (canon.getAireCanon().contains(mousePos.getX(), mousePos.getY())) {

				portails.setTypeCrosshair(2);
			} else {
				portails.setTypeCrosshair(0);
			}
		}

	}

	// Ayoub
	/**
	 * Verifier pour chaque balle si elle est dans portail et si elle doit etre
	 * teleportee.
	 */
	public void checkBallesTeleportation() {
		ArrayList<Balle> bTemp = new ArrayList<Balle>();
		bTemp.addAll(balles);
		for (Balle b : bTemp) {
			portails.teleport(b);
		}

	}

	// Ayoub
	/**
	 * Verifier les collisions entre les balles et le reste des balles dans le
	 * niveau
	 * 
	 */
	public void checkCollisionBalles() {

		for (int i = 0; i < balles.size() - 1; i++) {
			for (int j = i + 1; j < balles.size(); j++) {
				if (Collisions.enCollision(balles.get(i), balles.get(j))) {

					double impulsion = Physique.calculerImpulsion(balles.get(i), balles.get(j), coeffRest);
					Physique.calculerVit(impulsion, balles.get(i));
					Physique.calculerVit(-1 * impulsion, balles.get(j));
				}

			}
		}

	}

	// Ayoub
	/**
	 * Verifier les collisions entre les balles et le reste des objets dans le
	 * niveau
	 * 
	 */
	public void checkCollisionsPlans(double deltaT) {
		ArrayList<Plans> plansTot = new ArrayList<Plans>();
		plansTot.addAll(plans);
		for (Rectangle r : rectangles) {
			for (int i = 0; i < 4; i++) {
				plansTot.add(r.getPlans()[i]);
			}
		}

		for (int i = 0; i < balles.size(); i++) {
			boolean touchePlan = false;
			for (int j = 0; j < plansTot.size(); j++) {
				Vecteur ptCollision = Collisions.enCollision(balles.get(i), plansTot.get(j), portails);
				if (ptCollision != null && !teleporting) {

					touchePlan = true;

					calculefriction(balles.get(i), plansTot.get(j), ptCollision, deltaT);
					double impulsion = Physique.calculerImpulsion(balles.get(i), coeffRest);
					Physique.calculerVit(impulsion, balles.get(i));

				}
			}
			if (!touchePlan) {
				balles.get(i).setFriction(null);
				balles.get(i).setPtCollision(null);
			}
		}
	}

	// Duy
	/**
	 * Méthode qui calcule le vecteur accélération de la friction (Auteur Duy)
	 * 
	 * @param balle
	 *            la balle qui rentre en collision avec le plan
	 * @param plan
	 *            le plan en collision
	 * @param ptCollision
	 *            Le point ou la collision s'effectue
	 *
	 */

	private void calculefriction(Balle balle, Plans plan, Vecteur ptCollision, double deltaT) {

		Vecteur vitBalle = balle.getVitesse();
		Vecteur directionPlan = plan.getDirection();
		Vecteur friction = Vecteur.projection(vitBalle, directionPlan).multiplie(-1).normalise();

		// set la direction du vecteur de la friction
		balle.setFriction(friction);
		balle.setPtCollision(ptCollision);
		// calculer l'angle de la friction

		double angle = (Math.atan2(plan.getDirection().getX(), plan.getDirection().getY()) - Math.PI / 2);

		double forceFriction = Physique.calculefriction(grav.getY(), balle.getMasse(), angle, coeffFric);
		double accFriction = forceFriction / balle.getMasse();

		// assigner une nouvelle vitesse à la balle avec une friction
		Vecteur accF = friction.copy();
		accF.setModule(accFriction);
		Vecteur nouvelleVitesse = vitBalle.additionne(accF.multiplie(deltaT));
		balle.setVitesse(nouvelleVitesse);
	}

	// Ayoub
	/**
	 * Methode qui detecte la collision entre le curseur et un plan et qui cree un
	 * Preview du portail qui sera place
	 */
	public void checkCollisionCurseur() {
		double distance = Portails.getPlaceDist();

		// joindre tout les plans (Plans murs,plans rect);
		ArrayList<Plans> plansTot = new ArrayList<Plans>();
		plansTot.addAll(plans);
		ArrayList<Plans> plansRect = new ArrayList<Plans>();

		for (Rectangle r : rectangles) {
			for (int i = 0; i < 4; i++) {
				plansRect.add(r.getPlans()[i]);
			}
		}
		plansTot.addAll(plansRect);
		Plans plCollision = null;
		Vecteur ptPlan = null;

		for (Plans pl : plansTot) {
			// En collisions
			double[] resul = Collisions.enCollision(portails.getPosition(), pl);
			if (resul.length != 0) {
				if (resul[0] <= distance) {

					ptPlan = new Vecteur(resul[1], resul[2]);

					if (!inRect(mousePos.getX(), mousePos.getY())) {
						plCollision = pl;

					}
				}
			}
		}

		boolean surCanon = canon.getAireCanon().contains(mousePos.getX(), mousePos.getY());
		boolean surMire = canon.getAireMire().contains(mousePos.getX(), mousePos.getY());
		if (plCollision != null && !isSurMire && !surCanon && !surMire && !parent.getParent().isCreatif()
				&& plCollision.peutPlacerPortail()) {

			portails.setPlacable(true);
			portails.setBigParent(plCollision);
			portails.setPossiblePosition(ptPlan);
		} else {
			portails.setPlacable(false);
			portails.setBigParent(null);
			portails.setPossiblePosition(null);
		}
	}

	/**
	 * methode qui verifie si un point se trouve dans l'un des rectangles des
	 * niveaux.
	 * 
	 * @param x
	 *            position x en unites des pixels
	 * @param y
	 *            position y en unites des pixels
	 * @return vrai si le point est dans un rectangle faux sinon.
	 */
	public boolean inRect(double x, double y) {

		for (Rectangle r : rectangles) {
			if (r.contains(new Vecteur(x, y))) {
				return true;
			}
		}
		return false;
	}

	// Ayoub
	/**
	 * Rajouter un objet Balle dans la ArrayList balles
	 * 
	 * @param balleTemp
	 *            Balles temporaire a ajouter dans la liste
	 */
	public void addBalle(Vecteur posInit, Vecteur vitInit) {
		Balle ballTemp = new Balle(posInit, vitInit, this.grav);
		balles.add(ballTemp);
	}

	// Duy
	/**
	 * ajouter une balle avec une vecteur de vitesse
	 */
	public void shootCanon() {
		if (!parent.getParent().isCreatif()) {

			if (balles.size() == nbMaxBalles) {
				balles.get(0).setDying(true);
				ballesRemoved.add(balles.get(0));
				balles.remove(0);
			}
			Balle b = canon.shootBall();

			balles.add(b);

			currentBalle = b;
			nbTire++;
		}
	}

	// Ayoub
	/**
	 * Rajouter un plan dans la ArrayList des plans
	 * 
	 * @param planTemp
	 *            un plan temporaire qui sera rajoute a la liste
	 */
	public void addPlan(Vecteur posInit, Vecteur posFin) {
		plans.add(new Plans(posInit, posFin));
	}

	/**
	 * Auteur Duy
	 * 
	 * @param b
	 *            permet d'ajouter un plan dans l'arrayliste
	 */
	public void addPlan(Plans b) {
		plans.add(b);
	}

	public void addCanon(Canon c) {
		canon = c;
	}

	// Ayoub
	/**
	 * Dessiner l'objet en mode scienfique avec ses vecteurs
	 * 
	 * @param g2d
	 *            graphics ou sera dessiner l'objet
	 * @param matMC
	 *            matrice de transformation du monde reel vers le monde pixelise
	 */
	@Override
	public void dessinerModeCreatif(Graphics2D g2d, AffineTransform matMc) {
		// dessiner les plans
		for (int i = 0; i < plans.size(); i++) {
			plans.get(i).dessinerModeCreatif(g2d, matMc);
		}
		// dessiner les rectangles
		for (int i = 0; i < rectangles.size(); i++) {
			rectangles.get(i).dessinerModeCreatif(g2d, matMc);

		}

		// Dessiner le plan selectionnee au dessous des autres plans
		if (planSelec != null)

		{
			planSelec.dessinerModeCreatif(g2d, matMc);
		}
		// Dessiner le canon et le point d'arriver au dessu de tout
		if (canon != null) {
			canon.dessinerModeCreatif(g2d, matMc);
		}

		if (ptArrivee != null) {
			ptArrivee.dessinerModeCreatif(g2d, matMc);
		}
		portails.dessinerModeCreatif(g2d, matMc);

	}

	// Ayoub
	/**
	 * retourne les portails
	 */
	public Portails getPortails() {
		return portails;
	}

	// Ayoub
	/**
	 * update la position du curseur dans le composant
	 * 
	 * @param x
	 *            Position x du curseur
	 * @param y
	 *            Position y du curseur
	 *            @param xPx 
	 *            
	 */

	public void updateCurseur(double x, double y, double xPx, double yPx) {

		mousePos.setX(xPx);
		mousePos.setY(yPx);
		portails.updateSouris(x, y, mousePos.getX(), mousePos.getY());

	}

	// Ayoub
	/**
	 * Une methode qui verifie commet une action si l'utilisateur appuie sur le
	 * bouton droit de la souris. La seul action du bouton droit est de placer un
	 * portail bleu.
	 */
	public void clickDroit() {
		if (!parent.getParent().isCreatif()) {

			portails.placePortailBleu();

		}
	}

	// Ayoub
	/**
	 * set la valeur de la boolean isSurMire d'apres la position de la souris par
	 * rapport a la mire du canon.
	 * 
	 * @param b
	 *            La nouvelle valeur de isSUrMire
	 */
	public void setSurMire(boolean b) {
		isSurMire = b;

	}

	// Ayoub
	/**
	 * 
	 * @return La valeur de la boolean isSurMire.
	 */
	public boolean getSurMire() {

		return isSurMire;
	}

	/**
	 * Methode qui deselectionne chaque objet.
	 */
	public void DeselectionnerTout() {
		for (Plans p : plans) {
			p.setSelected(false);
		}

		for (Rectangle r : rectangles) {
			r.setSelected(false);
		}
		ptArrivee.setSelected(false);
		canon.setSelected(false);
	}

	/**
	 * Methode qui cherche a selectionner un objet.
	 */
	public void selectionnerObjet() {
		if (canon.getAireCanon().contains(mousePos.getX(), mousePos.getY())) {
			DeselectionnerTout();

			canon.setSelected(true);
			compSelec = "canon";
		} else if (ptArrivee.contains(mousePos)) {
			DeselectionnerTout();

			ptArrivee.setSelected(true);
			compSelec = "ptArrivee";

		} else {
			DeselectionnerTout();

			// voir si un plan est selectionne
			Plans pl = null;
			for (Plans p : plans) {
				if (p.contains(mousePos.getX(), mousePos.getY())) {
					pl = p;
				}
				p.setSelected(false);
			}

			if (pl != null) {

				pl.setSelected(true);
				planSelec = pl;
				compSelec = "pl";
			} else {
				planSelec = null;

				// trouver si un rectangle est selectionner
				Rectangle rect = null;
				for (Rectangle r : rectangles) {
					if (r.contains(mousePos) || r.movePointsContains(mousePos)) {
						rect = r;

					}
					r.setSelected(false);
				}
				if (rect != null) {
					rect.setSelected(true);
					rectSelec = rect;
					if (rect.movePointsContains(mousePos)) {
						rect.setScaling(true);
					}
					compSelec = "rect";
				} else {
					compSelec = "";
				}

			}
		}

	}

	// Ayoub
	/**
	 * Methode qui decide d'une action d'apres la position de la souris apres que
	 * l'utilisateur est release le bouton gauche de la souris
	 */
	public void clickGauche() {
		if (parent.getParent().isCreatif()) {
			// on est en mode creatif trouver si un objet doit etre selectionne
			selectionnerObjet();
			// si la souris etait dragged
			if (moving) {
				moving = false;
				canon.setPosInit();
				ptArrivee.setPositionsInit();
				if (planSelec != null) {
					planSelec.setPositionsInit();
					planSelec.setScaling(false);
				}

				if (rectSelec != null) {
					rectSelec.setPositionInit();
					rectSelec.setScaling(false);
					rectSelec.setScalerNull();
				}
			}

		} else {
			if (canon.getAireCanon().contains(mousePos.getX(), mousePos.getY())) {
				// la souris est sur le canon alors tirer le canon

				shootCanon();
			} else {

				portails.placePortailOrange();

			}

			isSurMire = false;
		}

	}

	// Ayoub
	/**
	 * Methode qui bouge les composants lorsque la souris est dragged
	 */
	public void mouseDragged() {

		if (parent.getParent().isCreatif()) {
			// voir si on peut bouger un composant
			if (moving) {

				if (compSelec.equals("canon")) {
					canon.move(portails.getPosition().getX(), startDrag);
				} else if (compSelec.equals("pl")) {
					if (planSelec.movePointsContains(mousePos.getX(), mousePos.getY()) || planSelec.isScaling()) {
						planSelec.scale(portails.getPosition(), mousePos);
					} else {
						planSelec.move(portails.getPosition(), startDrag);
					}
				} else if (compSelec.equals("rect")) {
					if (rectSelec.isScaling()) {
						rectSelec.scale(portails.getPosition(), mousePos);
					} else {
						rectSelec.move(portails.getPosition(), startDrag);
					}
				} else if (compSelec.equals("ptArrivee")) {
					ptArrivee.move(portails.getPosition(), startDrag);
				}
			}
		} else {
			if (isSurMire) {
				canon.changerAngle(portails.getPosition());
			}

		}
	}

	// Ayoub
	/**
	 * Methode qui decide d'une action d'apres la position de la souris apres que
	 * l'utilisateur est appuyer le bouton gauche de la souris
	 */
	public void mousePressed() {
		if (parent.getParent().isCreatif()) {
			// faire des trucs du mode creatif
			selectionnerObjet();
			if (compSelec.equals("canon")) {
				if (canon.getAireCanon().contains(mousePos.getX(), mousePos.getY())) {
					moving = true;
				} else {
					moving = false;
				}
			} else if (compSelec.equals("pl")) {
				if (planSelec.contains(mousePos.getX(), mousePos.getY())) {
					moving = true;
				} else {
					moving = false;
				}
			} else if (compSelec.equals("ptArrivee")) {
				if (ptArrivee.contains(mousePos)) {
					moving = true;
				} else {
					moving = false;
				}
			} else if (compSelec.equals("rect")) {
				if (rectSelec.contains(mousePos) || rectSelec.movePointsContains(mousePos)) {
					moving = true;
				} else {
					moving = false;
				}
			} else {
				moving = false;
			}

			// set le point ou le drag motion a commence
			if (moving) {
				startDrag = portails.getPosition().copy();

			} else {
				startDrag = null;
			}

		} else {
			if (canon.getAireMire().contains(mousePos.getX(), mousePos.getY())) {
				// la souris est sur la mire alors tourner le canon dapres la position de la
				// souris
				isSurMire = true;

			}
		}

	}

	// Ayoub
	/**
	 * Une methode qui reset le niveau, vide les balles et enleve les portails
	 */
	public void reset() {
		balles = new ArrayList<Balle>();
		canon.setAngleInit();
		portails.delete();

		// Enlever la selection
		if (compSelec.equals("canon")) {
			canon.setSelected(false);
		} else if (compSelec.equals("pl")) {
			planSelec.setSelected(false);
		}

		compSelec = "";
	}

	/**
	 * Methode qui update les elements du niveau apres qu'il a ete pris d'un fichier
	 * binaire.
	 */
	public void resetApresLecture(ComposantAnimation p) {
		parent = p;
		balles = new ArrayList<Balle>();
		canon.setAngleInit();
		if (compSelec.equals("canon")) {
			canon.setSelected(false);
		} else if (compSelec.equals("pl")) {
			planSelec.setSelected(false);
		}

		portails = new Portails(new Vecteur(1000, 1000, 0), modele, this);
	}

	// Ayoub
	/**
	 * Methode qui ajoute quatres plans, un a chaque cote du niveau.
	 */
	private void creerPlansCote() {
		// plan bas
		Vecteur posInit, posFin;
		posInit = new Vecteur(0, modele.getHautUnitesReelles());
		posFin = new Vecteur(modele.getLargUnitesReelles(), modele.getHautUnitesReelles());

		addPlan(posInit, posFin);
		// plan gauche
		posInit = new Vecteur(0, 0);
		posFin = new Vecteur(0, modele.getHautUnitesReelles());

		addPlan(posInit, posFin);

		// plan droit

		posInit = new Vecteur(modele.getLargUnitesReelles(), 0);
		posFin = new Vecteur(modele.getLargUnitesReelles(), modele.getHautUnitesReelles());

		addPlan(posInit, posFin);

	}

	// Ayoub
	/**
	 * 
	 * @return Le vecteur d'acceleration.
	 */
	public Vecteur getGrav() {
		return grav;
	}

	// Ayoub
	/**
	 * Methode qui imprime les informations du niveau.
	 */
	public void print() {
		System.out.println(nomNiveau + "\n Niveau créer par " + authorName + " le " + dateCreation + ".");

	}

	// Ayoub
	/**
	 * Methode qui donne un nouveau nom au niveau.
	 * 
	 * @param nomNiveau
	 *            Nouveau nom du niveau.
	 */
	public void setNomNiveau(String nomNiveau) {
		this.nomNiveau = nomNiveau;
	}

	// Ayoub
	/**
	 * Methode qui donne un nouveau nom au createur du niveau.
	 * 
	 * @param authorName
	 *            Nouvel auteur,
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	// Ayoub
	/**
	 * Methode qui assigne une nouvelle date de creation au niveau.
	 * 
	 * @param dateCreation
	 *            String de la nouvelle date de creation.
	 */
	public void setDateCreation(String dateCreation) {
		this.dateCreation = dateCreation;
	}

	// Ayoub
	/**
	 * 
	 * @return Les plans du niveau.
	 */
	public ArrayList<Plans> getPlans() {

		return plans;
	}

	// Ayoub
	/**
	 * 
	 * @return Le composant animation parent du niveau.
	 */
	private ComposantAnimation getParent() {

		return parent;
	}

	// Ayoub
	/**
	 * 
	 * @return Le modele d'affichage assigne au niveau.s
	 */
	private ModeleAffichage getModele() {

		return modele;
	}

	// Ayoub
	/**
	 *
	 * @return L'objet du canon du niveau.
	 */
	public Canon getCanon() {

		return canon;
	}

	// Ayoub
	/**
	 * 
	 * @return Le nom du niveau.
	 */
	public String getNomNiv() {

		return nomNiveau;
	}

	// Ayoub
	/**
	 * Methode qui verifie si deux niveau sont equivalent.
	 * 
	 * @param other
	 *            L'autre niveau.
	 * @return Vrai si other est le meme niveau que le niveau sur lequel la methode
	 *         est appellee
	 */
	public boolean equals(Niveau other) {

		if (plans.size() == other.getPlans().size()) {
			// les deux niveaux ont le meme nombre de plans
			for (int i = 0; i < plans.size(); i++) {
				if (!plans.get(i).equals(other.getPlans().get(i))) {

					return false;
				}
			}
			if (rectangles.size() == other.getRectangles().size()) {
				for (int i = 0; i < rectangles.size(); i++) {

					if (!rectangles.get(i).equals(other.getRectangles().get(i))) {

						return false;
					}
				}
			} else {
				return false;
			}
			if (!other.getCanon().getPosition().equals(canon.getPosition())) {
				return false;
			}

			if (!other.getPtArrivee().getPosition().equals(ptArrivee.getPosition())) {
				return false;
			}

			if (other.getCoeffFric() != coeffFric || other.getCoeffRest() != coeffRest
					|| other.getNbMaxBalles() != nbMaxBalles) {
				return false;
			}

		} else {

			return false;
		}

		return true;
	}

	/**
	 * Auteur Duy
	 * 
	 * @return vecteur position de la balle
	 */
	public Vecteur getPosBalle() {
		if (currentBalle != null) {
			return currentBalle.getPosition();
		} else {
			return new Vecteur(0, 0);
		}
	}

	/**
	 * Auteur Duy
	 * 
	 * @return vecteur vitesse de la balle
	 */
	public Vecteur getVitBalle() {
		if (currentBalle != null) {
			return currentBalle.getVitesse();
		} else {
			return new Vecteur(0, 0);
		}
	}

	/**
	 * Auteur Duy
	 * 
	 * @return vecteur position de la balle
	 */
	public Vecteur getAccBalle() {
		if (currentBalle != null) {
			return currentBalle.getAcceleration();
		} else {
			return new Vecteur(0, 0);
		}
	}

	// Ayoub
	/**
	 * 
	 * @return La liste des balles
	 */
	public ArrayList<Balle> getBalles() {

		return balles;
	}

	// Ayoub
	/**
	 * 
	 * @return L'objet point d'arrivee
	 */
	public PointArrivee getPtArrivee() {

		return ptArrivee;
	}

	// Duy
	/**
	 * Methode qui ajoute un rectangle a la liste des rectangles.
	 * 
	 * @param rect
	 *            Le nouveau rectangle a ajouter.
	 */
	public void addRectangle(Rectangle rect) {
		rectangles.add(rect);

	}

	// Duy
	/**
	 * Methode qui verifie si une balle a toucher le point d'Arrivee. Si oui le
	 * niveau est gagne.
	 */
	public void gagner() {
		Object[] options = { "Prochain niveau", "Recommencer" };
		Jeu grandparent = parent.getParent();
		int n = JOptionPane.showOptionDialog(null, "Vous avez gagné ce niveau avec " + nbTire + " tire(s)", "Bravo !",
				JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, 0);
		// enCollision = true
		if (n == 0) {
			parent.nextNiveau();
			grandparent.setLblNiveau();

		} else if (n == 1) {
			nbTire = 0;
			balles.clear();
		}
		gagne = false;
	}

	/**
	 * 
	 * @return Le nombre maximal de balles.
	 */

	public int getNbMaxBalles() {
		return nbMaxBalles;
	}

	/**
	 * 
	 * @return Le coefficient de restitution du niveau
	 */
	public double getCoeffRest() {
		return coeffRest;
	}

	/**
	 * Methode qui donne une nouvelle valeur au nombre maximale de balles que peut
	 * avoir le niveau.
	 * 
	 * @param nbBalles
	 *            Le nouveau nombre Maximale de balles.
	 */
	public void setNbMaxBalles(int nbBalles) {
		this.nbMaxBalles = nbBalles;
	}

	// Ayoub
	/**
	 * Donne une nouvelle valeur au coefficient de restition
	 * 
	 * @param coeffRest
	 *            Nouvelle valeur du coeffiction de restitution des balles
	 */
	public void setCoeffRest(double coeffRest) {
		this.coeffRest = coeffRest;
	}

	// Ayoub
	/**
	 * 
	 * @param coeffFric
	 * @return
	 */
	public double getCoeffFric() {

		return coeffFric;
	}

	/**
	 * Donne une nouvelle valeur au coefficient de friction
	 * 
	 * @param coeffFric
	 *            Nouvelle valeur du coefficient de friction du niveau.
	 */
	public void setCoeffFric(double coeffFric) {
		this.coeffFric = coeffFric;
	}

	public SuperObjets getObjetSelectionne() {
		if (compSelec.equals("pl")) {
			return planSelec;
		} else if (compSelec.equalsIgnoreCase("rect")) {
			return rectSelec;
		} else if (compSelec.equalsIgnoreCase("canon")) {
			return canon;
		} else if (compSelec.equals("ptArrivee")) {
			return ptArrivee;
		}
		return null;
	}

	public String getStringSelec() {
		return compSelec;
	}

	public void supprimerObj(SuperObjets elementSelec) {
		if (elementSelec instanceof Plans) {
			plans.remove(elementSelec);
			planSelec = null;
			compSelec = "";
		} else if (elementSelec instanceof Rectangle) {
			rectangles.remove(elementSelec);
			rectSelec = null;
			compSelec = "";
		}

	}
}
