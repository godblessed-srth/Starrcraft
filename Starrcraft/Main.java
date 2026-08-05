import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Main extends JFrame {
	// buttons
	private static final Font TEXT_FONT = new Font("Serif", Font.PLAIN, 15);
	private static final Image bg = new ImageIcon("dirt.png").getImage();
	private CardLayout cardLay;
	private JPanel mainPanel, mainCont;
	private WorldCreate world;
	private long seed = new Random().nextLong();
	private static final String[] MAIN_BUTTONS = {"Singleplayer", "Mods", "Quit"};
	public static final int WIDTH = 640, HEIGHT = 480;
	public static final int SCALEX = 240, SCALEY = 30, GAP = 10;
	public static final int X = (WIDTH - SCALEX - GAP * 2) / 2, Y = HEIGHT / 2;
	//window
	public Main() {
		setTitle("Starrcraft");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		
		cardLay = new CardLayout();
		mainCont = new JPanel(cardLay);
		//main panel
		mainPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
			}
		};
		
		mainPanel.setLayout(null);

		int pos = GAP / 2;

		JLabel l = new JLabel("Starrcraft Gamma 0.10");
		l.setBounds(pos, pos, SCALEX, SCALEY - 15);
		l.setForeground(Color.WHITE);
		l.setFont(TEXT_FONT);
		//buttons
		Button[] buttons = new Button[MAIN_BUTTONS.length];
		for (int i = 0; i < buttons.length; i++) {
			int y = Y + (SCALEY + GAP) * i;
			buttons[i] = new Button(X, y, SCALEX, SCALEY, MAIN_BUTTONS[i]);
			mainPanel.add(buttons[i]);
			
			final int index = i;
			buttons[i].addActionListener(e -> {
				String command = buttons[index].getText();
				switch (command) {
					case "Singleplayer": cardLay.show(mainCont, "GAME");
					SwingUtilities.invokeLater(() -> world.requestFocusInWindow()); break;
					case "Quit": System.exit(0); break;
				}
			});
		}
		
		world = new WorldCreate(seed);
		
		mainPanel.add(l);
		mainCont.add(mainPanel, "MAIN");
		mainCont.add(world, "GAME");		
		add(mainCont);
		setVisible(true);
	}
	
	static class Button extends JButton {
		public Button(int x, int y, int scaleX, int scaleY, String text) {
			setBounds(x, y, scaleX, scaleY);
			setText(text);
			setBackground(Color.GRAY);
			setForeground(Color.WHITE);
			setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			setFocusPainted(false);
			setCursor(new Cursor(Cursor.HAND_CURSOR));
			setFont(TEXT_FONT);

			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					setBackground(new Color(168, 205, 227));
					setForeground(new Color(255, 230, 0));
				}
				public void mouseExited(MouseEvent e) {
					setBackground(Color.GRAY);
					setForeground(Color.WHITE);
				}
			});
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Main().setVisible(true));
	}
}