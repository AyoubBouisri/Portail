package Boutons;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * 
 * Classe qui dessine les boutons qui gere l'animation du composant. Bouton
 * play. Bouton pause. Bouton nextPas.
 * 
 * @author Ayoub
 *
 */
public class BoutonAnim extends JPanel {

	private static final long serialVersionUID = 933281538082440105L;
	private final int MARGE_INIT = 2;
	private final Color AVAILABLE = new Color(246, 170, 35);
	private final Color DISABLED = Color.LIGHT_GRAY;
	private int hautImg = 30;
	private int marge = MARGE_INIT;
	private int typeBouton;

	private double width;

	private double diamCercle;

	private boolean on = true;

	private Image icon = null;

	private int largImg = hautImg;

	/**
	 * Creer un bouton qui gere l'animation
	 * 
	 * @param widthAnimBtn
	 *            La largeur totale du bouton
	 * @param type
	 *            Le type du bouton (0==play,1==pause,2==nextPas)
	 */
	public BoutonAnim(double widthAnimBtn, int type) {

		this.width = widthAnimBtn;
		diamCercle = width - marge * 2;
		this.typeBouton = type;

		loadImages();
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				if (on) {
					marge = 0;
					repaint();
				}
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				if (on) {
					marge = MARGE_INIT;
					repaint();

				}
			}
		});

	}

	/**
	 * preparer l'image qui correspond au type du bouton.
	 */
	private void loadImages() {
		URL urlIcon = null;
		if (typeBouton == 0) {
			urlIcon = getClass().getClassLoader().getResource("playBouton.png");
		} else if (typeBouton == 1) {
			urlIcon = getClass().getClassLoader().getResource("pauseBouton.png");
		} else if (typeBouton == 2) {
			urlIcon = getClass().getClassLoader().getResource("nextPasBouton.png");
		}

		if (urlIcon != null) {
			try {
				icon = ImageIO.read(urlIcon);
			} catch (IOException e) {
				System.out.println("Erreur pendant la lecture de l'image");
			}
		}

	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Ellipse2D.Double cercleOmbre = new Ellipse2D.Double(MARGE_INIT, MARGE_INIT, diamCercle, diamCercle);
		g2d.setColor(Color.GRAY);
		g2d.fill(cercleOmbre);
		Ellipse2D.Double cercle = new Ellipse2D.Double(marge, marge, diamCercle, diamCercle);
		if (on) {
			g2d.setColor(AVAILABLE);

		} else {
			g2d.setColor(DISABLED);
		}
		g2d.fill(cercle);

		// dessiner l'icone du bouton
		if (icon != null) {

			int x = (int) (width / 2 - largImg / 2 + marge);
			int y = (int) (width / 2 - hautImg / 2 + marge / 2);
			if (typeBouton == 0) {
				x += 1;
				hautImg = largImg + 4;
			} else if (typeBouton == 1) {
				x -= 2;
				largImg = hautImg - 2;
			}
			g2d.drawImage(icon, x, y, largImg, hautImg, null);
		}

	}

	/**
	 * Change l'etat du bouton.
	 * 
	 * @param temp
	 *            Etat voulu du bouton.
	 */
	public void setOn(boolean temp) {
		if (!temp) {
			marge = MARGE_INIT;
		}
		on = temp;
		repaint();
	}

	/**
	 * Retourne vrai si le bouton est active.
	 * 
	 * @return l'etat du bouton.
	 */
	public boolean isOn() {

		return on;
	}
}
