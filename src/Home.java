import java.sql.*;
import java.util.Scanner;
/**
 * @author Ankur Garg, agarg12@ncsu.edu
 */

public class Home {

    static final String jdbcURL 
	= "jdbc:oracle:thin:@orca.csc.ncsu.edu:1521:orcl01";

    public static void main(String[] args) throws Exception{
/*
        DatabaseConnector dbc = new DatabaseConnector("agarg12", "200157990");
        	
        dbc.connect();
        
        
//        UserSession us = UserSession.CreateUserSession(dbc, "dfs", "");
        /*
		dbc.updateDB("CREATE TABLE COFFEES1 " +
			   "(COF_NAME VARCHAR(32), SUP_ID INTEGER, " +
			   "PRICE FLOAT, SALES INTEGER, TOTAL INTEGER)");

		// Populate the COFFEES table

		dbc.updateDB("INSERT INTO COFFEES1 " +
			   "VALUES ('Colombian', 101, 7.99, 0, 0)");

		dbc.updateDB("INSERT INTO COFFEES1 " +
			   "VALUES ('French_Roast', 49, 8.99, 0, 0)");

		dbc.updateDB("INSERT INTO COFFEES1 " +
			   "VALUES ('Espresso', 150, 9.99, 0, 0)");

		dbc.updateDB("INSERT INTO COFFEES1 " +
			   "VALUES ('Colombian_Decaf', 101, 8.99, 0, 0)");

		dbc.updateDB("INSERT INTO COFFEES1 " +
			   "VALUES ('French_Roast_Decaf', 49, 9.99, 0, 0)");

		// Get data from the COFFEES table
        */
//        us.logout();
    	/*
		ResultSet rs = dbc.runQuery("SELECT COF_NAME, PRICE FROM COFFEES1");

		while (rs!=null && rs.next()) {
		    String s = rs.getString("COF_NAME");
		    float n = rs.getFloat("PRICE");
		    System.out.println(s + "   " + n);
		}
		*/
//    	Runtime.getRuntime().exec("clear");
    	UserSession us = new UserSession();
    	us.MainView();
    }
}



