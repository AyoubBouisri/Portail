package geometrie;

import java.util.ArrayList;

import objets.Balle;
import objets.Plans;
import objets.Portail;
import objets.Portails;

/**
 * Classe qui contient les methodes qui gerent la collision entre des objets
 * ainsi que la correction des positions.
 * 
 * @author Ayoub
 *
 */
public class Collisions {

	
	
	/**
	 * Verifier si deux balles sont en collisions et set la normale de la balle si
	 * en Collision
	 * 
	 * @param A
	 *            Premiere balle
	 * @param B
	 *            deuxieme balle
	 * @return vrai si les balles sont en collisions faux si elles ne sont pas en
	 *         collision
	 * @throws Exception
	 */
	public static boolean enCollision(Balle A, Balle B) {

		double distance = B.getPosition().dist(A.getPosition());
		if (distance <= A.getRayon() + B.getRayon()) {
			// set la normale de collision
			A.setNormaleCollision(getNormaleCollision(A, B.getPosition()));

			B.setNormaleCollision(getNormaleCollision(A, B.getPosition()));

			correction(A, B);

			return true;
		} else {
			return false;
		}

	}

	/**
	 * Verifier si une balle et un plan sont en collisions et set la normale de la
	 * balle si en Collision
	 * 
	 * @param bl
	 *            Balle en possibilite de collision
	 * @param pl
	 *            Plan en possibilite de collision
	 * @return La valeur boolean de la collision
	 * 
	 */
	public static Vecteur enCollision(Balle bl, Plans pl, Portails p) {
		
		
		Vecteur plPortail;
		Vecteur ballPlan = bl.getPosition().soustrait(pl.getPositionInit());
		Vecteur projection = Vecteur.projection(ballPlan, pl.getDirection());
		// Pt du plan le plus proche de la balle
		Vecteur ptPlan = pl.getPositionInit().additionne(projection);
		boolean found = false;
		Vecteur projectionTest = projection.normalise();
		if (projectionTest.equals(pl.getDirection().multiplie(-1))) {
			// Le point le plus proche de la balle est donc le pointInit du plan
			ptPlan = pl.getPositionInit();
			found = true;

		} else if (projection.module() > pl.getLongueur()) {
			// Le point le plus proche de la balle est donc le pointFinale du plan
			ptPlan = pl.getPosFin();
			found = true;

		} else {
			// si il y a des portails sur le plan, trouver le pt de collisions
			// le plus pres sans quil soit sur les portails.
			
			ArrayList<Portail> portails = new ArrayList<Portail>();
			if (p.getPOrange() != null) {
				if(p.getPBleu() !=null) {
					portails.add(p.getPOrange());
				}
				
			}
			if (p.getPBleu() != null) {
				if(p.getPOrange()!=null) {
					portails.add(p.getPBleu());
				}
				
			}

			ArrayList<Double> distances = new ArrayList<Double>();
			ArrayList<Vecteur> points = new ArrayList<Vecteur>();

			for (int i = 0; i < portails.size(); i++) {
				Portail currentPortail = portails.get(i);
				if (currentPortail.getBigParent().equals(pl)) {
					plPortail = currentPortail.getPosition().soustrait(pl.getPositionInit());

					if (projection.module() > plPortail.module() - Portails.getDiam() / 2
							&& projection.module() < plPortail.module() + Portails.getDiam() / 2) {
						if (Math.toDegrees(Vecteur.angleEntre(bl.getVitesse(), pl.getNormale().multiplie(currentPortail.getDirNormale()))) >= 90) {

							Vecteur plPortailTemp1 = plPortail.copy();
							plPortailTemp1.setModule(plPortail.module() - Portails.getDiam() / 2);
							Vecteur ptPossible1 = pl.getPositionInit().additionne(plPortailTemp1);

							
							plPortailTemp1.setModule(plPortail.module() + Portails.getDiam() / 2);
							Vecteur ptPossible2 = pl.getPositionInit().additionne(plPortailTemp1);

							// voir quel point est plus proche

							double dist1 = bl.getPosition().dist(ptPossible1);
							double dist2 = bl.getPosition().dist(ptPossible2);

							distances.add(dist1);
							points.add(ptPossible1);
							distances.add(dist2);
							points.add(ptPossible2);
						}
					}

				}
			}

			// trouver la plus petite distance et le point qui est relie a cette distance
			double distMin = 100000;
			int indexMin = 0;

			for (int i = 0; i < distances.size(); i++) {

				if (distances.get(i) < distMin) {
					distMin = distances.get(i);
					indexMin = i;
				}
			}

			if (distances.size() != 0) {
				ptPlan = points.get(indexMin);
				
				
							
				
				found = true;
			}

		}

		if (!found) {
			
			ptPlan = pl.getPositionInit().additionne(projection);
		}

		// verifier si il y a un intersection avec le ptPlan
		
		
		double distance = bl.getPosition().dist(ptPlan);
		
		if (distance <= bl.getRayon()) {
			// set la normale de collision de la balle
			bl.setNormaleCollision(getNormaleCollision(bl, ptPlan));
			
			// Corriger la collision
			
			correction(bl, ptPlan);
			return ptPlan;
		} 	
			return null;
		
	}

