import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
public class ConectionMySQL {
	Connection conn=null;
	public Connection CreateConnection (){
		String url = "jdbc:mysql://127.0.0.1/chat";
		String login = "root";
		String password = "";
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, login, password);
			
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),
					"ERREUR DE CONNEXION ! ", JOptionPane.ERROR_MESSAGE);
		}
		return conn;
	}
	public void ajouterHistorique(Connection con,int port, String date,String heure,String msg) throws SQLException{
		String query="insert into historique VALUES('"+date+"','"+msg+"',"+port+",'"+heure+"')";
		
		java.sql.Statement st=con.createStatement();
		st.executeUpdate(query);
		st.close();
		
	}
	
	public ResultSet afficherHistorique(Connection con,int choix) throws SQLException{
		Date date= new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		String d=formatter.format(date);
		System.out.println(d);
		String query = "";
		switch (choix){
		case 1:{ query="select *from historique where datemsg=CURDATE();";break;}
		case 2:	{query="select *from historique where datemsg=CURDATE()-1;";	break;}
		case 3:{ query="select *from historique where datemsg>CURDATE()-7;";	break;}
		case 4:{ query="select *from historique where datemsg>CURDATE()-30;";	break;}
		case 5:{ query="select *from historique;";break;}
	}
		java.sql.Statement st=con.createStatement();
		ResultSet res=null;
		res=st.executeQuery(query);
		return res;
		
	}
	}
	
	


