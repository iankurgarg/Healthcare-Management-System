import java.sql.ResultSet;

public class Patient {
	private String UID = null;
	private Profile p = null;
	private Diagnosis d = null;
	private Observations Obs = null;
	private Alerts al = null;
	private HealthSupporters hs = null;
	private String mainOptions = "Select an option:\n"
			+ "1. Profile\n"
			+ "2. Diagnosis\n"
			+ "3. Health Indicator\n"
			+ "4. Alerts\n"
			+ "5. Health Supporters\n"
			+ "6. Logout";
	
	static String tableName = "patient";
	static String coluserid = "userid";
	
	
	public Patient(String ID) {
		this.UID = ID;
		ResultSet res = DatabaseConnector.runQuery("SELECT * FROM "+tableName+" P WHERE P."+coluserid+"='"+ID+"'");
		try {
			if (res.isAfterLast()) {
				System.err.println("Error: Invalid Patient User Id");
				System.exit(-1);
			}
		}
		catch (Exception e) {
			System.out.println("Error: Unable to fetch patient information");
			System.exit(-1);
		}

		UID = ID;
		p = new Profile(UID);
		d = new Diagnosis(UID);
		Obs = new Observations(UID);
		al = new Alerts(UID);
		hs = new HealthSupporters(UID);
		hs.setPtype(d.isSick());
	}
	
	public int MainView() {
		if (d.isSick() == 0) {
			showMainOptions();
		}
		else {
			while (!hs.hasHS()) {
				hs.SickPatientWithNoHSView();
			}
			showMainOptions();
		}
		exit();
		return 0;
	}
	
	private void exit() {
		this.UID = null;
	}
	
	private void showMainOptions() {
		if (UID != null) {
			int option = 0;
			while (option != 6) {
				System.out.println(mainOptions);
				option = StaticFunctions.nextInt();
				switch (option) {
				case 1:
					p.MainView();
					break;
				case 2:
					d.MainView();
					hs.setPtype(d.isSick());
					break;
				case 3:
					Obs.MainView();
					break;
				case 4:
					al.MainView();
					break;
				case 5:
					hs.MainView();
					break;
				case 6:
					break;
				default:
					System.out.println("Invalid input\n");
					break;
				}
			}
		}
		else {
			System.err.println("Error: UID for Patient is invalid");
		}
	}
}
