package interfaces;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
/**
 * Interface dessinable.
 * @author Ayoub
 *
 */
public interface Dessinable {
	/**
	 * Dessiner un objet dessinable
	 * @param g2d graphics ou sera dessiner l'objet
	 * @param matMC matrice de transformation du monde reelle vers le monde pixelise
	 */
	public void dessiner( Graphics2D g2d,AffineTransform matMC);
	/**
	 * Dessiner l'objet en mode scienfique avec ses vecteurs
	 * @param g2d graphics ou sera dessiner l'objet
	 * @param matMC matrice de transformation du monde reelle vers le monde pixelise
	 */
	public void dessinerModeCreatif(Graphics2D g2d,AffineTransform matMC);

	
	

}
