
public class Signup {
	private String userid;
	private String password;
	private String fname;
	private String lname;
	private String address;
	private String dob;
	private String phone;
	private String gender;
	
	public Signup() {
		
	}
	
	public void MainView() {
		int option = 0;
		
		System.out.println("Select from the following options:\n"
				+ "1. Signup as patient\n"
				+ "2. Signup as Health Supporter\n"
				+ "3. Go Back");
		option = StaticFunctions.nextInt();
		if (option == 1 || option == 2)
		
		System.out.println("Enter userid:");
		StaticFunctions.nextLine();
		userid = StaticFunctions.nextLine();
		System.out.println("Enter password:");
		password = StaticFunctions.nextLine();
		System.out.println("Enter First Name:");
		fname = StaticFunctions.next();
		StaticFunctions.nextLine();
		System.out.println("Enter Last Name:");
		lname = StaticFunctions.nextLine();
		System.out.println("Enter Gender:");
		gender = StaticFunctions.next();
		StaticFunctions.nextLine();
		System.out.println("Enter Address:");
		address = StaticFunctions.nextLine();
		System.out.println("Enter Date of Birth (MM-DD-YYYY):");
		dob = StaticFunctions.next();
		StaticFunctions.nextLine();
		System.out.println("Enter Phone:");
		phone = StaticFunctions.nextLine();
		
		addUser();
		if (option == 1) {
			addPatient();
		}
		
		exit();
		return;
	}
	
	private void addPatient() {
		String query = "INSERT INTO "+Patient.tableName+" VALUES ('"+this.userid+"')";
		int r = DatabaseConnector.updateDB(query);
		if (r == 0) {
			System.out.println("Unable to add patient");
		}
	}

	private void addUser() {
		String query = "INSERT INTO "+UserSession.tableName+" VALUES('"+userid+"','"+fname+"','"+lname
				+"',TO_DATE('"+dob+"','MM-DD-YYYY'),'"+gender+"','"+address+"','"+phone+"','"+password+"')";
		int r = DatabaseConnector.updateDB(query);
		if (r == 0) {
			System.out.println("Unable to add user, data format incorrect");
		}
		else {
			System.out.println("User added");
		}
		
	}

	private void exit() {
		
	}
}
