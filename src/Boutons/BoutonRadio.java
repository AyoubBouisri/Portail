package Boutons;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import composants.Jeu;
import composants.PanelOutils;

/**
 * Classe contenant le blue print d'un bouton radio customiser.
 * 
 * @author Ayoub
 *
 */
public class BoutonRadio extends JPanel {

	private static final long serialVersionUID = 7026956105637925821L;
	private final int MARGE_CASE = 20;
	private final int MARGE_CASE_TEXT = 20;
	private boolean state = Jeu.afficherVecteur;
	private CaseCochable caseCochable;
	private double h;
	private int wCase;
	private String titre;

	private PanelOutils topPanel = null;

	/**
	 * Constructeur du bouton radio
	 * 
	 * @param w
	 *            Largeur du bouton en pixels
	 * @param h
	 *            Hauteur du bouton en pixels
	 * @param titre
	 *            String qui represente le texte qui sera afficher sur le bouton
	 *            radio
	 * @param parent
	 *            Le panel qui contiendra le bouton
	 */
	public BoutonRadio(double w, double h, String titre, Jeu parent) {

		this.setBackground(null);
		this.h = h;
		this.titre = titre;
		wCase = (int) (h - 15);
		caseCochable = new CaseCochable(wCase, this);
		caseCochable.setBounds(MARGE_CASE, (int) (h / 2 - wCase / 2), wCase, wCase);
		this.add(caseCochable);

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// draw string
		Font font = new Font("Ariel", Font.PLAIN, 18);
		g2d.setFont(font);
		g2d.setColor(Color.DARK_GRAY);
		g2d.drawString(titre, MARGE_CASE + (int) wCase + MARGE_CASE_TEXT, (int) (h / 2 + wCase / 3.5));

	}// fin paintComponent

	/**
	 * Switch le state du bouton radio. Si cocher alors decocher Si decocher alors
	 * cocher.
	 */
	public void switchState() {
		state = !state;
		setVecteurs(state);

	}

	/**
	 * 
	 * @return Le statut du bouton radio
	 */
	public boolean getState() {

		return state;
	}

	/**
	 * Change la valeur boolean de "afficherVecteur" du Jeu qui contient le bouton
	 * radio.
	 * 
	 * @param temp
	 */
	public void setVecteurs(boolean temp) {
		Jeu.afficherVecteur = temp;
		Jeu.vecteurBefore = temp;

	}

	/**
	 * Creer la reference pour les panels qui sont dessiner au dessus du bouton
	 * radio.
	 * 
	 * @param topPan
	 *            Panel qui cache le bouton radio.
	 */
	public void setTopPanel(PanelOutils topPan) {
		topPanel = topPan;

	}

	/**
	 * 
	 * @return Le panel qui se trouvera au dessus du bouton radio.
	 */
	public PanelOutils getTopPanel() {
		return topPanel;
	}
}
