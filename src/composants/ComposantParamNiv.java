package composants;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import niveau.Niveau;
/**
 * 
 * @author Duy
 *
 */
public class ComposantParamNiv extends JPanel {

	private static final long serialVersionUID = 4185980316070080576L;

	private double width;
	private double height;

	private final String TITRE = "PARAMÈTRES NIVEAU";

	private Color colorBg = new Color(240, 240, 240);
	private Niveau niveauCourant = null;

	// parametres du niveau

	private JSlider sliderMaxBalles;
	private JSlider sldrCoeFriction;
	private JSlider sldrCoeRes;
	private JLabel lblFriction;
	private JLabel lblRes;
	private JLabel lblTitreNbBalles;
	private JLabel lblNbBalles;

	public ComposantParamNiv(double width, double height, PanelOutils parent) {

		this.width = width;
		this.height = height;

		

		setLayout(null);

		sliderMaxBalles = new JSlider();
		sliderMaxBalles.setMajorTickSpacing(3);
		sliderMaxBalles.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (niveauCourant != null) {
					setValeursNiv();
				}
			}
		});

		sliderMaxBalles.setPaintTicks(true);
		sliderMaxBalles.setValue(10);
		sliderMaxBalles.setPaintLabels(true);
		sliderMaxBalles.setMinorTickSpacing(10);
		sliderMaxBalles.setMinimum(1);
		sliderMaxBalles.setMaximum(20);
		sliderMaxBalles.setBounds(10, 66, 166, 45);
		add(sliderMaxBalles);

		lblTitreNbBalles = new JLabel("Limite de balles :");
		lblTitreNbBalles.setFont(new Font("Arial", Font.PLAIN, 12));
		lblTitreNbBalles.setBounds(10, 38, 132, 14);
		add(lblTitreNbBalles);

		JLabel lblCoeRestitution = new JLabel("Coeff. Restitution : ");
		lblCoeRestitution.setFont(new Font("Arial", Font.PLAIN, 12));
		lblCoeRestitution.setBounds(10, 122, 110, 24);
		add(lblCoeRestitution);

		JLabel lblCoeFriction = new JLabel("Coeff. Friction : ");
		lblCoeFriction.setFont(new Font("Arial", Font.PLAIN, 12));
		lblCoeFriction.setBounds(10, 201, 110, 24);
		add(lblCoeFriction);

		lblRes = new JLabel("");
		lblRes.setBounds(119, 122, 57, 24);
		add(lblRes);

		lblFriction = new JLabel("");
		lblFriction.setBounds(119, 202, 68, 25);
		add(lblFriction);

		sldrCoeRes = new JSlider();
		sldrCoeRes.setMaximum(10);
		sldrCoeRes.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (niveauCourant != null) {
					setValeursNiv();
				}

			}
		});
		sldrCoeRes.setBounds(10, 164, 200, 26);
		add(sldrCoeRes);

		sldrCoeFriction = new JSlider();
		sldrCoeFriction.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (niveauCourant != null) {
					setValeursNiv();
				}

			}
		});
		sldrCoeFriction.setBounds(10, 243, 200, 26);
		add(sldrCoeFriction);

		lblNbBalles = new JLabel("");
		lblNbBalles.setBounds(119, 39, 46, 14);
		add(lblNbBalles);

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Dessiner le contour des composants

		RoundRectangle2D.Double rect = new RoundRectangle2D.Double(0, 0, width - 1, height - 1, 0, 0);
		g2d.setStroke(new BasicStroke(1.5f));
		g2d.setColor(colorBg);
		// g2d.fill(rect);
		g2d.setColor(new Color(95, 95, 95));
		g2d.draw(rect);

		// Dessiner le TITRE
		Font font = new Font("Ariel", Font.PLAIN, 15);
		g2d.setFont(font);
		int widthTitre = g2d.getFontMetrics().stringWidth(TITRE);
		int heighTitre = g2d.getFontMetrics().getAscent();

		g2d.drawString(TITRE, (int) (width / 2 - widthTitre / 2), heighTitre + 2);

	}

	/**
	 * Methode qui donne des valeurs initiales aux sliders et aux label d'apres le
	 * niveau courant.
	 * 
	 * @param courantNiv
	 */
	public void setValeursSliders(Niveau courantNiv) {

		sliderMaxBalles.setValue(courantNiv.getNbMaxBalles());

		sldrCoeFriction.setValue((int) (courantNiv.getCoeffFric() * 100));
		sldrCoeRes.setValue((int) (courantNiv.getCoeffRest() * 10));

		lblNbBalles.setText("" + sliderMaxBalles.getValue());
		lblFriction.setText("" + (double) sldrCoeFriction.getValue() / 100);
		lblRes.setText("" + (double) sldrCoeRes.getValue() / 10);

		this.niveauCourant = courantNiv;
	}

	/**
	 * Methode qui update les label et les valeurs du niveau courant d'apres la
	 * valeur des sliders.
	 */
	public void setValeursNiv() {
		// 1. set les valeurs du niveau courant.
		niveauCourant.setCoeffFric((double) sldrCoeFriction.getValue() / 100);
		niveauCourant.setCoeffRest((double) sldrCoeRes.getValue() / 10);
		niveauCourant.setNbMaxBalles(sliderMaxBalles.getValue());

		lblNbBalles.setText("" + sliderMaxBalles.getValue());
		lblFriction.setText("" + (double) sldrCoeFriction.getValue() / 100);
		lblRes.setText("" + (double) sldrCoeRes.getValue() / 10);

	}
}
