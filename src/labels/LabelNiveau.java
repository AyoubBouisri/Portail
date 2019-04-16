package labels;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

/**
 * 
 * @author Duy
 * Classe afin de créer un label pour afficher les niveaux
 */

public class LabelNiveau extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9100123682171405789L;
	private double height;
	private double width;

	private int arch = 20;
	private int niveau;
	int fontSize = 20;
	private int nivMax;

	
	
	
	/**
	 *  Méthode pour créer un label qui indique le niveau current
	 * @param posX la position initiale du label en X
	 * @param posY la position initiale du label en Y
	 * @param width la largeur du label
	 * @param height la hauteur du label
	 * @param niveau le niveau current du label
	 */
	public LabelNiveau(double x, double y, double w, double h, int niv,int nivMax){

		this.width = w;
		this.height = h;
		this.niveau = niv ;
		this.nivMax = nivMax;

		this.setBounds((int)x,(int)y,(int)w,(int)h);
		setBackground(null);
	}
	
	


	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// dessiner le background et la shape de la label
		RoundRectangle2D.Double label = new RoundRectangle2D.Double(0, 0, width-1, height-1, arch, arch);
		
		g2d.setStroke(new BasicStroke(1.5f));
		
		g2d.setColor(new Color(249, 249, 249));
		g2d.fill(label);
		g2d.setColor(Color.gray);
		g2d.draw(label);
		
		

		
		
		Font font = new Font("Ariel", Font.BOLD, fontSize);
		g2d.setFont(font);
		
		String strNiv = "Niveau " + niveau + " / " + nivMax;
		// TROUVER LA LONGUEUR DU STRING
		double longString = g2d.getFontMetrics().stringWidth(strNiv);
		// TROUVER LA HAUTEUR DU STRING
		double hautString = g2d.getFontMetrics().getMaxAscent();
		g2d.drawString(strNiv,(int) (width / 2 - longString / 2), (int) hautString + 5);
		
	
	}
	public void setCurrent(int current) {
		this.niveau = current;
		repaint();
	}
	public void setMax(int max) {
		this.nivMax = max;
		repaint();
	}
}
