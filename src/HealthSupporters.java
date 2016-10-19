import java.sql.Date;
import java.sql.ResultSet;
import java.util.Scanner;

public class HealthSupporters {
	private String patientID;
	private Profile primaryHS;
	private Profile secondaryHS;
	private Date since1;
	private Date since2;
	private Scanner sc;
	
	public HealthSupporters(String PatientID) {
		this.patientID = PatientID;
		sc = new Scanner(System.in);
		fetchHealthSupporterInformation();
	}
	
	public void MainView() {
		int option = 0;
		while(option != 3) {
			System.out.println("Choose from the following options:\n"
					+ "1. View Health Supporters\n"
					+ "2. Add/Change Health Supporters.\n"
					+ "3. Go Back");
			option = sc.nextInt();
			switch(option) {
			case 1:
				viewHealthSupporters();
				break;
			case 2:
				updateHS();
				break;
			case 3:
				break;
			default:
				System.out.println("Invalid Input, enter again!");
			}
		}
		exit();
	}
	
	public boolean hasHS() {
		if (primaryHS != null || secondaryHS != null)
			return true;
		else
			return false;
	}
	
	public void SickPatientWithNoHSView() {
		if (!hasHS()) {
			System.out.println("Enter Health Supporter Details:\n");
			updateHS(0);
		}
	}
	
	private void exit() {
		primaryHS.exit();
		secondaryHS.exit();
		sc.close();
	}

	private void fetchHealthSupporterInformation() {
		String query1 = "SELECT HP.PHSID, HP.since FROM hasprimary HP WHERE HP.patientID = "+this.patientID;
		ResultSet res1 = DatabaseConnector.runQuery(query1);
		
		String query2 = "SELECT HS.SHSID, HS.since FROM hasseconday HS WHERE HS.patientID = "+this.patientID;
		ResultSet res2 = DatabaseConnector.runQuery(query2);
		
		try {
			if (!(res1.isBeforeFirst() && res1.getRow() == 0)) {
				primaryHS = new Profile(res1.getString(1));
				since1 = res1.getDate(2);
			}
			
			if (!(res2.isBeforeFirst() && res2.getRow() == 0)) {
				secondaryHS = new Profile(res2.getString(1));
				since2 = res2.getDate(2);
			}
		}
		catch (Exception e) {
			System.err.println("Error: Unable to fetch Health Supporter Information for Patient-"+patientID);
		}
	}
	
	private void updateHS(int type) {
		String hsid="";
		Date d;
		if (type == 0) {
			System.out.println("Enter new Primary Health Supporter ID:");
			hsid = sc.next();
			System.out.println("Enter Authorization Date:");
			d = java.sql.Date.valueOf(sc.next());
			String query = "UPDATE hasprimary SET HSUSERID="
					+hsid+",since="+d+" WHERE patientid="+patientID;
			int r = DatabaseConnector.updateDB(query);
			if (r == 0) {
				System.out.println("Error updating primary health supporter");
			}
		}
		else {
			System.out.println("Enter new Secondary Health Supporter ID:");
			hsid = sc.next();
			System.out.println("Enter Authorization Date:");
			d = java.sql.Date.valueOf(sc.next());
			String query = "UPDATE hassecondary SET HSUSERID="
					+hsid+",since="+d+" WHERE patientid="+patientID;
			int r = DatabaseConnector.updateDB(query);
			if (r == 0) {
				System.out.println("Error updating secondary health supporter");
			}
		}
		fetchHealthSupporterInformation();
		
	}
	
	private void updateHS() {
		int option = 0;
		while (option != 3) {
			System.out.println("Choose:\n"
					+ "1. Update Primary HS\n"
					+ "2. Update Secondary HS\n"
					+ "3. Go Back");
			option = sc.nextInt();
			if (option < 1 || option > 2) {
				System.out.println("Invalid Selection");
				continue;
			}
			updateHS(option);
		}
	}
	
	private void viewHealthSupporters() {
		if (primaryHS != null) {
			System.out.println("Primary:");
			System.out.print("Name: "+primaryHS.getName());
			System.out.println("Address: "+primaryHS.getAddress());
			System.out.println("Phone: "+primaryHS.getPhone());
			System.out.println("Auth Date: "+since1.toString());
		}
		
		if (secondaryHS != null) {
			System.out.println("Secondary:");
			System.out.print("Name: "+secondaryHS.getName());
			System.out.println("Address: "+secondaryHS.getAddress());
			System.out.println("Phone: "+secondaryHS.getPhone());
			System.out.println("Auth Date: "+since2.toString());
		}
	}
}
