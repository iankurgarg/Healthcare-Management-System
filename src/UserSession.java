import java.sql.ResultSet;

public class UserSession {
	private String userid;
	private String password;
	private int userType;
	private Patient p;
	private HS h;
	
	static String tableName = "person";
	static String colID = "userid";
	static String colPassword = "password";
	static String colfName = "fname";
	static String collName = "lname";
	static String colAddress = "address";
	static String colPhone = "phone";
	static String colGender = "gender";
	static String colDOB = "dob";

	
	public UserSession () {
	}
	
	public void MainView() {
		int option = 0;
		
		while (option != 3) {
			System.out.println("Select from the following options:\n"
					+ "1. Login as patient\n"
					+ "2. Login as Health Supporter\n"
					+ "3. Go Back");
			option = StaticFunctions.nextInt();
			if (option != 1 && option !=2)
				continue;
			userType = option;
			System.out.println("Enter username:");
			this.userid = StaticFunctions.next();
			System.out.println("Enter password:");
			this.password = StaticFunctions.next();
			
			if (authenticate() == 0) {
				System.out.println("Success: User authenticated");
				if (userType == 1) {
					p = new Patient(this.userid);
					p.MainView();
				}
				else {
					h = new HS(this.userid);
					h.MainView();
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
		String query ="SELECT * FROM "+tableName+" P WHERE P."+colID+"='"+this.userid+"' AND P."+colPassword+"='"+this.password+"'";
		ResultSet rs = DatabaseConnector.runQuery(query);
		if (rs == null)
			return -1;
		try {
			if(!rs.next())
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
	}
}
