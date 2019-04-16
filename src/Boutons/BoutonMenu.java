package Boutons;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

import composants.Jeu;

/**
 * 
 * @author Duy Créer la classe bouton pour personnalisé les boutons de menu
 */
public class BoutonMenu extends JPanel implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -246636394165629470L;

	private final int TEMPSDUSLEEP = 1;
	private final double MIN_SIZE = 0.5;
	private final double MAX_SIZE = 2.5;
	private final double GROWING_RATE = 0.002;

	private int height;
	private int longueurBtn;
	private int fontSize;
	private Color couleur;
	private Color backSelected;
	private Color backNonSelected;
	private String lab;
	private boolean enCoursDAnimation = false;
	private boolean growing = true;
	private double strokeSize = MIN_SIZE;
	private double longString;
	private double hautString;
	private Jeu parent = null;

	/**
	 * La méthode pour créer le bouton dans le menu
	 * 
	 * @param posY
	 *            Position y en pixels
	 * @param width
	 *            largeur du bouton
	 * @param lab
	 *            Le String affiche sur le bouton
	 * @param selected
	 *            Couleur lorsque le bouton est selectionne
	 * @param nonSelected
	 *            Couleur lorsque le bouton n'est pas selectionne
	 * @param fontSize
	 *            la grandeur du texte en pixels
	 */
	public BoutonMenu(int height, int width, String lab, Color selected, Color nonSelected, int fontSize) {
		this.setLayout(null);
		this.longueurBtn = width;
		this.height = height;
		this.lab = lab;
		this.couleur = nonSelected;
		this.backSelected = selected;
		this.backNonSelected = nonSelected;
		this.fontSize = fontSize;
		setBackground(null);
		/*
		 * JLabel lab = new JLabel(string); lab.setLocation(10, height); this.add(lab);
		 */

		this.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent arg0) {
				if (parent != null) {
					if (!parent.isCreatif()) {
						couleur = backSelected;
						demarrer();
						repaint();
					}
				} else {
					couleur = backSelected;
					demarrer();
					repaint();
				}

			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (parent != null) {
					if (!parent.isCreatif()) {

						couleur = backNonSelected;
						arreter();

						repaint();
					}
				} else {
					couleur = backNonSelected;
					arreter();

					repaint();
				}

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (parent != null) {
					if (!parent.isCreatif()) {
						couleur = backNonSelected;
					}
				} else {
					couleur = backNonSelected;
				}

			}
		});

	}

	@Override
	/**
	 * Méthode pour dessiner le bouton BoutonMenu
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Font font = new Font("Ariel", Font.BOLD, fontSize);
		RoundRectangle2D.Double rect;
		if (backNonSelected != null) {
			rect = new RoundRectangle2D.Double(0, 0, longueurBtn - 1, height - 1, 0, 0);
			g2d.setColor(couleur);
			g2d.fill(rect);

			g2d.setStroke(new BasicStroke((float) strokeSize));

		} else {

			rect = new RoundRectangle2D.Double(0, 0, longueurBtn - 1, height - 1, 20, 20);
			if (couleur != null) {
				g2d.setColor(couleur);
				g2d.fill(rect);

			}
			g2d.setStroke(new BasicStroke((float) 4));

		}
		g2d.setColor(Color.white);
		g2d.draw(rect);
		g2d.setColor(new Color(255, 255, 255));

		g2d.setFont(font);
		// TROUVER LA LONGUEUR DU STRING
		longString = g2d.getFontMetrics().stringWidth(lab);
		// TROUVER LA HAUTEUR DU STRING
		hautString = g2d.getFontMetrics().getMaxAscent();

		g2d.drawString(lab, (int) (longueurBtn / 2 - longString / 2), (int) hautString);

	}

	/**
	 * Méthode qui update la taille du stroke du bouton d'après le temps.
	 */
	public void update() {
		if (growing) {
			strokeSize += GROWING_RATE;
			if (strokeSize > MAX_SIZE) {
				strokeSize = MAX_SIZE;
				growing = false;
			}
		} else {
			strokeSize -= GROWING_RATE;
			if (strokeSize <= MIN_SIZE) {
				strokeSize = MIN_SIZE;
				growing = true;
			}
		}
	}

	/**
	 * Animation de la stroke lors de la selection
	 */
	@Override
	public void run() {
		while (enCoursDAnimation) {

			update();

			repaint();
			try {
				Thread.sleep(TEMPSDUSLEEP);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Créer un nouveau thread et le demarrer pour demarrer l'animation
	 */
	public void demarrer() {
		if (!enCoursDAnimation) {
			Thread proc = new Thread(this);
			proc.start();
			enCoursDAnimation = true;

		}
	}

	/**
	 * Arrêter l'animation
	 */

	public void arreter() {
		enCoursDAnimation = false;
		strokeSize = MIN_SIZE;
		growing = true;
	}

	/**
	 * Assigne un composant jeu au bouton, generalement le composant ou se trouve le
	 * bouton. Pour savoir si le bouton est cacher par le panel outils.
	 * 
	 * @param p
	 *            Le panel Jeu ou est contenu le bouton.
	 */

	public void setParent(Jeu p) {
		this.parent = p;
	}

}
