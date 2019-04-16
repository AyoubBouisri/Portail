package composants;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Boutons.BoutonOutils;
import niveau.Niveau;
import objets.SuperObjets;

/**
 * Creation d'un panel glissant qui contient un boutonOutils et plusieurs panels
 * contenant des options pour modifier le niveau courant.
 * 
 * @author Ayoub
 *
 */
public class PanelOutils extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;

	private final int WIDTH_BOUTON = 30;
	private final int MARGE_COMP = 15;
	private final int tempsDuSleep = 10;

	private int MARGE = 15;
	private int height;
	private boolean out = false;
	private boolean enCoursDAnimation = false;
	private int x;
	private int currentWidth;
	private int widthMax;
	private int widthMin = WIDTH_BOUTON;
	private int posXMax;
	private int posXMin;
	private double rateAnimationInit = 5;
	private double rateAnimation = rateAnimationInit;
	private double accAnim = 1.5;

	private Jeu parent;

	private BoutonsCreatifs boutonsCreatifs;

	private double xCompBoutons;
	private double yCompBoutons;

	private double widthCompBoutons;
	private double heightCompBoutons = 220;

	private ComposantParamNiv paramNiv;

	private double xParamNiv;
	private double yParamNiv;
	private double widthParamNiv;
	private double heightParamNiv = 300;
	private ComposantObjets compObj;
	private double xCompObj;
	private double yCompObj;
	private double widthCompObj;
	private double heightCompObj = 80;
	private JCheckBox btnPortailPlacable;

	/**
	 * 
	 * @param xMax
	 *            La position x en pixels qui sera atteinte lorsque le panel sera
	 *            ouvert
	 * @param widthJeu
	 *            La largeur du panel qui contiendra cet objet
	 * @param height
	 *            Hauteur du panelOutils en pixel
	 */
	public PanelOutils(int xMax, int widthJeu, int height, Jeu parent) {
		this.height = height;
		this.posXMax = xMax;
		this.posXMin = widthJeu - widthMin - MARGE;
		this.widthMax = widthJeu - posXMax;
		this.widthCompBoutons = widthMax - WIDTH_BOUTON - MARGE_COMP * 3;
		this.widthParamNiv = widthCompBoutons;
		this.widthCompObj = widthParamNiv;
		this.x = posXMin;

		this.currentWidth = widthMax;
		this.setBounds(x, 0, currentWidth, height);
		setBackground(new Color(235, 235, 235));
		setLayout(null);

		this.parent = parent;
		BoutonOutils btnOutils = new BoutonOutils(WIDTH_BOUTON, height);

		btnOutils.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				btnOutils.switchSide();

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				preview();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				closePreview();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (!enCoursDAnimation) {

					out = parent.setCreatif(!out);

					demarrer();
				}

			}
		});

		btnOutils.setLayout(null);
		this.add(btnOutils);

		// Creer les composants dans le panel outils

		// 1. Composant contenant tout les boutons
		boutonsCreatifs = new BoutonsCreatifs(widthCompBoutons, heightCompBoutons, this);
		setPosCompBoutons();
		boutonsCreatifs.setLayout(null);
		this.add(boutonsCreatifs);

		// 2. Composant contenant les parametres de l'objet selectionner

		// 3. Composant contenant les parametres du niveau

		paramNiv = new ComposantParamNiv(widthParamNiv, heightParamNiv, this);
		setPosParamNiv();
		paramNiv.setLayout(null);
		this.add(paramNiv);
		compObj = new ComposantObjets(widthCompObj, heightCompObj, this);
		setPosCompObj();
		this.add(compObj);
		
		
		
	}

	/**
	 * Animation d'ouverture et fermeture du panel d'Outils
	 */
	public void slide() {
		if (out) {
			// animation de sortie
			x -= rateAnimation;
			rateAnimation += accAnim;

			if (x <= posXMax) {
				x = posXMax;
				Jeu.afficherVecteur = true;
				arreter();
				rateAnimation = rateAnimationInit;
				// changerla fleche de cote

			}
		} else {
			// animation de fermeture
			x += rateAnimation;
			rateAnimation += accAnim;
			Jeu.afficherVecteur = Jeu.vecteurBefore;
			if (x >= posXMin) {
				x = posXMin;
				arreter();
				rateAnimation = rateAnimationInit;
				// changerla fleche de cote

			}
		}

		this.setBounds(x, 0, currentWidth, height);
		setPosCompBoutons();
	}

	@Override
	public void run() {
		while (enCoursDAnimation) {

			try {
				slide();
			} catch (Exception e) {

				e.printStackTrace();
			}

			repaint();
			try {
				Thread.sleep(tempsDuSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Glisse le panel legerement vers la gauche. Peut seulement etre appelee
	 * lorsque le panel est ferme.
	 */
	public void preview() {
		if (!out) {
			int move = 5;
			x = x - move;

			this.setBounds(x, 0, currentWidth, height);
		}
	}

	/**
	 * remet le panel a sa position originelle. Peut seulement etre appelee lorsque
	 * le panel est ferme.
	 */
	public void closePreview() {
		if (!out && !enCoursDAnimation) {
			x = posXMin;

			this.setBounds(x, 0, currentWidth, height);
		}
	}

	/**
	 * Creer un nouveau thread et le demarrer pour demarrer l'animation
	 */
	public void demarrer() {
		if (!enCoursDAnimation) {
			Thread proc = new Thread(this);
			proc.start();
			enCoursDAnimation = true;

		}
	}

	/**
	 * Arreter l'animation
	 */

	public void arreter() {
		enCoursDAnimation = false;
	}

	/**
	 * 
	 * @return retourne la largeur du bouton.
	 */
	public int getBoutonWidth() {
		return WIDTH_BOUTON;
	}

	/**
	 * 
	 * @return Vrai si le panel est sorti sinon faux.
	 */
	public boolean isOut() {
		return out;
	}

	/**
	 * Methode qui retourne le Jeu dans lequel se trouve le panel.
	 */
	public Jeu getParent() {
		return parent;
	}

	/**
	 * Methode qui set la position de tout les regroupements d'entrees.
	 */
	public void setPosComposants() {
		setPosCompBoutons();
		setPosParamNiv();
	}

	/**
	 * Methode qui repositionne le composants qui rassemble les boutons
	 */
	public void setPosCompBoutons() {
		xCompBoutons = WIDTH_BOUTON + MARGE_COMP;
		yCompBoutons = MARGE_COMP;

		boutonsCreatifs.setBounds((int) xCompBoutons, (int) yCompBoutons, (int) widthCompBoutons,
				(int) heightCompBoutons);

	}

	/**
	 * Methode qui repositionne le composant qui gere les parametres du niveau.
	 */
	public void setPosParamNiv() {
		xParamNiv = WIDTH_BOUTON + MARGE_COMP;
		yParamNiv = 350;

		paramNiv.setBounds((int) xParamNiv, (int) yParamNiv, (int) widthParamNiv, (int) heightParamNiv);
	}
	/**
	 * Repositionne le composants qui contient les parametres de l'objet selectionne.
	 */
	public void setPosCompObj() {
		xCompObj = WIDTH_BOUTON + MARGE_COMP;
		yCompObj = yCompBoutons + widthCompBoutons;

		compObj.setBounds((int) xCompObj, (int) yCompObj, (int) widthCompObj, (int) heightCompObj);
	}

	public void setPosBtn() {

		btnPortailPlacable.setBounds(45, 250, 229, 23);
	}

	/**
	 * Methode qui permet d'initialiser les valeurs des sliders d'apres un niveau
	 * initial
	 * 
	 * @param niv
	 *            Le niveau courant
	 */
	public void setValeurSliders(Niveau niv) {
		paramNiv.setValeursSliders(niv);
	}

	public void setCompObj(SuperObjets compSelec) {
		compObj.setElements(compSelec);
		
	}

	public void supprimerObj(SuperObjets elementSelec) {
		parent.supprimerObj(elementSelec);
		
	}
}
