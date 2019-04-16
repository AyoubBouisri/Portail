package composants;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.MemoryImageSource;

import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import geometrie.Vecteur;
import interfaces.ObjListener;
import modeleaffichage.ModeleAffichage;
import niveau.ArrayNiveau;
import niveau.Niveau;
import objets.SuperObjets;
import interfaces.ObjListener;

/**
 * Composant principal ou se deroule la simulation physique
 * 
 * @author Ayoub
 * 
 */

public class ComposantAnimation extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	private final EventListenerList OBJETS_ENREGISTRES = new EventListenerList();
	private final int LARGEUR_DU_MONDE = 200; // 200m
	private final int NB_ITERATIONS_PHYSIQUES = 20;
	private final int TEMPS_DU_SLEEP = 10;
	private final double DELTA_T = 0.002;
	private final Vecteur GRAVITE = new Vecteur(0, 9.8);
	private final int MARGE_LIGNES = 20;
	private int MULT_RECT = 4;

	private boolean enCoursDAnimation = false;
	private boolean animationPaused = false;
	private ModeleAffichage modele;

	private ArrayNiveau niveaux;
	private Niveau currentNiveau;
	private Jeu parent;

	/**
	 * Méthode pour créer une composant d'animation
	 * 
	 * @param width
	 *            La largeur du composant
	 * @param height
	 *            La hauteur du composant
	 * @param border
	 *            La distance entre le composant et le panel parent
	 */
	public ComposantAnimation(int width, int height, int border, Jeu parent) {
		this.parent = parent;
		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent arg0) {
				if (arg0.getModifiers() == MouseEvent.BUTTON1_MASK) {
					// verifier si cest un click gauche

					currentNiveau.mousePressed();
					leverEvenSelection();
				}

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				currentNiveau.getPortails().setInComposant(true);
				hideCursor();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				currentNiveau.getPortails().setInComposant(false);

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				Vecteur temp = modele.getInUnitesReelles(e.getX(), e.getY());
				currentNiveau.updateCurseur(temp.getX(), temp.getY(), e.getX(), e.getY());
				if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
					// si cest un click gauche placer orange

					currentNiveau.clickGauche();

				} else if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
					// si cest un click droit placer bleu

					currentNiveau.clickDroit();

				}
				currentNiveau.setSurMire(false);
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {

			// update le custom curseur du composant
			@Override
			public void mouseMoved(MouseEvent e) {
				Vecteur temp = modele.getInUnitesReelles(e.getX(), e.getY());
				currentNiveau.updateCurseur(temp.getX(), temp.getY(), e.getX(), e.getY());
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				Vecteur temp = modele.getInUnitesReelles(e.getX(), e.getY());
				currentNiveau.updateCurseur(temp.getX(), temp.getY(), e.getX(), e.getY());

				currentNiveau.mouseDragged();

			}
		});

		this.setBounds(border, border, width, height);
		modele = new ModeleAffichage(getWidth(), getHeight(), 0, 0, LARGEUR_DU_MONDE);
		// Creer un niveau pour tester les objets

		niveaux = new ArrayNiveau(this);
		currentNiveau = niveaux.getCurrentNiveau();
		this.demarrer();
		setBackground(new Color(146, 146, 146));
	}

	/**
	 * Méthode pour dessiner la composant d'animation
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		AffineTransform matMC = modele.getMatMC();

		// dessiner le current niveau
		if (parent.isCreatif()) {
			// dessinerle niveau en mode scientifique
			dessinerModeCreatif(g2d, matMC);

		} else {
			// dessiner le niveau en mode normal
			setBackground(new Color(146, 146, 146));
			currentNiveau.dessiner(g2d, matMC);
			dessinerEchelle(g2d, matMC);
		}

	}// fin paintComponent

	/**
	 * Méthode qui dessine le composant en mode créatif
	 * 
	 * @param g2d
	 *            Graphics ou sera dessinee l'echelle
	 * @param matMC
	 *            Matrice de transformation vers le monde reel
	 */
	public void dessinerModeCreatif(Graphics2D g2d, AffineTransform matMC) {
		// Dessiner le background

		setBackground(new Color(117, 169, 249));

		// Dessiner les lignes blanches
		// lignes horizontales
		for (int i = 0; i < this.getHeight() / MARGE_LIGNES + 1; i++) {
			Line2D.Double l = new Line2D.Double(0, i * MARGE_LIGNES, this.getWidth(), i * MARGE_LIGNES);
			Line2D.Double l2 = new Line2D.Double(0, i * MARGE_LIGNES * MULT_RECT, this.getWidth(),
					i * MARGE_LIGNES * MULT_RECT);
			g2d.setColor(new Color(255, 255, 255, 50));
			g2d.draw(l);
			g2d.setColor(new Color(255, 255, 255, 100));
			g2d.draw(l2);
		}

		// lignes verticales
		for (int i = 0; i < this.getWidth() / MARGE_LIGNES + 1; i++) {
			Line2D.Double l = new Line2D.Double(i * MARGE_LIGNES, 0, i * MARGE_LIGNES, this.getHeight());
			Line2D.Double l2 = new Line2D.Double(i * MARGE_LIGNES * MULT_RECT, 0, i * MARGE_LIGNES * MULT_RECT,
					this.getHeight());

			g2d.setColor(new Color(255, 255, 255, 50));
			g2d.draw(l);
			g2d.setColor(new Color(255, 255, 255, 100));
			g2d.draw(l2);
		}
		// Dessiner les objets du niveau
		currentNiveau.dessinerModeCreatif(g2d, matMC);
	}

	/**
	 * Méthode qui dessiner l'échelle qui représente les mesures du monde réel
	 * 
	 * @param g2d
	 *            Graphics où l'échelle sera dessiner
	 * @param matMC
	 *            Matrice de transformation vers le monde réel
	 */
	public void dessinerEchelle(Graphics2D g2d, AffineTransform matMC) {
		// DEssiner echelle monde reel
		g2d.setStroke((new BasicStroke(1.0f)));
		Font font = new Font("Ariel", Font.PLAIN, 13);
		g2d.setFont(font);
		g2d.setColor(new Color(255, 255, 255, 100));
		int tickL = 2;
		g2d.draw(matMC.createTransformedShape(new Line2D.Double(0, (int) modele.getHautUnitesReelles() - tickL / 2,
				LARGEUR_DU_MONDE, (int) modele.getHautUnitesReelles() - tickL / 2)));
		// 50 m
		g2d.drawString("50 m", getWidth() / 4 - 10, getHeight() - 15);

		g2d.draw(matMC.createTransformedShape(
				new Line2D.Double(LARGEUR_DU_MONDE / 4, (int) modele.getHautUnitesReelles() - tickL,
						LARGEUR_DU_MONDE / 4, (int) modele.getHautUnitesReelles())));
		// 100 m
		g2d.drawString("100 m", getWidth() / 2 - 10, getHeight() - 15);
		g2d.draw(matMC.createTransformedShape(
				new Line2D.Double(LARGEUR_DU_MONDE / 2, (int) modele.getHautUnitesReelles() - tickL,
						LARGEUR_DU_MONDE / 2, (int) modele.getHautUnitesReelles())));
		// 75m
		g2d.drawString("150 m", 3 * getWidth() / 4 - 10, getHeight() - 15);
		g2d.draw(matMC.createTransformedShape(
				new Line2D.Double(3 * LARGEUR_DU_MONDE / 4, (int) modele.getHautUnitesReelles() - tickL,
						3 * LARGEUR_DU_MONDE / 4, (int) modele.getHautUnitesReelles())));
	}

	@Override
	public void run() {
		while (enCoursDAnimation) {
			if (!animationPaused) {
				for (int k = 0; k < NB_ITERATIONS_PHYSIQUES; k++) {
					try {

						currentNiveau.update(DELTA_T);
					} catch (Exception e) {

						e.printStackTrace();
					}
				}
			}
			currentNiveau.updateUtilisateur();

			if (!parent.isCreatif() && niveaux.getCurrentNiveau().getBalles().size() != 0) {
				parent.updateLabels(currentNiveau.getPosBalle(), currentNiveau.getVitBalle(),
						currentNiveau.getAccBalle());
			}

			repaint();

			try {
				Thread.sleep(TEMPS_DU_SLEEP);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Auteur Duy Créer la méthode pasEuler pour la bouton pas à pas
	 */
	public void pasEuler() {
		arreter();
		for (int k = 0; k < NB_ITERATIONS_PHYSIQUES; k++) {
			currentNiveau.update(DELTA_T);
		}
		repaint();
	}

	/**
	 * Auteur Duy Créer un nouveau thread et le lancer pour démarrer l'animation
	 */
	public void demarrer() {
		if (!enCoursDAnimation) {
			Thread proc = new Thread(this);
			proc.start();
			enCoursDAnimation = true;

		} else {
			animationPaused = false;
		}
	}

	/**
	 * Auteur Duy Arreter l'animation
	 */
	public void arreter() {
		animationPaused = true;
	}

	public void creerNiveauTest1() {
		// creer des plans sur les bord et animer une balle
		Niveau nivTest1 = new Niveau(GRAVITE, modele, this);

		Vecteur posInit = new Vecteur(LARGEUR_DU_MONDE / 2, 10);
		Vecteur vitInit = new Vecteur(0, 0);

		nivTest1.addBalle(posInit, vitInit);

		double lowerX = 0;
		double higherX = LARGEUR_DU_MONDE;
		double lowerY = 0;
		double higherY = modele.getHautUnitesReelles();
		for (int i = 0; i < 5; i++) {
			posInit = new Vecteur((double) (Math.random() * (higherX - lowerX)) + lowerX,
					(double) (Math.random() * (higherY - lowerY)) + lowerY);
			nivTest1.addBalle(posInit, vitInit);
		}

		Vecteur posFin;

		// plan portail test

		posFin = new Vecteur(50, 30);
		posInit = new Vecteur(LARGEUR_DU_MONDE - 50, 30);
		nivTest1.addPlan(posInit, posFin);
		// 2 em plan portail test

		posInit = new Vecteur(50, 50);
		posFin = new Vecteur(LARGEUR_DU_MONDE - 50, 70);

		nivTest1.addPlan(posInit, posFin);

		// 3 em
		posInit = new Vecteur(LARGEUR_DU_MONDE - 50, 100);
		posFin = new Vecteur(50, 100);

		// currentNiveau.addPlan(posInit, posFin);

		niveaux.add(nivTest1, "Niveau Test", "Ayoub");
	}

	/**
	 * cache le curseur lorsquil est dans le composant
	 */
	public void hideCursor() {

		int[] pixels = new int[16 * 16];
		Image image = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16, 16, pixels, 0, 16));
		Cursor transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0),
				"invisibleCursor");
		setCursor(transparentCursor);

	}

	/**
	 * 
	 * @return Le modele affichage du composant
	 */
	public ModeleAffichage getModele() {
		return modele;
	}

	/**
	 * Methode qui imprime l'information de chaque niveau existant.
	 */
	public void printNiveaux() {
		niveaux.print();
	}

	/**
	 * 
	 * @return Le jeu dans le quel se trouve le composant
	 */
	public Jeu getParent() {
		return parent;
	}

	/**
	 * 
	 * @return Le niveau courant
	 */
	public Niveau getCurrentNiv() {

		return currentNiveau;
	}

	/**
	 * Methode qui donne une nouvelle reference au niveau courant.
	 * 
	 * @param niv
	 *            Le nouveau niveau courant.
	 */
	public void setCurrentNiv(Niveau niv) {
		currentNiveau = niv;
	}

	/**
	 * Methode qui retrouve le currentNiveau dans la liste des nivaeux et l'assigne
	 * au currentNiveau du composant.
	 */
	public void setCurrentNivFromList() {
		currentNiveau = niveaux.getCurrentNiveau();
	}

	/**
	 * Ajoute un niveau dans la liste des niveaux
	 * 
	 * @param niv
	 *            Nouveau niveau a ajouter.
	 */
	public void addNiveau(Niveau niv) {
		niveaux.add(niv, "Niveau ajoutee", "Test");
	}

	/**
	 * Methode qui change le niveau courant pour le prochain niveau dans la liste.
	 * Si le prochain n'existe pas le niveau courant ne change pas.
	 */
	public void nextNiveau() {
		niveaux.next();
		setCurrentNivFromList();
	}

	/**
	 * Methode qui change le niveau courant pour le precedant niveau dans la liste.
	 * Si le precedant n'existe pas le niveau courant ne change pas.
	 */
	public void previousNiveau() {
		niveaux.previous();
		setCurrentNivFromList();
	}

	/**
	 * Methode qui supprimer le niveau courant.
	 */
	public void supprimerNiveauCourant() {
		niveaux.supprimer(currentNiveau);
		setCurrentNivFromList();
	}

	/**
	 * @return Methode qui retourne la grandeur de la liste des niveaux.
	 */
	public int getLength() {
		return niveaux.getSize();
	}

	/**
	 * 
	 * @return L'index du niveau courant dans la liste.
	 */
	public int indexOfCurrent() {
		return niveaux.indexOfCurrent() + 1;
	}

	/**
	 * Methode qui sauvegarde les niveaux dans un fichier binaire
	 */
	public void sauvegarde() {
		niveaux.ecrireFichierBinaire();

	}

	/**
	 * Methode qui remplace un niveau dans la liste par un autre
	 * 
	 * @param nivARemplacer
	 *            Le niveau qui sera remplace
	 * @param nouveauNiveau
	 *            Le nouveau niveau qui prendera la place du niveau a remplacer
	 * @param nomNiv
	 *            Le nom du nouveau niveau
	 * @param authorName
	 *            Le nom de l'auteur du nouveau niveau
	 */
	public void replace(int indexNivARemplacer, Niveau nouveauNiveau, String nomNiv, String authorName) {
		niveaux.replace(indexNivARemplacer, nouveauNiveau, nomNiv, authorName);
	}

	/**
	 * Methode qui ajoute un listenerde type ObjLIstener
	 * 
	 * @param objEcouteur
	 */
	public void addSelectionListener(ObjListener objEcouteur) {
		// enregistre un autre objet qu'on devra aviser au moment de l'evenement
		OBJETS_ENREGISTRES.add(ObjListener.class, objEcouteur);
	}
	
	
	public void leverEvenSelection() {
		for (ObjListener ecout : OBJETS_ENREGISTRES.getListeners(ObjListener.class)) {
			ecout.getObjSelectionner(currentNiveau.getStringSelec(), getObjetSelectionne());
		}
	}
	public SuperObjets getObjetSelectionne() {

		return currentNiveau.getObjetSelectionne();
	}

	public void supprimerObj(SuperObjets elementSelec) {
		currentNiveau.supprimerObj(elementSelec);
		leverEvenSelection();
		
	}
}
