import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.TextArea;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
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


public class Serveur {

	private JFrame frmFenetreServeur;

	TextArea message;

	static TextArea historique;
	JButton envoyer,histo;
	JComboBox choix;
	static String ligne ;
	static ServerSocket s;
	static Socket conn;
	static ConectionMySQL sql;
	static Connection con;
	
	public static void main(String[] args) throws IOException {
		
					Serveur window = new Serveur();
					window.frmFenetreServeur.setVisible(true);
					
					String h;
					Date date;
					 sql=new ConectionMySQL();
					con=sql.CreateConnection();
					s = new ServerSocket(20000);
					System.out.println("Serveur en ecoute sur le port "+s.getLocalPort());
					 conn = s.accept();
					System.out.println("Connection recue de "+conn.getRemoteSocketAddress());
					BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					while (true) {
						
				            ligne = in.readLine();         
				           if (ligne.equals("end")) break;
				           date= new Date();
							h=date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
				          historique.append("\n"+h+"  Client :   ");
				          historique.setBackground(Color.LIGHT_GRAY);
				           historique.append(ligne);
				                               
				        }
					
			
	}

	/**
	 * Create the application.
	 */
	public Serveur() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFenetreServeur = new JFrame();
		frmFenetreServeur.setResizable(false);
		frmFenetreServeur.setIconImage(Toolkit.getDefaultToolkit().getImage("./images/icon.png"));
		frmFenetreServeur.setTitle("Fenetre Serveur");
		frmFenetreServeur.setBounds(100, 100, 635, 430);
		frmFenetreServeur.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmFenetreServeur.getContentPane().setLayout(null);
		
		 historique = new TextArea();
		historique.setEditable(false);
		historique.setBackground(Color.WHITE);
		historique.setBounds(36, 10, 423, 195);
		frmFenetreServeur.getContentPane().add(historique);
		
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
		frmFenetreServeur.getContentPane().add(message);
		
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
		frmFenetreServeur.getContentPane().add(envoyer);
		
		 choix = new JComboBox();
		choix.setModel(new DefaultComboBoxModel(new String[] {"Aujourd\u2019hui", "Hier", "Derri\u00E8re semaine", "Il ya un mois", "Tous"}));
		choix.setBounds(83, 211, 129, 29);
		frmFenetreServeur.getContentPane().add(choix);
		
		 histo = new JButton("Historique");
		 histo.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {

				historique.setText("");
				try {
					ResultSet res=sql.afficherHistorique(con,choix.getSelectedIndex()+1);
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
		frmFenetreServeur.getContentPane().add(histo);
	}
	public void send() throws SQLException{
		Date date= new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		String h=date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
		sql.ajouterHistorique(con, s.getLocalPort(),formatter.format(date), h, "  Serveur :   "+message.getText());
		//historique.append("\n"+h+"     "+formatter.format(date));
		historique.append("\n"+h);
		historique.append("  Serveur :   ");
		historique.append(message.getText());
		historique.setBackground(Color.WHITE);
		
		PrintWriter out;
		try {
			out = new PrintWriter(conn.getOutputStream());
			out.println(message.getText());
			out.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		message.setText("");
		//sortie.flush();
		
	}
}
