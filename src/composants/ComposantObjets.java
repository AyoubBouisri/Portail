package composants;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

import Boutons.BoutonRadio;
import niveau.Niveau;
import objets.Canon;
import objets.Plans;
import objets.PointArrivee;
import objets.Rectangle;
import objets.SuperObjets;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ComposantObjets extends JPanel {
	private double width;
	private double height;

	private String titre = "SELECTIONNER OBJET";

	private Color colorBg = new Color(240, 240, 240);
	private SuperObjets elementSelec = null;
	private JButton btnSupprimerElement;
	private JCheckBox chckbxPlacable;

	public ComposantObjets(double width, double height, PanelOutils parent) {

		this.width = width;
		this.height = height;

		setLayout(null);

		btnSupprimerElement = new JButton("Supprimer élément");
		btnSupprimerElement.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				parent.supprimerObj(elementSelec);
			}
		});
		btnSupprimerElement.setBounds(10, 48, 211, 23);
		add(btnSupprimerElement);

		chckbxPlacable = new JCheckBox("Portails placables");
		chckbxPlacable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.getParent().setPlacable(chckbxPlacable.isSelected(), elementSelec);
			}
		});

		chckbxPlacable.setHorizontalAlignment(SwingConstants.CENTER);
		chckbxPlacable.setBounds(10, 25, 211, 23);
		add(chckbxPlacable);

	}

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

		// Dessiner le TITRE
		Font font = new Font("Ariel", Font.PLAIN, 15);
		g2d.setFont(font);
		int widthTitre = g2d.getFontMetrics().stringWidth(titre);
		int heighTitre = g2d.getFontMetrics().getAscent();

		g2d.drawString(titre, (int) (width / 2 - widthTitre / 2), heighTitre + 2);

	}

	public void setElements(SuperObjets compSelec) {

		if (compSelec instanceof Plans) {
			titre = "PLAN";
			btnSupprimerElement.setEnabled(true);
			chckbxPlacable.setEnabled(true);
			
				chckbxPlacable.setSelected(((Plans) compSelec).peutPlacerPortail());
			

		} else if (compSelec instanceof Rectangle) {
			titre = "RECTANGLE";
			btnSupprimerElement.setEnabled(true);
			chckbxPlacable.setEnabled(true);
			Rectangle rectTemp = (Rectangle) compSelec;

			Plans plTemp = rectTemp.getPlans()[0];
			
				chckbxPlacable.setSelected(plTemp.peutPlacerPortail());
			
		} else if (compSelec instanceof Canon) {
			titre = "CANON";
			btnSupprimerElement.setEnabled(false);
			chckbxPlacable.setEnabled(false);
			chckbxPlacable.setSelected(false);
		} else if (compSelec instanceof PointArrivee) {
			titre = "POINT D'ARRIVEE";
			btnSupprimerElement.setEnabled(false);
			chckbxPlacable.setEnabled(false);
			chckbxPlacable.setSelected(false);
		} else {
			titre = "SELECTIONNER OBJET";
			btnSupprimerElement.setEnabled(false);
			chckbxPlacable.setEnabled(false);
			chckbxPlacable.setSelected(false);
		}

		elementSelec = compSelec;
		repaint();
	}
}
