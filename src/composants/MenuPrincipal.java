package composants;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Boutons.BoutonMenu;
import aaplication.App17Portails;
import backgroundanimation.SystemParticules;

public class MenuPrincipal extends JPanel implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * author Duy implemente un menu
	 */
	private int marge = 80, posYInit;
	private int widthBtn = 500;
	private int heightBtn = 60;

	private Color colorSelected;

	private int fontSize = 40;

	private SystemParticules sysParticules;
	private double deltaT = 0.1;
	private long tempsDuSleep = 20;
	private boolean enCoursDAnimation = false;

	private int nbParticules = 100;

	/**
	 * 
	 * @param width
	 *            Largeur du menu principal
	 * @param height
	 *            Hauteur du menu principal
	 */
	public MenuPrincipal(int width, int height) {
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				sysParticules.updateSouris(e.getX(), e.getY());
			}
		});
		posYInit = height / 2;
		this.setBounds(0, 0, width, height);
		// choisir une couleur random parmis 4 couleur prechoisie
		Color[] c = { new Color(210, 105, 30), new Color(119, 136, 153), new Color(153, 50, 204),
				new Color(60, 179, 113) , Color.DARK_GRAY};
		Color bgColor = c[ThreadLocalRandom.current().nextInt(0, c.length)];
		
		// trouver une couleur legerement plus claire pour la couleur dse boutons
		// selectionnes
		double ratio = 0.3;
		double newR = (255 - bgColor.getRed()) * ratio + bgColor.getRed();
		double newG = (255 - bgColor.getGreen()) * ratio + bgColor.getGreen();
		double newB = (255 - bgColor.getBlue()) * ratio + bgColor.getBlue();
		colorSelected = new Color((int) newR, (int) newG, (int) newB);
		this.setBackground(bgColor);
		setLayout(null);

		/**
		 * créer boutons personnalisés
		 */

		BoutonMenu btnJouer = new BoutonMenu(heightBtn, widthBtn, "Jouer", colorSelected, null, fontSize);
		btnJouer.setBounds(width / 2 - widthBtn / 2, posYInit, widthBtn, heightBtn);
		btnJouer.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				App17Portails.lancerJeu();
			}
		});

		BoutonMenu btnAide = new BoutonMenu(heightBtn, widthBtn, "Aide", colorSelected, null, fontSize);
		btnAide.setBounds(width / 2 - widthBtn / 2, posYInit + (marge), widthBtn, heightBtn);
		;
		btnAide.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {

				App17Portails.lancerAide();
			}
		});

		BoutonMenu btnQuitter = new BoutonMenu(heightBtn, widthBtn, "Quitter", colorSelected, null, fontSize);
		btnQuitter.setBounds(width / 2 - widthBtn / 2, posYInit + (marge * 2), widthBtn, heightBtn);
		;

		btnQuitter.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {

				System.exit(0);
			}
		});

		btnJouer.setLayout(null);
		btnAide.setLayout(null);
		btnQuitter.setLayout(null);

		this.add(btnJouer);
		this.add(btnAide);
		this.add(btnQuitter);

		JLabel lblportails = new JLabel("PORTAILS", SwingConstants.CENTER);
		Font ft = new Font("Aharoni", Font.BOLD, 100);
		lblportails.setFont(ft);
		lblportails.setForeground(Color.WHITE);
		lblportails.setBounds(0, 0, width, 300);

		add(lblportails);

		// creer le systeme de particules qui sera dessiner dans le background
		sysParticules = new SystemParticules(nbParticules, width, height);
		demarrer();

	}

	// author Ayoub
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// dessiner les particules

		sysParticules.dessiner(g2d);
	}

	@Override
	public void run() {
		while (enCoursDAnimation) {
			try {
				sysParticules.update(deltaT);
			} catch (Exception e) {

				e.printStackTrace();
			}

			repaint();

			try {
				Thread.sleep(tempsDuSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void demarrer() {
		if (!enCoursDAnimation) {
			Thread proc = new Thread(this);
			proc.start();
			enCoursDAnimation = true;

		}
	}

}
