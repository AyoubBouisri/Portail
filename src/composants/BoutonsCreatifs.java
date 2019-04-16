package composants;

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

import Boutons.BoutonObjet;

/**
 * 
 * @author Duy un panel qui contient tout les boutons objets
 *
 */
public class BoutonsCreatifs extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7454506699035594576L;
	private final int MARGE = 10;
	private double width;
	private double height;
	private Jeu grandParent;
	private String titre = "AJOUTER COMPOSANTS";
	private final int HEIGHT_TITRE = 10;
	private Color colorBg = new Color(240, 240, 240);

	private BoutonObjet   btnRectangle, btnTriangle, btnPlan;
	private double widthBtnsSmall;
	private double widthBtnsBig;

	public BoutonsCreatifs(double width, double height, PanelOutils parent) {

		this.width = width;
		this.height = height;

		grandParent = parent.getParent();

		widthBtnsSmall = (width - MARGE * 5);
		widthBtnsBig = (width - MARGE * 2);
		if (widthBtnsSmall <= 0) {
			widthBtnsSmall = 0;

		} else {
			widthBtnsSmall /= 2;
		}

		this.setBackground(null);

		// Creer les boutons permettant de creer des objets dans le composant animation

		btnPlan = new BoutonObjet(widthBtnsBig, widthBtnsSmall, "plan");
		btnPlan.setBounds((int) (MARGE), (int) (MARGE + HEIGHT_TITRE), (int) widthBtnsBig, (int) widthBtnsSmall);
		this.add(btnPlan);

		btnPlan.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				grandParent.ajouterPlan();
			}
		});

		btnRectangle = new BoutonObjet(widthBtnsSmall, widthBtnsSmall, "rectangle");
		btnRectangle.setBounds((int) MARGE, (int) (MARGE + widthBtnsSmall + HEIGHT_TITRE), (int) widthBtnsSmall,
				(int) widthBtnsSmall);
		this.add(btnRectangle);

		btnRectangle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				grandParent.ajouterRectangle();
			}
		});

		btnTriangle = new BoutonObjet(widthBtnsSmall, widthBtnsSmall, "triangle");
		btnTriangle.setBounds((int) (MARGE * 4 + widthBtnsSmall), (int) (MARGE + widthBtnsSmall + HEIGHT_TITRE),
				(int) widthBtnsSmall, (int) widthBtnsSmall);
		this.add(btnTriangle);

		
	}

	@Override
	/**
	 * Méthode pour dessiner les boutons des objets
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Dessiner le contour des composants

		RoundRectangle2D.Double rect = new RoundRectangle2D.Double(0, 0, width - 1, height - 1, 0, 0);
		g2d.setStroke(new BasicStroke(1.5f));
		g2d.setColor(colorBg);
		// g2d.fill(rect);
		g2d.setColor(new Color(95, 95, 95));
		g2d.draw(rect);
		
		
		// Dessiner le titre 
		Font font = new Font("Ariel", Font.PLAIN, 15);
		g2d.setFont(font);
		int widthTitre = g2d.getFontMetrics().stringWidth(titre);
		int heighTitre = g2d.getFontMetrics().getAscent();
		
		g2d.drawString(titre,(int) (width/2 - widthTitre/2) , heighTitre + 2);
	}
}
