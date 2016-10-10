import java.sql.ResultSet;
import java.util.Scanner;

public class UserSession {
	private String userid;
	private String password;
	private int userType;
	private Scanner sc;
	private Patient p;
	
	static String tableName = "users";
	static String colID = "userID";
	private static String colPassword = "password";
	static String colName = "Name";
	static String colAddress = "Address";
	static String colPhone = "Phone";

	
	public UserSession () {
		sc = new Scanner(System.in);
	}
	
	public void MainView() {
		int option = 0;
		
		while (option != 3) {
			System.out.println("Select from the following options:\n"
					+ "1. Login as patient\n"
					+ "2. Login as Health Supporter\n"
					+ "3. Exit");
			option = sc.nextInt();
			if (option == 3)
				continue;
			userType = option;
			System.out.println("Enter username:");
			this.userid = sc.next();
			System.out.println("Enter password:");
			this.password = sc.next();
			
			if (authenticate() == 0) {
				System.out.println("Success: User authenticated");
				if (userType == 1) {
					p = new Patient(this.userid);
					p.MainView();
				}
				else {
					
				}
			}
			else {
				System.err.println("Error: Unable to authenticate. Invalid Username or password");
			}
		}
		
		logout();
		return;
	}

	private int authenticate() {
		String query ="SELECT * FROM "+UserSession.tableName+" U WHERE U."+colID+"="+this.userid+" AND U."+colPassword+"="+this.password;
		ResultSet rs = DatabaseConnector.runQuery(query);
		if (rs == null)
			return -1;
		try {
			if(rs.isLast() || rs.isAfterLast())
				return -1;
		}
		catch (Exception e) {
		}
		return 0;
	}
	
	private void logout() {
		userid = null;
		password = null;
		userType = -1;
		sc.close();
	}
}
