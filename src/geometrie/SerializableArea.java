package geometrie;

import java.awt.Shape;
import java.awt.geom.Area;
import java.io.Serializable;
/**
 * SerializableArea est un objet qui prend tout les champs et methodes d'une Area, en lui permettant d'etre Serializable.
 * @author Ayoub
 *
 */
public class SerializableArea extends Area implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2859486533115796193L;
	/**
	 * Creer une SeralizableArea a partir d'une shape
	 * @param s La shape a partir de laquelle la Area sera creer
	 */
	public SerializableArea(Shape s) {
		super(s);
	}
	/**
	 * Creer une area vide.
	 */
	public SerializableArea() {
		super();
	}
	

}
