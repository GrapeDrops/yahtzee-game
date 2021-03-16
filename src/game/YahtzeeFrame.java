package game;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import server.SaveServer;

public class YahtzeeFrame extends JFrame {

	private JTextField nameField;
	private YahtzeeGame game;
	private JTextField acesField;
	private JTextField twosField;
	private JTextField threesField;
	private JTextField foursField;
	private JTextField fivesField;
	private JTextField sixesField;
	private JTextField upperBonusField;
	private JTextField upperTotalField;
	private JTextField threeKindField;
	private JTextField fourKindField;
	private JTextField fullHouseField;
	private JTextField sStraightField;
	private JTextField lStraightField;
	private JTextField yahtzeeField;
	private JTextField chanceField;
	private JTextField yahtzeeBonusField;
	private JTextField lowerTotalField;
	private JTextField grandTotalField;
	private ImagePanel im0;
	private ImagePanel im1;
	private ImagePanel im2;
	private ImagePanel im3;
	private ImagePanel im4;
	private JCheckBox cb0;
	private JCheckBox cb1;
	private JCheckBox cb2;
	private JCheckBox cb3;
	private JCheckBox cb4;
	private int upperBonus = 0;
	private int upperTotal = 0;
	private int lowerTotal = 0;
	
	public YahtzeeFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,800);
		
		// Initialize game
		game = new YahtzeeGame();
		
		createMenu();
		createNamePanel();
		createGamePanel();
		JOptionPane.showMessageDialog(null, "Welcome to Yahtzee! First round of dice has been rolled for you :)");
	}
	
	// Menu builder
	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();     
	    setJMenuBar(menuBar);
	    JMenu menu = new JMenu("Game");
	    JMenuItem loadGame = new JMenuItem("Load Game");
	    JMenuItem saveGame = new JMenuItem("Save Game");
	    JMenuItem exitItem = new JMenuItem("Exit");
		
	    // Action listeners for each menu choice
		class LoadActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				try {
					Socket socket = new Socket("localhost", 7777);
					PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true);
					printWriter.println("LOAD"); // LOAD command for server
					InputStream is = socket.getInputStream();
		            InputStreamReader isr = new InputStreamReader(is);
		            BufferedReader br = new BufferedReader(isr);
		            String option = br.readLine();
		            ArrayList<String> dbNames = new ArrayList<String>();
		            // The following string acts as a terminator. The text field is only 20 characters long
		            // whereas this one is 21 characters long. This way I know I have received everything
		            // from the server.
		            while(!option.equals("ABCDEFGHIJKLMNOPQRSTU")) {
		            	dbNames.add(option);
		            	option = br.readLine();
		            }
		            String[] choices = dbNames.toArray(new String[0]);
		            // User will choose savefile from this panel
		            String loadInput = (String) JOptionPane.showInputDialog(null, "Choose savefile", 
		            		"LOAD", JOptionPane.PLAIN_MESSAGE, null, choices, choices[0]);
		            if(loadInput == null) { // User canceled the load
		            	printWriter.println("ABCDEFGHIJKLMNOPQRSTU");
		            	socket.close();
		            	return;
		            }
		            printWriter.println(loadInput); // Send selection to server
		            // Get data from server and set each field in the game object
		            nameField.setText(br.readLine());
		            for(int i=0; i < 5; i++) game.setDice(i, Integer.valueOf(br.readLine()));
					for(int i=0; i < 6; i++) game.setCount(i, Integer.valueOf(br.readLine()));
					game.setRoll(Integer.valueOf(br.readLine()));
					game.setJoker(Boolean.getBoolean(br.readLine()));
					game.setAces(Integer.valueOf(br.readLine()));
					game.setTwos(Integer.valueOf(br.readLine()));
					game.setThrees(Integer.valueOf(br.readLine()));
					game.setFours(Integer.valueOf(br.readLine()));
					game.setFives(Integer.valueOf(br.readLine()));
					game.setSixes(Integer.valueOf(br.readLine()));
					game.setThreeKind(Integer.valueOf(br.readLine()));
					game.setFourKind(Integer.valueOf(br.readLine()));
					game.setFullHouse(Integer.valueOf(br.readLine()));
					game.setSmallStraight(Integer.valueOf(br.readLine()));
					game.setLargeStraight(Integer.valueOf(br.readLine()));
					game.setYahtzee(Integer.valueOf(br.readLine()));
					game.setChance(Integer.valueOf(br.readLine()));
					game.setBonus(Integer.valueOf(br.readLine()));
					// Set the GUI
					im0.setImage("die" + (game.getDice(0)+1) + ".png");
					im1.setImage("die" + (game.getDice(1)+1) + ".png");
					im2.setImage("die" + (game.getDice(2)+1) + ".png");
					im3.setImage("die" + (game.getDice(3)+1) + ".png");
					im4.setImage("die" + (game.getDice(4)+1) + ".png");
					acesField.setText("");
					twosField.setText("");
					threesField.setText("");
					foursField.setText("");
					fivesField.setText("");
					sixesField.setText("");
					threeKindField.setText("");
					fourKindField.setText("");
					fullHouseField.setText("");
					sStraightField.setText("");
					lStraightField.setText("");
					yahtzeeField.setText("");
					chanceField.setText("");
					if(game.getAces() >= 0) acesField.setText(String.valueOf(game.getAces()));
					if(game.getTwos() >= 0) twosField.setText(String.valueOf(game.getTwos()));
					if(game.getThrees() >= 0) threesField.setText(String.valueOf(game.getThrees()));
					if(game.getFours() >= 0) foursField.setText(String.valueOf(game.getFours()));
					if(game.getFives() >= 0) fivesField.setText(String.valueOf(game.getFives()));
					if(game.getSixes() >= 0) sixesField.setText(String.valueOf(game.getSixes()));
					upperTotal = Math.max(0, game.getAces())+Math.max(0, game.getTwos())+Math.max(0, game.getThrees())+
							Math.max(0, game.getFours())+Math.max(0, game.getFives())+Math.max(0, game.getSixes());
					if(upperTotal >= 63) upperBonus = 35; else upperBonus = 0;
					upperTotalField.setText(String.valueOf(upperTotal+upperBonus));
					if(game.getThreeKind() >= 0) threeKindField.setText(String.valueOf(game.getThreeKind()));
					if(game.getFourKind() >= 0) fourKindField.setText(String.valueOf(game.getFourKind()));
					if(game.getFullHouse() >= 0) fullHouseField.setText(String.valueOf(game.getFullHouse()));
					if(game.getSmallStraight() >= 0) sStraightField.setText(String.valueOf(game.getSmallStraight()));
					if(game.getLargeStraight() >= 0) lStraightField.setText(String.valueOf(game.getLargeStraight()));
					if(game.getYahtzee() >= 0) yahtzeeField.setText(String.valueOf(game.getYahtzee()));
					if(game.getChance() >= 0) chanceField.setText(String.valueOf(game.getChance()));
					yahtzeeBonusField.setText(String.valueOf(game.getBonus()));
					lowerTotal = Math.max(0, game.getThreeKind())+Math.max(0, game.getFourKind())+
							Math.max(0, game.getFullHouse())+Math.max(0, game.getSmallStraight())+
							Math.max(0, game.getLargeStraight())+Math.max(0, game.getYahtzee())+
							Math.max(0, game.getChance());
					lowerTotalField.setText(String.valueOf(lowerTotal));
					grandTotalField.setText(String.valueOf(upperTotal+upperBonus+lowerTotal+game.getBonus()));
					socket.close();
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		class SaveActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if(nameField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please type your name.");
				}
				else {
					try {
						Socket socket = new Socket("localhost", 7777);
						PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true);
						printWriter.println("SAVE"); // SAVE command for server
						// Send each field to the server
						printWriter.println(nameField.getText());
						for(int i=0; i < 5; i++) printWriter.println(game.getDice(i));
						for(int i=0; i < 6; i++) printWriter.println(game.getCount(i));
						printWriter.println(game.getRoll());
						printWriter.println(game.getJoker());
						printWriter.println(game.getAces());
						printWriter.println(game.getTwos());
						printWriter.println(game.getThrees());
						printWriter.println(game.getFours());
						printWriter.println(game.getFives());
						printWriter.println(game.getSixes());
						printWriter.println(game.getThreeKind());
						printWriter.println(game.getFourKind());
						printWriter.println(game.getFullHouse());
						printWriter.println(game.getSmallStraight());
						printWriter.println(game.getLargeStraight());
						printWriter.println(game.getYahtzee());
						printWriter.println(game.getChance());
						printWriter.println(game.getBonus());
				        socket.close();
					} catch (UnknownHostException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
		
		class ExitListener implements ActionListener
	      {
	         public void actionPerformed(ActionEvent event)
	         {
	            System.exit(0);
	         }
	      }   
		
		// Add items to bar
		loadGame.addActionListener(new LoadActionListener());
		saveGame.addActionListener(new SaveActionListener());
		exitItem.addActionListener(new ExitListener());
		
		menu.add(loadGame);
		menu.add(saveGame);
		menu.add(exitItem);
		menuBar.add(menu);
	}
	
	// Name Panel builder
	private void createNamePanel() {
		JPanel namePanel = new JPanel();
		JLabel nameLabel = new JLabel("Player Name: ");
		nameField  = new JTextField(20);
		namePanel.add(nameLabel);
		namePanel.add(nameField);
		
		add(namePanel, BorderLayout.NORTH);
	}
	
	// Game Panel builder
	private void createGamePanel() {
		// Set layout for the game related items
		JPanel gamePanel = new JPanel();
		JPanel sectionPanel = new JPanel();
		sectionPanel.setLayout(new GridLayout(2,1));
		JPanel upperSectionPanel = new JPanel();
		upperSectionPanel.setLayout(new GridLayout(8,2));
		JPanel lowerSectionPanel = new JPanel();
		lowerSectionPanel.setLayout(new GridLayout(10,2));
		JPanel rollPanel = new JPanel();
		JPanel dicePanel = new JPanel();
		dicePanel.setLayout(new GridLayout(2,5));
		
		// Action listener section for buttons
		class AcesActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if(game.getRoll() > 0) {
					game.calcAces();
					uncheckBoxes();
					if(acesField.getText().isEmpty()) {
						acesField.setText(String.valueOf(game.getAces()));
						upperTotal += game.getAces();
					}
					if(upperBonus == 0 && upperTotal >= 63) {
						upperBonus = 35;
						upperBonusField.setText("35");
					}
					upperTotalField.setText(String.valueOf(upperTotal+upperBonus));
					grandTotalField.setText(String.valueOf(upperTotal+upperBonus+lowerTotal+game.getBonus()));
				}
				else {
					JOptionPane.showMessageDialog(null, "You must roll at least once!");
				}
			}
		}
		
		class TwosActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if(game.getRoll() > 0) {
					game.calcTwos();
					uncheckBoxes();
					if(twosField.getText().isEmpty()) {
						twosField.setText(String.valueOf(game.getTwos()));
						upperTotal += game.getTwos();
					}
					if(upperBonus == 0 && upperTotal >= 63) {
						upperBonus = 35;
						upperBonusField.setText("35");
					}
					upperTotalField.setText(String.valueOf(upperTotal+upperBonus));
					grandTotalField.setText(String.valueOf(upperTotal+upperBonus+lowerTotal+game.getBonus()));
				}
				else {
					JOptionPane.showMessageDialog(null, "You must roll at least once!");
				}
			}
		}
		
		class ThreesActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if(game.getRoll() > 0) {
					game.calcThrees();
					uncheckBoxes();
					if(threesField.getText().isEmpty()) {
						threesField.setText(String.valueOf(game.getThrees()));
						upperTotal += game.getThrees();
					}
					if(upperBonus == 0 && upperTotal >= 63) {
						upperBonus = 35;
						upperBonusField.setText("35");
					}
					upperTotalField.setText(String.valueOf(upperTotal+upperBonus));
					grandTotalField.setText(String.valueOf(upperTotal+upperBonus+lowerTotal+game.getBonus()));
				}
				else {
					JOptionPane.showMessageDialog(null, "You must roll at least once!");
				}
			}
		}
		
		class FoursActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if(game.getRoll() > 0) {
					game.calcFours();
					uncheckBoxes();
					if(foursField.getText().isEmpty()) {
						foursField.setText(String.valueOf(game.getFours()));
						upperTotal += game.getFours();
					}
					if(upperBonus == 0 && upperTotal >= 63) {
						upperBonus = 35;
						upperBonusField.setText("35");
					}
					upperTotalField.setText(String.valueOf(upperTotal+upperBonus));
					grandTotalField.setText(String.valueOf(upperTotal+upperBonus+lowerTotal+game.getBonus()));
				}
				else {
					JOptionPane.showMessageDialog(null, "You must roll at least once!");
				}
			}
		}
		
		class FivesActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if(game.getRoll() > 0) {
					game.calcFives();
					uncheckBoxes();
					if(fivesField.getText().isEmpty()) {
						fivesField.setText(String.valueOf(game.getFives()));
						upperTotal += game.getFives();
					}
					if(upperBonus == 0 && upperTotal >= 63) {
						upperBonus = 35;
						upperBonusField.setText("35");
					}
					upperTotalField.setText(String.valueOf(upperTotal+upperBonus));
					grandTotalField.setText(String.valueOf(upperTotal+upperBonus+lowerTotal+game.getBonus()));
				}
				else {
					JOptionPane.showMessageDialog(null, "You must roll at least once!");
				}
			}
		}
		
		class SixesActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if(game.getRoll() > 0) {
					game.calcSixes();
					uncheckBoxes();
					if(sixesField.getText().isEmpty()) {
						sixesField.setText(String.valueOf(game.getSixes()));
						upperTotal += game.getSixes();
					}
					if(upperBonus == 0 && upperTotal >= 63) {
						upperBonus = 35;
						upperBonusField.setText("35");
					}
					upperTotalField.setText(String.valueOf(upperTotal+upperBonus));
					grandTotalField.setText(String.valueOf(upperTotal+upperBonus+lowerTotal+game.getBonus()));
				}
				else {
					JOptionPane.showMessageDialog(null, "You must roll at least once!");
				}
			}
		}
		
		class ThreeKindActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if(game.getRoll() > 0) {
					game.calcThreeKind();
					uncheckBoxes();
					if(threeKindField.getText().isEmpty()) {
						threeKindField.setText(String.valueOf(game.getThreeKind()));
						lowerTotal += game.getThreeKind();
					}
					lowerTotalField.setText(String.valueOf(lowerTotal));
					grandTotalField.setText(String.valueOf(upperTotal+upperBonus+lowerTotal+game.getBonus()));
				}
				else {
					JOptionPane.showMessageDialog(null, "You must roll at least once!");
				}
			}
		}
		
		class FourKindActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if(game.getRoll() > 0) {
					game.calcFourKind();
					uncheckBoxes();
					if(fourKindField.getText().isEmpty()) {
						fourKindField.setText(String.valueOf(game.getFourKind()));
						lowerTotal += game.getFourKind();
					}
					lowerTotalField.setText(String.valueOf(lowerTotal));
					grandTotalField.setText(String.valueOf(upperTotal+upperBonus+lowerTotal+game.getBonus()));
				}
				else {
					JOptionPane.showMessageDialog(null, "You must roll at least once!");
				}
			}
		}
		
		class FullHouseActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if(game.getRoll() > 0) {
					game.calcFullHouse();
					uncheckBoxes();
					if(fullHouseField.getText().isEmpty()) {
						fullHouseField.setText(String.valueOf(game.getFullHouse()));
						lowerTotal += game.getFullHouse();
					}
					lowerTotalField.setText(String.valueOf(lowerTotal));
					grandTotalField.setText(String.valueOf(upperTotal+upperBonus+lowerTotal+game.getBonus()));
				}
				else {
					JOptionPane.showMessageDialog(null, "You must roll at least once!");
				}
			}
		}
		
		class SmallStraightActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if(game.getRoll() > 0) {
					game.calcSmallStraight();
					uncheckBoxes();
					if(sStraightField.getText().isEmpty()) {
						sStraightField.setText(String.valueOf(game.getSmallStraight()));
						lowerTotal += game.getSmallStraight();
					}					
					lowerTotalField.setText(String.valueOf(lowerTotal));
					grandTotalField.setText(String.valueOf(upperTotal+upperBonus+lowerTotal+game.getBonus()));
				}
				else {
					JOptionPane.showMessageDialog(null, "You must roll at least once!");
				}
			}
		}
		
		class LargeStraightActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if(game.getRoll() > 0) {
					game.calcLargeStraight();
					uncheckBoxes();
					if(lStraightField.getText().isEmpty()) {
						lStraightField.setText(String.valueOf(game.getLargeStraight()));
						lowerTotal += game.getLargeStraight();
					}
					lowerTotalField.setText(String.valueOf(lowerTotal));
					grandTotalField.setText(String.valueOf(upperTotal+upperBonus+lowerTotal+game.getBonus()));
				}
				else {
					JOptionPane.showMessageDialog(null, "You must roll at least once!");
				}
			}
		}
		
		class YahtzeeActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if(game.getRoll() > 0) {
					game.calcYahtzee();
					uncheckBoxes();
					if(yahtzeeField.getText().isEmpty()) {
						yahtzeeField.setText(String.valueOf(game.getYahtzee()));
						lowerTotal += game.getYahtzee();
					}
					lowerTotalField.setText(String.valueOf(lowerTotal));
					grandTotalField.setText(String.valueOf(upperTotal+upperBonus+lowerTotal+game.getBonus()));
				}
				else {
					JOptionPane.showMessageDialog(null, "You must roll at least once!");
				}
			}
		}
		
		class ChanceActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if(game.getRoll() > 0) {
					game.calcChance();
					uncheckBoxes();
					if(chanceField.getText().isEmpty()) {
						chanceField.setText(String.valueOf(game.getChance()));
						lowerTotal += game.getChance();
					}
					lowerTotalField.setText(String.valueOf(lowerTotal));
					grandTotalField.setText(String.valueOf(upperTotal+upperBonus+lowerTotal+game.getBonus()));
				}
				else {
					JOptionPane.showMessageDialog(null, "You must roll at least once!");
				}
			}
		}
		
		class RollActionListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if(game.getRoll() < 3) {
					int d = 0;
					if(game.getRoll() == 0 || !cb0.isSelected()) {
						d = game.rollDie(0);
						im0.setImage("die" + (game.getDice(0)+1) + ".png");
					}
					if(game.getRoll() == 0 || !cb1.isSelected()) {
						d = game.rollDie(1);
						im1.setImage("die" + (game.getDice(1)+1) + ".png");
					}
					if(game.getRoll() == 0 || !cb2.isSelected()) {
						d = game.rollDie(2);
						im2.setImage("die" + (game.getDice(2)+1) + ".png");
					}
					if(game.getRoll() == 0 || !cb3.isSelected()) {
						d = game.rollDie(3);
						im3.setImage("die" + (game.getDice(3)+1) + ".png");
					}
					if(game.getRoll() == 0 || !cb4.isSelected()) {
						d = game.rollDie(4);
						im4.setImage("die" + (game.getDice(4)+1) + ".png");
					}
					if(d == 0) {
						JOptionPane.showMessageDialog(null, "Yahtzee Bonus!");
						yahtzeeBonusField.setText(String.valueOf(game.getBonus()));
						grandTotalField.setText(String.valueOf(upperTotal+upperBonus+lowerTotal+game.getBonus()));
					}
					else if(d == 1) {
						JOptionPane.showMessageDialog(null, "Special Yahtzee rules: aces field filled.");
						acesField.setText(String.valueOf(game.getAces()));
						upperTotal += game.getAces();
						if(upperBonus == 0 && upperTotal >= 63) {
							upperBonus = 35;
							upperBonusField.setText("35");
						}
						upperTotalField.setText(String.valueOf(upperTotal+upperBonus));
						grandTotalField.setText(String.valueOf(upperTotal+upperBonus+lowerTotal+game.getBonus()));
					}
					else if(d == 2) {
						JOptionPane.showMessageDialog(null, "Special Yahtzee rules: twos field filled.");
						twosField.setText(String.valueOf(game.getTwos()));
						upperTotal += game.getTwos();
						if(upperBonus == 0 && upperTotal >= 63) {
							upperBonus = 35;
							upperBonusField.setText("35");
						}
						upperTotalField.setText(String.valueOf(upperTotal+upperBonus));
						grandTotalField.setText(String.valueOf(upperTotal+upperBonus+lowerTotal+game.getBonus()));
					}
					else if(d == 3) {
						JOptionPane.showMessageDialog(null, "Special Yahtzee rules: threes field filled.");
						threesField.setText(String.valueOf(game.getThrees()));
						upperTotal += game.getThrees();
						if(upperBonus == 0 && upperTotal >= 63) {
							upperBonus = 35;
							upperBonusField.setText("35");
						}
						upperTotalField.setText(String.valueOf(upperTotal+upperBonus));
						grandTotalField.setText(String.valueOf(upperTotal+upperBonus+lowerTotal+game.getBonus()));
					}
					else if(d == 4) {
						JOptionPane.showMessageDialog(null, "Special Yahtzee rules: fours field filled.");
						foursField.setText(String.valueOf(game.getFours()));
						upperTotal += game.getFours();
						if(upperBonus == 0 && upperTotal >= 63) {
							upperBonus = 35;
							upperBonusField.setText("35");
						}
						upperTotalField.setText(String.valueOf(upperTotal+upperBonus));
						grandTotalField.setText(String.valueOf(upperTotal+upperBonus+lowerTotal+game.getBonus()));
					}
					else if(d == 5) {
						JOptionPane.showMessageDialog(null, "Special Yahtzee rules: fives field filled.");
						fivesField.setText(String.valueOf(game.getFives()));
						upperTotal += game.getFives();
						if(upperBonus == 0 && upperTotal >= 63) {
							upperBonus = 35;
							upperBonusField.setText("35");
						}
						upperTotalField.setText(String.valueOf(upperTotal+upperBonus));
						grandTotalField.setText(String.valueOf(upperTotal+upperBonus+lowerTotal+game.getBonus()));
					}
					else if(d == 6) {
						JOptionPane.showMessageDialog(null, "Special Yahtzee rules: sixes field filled.");
						sixesField.setText(String.valueOf(game.getSixes()));
						upperTotal += game.getSixes();
						if(upperBonus == 0 && upperTotal >= 63) {
							upperBonus = 35;
							upperBonusField.setText("35");
						}
						upperTotalField.setText(String.valueOf(upperTotal+upperBonus));
						grandTotalField.setText(String.valueOf(upperTotal+upperBonus+lowerTotal+game.getBonus()));
					}
					game.incrementRoll();
				}
				else {
					JOptionPane.showMessageDialog(null, "Roll limit exceeded!");
				}
			}
		}
		
		// Upper section
		Border upperBorder = BorderFactory.createTitledBorder("Upper Section");
		upperSectionPanel.setBorder(upperBorder);
		
		JButton acesButton = new JButton("Aces");
		acesButton.addActionListener(new AcesActionListener());
		JButton twosButton = new JButton("Twos");
		twosButton.addActionListener(new TwosActionListener());
		JButton threesButton = new JButton("Threes");
		threesButton.addActionListener(new ThreesActionListener());
		JButton foursButton = new JButton("Fours");
		foursButton.addActionListener(new FoursActionListener());
		JButton fivesButton = new JButton("Fives");
		fivesButton.addActionListener(new FivesActionListener());
		JButton sixesButton = new JButton("Sixes");
		sixesButton.addActionListener(new SixesActionListener());
		acesField = new JTextField(5);
		acesField.setEditable(false);
		twosField = new JTextField(5);
		twosField.setEditable(false);
		threesField = new JTextField(5);
		threesField.setEditable(false);
		foursField = new JTextField(5);
		foursField.setEditable(false);
		fivesField = new JTextField(5);
		fivesField.setEditable(false);
		sixesField = new JTextField(5);
		sixesField.setEditable(false);
		upperBonusField = new JTextField(5);
		upperBonusField.setEditable(false);
		upperTotalField = new JTextField(5);
		upperTotalField.setEditable(false);
		JLabel upperBonusLabel = new JLabel("Bonus");
		JLabel upperTotalLabel = new JLabel("Upper Section Total");
		
		upperSectionPanel.add(acesButton);
		upperSectionPanel.add(acesField);
		upperSectionPanel.add(twosButton);
		upperSectionPanel.add(twosField);
		upperSectionPanel.add(threesButton);
		upperSectionPanel.add(threesField);
		upperSectionPanel.add(foursButton);
		upperSectionPanel.add(foursField);
		upperSectionPanel.add(fivesButton);
		upperSectionPanel.add(fivesField);
		upperSectionPanel.add(sixesButton);
		upperSectionPanel.add(sixesField);
		upperSectionPanel.add(upperBonusLabel);
		upperSectionPanel.add(upperBonusField);
		upperSectionPanel.add(upperTotalLabel);
		upperSectionPanel.add(upperTotalField);
		
		// Lower section
		Border lowerBorder = BorderFactory.createTitledBorder("Lower Section");
		lowerSectionPanel.setBorder(lowerBorder);
		
		JButton threeKindButton = new JButton("3 of a kind");
		threeKindButton.addActionListener(new ThreeKindActionListener());
		JButton fourKindButton = new JButton("4 of a kind");
		fourKindButton.addActionListener(new FourKindActionListener());
		JButton fullHouseButton = new JButton("Full House");
		fullHouseButton.addActionListener(new FullHouseActionListener());
		JButton sStraightButton = new JButton("Small Straight");
		sStraightButton.addActionListener(new SmallStraightActionListener());
		JButton lStraightButton = new JButton("Large Straight");
		lStraightButton.addActionListener(new LargeStraightActionListener());
		JButton yahtzeeButton = new JButton("Yahtzee");
		yahtzeeButton.addActionListener(new YahtzeeActionListener());
		JButton chanceButton = new JButton("Chance");
		chanceButton.addActionListener(new ChanceActionListener());
		threeKindField = new JTextField(5);
		threeKindField.setEditable(false);
		fourKindField = new JTextField(5);
		fourKindField.setEditable(false);
		fullHouseField = new JTextField(5);
		fullHouseField.setEditable(false);
		sStraightField = new JTextField(5);
		sStraightField.setEditable(false);
		lStraightField = new JTextField(5);
		lStraightField.setEditable(false);
		yahtzeeField = new JTextField(5);
		yahtzeeField.setEditable(false);
		chanceField = new JTextField(5);
		chanceField.setEditable(false);
		yahtzeeBonusField = new JTextField(5);
		yahtzeeBonusField.setEditable(false);
		lowerTotalField = new JTextField(5);
		lowerTotalField.setEditable(false);
		grandTotalField = new JTextField(5);
		grandTotalField.setEditable(false);
		JLabel yahtzeeBonusLabel = new JLabel("Yahtzee Bonus");
		JLabel lowerTotalLabel = new JLabel("Lower Section Total");
		JLabel grandTotalLabel = new JLabel("Grand Total");
		
		lowerSectionPanel.add(threeKindButton);
		lowerSectionPanel.add(threeKindField);
		lowerSectionPanel.add(fourKindButton);
		lowerSectionPanel.add(fourKindField);
		lowerSectionPanel.add(fullHouseButton);
		lowerSectionPanel.add(fullHouseField);
		lowerSectionPanel.add(sStraightButton);
		lowerSectionPanel.add(sStraightField);
		lowerSectionPanel.add(lStraightButton);
		lowerSectionPanel.add(lStraightField);
		lowerSectionPanel.add(yahtzeeButton);
		lowerSectionPanel.add(yahtzeeField);
		lowerSectionPanel.add(chanceButton);
		lowerSectionPanel.add(chanceField);
		lowerSectionPanel.add(yahtzeeBonusLabel);
		lowerSectionPanel.add(yahtzeeBonusField);
		lowerSectionPanel.add(lowerTotalLabel);
		lowerSectionPanel.add(lowerTotalField);
		lowerSectionPanel.add(grandTotalLabel);
		lowerSectionPanel.add(grandTotalField);
		
		sectionPanel.add(upperSectionPanel);
		sectionPanel.add(lowerSectionPanel);
		
		// Roll button
		JButton rollButton = new JButton("Roll");
		rollButton.addActionListener(new RollActionListener());
		
		rollPanel.add(rollButton);
		
		// Dice panel
		im0 = new ImagePanel("die" + (game.getDice(0)+1) + ".png");
		im1 = new ImagePanel("die" + (game.getDice(1)+1) + ".png");
		im2 = new ImagePanel("die" + (game.getDice(2)+1) + ".png");
		im3 = new ImagePanel("die" + (game.getDice(3)+1) + ".png");
		im4 = new ImagePanel("die" + (game.getDice(4)+1) + ".png");
		im0.scaleImage(0.4);
		im1.scaleImage(0.4);
		im2.scaleImage(0.4);
		im3.scaleImage(0.4);
		im4.scaleImage(0.4);
		cb0 = new JCheckBox("Keep");
		cb1 = new JCheckBox("Keep");
		cb2 = new JCheckBox("Keep");
		cb3 = new JCheckBox("Keep");
		cb4 = new JCheckBox("Keep");
		
		dicePanel.add(im0);
		dicePanel.add(im1);
		dicePanel.add(im2);
		dicePanel.add(im3);
		dicePanel.add(im4);
		dicePanel.add(cb0);
		dicePanel.add(cb1);
		dicePanel.add(cb2);
		dicePanel.add(cb3);
		dicePanel.add(cb4);
		
		gamePanel.add(sectionPanel, BorderLayout.CENTER);
		gamePanel.add(rollPanel, BorderLayout.EAST);
		gamePanel.add(dicePanel, BorderLayout.SOUTH);
		
		add(gamePanel);
	}
	
	// Helper function to uncheck checkboxes
	private void uncheckBoxes() {
		if(game.getRoll() == 0) {
			cb0.setSelected(false);
			cb1.setSelected(false);
			cb2.setSelected(false);
			cb3.setSelected(false);
			cb4.setSelected(false);
		}
	}
	
	public static void main(String args[]) {
		YahtzeeFrame yahtzee = new YahtzeeFrame();
		yahtzee.setLocationRelativeTo(null);
		yahtzee.setVisible(true);
		
		// Simpler to have server on together
		SaveServer saveServer = new SaveServer();
		do {
			saveServer.execute();
		} while(saveServer.getSocket().isClosed());
	}
}
