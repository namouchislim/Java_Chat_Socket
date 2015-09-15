import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.TextArea;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;


public class Client {

	private JFrame frmFenetreClient;

	TextArea message;

	static TextArea historique;
	JButton envoyer,histo;
	JComboBox choix;
	static String ligne;
	static Socket conn;
	static ConectionMySQL sql;
	static Connection con;
	public static void main(String[] args) throws IOException {
		
					Client window = new Client();
					window.frmFenetreClient.setVisible(true);
					Date date;
					String h;
					 conn = new Socket("localhost", 20000);
					 sql=new ConectionMySQL();
					con=sql.CreateConnection();
					System.out.println("Connection etablie avec serveur "+conn.getRemoteSocketAddress());
					BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					while (true) {
						
						
				             ligne = in.readLine();         
				           if (ligne.equals("end")) break;
				           System.out.println(ligne); 
				           historique.setBackground(Color.LIGHT_GRAY);
				           date= new Date();
							h=date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
				           historique.append("\n"+h+"  Serveur :   ");
				           historique.append(ligne);
				           //in.println(l);                     
				        }//conn.close();
			
	}

	/**
	 * Create the application.
	 */
	public Client() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFenetreClient = new JFrame();
		frmFenetreClient.setResizable(false);
		frmFenetreClient.setIconImage(Toolkit.getDefaultToolkit().getImage("./images/icon.png"));
		frmFenetreClient.setTitle("Fenetre Client");
		frmFenetreClient.setBounds(100, 100, 635, 430);
		frmFenetreClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmFenetreClient.getContentPane().setLayout(null);
		
		 historique = new TextArea();
		historique.setEditable(false);
		historique.setBackground(Color.WHITE);
		historique.setBounds(36, 10, 423, 195);
		frmFenetreClient.getContentPane().add(historique);
		
		 message = new TextArea();
		message.setBackground(Color.WHITE);
		message.setBounds(36, 260, 415, 105);
		message.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				
				if(arg0.getKeyCode()== KeyEvent.VK_ENTER)
				{
					try {
						send();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
			}});
		frmFenetreClient.getContentPane().add(message);
		
		 envoyer = new JButton("Envoyer");
		 envoyer.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
		 		try {
					send();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		 	}
		 });
		envoyer.setIcon(new ImageIcon("./images/enter.gif"));
		envoyer.setBounds(469, 260, 129, 29);
		frmFenetreClient.getContentPane().add(envoyer);
		
		choix = new JComboBox();
		choix.setModel(new DefaultComboBoxModel(new String[] {"Aujourd\u2019hui", "Hier", "Derri\u00E8re semaine", "Il ya un mois", "Tous"}));
		choix.setBounds(83, 211, 129, 29);
		frmFenetreClient.getContentPane().add(choix);
		
		 histo = new JButton("Historique");
		 histo.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
		 		try {
					ResultSet res=sql.afficherHistorique(con,choix.getSelectedIndex()+1);
					historique.setText("");
					if(!(res.next()))
						historique.append("Pas d'historique");
					else{
					while(res.next()){	
					historique.append("\n"+res.getString("datemsg")+"-"+res.getString("heuremsg")+"  "+res.getString("msg"));
					}
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		 	}
		 });
		histo.setBounds(241, 211, 129, 29);
		frmFenetreClient.getContentPane().add(histo);
	}
	public void send() throws SQLException{
		Date date= new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		String h=date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
		sql.ajouterHistorique(con, conn.getLocalPort(),formatter.format(date), h, "  Client   :   "+message.getText());
		historique.append("\n"+h);
		historique.append("  Client   :   ");
		historique.append(message.getText());
		historique.setBackground(Color.white);
		
		PrintWriter out;
		try {
			out = new PrintWriter(conn.getOutputStream(),true);
			out.println(message.getText());
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		message.setText("");
	}
}
