package labels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

import geometrie.Vecteur;

/**
 * 
 * @author Ayoub Creer une label personnalisee
 */
public class Label extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2814579858933442750L;
	private final int MARGE = 25;
	private double height;
	private double width;
	private double posX;
	private double posY;
	private String titre;
	private String var;
	private String x, y;
	private int yTitre = 3;

	private int xVar = 25;
	private int yXString;
	private int yYString;
	private int arch = 20;
	

	/**
	 * 
	 * @param x
	 *            Position en x du label
	 * @param y
	 *            Position en y du label
	 * @param w
	 *            La largeur du label
	 * @param h
	 *            La hauteur du label
	 * @param titre
	 *            Le String representant le titre du label
	 * @param var
	 *            La variable du label (Vitesse => m/s ...)
	 */
	public Label(double x, double y, double w, double h, String titre, String var) {
		this.posX = x;
		this.posY = y;
		this.width = w;
		this.height = h;
		this.titre = titre;
		this.var = var;
		this.setBounds((int) x, (int) y, (int) w, (int) h);
		setBackground(null);
		this.x = "0";
		this.y = "0";

	}

	@Override
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
		
		
		
		Font font = new Font("ARIAL",Font.PLAIN,18);
		g2d.setFont(font);
		
		//centrer titre
		
		int widthTitre = g2d.getFontMetrics().stringWidth(titre);
		int heightTitre = g2d.getFontMetrics().getMaxAscent();
		double xTitre = width/2 - widthTitre/2;
		
		// drawString le titre de la label
		g2d.drawString(titre, (int) xTitre, yTitre + heightTitre);
		
		// drawString X  et Y  
		font = new Font("ARIAL",Font.BOLD,25);
		g2d.setFont(font);
		int yXStringH = g2d.getFontMetrics().getMaxAscent();
		
		yXString = yTitre + heightTitre +yXStringH+ MARGE/2;
		yYString = yXString + yXStringH + MARGE ;
		
		g2d.drawString("X ",xVar,yXString);
		g2d.drawString("Y ", xVar, yYString);
		
		//drawString des variables
		font = new Font("ARIAL",Font.PLAIN,20);
		g2d.setFont(font);
		
		int varH = g2d.getFontMetrics().getAscent();
		int varW = g2d.getFontMetrics().stringWidth("m/s2");
		yXString = yTitre + heightTitre +yXStringH+ MARGE/2;
		yYString = yXString + yXStringH + MARGE ;
		
		int xActualVar = (int) (width - xVar) - varW;
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.drawString(var,xActualVar,yXString);
		g2d.drawString(var, xActualVar, yYString);
		
		// Dessiner la mesure au centre de la label
		double wX = g2d.getFontMetrics().stringWidth(x);
		double wY = g2d.getFontMetrics().stringWidth(y);
		int xVar = (int) (width /2 - wX/2);
		
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.drawString(x,xVar,yXString);
		xVar = (int) (width /2 - wY/2);
		g2d.drawString(y, xVar, yYString);
		
		
	}
	
	public void setXY(Vecteur vec) {
		x = "" + round(vec.getX(), 3);
		y = "" + round(vec.getY(), 3);
		repaint();
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
}
