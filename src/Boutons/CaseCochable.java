package Boutons;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

/**
 * Blue print d'une case cochable contenue dans les boutons radio.
 * 
 * @author Ayoub
 *
 */
public class CaseCochable extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5616349123307800212L;
	private BoutonRadio parent;
	private double w;

	private boolean preview = false;

	/**
	 * 
	 * @param w
	 *            Largeur et hauteur de la case cochable contenu dans un bouton
	 *            radio
	 * @param parent
	 *            Le bouton radio qui contiendra la case cochable
	 */
	public CaseCochable(int w, BoutonRadio parent) {
		this.setBackground(null);
		this.w = w;
		this.parent = parent;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				if (!parent.getTopPanel().isOut()) {
					preview = true;
					repaint();
				}

			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (!parent.getTopPanel().isOut()) {

					preview = false;
					repaint();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// Cocher / decocher le bouton
				if (!parent.getTopPanel().isOut()) {

					parent.switchState();

					repaint();
				}
			}
		});

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Ellipse2D.Double contour = new Ellipse2D.Double(0, 0, w - 2, w - 2);
		// RoundRectangle2D.Double rect = new RoundRectangle2D.Double(0, 0, w - 1, w -
		// 1, round, round);
		g2d.setStroke(new BasicStroke(1.2f));
		g2d.setColor(Color.gray);
		// Dessiner le carre

		// dessiner le preview
		if (preview) {
			g2d.setColor(Color.orange);
			// g2d.setStroke(new BasicStroke(1.8f));
		}
		g2d.draw(contour);
		// dessiner le cochage
		if (parent.getState()) {
			double diam = 12;
			double newX = (w - 2) / 2 - diam / 2;
			double newY = (w - 2) / 2 - diam / 2;
			Ellipse2D.Double fill = new Ellipse2D.Double(newX, newY, diam, diam);
			g2d.setColor(Color.orange);
			g2d.fill(fill);
		}
	}
}
