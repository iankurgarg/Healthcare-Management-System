import java.sql.ResultSet;
import java.util.Scanner;

public class Patient {
	private String UID = null;
	private Profile p = null;
	private Diagnosis d = null;
	private int patientType = 0;
	private Scanner sc;
	private String mainOptions = "Select an option:\n"
			+ "1. Profile\n"
			+ "2. Diagnosis\n"
			+ "3. Health Indicator\n"
			+ "4. Alerts\n"
			+ "5. Health Supporters\n"
			+ "6. Logout\n";
	
	
	public Patient(String ID) {
		sc = new Scanner(System.in);

		ResultSet res = DatabaseConnector.runQuery("SELECT * FROM Patients P WHERE P.userID="+ID);
		try {
			if (res == null || res.isLast() || res.isAfterLast()) {
				throw new Exception ("Error: Invalid Patient User Id");
			}
			else {
				patientType = res.getInt(2);
			}
		}
		catch (Exception e) {
			
		}

		UID = ID;
		p = new Profile(UID);
		d = new Diagnosis(UID);
	}
	
	public int MainView() {
		if (patientType == 0) {
			//Well Patient;
			showMainOptions();
		}
		else {
			// Sick Patient
			// check for health supporter first.
			// if no health supporter ask the patient to enter health supporter details.
			// After details entered:
			showMainOptions();
		}
		exit();
		return 0;
	}
	
	private void exit() {
		this.UID = null;
		sc.close();
	}
	
	private void showMainOptions() {
		if (UID != null) {
			int option = 0;
			while (option != 6) {
				System.out.println(mainOptions);
				option = sc.nextInt();
				switch (option) {
				case 1:
					p.MainView();
					break;
				case 2:
					d.MainView();
					break;
				case 3:
					System.out.println("Health Indicator");
					break;
				case 4:
					System.out.println("Alerts");
					break;
				case 5:
					System.out.println("Health Supporter");
					break;
				default:
					System.out.println("Invalid input\n");
				}
			}
		}
		else {
			System.err.println("Error: UID for Patient is invalid");
		}
	}
}
