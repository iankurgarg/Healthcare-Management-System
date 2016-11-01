import java.sql.Date;
import java.sql.ResultSet;

public class Profile {
	private String UID;
	private String FName;
	private String LName;
	private String Address;
	private Date dob;
	private String gender;
	private String Phone;
	private String profileOptions = "Select an option:\n"
			+ "1. Update First Name\n"
			+ "2. Update Last Name\n"
			+ "3. Update Gender\n"
			+ "4. Update Date of Birth\n"
			+ "5. Update Address\n"
			+ "6. Update Phone\n"
			+ "7. Go Back";
	
	public Profile (String ID) {
		this.UID = ID;
		fetchProfile();
	}
	
	public void MainView() {
		viewProfileOptions();
		exit();
	}
	
	public void exit() {
	}
	
	private void fetchProfile() {
		if (UID != null) {
			if (FName == null || LName == null || Address == null || Phone == null){
				String query = "SELECT P."+UserSession.colfName+", P."+UserSession.collName+", P."+UserSession.colAddress+
						", P."+UserSession.colPhone+", P."+UserSession.colGender+", P."+
						UserSession.colDOB+" FROM "+UserSession.tableName+
						" P WHERE P."+UserSession.colID+" = '"+UID+"'";
				ResultSet res = DatabaseConnector.runQuery(query);
				try {
					if (!res.next())
						System.err.println("Error: Couldn't fetch Patient's Profile Information");
					else {
						res.first();
						this.FName = res.getString(1);
						this.LName = res.getString(2);
						this.Address = res.getString(3);
						this.Phone = res.getString(4);
						this.gender = res.getString(5);
						this.dob = res.getDate(6);
					}
				}
				catch (Exception e) {
					System.out.println("Error: Unable to fetch Patient Profile Information");
					e.printStackTrace();
				}
			}
		}
	}
	
	private void updateDetails(int option) {
		switch(option) {
		case 1:
		{
			String newName = "";
			System.out.println("Enter new First Name:");
			StaticFunctions.nextLine();
			newName = StaticFunctions.nextLine();
			if (newName.equals("")) {
				System.out.println("Name not updated");
				break;
			}
			updateName(newName, 1);
			break;
		}
		case 2:
		{
			String newName = "";
			System.out.println("Enter new Last Name:");
			StaticFunctions.nextLine();
			newName = StaticFunctions.nextLine();
			if (newName.equals("")) {
				System.out.println("Name not updated");
				break;
			}
			updateName(newName, 2);
			break;
		}
		case 3:
		{
			String newGender = "";
			System.out.println("Enter new Gender (M/F):");
			StaticFunctions.nextLine();
			newGender = StaticFunctions.next();
			if (newGender.equals("")) {
				System.out.println("Gender not updated");
				break;
			}
			updateGender(newGender);
			break;
		}
		case 4:
		{
			String newName = "";
			System.out.println("Enter new Date of Birth (MM-DD-YYYY):");
			StaticFunctions.nextLine();
			newName = StaticFunctions.nextLine();
			if (newName.equals("")) {
				System.out.println("Date of Birth not updated");
				break;
			}
			updateDOB(newName);
			break;
		}
		case 5:
		{
			String newAddress = "";
			System.out.println("Enter new Address:");
			StaticFunctions.nextLine();
			newAddress = StaticFunctions.nextLine();
			if (newAddress.equals("")) {
				System.out.println("Address not updated");
				break;
			}
			updateAddress(newAddress);
			break;
		}
		case 6:
		{
			String newPhone = "";
			System.out.println("Enter new Phone:");
			StaticFunctions.nextLine();
			newPhone = StaticFunctions.nextLine();
			if (newPhone.equals("")) {
				System.out.println("Phone not updated");
				break;
			}
			updatePhone(newPhone);
			break;
		}
		default:
			System.out.println("Invalid option selection");
		}
	}
	
	public String getUID() {
		return this.UID;
	}
	
	public String getName() {
		return FName+" "+LName;
	}
	
	public String getAddress() {
		return Address;
	}
	
	public String getPhone() {
		return Phone;
	}
	
	public String getGender() {
		return gender;
	}
	
	public Date getDOB() {
		return dob;
	}
	
	private void updateName(String newName, int t) {
		int r = -1;
		if (t == 1) {
			this.FName = newName;
			String query = "UPDATE "+UserSession.tableName+" P SET "+UserSession.colfName+"='"+newName+"' WHERE "+UserSession.colID+"='"+this.UID+"'";
			r = DatabaseConnector.updateDB(query);
		}
		else if (t == 2){
			this.LName = newName;
			String query = "UPDATE "+UserSession.tableName+" P SET "+UserSession.collName+"='"+newName+"' WHERE "+UserSession.colID+"='"+this.UID+"'";
			r = DatabaseConnector.updateDB(query);
		}
		
		if (r == 0) {
			System.out.println("Couldn't update Name: Possible invalid input");
		}
		else
			System.out.println("Name Updated");
	}
	
	private void updateAddress(String newAddress) {
		this.Address = newAddress;
		String query = "UPDATE "+UserSession.tableName+" P SET "+UserSession.colAddress+"='"+newAddress+"' WHERE "+UserSession.colID+"='"+this.UID+"'";
		int r = DatabaseConnector.updateDB(query);
		if (r == 0) {
			System.out.println("Couldn't update Address: Possible invalid input");
		}
		else
			System.out.println("Address Updated");
	}
	
	private void updatePhone(String newPhone) {
		this.Phone = newPhone;
		String query = "UPDATE "+UserSession.tableName+" P SET "+UserSession.colPhone+"='"+newPhone+"' WHERE "+UserSession.colID+"='"+this.UID+"'";
		int r = DatabaseConnector.updateDB(query);
		
		if (r == 0) {
			System.out.println("Couldn't update Phone: Possible invalid input");
		}
		else
			System.out.println("Phone Updated");
	}
	
	private void updateGender(String newGender) {
		this.gender = newGender;
		String query = "UPDATE "+UserSession.tableName+" P SET "+UserSession.colGender+"='"+newGender+"' WHERE "+UserSession.colID+"='"+this.UID+"'";
		int r = DatabaseConnector.updateDB(query);
		
		if (r == 0) {
			System.out.println("Couldn't update Gender: Possible invalid input");
		}
		else
			System.out.println("Gender Updated");
		
		fetchProfile();
	}
	
	private void updateDOB (String newDOB) {
		String query = "UPDATE "+UserSession.tableName+" P SET "+UserSession.colDOB+"=TO_DATE('"+newDOB+"','MM-DD-YYYY') WHERE "+UserSession.colID+"='"+this.UID+"'";
		int r = DatabaseConnector.updateDB(query);
		
		if (r == 0) {
			System.out.println("Couldn't update Date of Birth: Possible invalid input");
		}
		else
			System.out.println("Date of Birth Updated");
		
		fetchProfile();
	}
	
	public void viewProfile(){
		System.out.println("Name = "+FName+" " + LName);
		System.out.println("Gender = "+gender);
		System.out.println("Date of Birth = "+dob.toString());
		System.out.println("Address = "+Address);
		System.out.println("Phone = "+Phone);
	}
	
	private void viewProfileOptions() {
		if (!(FName == null)) {
			viewProfile();

			int option = 0;
			while (option != 7) {
				System.out.println(profileOptions);
				option = StaticFunctions.nextInt();
				if (option == 7)
					break;
				updateDetails(option);
			}
			return;
		}
		else {
			System.err.println("Error: First Name is Empty");
		}
	}
}
