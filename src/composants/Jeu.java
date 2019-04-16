package composants;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Boutons.BoutonAnim;
import Boutons.BoutonMenu;
import Boutons.BoutonNiveau;
import Boutons.BoutonRadio;
import aaplication.App17Portails;
import geometrie.Vecteur;
import interfaces.ObjListener;
import labels.Label;
import labels.LabelNiveau;
import niveau.Niveau;
import objets.Canon;
import objets.Plans;
import objets.PointArrivee;
import objets.Rectangle;
import objets.SuperObjets;
import objets.Triangle;

/**
 * Classe regroupant les paneaux du jeu. (Composant,outils,boutons ... )
 * 
 * @author Ayoub
 *
 */
public class Jeu extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9068913769750471899L;
	// Marge entre le composant graphique et le panel outils
	private final int MARGE = 30;
	private final double MARGE_LABELS_X = MARGE;
	private final double MARGE_LABELS_Y = 35;
	private int width;

	// VAriables composant Animation
	private int widthComposantAnim;
	private int heightComposantAnim;
	private int xOutils;
	private int y = 10;
	// Variables bouton retour
	private final int WIDTH_BOUTON = 145;
	private final int HEIGHT_BOUTON = 45;

	private Color selected = new Color(229, 142, 29); // dark orange
	private Color nonSelected = new Color(246, 170, 35); // light orange
	// boolean afficher vecteurs
	public static boolean afficherVecteur;
	public static boolean vecteurBefore;

	// variables pour les boutons qui gerent l'animation
	private BoutonAnim boutPlay;
	private BoutonAnim boutPause;
	private BoutonAnim boutNextPas;
	private LabelNiveau lblNiv;
	private final double widthAnimBtn = 75;
	private final double widthbtnNiv = 40;
	private double xAnimBtn;
	private double yAnimBtn;

	private PanelOutils outils;
	private ComposantAnimation compAnim;
	private boolean modeCreatif = false;
	private Niveau currentNivCreatif = null;
	private Niveau nivSaved = null;
	private int indexNivSaved = -1;
	private Label labelPosition, labelVitesse, labelAcceleration;

	/**
	 * @wbp.nonvisual location=117,429
	 */
	/**
	 * 
	 * @param width
	 *            Largeur du panel en pixels
	 * @param height
	 *            Hauteur du panel en pixels
	 */
	public Jeu(int width, int height) {
		this.setLayout(null);
		this.width = width;
		widthComposantAnim = (int) (width * 0.75);
		heightComposantAnim = (int) (height * 0.75);
		afficherVecteur = false;
		vecteurBefore = afficherVecteur;
		this.setBounds(0, 0, width, height);
		compAnim = new ComposantAnimation(widthComposantAnim, heightComposantAnim, y, this);
		compAnim.addSelectionListener(new ObjListener() {

			@Override
			public void getObjSelectionner(String compSelecString, SuperObjets compSelec) {
				outils.setCompObj(compSelec);

			}
		}

		);
		compAnim.setBounds(y, y, widthComposantAnim, heightComposantAnim);
		this.add(compAnim);
		compAnim.setLayout(null);

		xOutils = width - (width - widthComposantAnim) + MARGE;

		outils = new PanelOutils(xOutils, width, height, this);
		this.add(outils);
		outils.setLayout(null);

		// CREER BOUTON RETOUR MENU
		BoutonMenu btnRetour = new BoutonMenu(HEIGHT_BOUTON, WIDTH_BOUTON, "MENU", selected, nonSelected, 34);

		this.add(btnRetour);
		btnRetour.setBounds((int) (widthComposantAnim + y - WIDTH_BOUTON), (int) (height - 50 - HEIGHT_BOUTON),
				WIDTH_BOUTON, HEIGHT_BOUTON);
		btnRetour.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				compAnim.sauvegarde();
				App17Portails.retourner();
			}
		});

		// CREER BOUTON RADIO
		int w = 230;
		int h = 40;
		BoutonRadio rdbtnVecteurs = new BoutonRadio(w, h, "Voir vecteurs", this);
		rdbtnVecteurs.setTopPanel(outils);
		rdbtnVecteurs.setLayout(null);
		rdbtnVecteurs.setBounds((int) (widthComposantAnim + y + MARGE_LABELS_X), (int) (MARGE_LABELS_Y), w, h);
		add(rdbtnVecteurs);

		// CREER LES BOUTON PLAY,PAUSE ET NEXTPAS
		int xBtnAnim = y;
		boutPlay = new BoutonAnim(widthAnimBtn, 0);
		boutPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {

				if (boutPlay.isOn()) {
					compAnim.demarrer();
					boutPlay.setOn(false);
					boutPause.setOn(true);
				}
			}
		});
		boutPlay.setOn(false);
		boutPlay.setLayout(null);
		boutPlay.setBounds(xBtnAnim, (int) (height - widthAnimBtn - 50), (int) widthAnimBtn, (int) widthAnimBtn);
		add(boutPlay);

		xBtnAnim += widthAnimBtn + MARGE_LABELS_X;
		boutPause = new BoutonAnim(widthAnimBtn, 1);
		boutPause.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {

				if (boutPause.isOn()) {
					compAnim.arreter();
					boutPause.setOn(false);
					boutPlay.setOn(true);
				}
			}
		});
		boutPause.setLayout(null);
		boutPause.setBounds(xBtnAnim, (int) (height - widthAnimBtn - 50), (int) widthAnimBtn, (int) widthAnimBtn);
		add(boutPause);

		xBtnAnim += widthAnimBtn + MARGE_LABELS_X;
		boutNextPas = new BoutonAnim(widthAnimBtn, 2);
		boutNextPas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {

				if (boutNextPas.isOn()) {
					compAnim.pasEuler();
					boutPause.setOn(false);
					boutPlay.setOn(true);
				}
			}
		});
		boutNextPas.setLayout(null);
		boutNextPas.setBounds(xBtnAnim, (int) (height - widthAnimBtn - 50), (int) widthAnimBtn, (int) widthAnimBtn);
		add(boutNextPas);

		// creer les labels
		createLabels();

		// creer le bouton changer de niveau gauche
		double x = widthComposantAnim + MARGE_LABELS_X;
		double posY = (int) (y + (heightComposantAnim - 125 * 3 - MARGE_LABELS_Y * 2) + (MARGE_LABELS_Y + 125) * 3);
		BoutonNiveau btnNivGauche = new BoutonNiveau(x, posY - 20, widthbtnNiv, 40, selected, nonSelected, 0, this);
		btnNivGauche.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				compAnim.previousNiveau();
				lblNiv.setCurrent(compAnim.indexOfCurrent());
				repaint();
			}
		});

		this.add(btnNivGauche);

		// creer le bouton changer de niveau droite
		double xbtnDroite = x + (width - outils.getBoutonWidth()) - x - MARGE_LABELS_X - widthbtnNiv;
		BoutonNiveau btnNivDroite = new BoutonNiveau(xbtnDroite, posY - 20, widthbtnNiv, 40, selected, nonSelected, 1,
				this);
		btnNivDroite.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {

				compAnim.nextNiveau();
				lblNiv.setCurrent(compAnim.indexOfCurrent());
				repaint();
			}
		});
		this.add(btnNivDroite);

		// creer le bouton pour supprimer le niveau courant

		double xBtnSupp = x;
		double widthBtnSupp = (width - outils.getBoutonWidth()) - xBtnSupp - MARGE_LABELS_X;
		BoutonMenu btnSupp = new BoutonMenu(HEIGHT_BOUTON, (int) widthBtnSupp, "SUPPRIMER", Color.red, Color.red, 32);
		btnSupp.setParent(this);
		btnSupp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (!modeCreatif) {
					int confirmed = JOptionPane.showConfirmDialog(null,
							"Êtes-vous sur de vouloir supprimer le niveau courant ?", "Validation",
							JOptionPane.YES_NO_OPTION);

					if (confirmed == JOptionPane.YES_OPTION) {

						compAnim.supprimerNiveauCourant();
						lblNiv.setMax(compAnim.getLength());
						lblNiv.setCurrent(compAnim.indexOfCurrent());
						repaint();

					}
				}

			}
		});
		btnSupp.setBounds((int) xBtnSupp, (int) (height - 50 - HEIGHT_BOUTON), (int) widthBtnSupp, HEIGHT_BOUTON);
		this.add(btnSupp);

		lblNiv = new LabelNiveau(x + widthbtnNiv, posY - 20, xbtnDroite - (x + widthbtnNiv), widthbtnNiv, 1,
				compAnim.getLength());

		this.add(lblNiv);

	}

	/**
	 * Créer les labels pour le mode non scientifique
	 */
	public void createLabels() {
		double posX = widthComposantAnim + MARGE_LABELS_X;

		double labelWidth = (width - outils.getBoutonWidth()) - posX - MARGE_LABELS_X;
		double heightLabel = 125;
		double heightLabelNiveau = 40;
		double posY = y + (heightComposantAnim - heightLabel * 3 - MARGE_LABELS_Y * 2);
		// label position
		labelPosition = new Label(posX, posY, labelWidth, heightLabel, "Position Balle", " m");
		this.add(labelPosition);
		labelPosition.setLayout(null);

		// label vitesse
		posY += MARGE_LABELS_Y + heightLabel;
		labelVitesse = new Label(posX, posY, labelWidth, heightLabel, "Vitesse Balle", " m/s");
		this.add(labelVitesse);
		labelVitesse.setLayout(null);

		// label acceleration
		posY += MARGE_LABELS_Y + heightLabel;
		labelAcceleration = new Label(posX, posY, labelWidth, heightLabel, "Accélération Balle", " m/s\u00B2");
		this.add(labelAcceleration);
		labelAcceleration.setLayout(null);

	}

	/**
	 * Un boolean qui permet de savoir si le jeu est en mode créatif ou non
	 * 
	 * @return True si le jeu est en mode créatif sinon retourne false.
	 */
	public boolean isCreatif() {
		return modeCreatif;
	}

	/**
	 * Set la boolean modeCreatif signifiant l'état du Jeu.
	 * 
	 * @param b
	 *            boolean de l'état désiré
	 */
	public boolean setCreatif(boolean b) {

		if (b) {
			compAnim.getCurrentNiv().reset();
			indexNivSaved = compAnim.indexOfCurrent() - 1;
			nivSaved = new Niveau(compAnim.getCurrentNiv());
			currentNivCreatif = new Niveau(compAnim.getCurrentNiv());
			compAnim.setCurrentNiv(currentNivCreatif);

			outils.setValeurSliders(currentNivCreatif);
			compAnim.leverEvenSelection();

		} else {
			if (!nivSaved.equals(currentNivCreatif)) {
				int confirmed = JOptionPane.showConfirmDialog(null, "Voulez vous sauvegardez le niveau ?", "Validation",
						JOptionPane.YES_NO_CANCEL_OPTION);

				if (confirmed == JOptionPane.YES_OPTION) {
					// demander si l'utilisateur veut remplacer le niveau ou en ajouter un nouveau
					Object[] options = { "Sauvegarder Un Nouveau Niveau", "Remplacer Le Niveau Courant" };
					int n = JOptionPane.showOptionDialog(this, "Voulez vous sauvegarder un nouveau niveau?",
							"Validation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
							options[0]);

					if (n == JOptionPane.YES_OPTION) {
						// sauvegarder un nouveau niveau
						compAnim.addNiveau(currentNivCreatif);
						compAnim.setCurrentNivFromList();
						lblNiv.setMax(compAnim.getLength());
						lblNiv.setCurrent(compAnim.indexOfCurrent());
					} else {
						// remplacer le niveau courant
						compAnim.replace(indexNivSaved, currentNivCreatif, "Niveau remplacer", "Joe Blo");
						compAnim.setCurrentNivFromList();
						lblNiv.setCurrent(compAnim.indexOfCurrent());

					}

				} else if (confirmed == JOptionPane.CANCEL_OPTION || confirmed == JOptionPane.CLOSED_OPTION) {
					// L'utilisateur reste sur le mode creatif sans sauvegarder le niveau
					b = true;
				} else {
					// L'utilisateur ne desire pas sauvegarder son niveau alors retrouver celui
					// avant les changements.
					compAnim.setCurrentNivFromList();
				}
			} else {
				// L'utilisateur n'a pas changer des elements du niveau alors retrouver le
				// niveau avant le mode creatif
				compAnim.setCurrentNivFromList();
			}

		}
		modeCreatif = b;
		if (!b) {
			Vecteur vide = new Vecteur(0, 0);
			updateLabels(vide, vide, vide);
		}
		return modeCreatif;
	}

	/**
	 * Auteur Duy Méthode pour donner la valeur de la position, la vitesse et
	 * l'accélération de la balle vers le label
	 * 
	 * @param pos
	 *            changer le label de la position
	 * @param vit
	 *            changer le label de la vitesse
	 * @param acc
	 *            changer le label de l'accéleration
	 */
	public void updateLabels(Vecteur pos, Vecteur vit, Vecteur acc) {
		if (labelPosition != null && labelVitesse != null && labelAcceleration != null) {
			labelPosition.setXY(pos);
			labelVitesse.setXY(vit);
			labelAcceleration.setXY(acc);
		}
	}

	/**
	 * Méthode qui sauvegarder les niveaux courants dans un fichier.
	 */
	public void ecrireFichierBinaire() {
		compAnim.sauvegarde();
	}

	/**
	 * Méthode pour ajouter un plan dans le niveau
	 */
	public void ajouterPlan() {
		currentNivCreatif.addPlan(new Vecteur(50, 50), new Vecteur(100, 50));
	}

	/**
	 * Méthode pour ajouter un Rectangle dans le niveau
	 */
	public void ajouterRectangle() {
		Rectangle rect = new Rectangle(new Vecteur(70, 50), 50, 50);
		currentNivCreatif.addRectangle(rect);

	}

	/**
	 * Méthode pour ajouter une triangle dans le niveau
	 */
	public void ajouterTriangle() {
		Triangle tri = new Triangle(new Vecteur(20, 20), currentNivCreatif);
	}

	public Niveau getCurrentNiveau() {
		return compAnim.getCurrentNiv();
	}

	public void setLblNiveau() {
		lblNiv.setCurrent(compAnim.indexOfCurrent());
	}

	public void setPlacable(boolean b, SuperObjets o) {

		Rectangle rect = null;
		Plans plan = null;
		if (o != null) {
			o.setPeutPlacer(b);
			if (o.getClass() == Rectangle.class) {
				
				rect = (Rectangle) o;
			
			} else if (o.getClass() == Plans.class) {
				
				plan = (Plans) o;
				System.out.println(plan.peutPlacerPortail());
				
			}
		}
	}

	public void supprimerObj(SuperObjets elementSelec) {
		compAnim.supprimerObj(elementSelec);

	}
}
