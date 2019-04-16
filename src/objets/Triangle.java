package objets;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

import geometrie.Vecteur;
import niveau.Niveau;
/**
 * 
 * @author Duy
 * Méthode pour créer un triangle
 *
 */
public class Triangle extends SuperObjets implements Serializable{

		/**
	 * 
	 */
	private static final long serialVersionUID = 964231527396316049L;
		Vecteur pt1 = new Vecteur(50,50);
		Vecteur pt2 = new Vecteur(30,30);
		Vecteur pt3 = new Vecteur(70,30);
		
		private boolean selected;
		private Niveau parent;
		private double posX, posY;
		private Vecteur posInit = new Vecteur(posX, posY);
		
		
		int xTriangle[] = {(int)pt1.getX(),(int)pt2.getX(),(int)pt3.getX()};
		int yTriangle[] = {(int)pt1.getY(),(int)pt2.getY(),(int)pt3.getY()};
		
		
	
		public Triangle(Vecteur posInit,Niveau parent) {
		super(posInit);
		
		/*
		Plans plan1 = new Plans(pt1, pt2);
		Plans plan2 = new Plans(pt2, pt3);
		Plans plan3 = new Plans(pt3, pt1);
		*/
		
		this.parent = parent;
		//add plan1 triangle
		parent.addPlan(pt1, pt2);
		
		//add plan2 triangle
		parent.addPlan(pt2, pt3);
		
		//add plan3 triangle
		parent.addPlan(pt3, pt1);
		// TODO Auto-generated constructor stub
		}



		public void dessiner(Graphics2D g2d, AffineTransform matMc) {
			Polygon triangle = new Polygon(xTriangle, yTriangle,3);
			g2d.setColor(Color.WHITE);
			g2d.fill(triangle);
			
		}


		@Override
		public void dessinerModeCreatif(Graphics2D g2d, AffineTransform matMC) {
			// TODO Auto-generated method stub

		}
		public PointArrivee clone() {
			return new PointArrivee(new Vecteur(posX, posY));
		}

		/**
		 * Methode qui donne une valeur a la selection de l'objet
		 * 
		 * @param b
		 *            valeur de la selection de l'objet
		 */
		public void setSelected(boolean b) {
			selected = b;
		}

		/**
		 * Methode qui bouge l'objet d'apres le mouvement de la souris
		 * 
		 * @param souris
		 *            Position courante de la souris
		 * @param startDrag
		 *            Position ou le drag de la souris a commence.
		 */
		public void move(Vecteur souris, Vecteur startDrag) {
			Vecteur offSetPos1 = startDrag.soustrait(posInit);
			position = souris.soustrait(offSetPos1);
			posX = position.getX();
			posY = position.getY();

		}

		/**
		 * Donne une valeur a la position initiale
		 * 
		 */
		public void setPositionsInit() {
			posInit = new Vecteur(posX, posY);
		}



		@Override
		public void setPeutPlacer(boolean b) {
			// TODO Auto-generated method stub
			
		}
		
}
