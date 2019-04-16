package aaplication;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import composants.Aide;

import composants.Jeu;
import composants.MenuPrincipal;
import javax.swing.UIManager;

/**
 * Classe de test des menus
 * 
 * @author Duy
 *
 */
public class App17Portails extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static JPanel contentPane;

	// real
	 //private static int WIDTH_APP = 1480;
	// private static int HEIGHT_APP = 900;

	// at home
	private static int WIDTH_APP = 1360;
	private static int HEIGHT_APP = 700;

	private static Jeu jeu;
	private static Aide aide;
	private static MenuPrincipal menu;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App17Portails frame = new App17Portails();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public App17Portails() {
		setTitle("17Portails");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, WIDTH_APP, HEIGHT_APP);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		menu = new MenuPrincipal(WIDTH_APP, HEIGHT_APP);
		contentPane.add(menu);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if (jeu != null) {
					jeu.ecrireFichierBinaire();
				}

			}
		});

	}

	/**
	 * Methode qui lance le jeu
	 */
	public static void lancerJeu() {
		contentPane.removeAll();

		jeu = new Jeu(WIDTH_APP, HEIGHT_APP);
		contentPane.add(jeu);
		contentPane.repaint();

	}

	/**
	 * Méthode qui lance le panel aide
	 */
	public static void lancerAide() {
		contentPane.removeAll();

		aide = new Aide(WIDTH_APP, HEIGHT_APP);
		contentPane.add(aide);
		contentPane.repaint();
	}

	/**
	 * Methode qui lance le menu et supprimer le jeu
	 */
	public static void retourner() {
		contentPane.removeAll();

		menu = new MenuPrincipal(WIDTH_APP, HEIGHT_APP);
		contentPane.add(menu);

		jeu = null;

		contentPane.repaint();

	}
}