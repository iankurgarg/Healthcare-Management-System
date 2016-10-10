import java.sql.ResultSet;
import java.util.Scanner;

public class Profile {
	private String UID;
	private String Name;
	private String Address;
	private String Phone;
	private String profileOptions = "Select an option:\n"
			+ "1. Update Name\n"
			+ "2. Update Address\n"
			+ "3. Update Phone\n"
			+ "4. Go Back";
	private Scanner sc;
	
	public Profile (String ID) {
		this.UID = ID;
		sc = new Scanner(System.in);
	}
	
	public void MainView() {
		fetchProfile();
		viewProfileOptions();
		exit();
	}
	
	private void exit() {
		UID = null;
		Name = "";
		Address = "";
		Phone = "";
		sc.close();
	}
	
	private void fetchProfile() {
		if (UID != null) {
			if (Name == null || Address == null || Phone == null){
				String query = "SELECT U."+UserSession.colName+", U."+UserSession.colAddress+
						", U."+UserSession.colPhone+" FROM "+UserSession.tableName+
						" U WHERE U."+UserSession.colID+" = ID";
				ResultSet res = DatabaseConnector.runQuery(query);
				try {
					if (res == null || res.isAfterLast())
						System.err.println("Error: Couldn't fetch Patient's Profile Information");
					else {
						this.Name = res.getString(1);
						this.Address = res.getString(2);
						this.Phone = res.getString(3);
					}
				}
				catch (Exception e) {
					System.out.println("Error: Unable to fetch Patient Profile Information");
				}
			}
		}
	}
	
	private void updateDetails(int option) {
		switch(option) {
		case 1:
		{
			String newName = "";
			while (newName.equals("")) {
				System.out.println("Enter new Name:");
				newName = sc.nextLine();
			}
			updateName(newName);
			break;
		}
		case 2:
		{
			String newAddress = "";
			while (newAddress.equals("")) {
				System.out.println("Enter new Address:");
				newAddress = sc.nextLine();
			}
			updateAddress(newAddress);
			break;
		}
		case 3:
		{
			String newPhone = "";
			while (newPhone.equals("")) {
				System.out.println("Enter new Phone:");
				newPhone = sc.nextLine();
			}
			updatePhone(newPhone);
			break;
		}
		default:
			System.out.println("Invalid option selection");
		}
	}
	
	private void updateName(String newName) {
		//TODO
		this.Name = newName;
		System.out.println("Name Updated");
	}
	
	private void updateAddress(String newAddress) {
		//TODO
		this.Address = newAddress;
		System.out.println("Address Updated");
	}
	
	private void updatePhone(String newPhone) {
		//TODO
		this.Phone = newPhone;
		System.out.println("Phone Updated");
	}
	
	private void viewProfileOptions() {
		if (!(Name == null || Address == null || Phone == null)) {
			System.out.println("Name = "+Name);
			System.out.println("Address = "+Address);
			System.out.println("Phone = "+Phone);
			
			int option = 0;
			while (option != 4) {
				System.out.println(profileOptions);
				option = sc.nextInt();
				updateDetails(option);
			}
			return;
		}
		else {
			System.err.println("Error: Name or Address or Phone is Empty");
		}
	}
}
