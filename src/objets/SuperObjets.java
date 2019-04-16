package objets;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

import geometrie.Vecteur;
import interfaces.Dessinable;
/**
 * Classe mere de chaque objet.
 * @author Ayoub
 *
 */
public abstract class SuperObjets implements Dessinable,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3227583556400920461L;
	public Vecteur position;

	
	/**
	 * Constructeur super d'apres la position initiale de l'objet
	 * @param posInit position initiale de l'objet
	 */
	public SuperObjets(Vecteur posInit){
		this.position = posInit;
	}
	
	@Override
	public abstract void dessiner(Graphics2D g2d, AffineTransform matMc );
	
	/**
	 * 
	 * @return La position de l'objet
	 */
	public Vecteur getPosition() {
		return position;
	}
	
	public abstract void setPeutPlacer(boolean b);

	
}
