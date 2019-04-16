package Boutons;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * 
 * @author Duy Créer le bouton personnalisé pour le panel outil
 */
public class BoutonObjet extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3694459572861188949L;

	private String type;
	private boolean selected = false;
	private boolean disabled = false;
	private final int OFFSET = 3;
	private double widthInside, heightInside;
	private Image icon = null;

	public BoutonObjet(double width, double height, String type) {

		this.type = type;
		this.widthInside = width - OFFSET;
		this.heightInside = height - OFFSET;
		loadImages();
		// MOUSE EVENTS
		if (!type.equals("triangle")) {
			addMouseListener(new MouseAdapter() {
				@Override

				public void mouseEntered(MouseEvent arg0) {
					if (!disabled) {
						selected = true;
						repaint();
					}
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					if (!disabled) {
						selected = false;
						repaint();

					}
				}

				@Override
				public void mousePressed(MouseEvent e) {
					if (!disabled) {
						selected = false;
						repaint();
					}

				}

				@Override
				public void mouseReleased(MouseEvent e) {
					if (!disabled) {
						selected = true;
						repaint();
					}

				}
			});
		}
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Dessiner le carre en pointille
		Stroke originalStroke = g2d.getStroke();
		float dash1[] = { 4.0f };
		BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
		g2d.setStroke(dashed);
		g2d.setColor(Color.BLACK);
		Rectangle2D.Double dashedRect = new Rectangle2D.Double(0, OFFSET, widthInside - 1, heightInside - 1);
		g2d.draw(dashedRect);
		g2d.setStroke(originalStroke);
		// Dessiner le main rectangle
		int y;
		int x;
		if (!disabled) {
			if (selected) {
				x = OFFSET;
				y = 0;
			} else {
				x = 0;
				y = OFFSET;
			}
			g2d.setColor(new Color(117, 169, 249));
		} else {
			g2d.setColor(Color.GRAY);
			x = 0;
			y = OFFSET;
		}
		Rectangle2D.Double rect = new Rectangle2D.Double(x, y, widthInside, heightInside);

		g2d.fill(rect);
		if (type == "plan") {
			g2d.setStroke(new BasicStroke(2.0f));
			g2d.setColor(Color.WHITE);
			g2d.drawLine(x + 10, y + 10, (int) (widthInside - 10 + x), (int) (heightInside - 10 + y));
		} else {
			g2d.drawImage(icon, x + 10, y + 10, (int) widthInside - 20, (int) heightInside - 20, null);
		}

		// Dessiner le "En construction"

		if (type.equals("triangle")) {
			int heightRect = 15;
			int xInit = -50;
			Color colBanner = new Color(255, 235, 24);
			Rectangle2D.Double enConstruction = new Rectangle2D.Double(xInit, heightInside / 2 - heightRect / 2,
					widthInside * 2, heightRect);

			g2d.setColor(colBanner);
			AffineTransform mat = new AffineTransform();
			 mat.rotate(-Math.PI / 4, widthInside / 2, heightInside / 2);
			g2d.fill(mat.createTransformedShape(enConstruction));
			Area r = new Area((mat.createTransformedShape(enConstruction)));

			// Dessiner les lignes noires
			int nbLignes = 10;
			int espacementLig = 25;
			for (int i = 0; i < nbLignes; i++) {

				int widthLig = 10;
				int xLig = xInit + espacementLig * (i);

				// creer la matrice pour rotate chaque ligne
				AffineTransform mat2 = new AffineTransform();
				mat2.rotate(Math.PI / 6, xLig + widthLig / 2, heightInside / 2);
				Shape ligne = mat.createTransformedShape(mat2.createTransformedShape(new Rectangle2D.Double(xLig,
						heightInside / 2 - heightRect / 2 - 10, widthLig, heightRect * 2)));
				Area l = new Area(ligne);
				l.intersect(r);

				g2d.setColor(Color.BLACK);
				g2d.fill(l);
			}

			// dessiner le rect en construction
			int widthSign = 27;

			RoundRectangle2D.Double sign = new RoundRectangle2D.Double(widthInside / 2 - widthSign / 2,
					heightInside / 2 - widthSign / 2, widthSign, widthSign, 5, 5);
			int offSet = 3;
			RoundRectangle2D.Double signInside = new RoundRectangle2D.Double(widthInside / 2 - widthSign / 2 + offSet,
					heightInside / 2 - widthSign / 2 + offSet, widthSign - offSet * 2, widthSign - offSet * 2, 3, 3);

			AffineTransform signRotate = new AffineTransform();
			signRotate.rotate(Math.PI / 4, widthInside / 2, heightInside / 2);
			Shape signShape = signRotate.createTransformedShape(sign);
			Shape signInsideShape = signRotate.createTransformedShape(signInside);
			g2d.setColor(colBanner);
			g2d.fill(signShape);
			g2d.setStroke(new BasicStroke(0.7f));
			g2d.setColor(Color.black);

			g2d.draw(signInsideShape);

			// dessiner un point exclamation
			double largExcl = 2.5;
			double heightExcl = 11;
			Rectangle2D.Double exclamation = new Rectangle2D.Double(widthInside / 2 - largExcl / 2,
					heightInside / 2 - heightExcl / 2 - 2, largExcl, heightExcl);
			double diam = 3;
			Ellipse2D.Double cercle = new Ellipse2D.Double(widthInside / 2 - diam / 2,
					heightInside / 2 + heightExcl / 2, diam, diam);
			g2d.fill(exclamation);
			g2d.fill(cercle);
		}
	}

	/**
	 * Méthode pour charger les Images des boutons.
	 */
	private void loadImages() {
		URL urlIcon = null;
		if (type == "canon") {
			urlIcon = getClass().getClassLoader().getResource("canon.png");
		}
		if (type == "rectangle") {
			urlIcon = getClass().getClassLoader().getResource("rectangle.png");
		}
		if (type == "triangle") {
			urlIcon = getClass().getClassLoader().getResource("triangle.png");
		}
		if (type == "ptArrive") {
			urlIcon = getClass().getClassLoader().getResource("ptArrive.png");
		}
		if (urlIcon != null) {
			try {
				icon = ImageIO.read(urlIcon);
			} catch (IOException e) {
				System.out.println("Erreur pendant la lecture de l'image");
			}
		}
	}
}
