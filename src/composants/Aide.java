package composants;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Boutons.BoutonMenu;
import aaplication.App17Portails;
import labels.ImageAvecDefilement;
/**
 * 
 * @author Duy
 *	Créer un panel pour la fenêtre aide qui contient : guide utilisation, concepts scientifiques, a propos
 */

public class Aide extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4503556163817425939L;
	private int widthComposantAnim;

	private int y = 10;
	// Variables bouton retour
	private final int WIDTH_BTN_RETOUR = 145;
	private final int HEIGHT_BTN_RETOUR = 45;
	private final int WIDTH_BTN_PANEL = 170;
	private final int HEIGHT_BTN_PANEL = 25;
	private final int WIDTH_PANEL = 1160;
	private final int HEIGHT_PANEL = 520;
	ImageAvecDefilement guideUtilisation = new ImageAvecDefilement();
	ImageAvecDefilement conceptScientifique = new ImageAvecDefilement();
	JPanel aPropos = new JPanel();
	private int stat = 1;
	private int statC = 1;
	
	private Image icon;
	
	
	private Color selected = new Color(229, 142, 29); // dark orange
	private Color nonSelected = new Color(246, 170, 35); // light orange
	
	/**
	 * Méthode pour créer le JPanel aide
	 * @param width la largeur du panel
	 * @param height la longueur du panel
	 */
	public Aide(int width, int height){


		widthComposantAnim = (int) (width * 0.75);

		
		this.setBounds(0, 0, width, height);
		this.setBackground(Color.LIGHT_GRAY);
		setLayout(null);
		BoutonMenu btnRetour = new BoutonMenu(HEIGHT_BTN_RETOUR, WIDTH_BTN_RETOUR, "MENU", selected, nonSelected, 34);

		this.add(btnRetour);
		btnRetour.setBounds((int) (widthComposantAnim + y - WIDTH_BTN_RETOUR), (int) (height - 50 - HEIGHT_BTN_RETOUR), WIDTH_BTN_RETOUR,
				HEIGHT_BTN_RETOUR);
		btnRetour.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				App17Portails.retourner();
			}
			
		});
		
		BoutonMenu btnGuide = new BoutonMenu(HEIGHT_BTN_PANEL, WIDTH_BTN_PANEL, "Guide d'utilisation", selected, nonSelected, 15);

		this.add(btnGuide);
		btnGuide.setBounds(100, 50, WIDTH_BTN_PANEL, HEIGHT_BTN_PANEL);
		btnGuide.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				conceptScientifique.setVisible(false);
				guideUtilisation.setVisible(true);
				aPropos.setVisible(false);
			}
			
		});
		
		BoutonMenu btnConcept = new BoutonMenu(HEIGHT_BTN_PANEL, WIDTH_BTN_PANEL, "Concepts scientifiques", selected, nonSelected, 15);

		this.add(btnConcept);
		btnConcept.setBounds(100 + WIDTH_BTN_PANEL, 50, WIDTH_BTN_PANEL, HEIGHT_BTN_PANEL);
		btnConcept.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				conceptScientifique.setVisible(true);
				guideUtilisation.setVisible(false);
				aPropos.setVisible(false);
			}
			
		});
		
		BoutonMenu btnPropos = new BoutonMenu(HEIGHT_BTN_PANEL, WIDTH_BTN_PANEL, "À propos", selected, nonSelected, 15);

		this.add(btnPropos);
		btnPropos.setBounds(100 + (WIDTH_BTN_PANEL*2), 50, WIDTH_BTN_PANEL, HEIGHT_BTN_PANEL);
		btnPropos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				conceptScientifique.setVisible(false);
				guideUtilisation.setVisible(false);
				aPropos.setVisible(true);
			}
			
		});
		
		guideUtilisation.setBounds(100, 80, WIDTH_PANEL, HEIGHT_PANEL);
		guideUtilisation.setBackground(Color.WHITE);
		guideUtilisation.setFichierImage("guide-1.jpg");
		
		
		
		conceptScientifique.setBounds(100, 80, width - 200, height - 180);
		conceptScientifique.setBackground(Color.WHITE);
		conceptScientifique.setFichierImage("conceptsScientifique-1.jpg");
		
		BoutonMenu btnNext = new BoutonMenu(HEIGHT_BTN_RETOUR, WIDTH_BTN_RETOUR, "SUIVANT", selected, nonSelected, 20);
		this.add(btnNext);
		btnNext.setBounds((int) (widthComposantAnim + y - (WIDTH_BTN_RETOUR*2)), (int) (height - 50 - HEIGHT_BTN_RETOUR), WIDTH_BTN_RETOUR,HEIGHT_BTN_PANEL);
		btnNext.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (conceptScientifique.isVisible()){
					if (statC < 5){
						statC++;
						conceptScientifique.setFichierImage("conceptsScientifique-" + statC +".jpg");
					}
				} else if (guideUtilisation.isVisible()){
					if (stat < 5){
						stat++;
							guideUtilisation.setFichierImage("guide-" + stat +".jpg");
					}
				}
			
				}
				
			
		});
		
		BoutonMenu btnPrevious = new BoutonMenu(HEIGHT_BTN_RETOUR, WIDTH_BTN_RETOUR, "PRÉCÉDENT", selected, nonSelected, 20);
		this.add(btnPrevious);
		btnPrevious.setBounds((int) (widthComposantAnim + y - (WIDTH_BTN_RETOUR*3)), (int) (height - 50 - HEIGHT_BTN_RETOUR), WIDTH_BTN_RETOUR,HEIGHT_BTN_PANEL);
		btnPrevious.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (conceptScientifique.isVisible()){
				
					if (statC > 1){
						statC--;	
						conceptScientifique.setFichierImage("conceptsScientifique-" + statC +".jpg");
					}
					
				} else if (guideUtilisation.isVisible()){
					if (stat > 1){
						stat--;
						guideUtilisation.setFichierImage("guide-" + stat +".jpg");
					}
				}
				}
				
			
		});
		
		
		
		
		
		
		aPropos.setBounds(100, 80, width - 200, height - 180);
		aPropos.setBackground(Color.WHITE);
		loadImages("aPropos.png");
		JLabel lblPropos = new JLabel(new ImageIcon(icon));
		aPropos.add(lblPropos);



		this.add(guideUtilisation);
		this.add(conceptScientifique);
		this.add(aPropos);
		
	}
	/**
	 * Méthode pour charger une image 
	 * @param str le nom du fichier de l'image en String
	 */
	private void loadImages(String str) {
		URL urlIcon = null;
	
			urlIcon = getClass().getClassLoader().getResource(str);
	
		if (urlIcon != null) {
			try {
				icon = ImageIO.read(urlIcon);
			} catch (IOException e) {
				System.out.println("Erreur pendant la lecture de l'image");
			}
		}

	}
	
	
	
}
