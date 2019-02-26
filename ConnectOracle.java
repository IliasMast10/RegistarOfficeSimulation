package src;

import java.sql.*;

/* Ilias Mastoras
 * 
 * This class connects with the Oracle Database.
 */

public class ConnectOracle {
		
	// variables
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;
    Login  login;
    
    public ConnectOracle() {
    	
    	// Step 1: Loading or registering Oracle JDBC driver class
    	try {
    		Class.forName("oracle.jdbc.driver.OracleDriver");
    	}
    	catch(ClassNotFoundException cnfex) {
    		System.out.println("Problem in loading or "
                + "registering Oracle JDBC driver");
            cnfex.printStackTrace();
        }

    	// Step 2: Opening database connection
    	try {
    		String dbURL = "jdbc:oracle:thin:@localhost:1521:xe";
    		String username = "system";
    		String password = "Katerina13"; 

    		// Step 3. Create and get connection using DriverManager class
    		connection = DriverManager.getConnection(dbURL,username,password); 
            System.out.println("Connection established!");
    	}
        catch(SQLException sqlex){
            sqlex.printStackTrace();
        }	
    }//end of constructor
}

