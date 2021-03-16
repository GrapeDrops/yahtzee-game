package server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class SaveServer extends JFrame {

	private JTextArea wordsBox;
	private Socket socket;
	private Connection conn;
	private PreparedStatement save;
	private PreparedStatement load;
	private ServerSocket ss;
	
	public SaveServer() {
		createMainPanel();
		wordsBox.append("Ready to Accept Connections\n");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,400);
		setVisible(true);
		try {
			ss = new ServerSocket(7777);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:yahtzee.db");
			PreparedStatement table = conn.prepareStatement("CREATE TABLE IF NOT EXISTS YAHTZEE ("
					+ "Name varchar(20) PRIMARY KEY,"
					+ "D0 varchar(10),"
					+ "D1 varchar(10),"
					+ "D2 varchar(10),"
					+ "D3 varchar(10),"
					+ "D4 varchar(10),"
					+ "C0 varchar(10),"
					+ "C1 varchar(10),"
					+ "C2 varchar(10),"
					+ "C3 varchar(10),"
					+ "C4 varchar(10),"
					+ "C5 varchar(10),"
					+ "Roll varchar(10),"
					+ "Joker varchar(10),"
					+ "Aces varchar(10),"
					+ "Twos varchar(10),"
					+ "Threes varchar(10),"
					+ "Fours varchar(10),"
					+ "Fives varchar(10),"
					+ "Sixes varchar(10),"
					+ "ThreeKind varchar(10),"
					+ "FourKind varchar(10),"
					+ "FullHouse varchar(10),"
					+ "SmallStraight varchar(10),"
					+ "LargeStraight varchar(10),"
					+ "Yahtzee varchar(10),"
					+ "Chance varchar(10),"
					+ "Bonus varchar(10))");
			table.execute();
			save = conn.prepareStatement("INSERT INTO YAHTZEE "
					+ "(Name, D0, D1, D2, D3, D4, C0, C1, C2, C3, C4, C5, Roll, Joker, Aces, "
					+ "Twos, Threes, Fours, Fives, Sixes, ThreeKind, FourKind, FullHouse, "
					+ "SmallStraight, LargeStraight, Yahtzee, Chance, Bonus) Values "
					+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
					+ "ON CONFLICT(Name) DO UPDATE SET Name=?, D0=?, D1=?, D2=?, D3=?, D4=?, C0=?, C1=?, "
					+ "C2=?, C3=?, C4=?, C5=?, Roll=?, Joker=?, Aces=?, Twos=?, Threes=?, Fours=?, Fives=?, "
					+ "Sixes=?, ThreeKind=?, FourKind=?, FullHouse=?, SmallStraight=?, LargeStraight=?, "
					+ "Yahtzee=?, Chance=?, Bonus=?");
			load = conn.prepareStatement("SELECT * FROM YAHTZEE WHERE Name = ?");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createMainPanel() {
		wordsBox = new JTextArea(35,10);

		JScrollPane listScroller = new JScrollPane(wordsBox);
		this.add(listScroller, BorderLayout.CENTER);
		listScroller.setPreferredSize(new Dimension(250, 80));
	}
	
	// This function is for having a server on YahtzeeFrame, it's simpler to run the game and server at once
	public Socket getSocket() {
		return socket;
	}
	
	public void execute() {
		try {
			PreparedStatement stmt;
			socket = ss.accept();
			wordsBox.append("\nConnected. \n");
			InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String nextLine = br.readLine();
            if(nextLine.equals("SAVE")) {
            	wordsBox.append("SAVE command received \n");
            	stmt = save;
            	for(int i = 1; i <= 28; i++) {
            		nextLine = br.readLine();
            		stmt.setString(i, nextLine);
            		stmt.setString(i+28, nextLine);
            	}
            	stmt.executeUpdate();
            	wordsBox.append("SAVE completed. \n");
            }
            else if(nextLine.equals("LOAD")) {
            	wordsBox.append("LOAD command received \n");
            	stmt = conn.prepareStatement("SELECT Name from YAHTZEE");
            	ResultSet rset = stmt.executeQuery();
            	PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true);
            	while(rset.next()) {
            		printWriter.println(rset.getString(1));
            	}
            	printWriter.println("ABCDEFGHIJKLMNOPQRSTU"); // Explained in YahtzeeFrame lines 108-110
            	wordsBox.append("Sent names \n");
            	String name = br.readLine(); // User selected name
            	if(name.equals("ABCDEFGHIJKLMNOPQRSTU")) {
            		wordsBox.append("Other side closed. \n");
            		wordsBox.append("Connection closed. \n");
                    socket.close();
                    return;
            	}
            	wordsBox.append("Selected: " + name + "\n");
            	stmt = load;
            	stmt.setString(1, name);
            	rset = stmt.executeQuery();
            	wordsBox.append("getting data \n");
            	rset.next();
            	for(int i=1; i <= 28; i++) printWriter.println(rset.getString(i));
            	wordsBox.append("data sent \n");
            }
            wordsBox.append("Connection closed. \n");
            socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] main) {
		SaveServer saveServer = new SaveServer();
		do {
			saveServer.execute();
		} while(saveServer.getSocket().isClosed());
	}
}
