package doa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class DBConnection {
	public Connection connect() {
		String url="jdbc:postgresql://pgdb.veapp.net:5440/training_2023";
		String user="training";
		String password="training@23";
	    Connection conn = null;
	    while(conn==null) {
	    	try {
		        Class.forName("org.postgresql.Driver");
		        conn = DriverManager.getConnection(url, user, password);
		    } catch (SQLException e) {
		        System.out.println(e.getMessage());
		        System.out.println("Enter any input to retry:");
		    	new Scanner(System.in).nextLine();
		    	
		    } catch (Exception e) {
		    }
	    }

	    return conn;
	}
}