	/**
	 * Methode pour voir si il y a une collision avec un point d'un certain rayon
	 * avec un plan. Methode utilisee pour voir si la souris se trouve a proximite
	 * d'un plan
	 * 
	 * @param pt
	 *            Position de la souris
	 * @param pl
	 *            Plan en possibilite de collision
	 * @return Vrai si il y a une collision faux sinon
	 */
	public static double[] enCollision(Vecteur pt, Plans pl) {
		Vecteur ptPlan;

		Vecteur ptPl = pt.soustrait(pl.getPositionInit());
		Vecteur projection = Vecteur.projection(ptPl, pl.getDirection());

		Vecteur projectionTest = projection.normalise();
		if (projectionTest.equals(pl.getDirection().multiplie(-1))) {
			// Le point le plus proche de la balle est donc le pointInit du plan
			// trop pres du bord pour placer un portail
			return new double[0];

		} else if (projection.module() > pl.getLongueur()) {
			// Le point le plus proche de la balle est donc le pointFinale du plan
			// trop pres du bord pour placer un portail
			return new double[0];

		} else {
			// le point le plus proche de la balle est sur le plan
			ptPlan = pl.getPositionInit().additionne(projection);

		}

		double distance = pt.dist(ptPlan);
		// tester si il y a assez de place pour mettre un plan

		if (distance <= Portails.getPlaceDist()) {
			double distDansPl = ptPlan.soustrait(pl.getPositionInit()).module();
			if (distDansPl >= Portails.getDiam() / 2 && distDansPl <= pl.getLongueur() - Portails.getDiam() / 2) {
				double[] resul = { distance, ptPlan.getX(), ptPlan.getY() };
				return resul;
			}

		}
		return new double[0];

	}
	
	/**
	 * Apres une collision donne une normale de collision aux objets concernes
	 * 
	 * @param ballTemp
	 *            Balle avec laquelle il y a eu collision
	 * @param pt
	 *            Pt avec le quel la balle a eu une collision
	 * 
	 */
	public static Vecteur getNormaleCollision(Balle ballTemp, Vecteur pt) {
		Vecteur normale = ballTemp.getPosition().soustrait(pt);
		normale = normale.normalise();
		return normale;

	}

	/**
	 * Corriger les positions des balles concernees pour eviter qu'elles restent
	 * collees
	 * 
	 * @param b1
	 *            Premiere balle
	 * @param b2
	 *            Deuxieme balle
	 */
	public static void correction(Balle b1, Balle b2) {
		double dist = b1.getPosition().dist(b2.getPosition());
		double distDesiree = b1.getRayon() + b2.getRayon();
		Vecteur correctionDir = b1.getNormaleCollision().copy();
		
		
		correctionDir.setModule(distDesiree - dist);

		b1.setPosition(b1.getPosition().additionne(correctionDir));
		b2.setPosition(b2.getPosition().additionne(correctionDir.multiplie(-1)));

	}

	/**
	 * Corriger les position de la balle concernee pour eviter qu'elle reste collee
	 * 
	 * @param b1
	 *            Balle en collision
	 * @param pl
	 *            Plan avec lequel la balle est en collision
	 */
	public static void correction(Balle b1, Vecteur pt) {
		double dist = b1.getPosition().dist(pt);
		double distDesiree = b1.getRayon();

		Vecteur correctionDir = b1.getNormaleCollision().copy();
		correctionDir.setModule(distDesiree - dist);

		b1.setPosition(b1.getPosition().additionne(correctionDir));

	}
}
