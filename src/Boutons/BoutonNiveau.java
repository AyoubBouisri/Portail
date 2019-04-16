package Boutons;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import composants.Jeu;

/**
 * 
 * @author Duy Classe qui gère les méthode pour les boutons niveau.
 */
public class BoutonNiveau extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4877475411648764238L;
	private double height;
	private double width;
	private final int ARCH = 20;
	private final double WIDTH_BTN = 40;
	private Image icon = null;
	private int type; // 0 = gauche, 1 = droite
	private final int HAUT_IMG = 30;
	private final int LARG_IMG = 50;
	private final int MARGE = 1;
	private Color couleur;
	private Color backSelected;
	private Color backNonSelected;

	/**
	 * Creer un bouton qui permet de changer de niveau
	 * 
	 * @param x
	 *            position x initiale du bouton
	 * @param y
	 *            position y initiale du bouton
	 * @param w
	 *            la largeur du bouton
	 * @param h
	 *            la hauteur du bouton
	 * @param selected
	 *            couleur lorsque le curseur est sur le bouton
	 * @param nonSelected
	 *            couleur lorsque le curseur n'est pas sur le bouton
	 * @param type
	 *            le type de bouton : 1 = gauche, 2 = droite
	 * @param parent
	 *            Le jeu ou est place le bouton
	 */
	public BoutonNiveau(double x, double y, double w, double h, Color selected, Color nonSelected, int type,
			Jeu parent) {

		this.width = w;
		this.height = h;
		this.type = type;
		this.couleur = nonSelected;
		this.backSelected = selected;
		this.backNonSelected = nonSelected;
		setBackground(null);
		loadImages();

		this.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent arg0) {
				if (!parent.isCreatif()) {
					couleur = backSelected;
					repaint();
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (!parent.isCreatif()) {
					couleur = backNonSelected;
					repaint();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (!parent.isCreatif()) {
					couleur = backNonSelected;
					repaint();
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (!parent.isCreatif()) {
					couleur = backSelected;
					repaint();
				}
			}
		});
		this.setBounds((int) x, (int) y, (int) w, (int) h);

	}

	/**
	 * Préparer l'image qui correspond au type du bouton.
	 */
	private void loadImages() {
		URL urlIcon = null;
		if (type == 0) {
			urlIcon = getClass().getClassLoader().getResource("flecheGauche.png");
		} else if (type == 1) {
			urlIcon = getClass().getClassLoader().getResource("flecheDroite.png");
		}

		if (urlIcon != null) {
			try {
				icon = ImageIO.read(urlIcon);
			} catch (IOException e) {
				System.out.println("Erreur pendant la lecture de l'image");
			}
		}

	}
	/**
	 * Méthode pour dessiner les boutons pour changer le niveau
	 */
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// dessiner le bouton gauche
		RoundRectangle2D.Double btnGauche = new RoundRectangle2D.Double(1, 1, WIDTH_BTN - 1, height - 2, ARCH, ARCH);

		g2d.setColor(couleur);
		g2d.fill(btnGauche);

		// dessiner l'icone du bouton
		if (icon != null) {
			int x = 0;
			int y = 0;
			if (type == 0) {
				x = (int) (width / 2 - LARG_IMG / 2 + MARGE * 2);
				y = (int) (width / 2 - HAUT_IMG / 2);

			}
			if (type == 1) {
				x = (int) (width / 2 - LARG_IMG / 2 - MARGE * 3);
				y = (int) (width / 2 - HAUT_IMG / 2);
			}

			g2d.drawImage(icon, x, y, LARG_IMG, HAUT_IMG, null);
		}

	}
}
