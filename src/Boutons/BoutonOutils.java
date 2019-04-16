package Boutons;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * Creation d'un bouton glissant.
 * 
 * @author Ayoub
 *
 */
public class BoutonOutils extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2356718129509683895L;
	private boolean side = false; // false == fleche vers la gauche


	
	/**
	 * 
	 * @param width Largeur du bouton outils en pixels
	 * @param height Hauteur du bouton outils en pixels
	 */
	public BoutonOutils(int width, int height) {
		setBackground(new Color(246,170,35));
		setBounds(0, 0, width, height);
	

	}
	
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
	}

	/**
	 * Changer le coter de la fleche
	 */
	public void switchSide() {
		side = !side;

		repaint();
	}
	/**
	 * Auteur Duy
	 * Méthode qui renvoie l'état du boolean side
	 * @return l'état du boolean side
	 */
	public boolean getSide(){
		return side;
	}
}
