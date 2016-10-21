import java.sql.Date;
import java.sql.ResultSet;

public class PFORHS {
	private String patientid;
	private Profile p;
	private Record r;
	private Date authDate;
	private String options = "Select from the following:\n"
			+ "1. View Patient Profile\n"
			+ "2. View Patient Records\n"
			+ "3. Add Specific Reccommendation\n"
			+ "4. View Alerts"
			+ "5. Clear Alerts"
			+ "6. Go Back";
	
	public PFORHS(String patientID, Date auth) {
		this.patientid = patientID;
		p = new Profile(this.patientid);
		r = new Record(this.patientid);
		this.authDate = auth;
	}
	
	public String getName() {
		return p.getName();
	}
	
	public void MainView() {
		if (authDate.after(new Date((new java.util.Date()).getTime())))
			System.out.println("You are not yet authorised to manage this patient");
		else {
			int option = 0;
			while (option != 6) {
				System.out.println(options);
				option = StaticFunctions.nextInt();
				switch(option){
				case 1: 
					p.viewProfile();
					break;
				case 2:
					r.showRecords();
					break;
				case 3:
					addRecco();
					break;
				case 4:
					clearAlerts();
					break;
				case 5:
					viewAlerts();
					break;
				case 6:
					break;
				default:
					System.out.println("Invalid Selection");
				}
			}
			
		}
		
	}

	private void addRecco() {
		System.out.println("Enter the Obs Name:");
		StaticFunctions.nextLine();
		String obsName = StaticFunctions.nextLine();
		
		String query = "SELECT M."+Observations.mcolobstype+", M."+Observations.mcolupper+", M."+Observations.mcollower
				+", M."+Observations.mcolfreq+" FROM "+Observations.mtableName+" M WHERE M."
				+Observations.mcolobsname+" = '"+obsName+"'";
		ResultSet res = DatabaseConnector.runQuery(query);
		
		try {
			if (res.next()) {
				res.beforeFirst();
				while(res.next()) {
					String measure = res.getString(1);
					System.out.println("Enter Lower Limit for "+measure+" (Default:"+res.getInt(2)+")");
					int lowerlimit = StaticFunctions.nextInt();
					System.out.println("Enter Upper Limit for "+measure+" (Default:"+res.getInt(3)+")");
					int upperlimit = StaticFunctions.nextInt();
					System.out.println("Enter frequency for "+measure+" (Default:"+res.getInt(4)+")");
					int freq = StaticFunctions.nextInt();
					String query1 = "INSERT INTO "+Observations.smtableName+" VALUES('"+patientid+"','"+obsName+"','"+measure+
							"',"+lowerlimit+","+upperlimit+","+freq+")";
					int r = DatabaseConnector.updateDB(query1);
					if (r != 0) {
						System.out.println("Specific Reccommendation Added");
					}
					else {
						System.out.println("Invalid input OR Reccomendation already exists");
					}
					
				}
			}
			else {
				System.out.println("Invalid Observation Name");
			}
		} catch (Exception e) {
			System.out.println("Unable to fetch observation measures");
		}
	}
	
	private void viewAlerts() {
		// Fetch all alerts 
		// View them
	}
	
	private void clearAlerts() {
		//fetch all alerts,
		// if no alerts , print msg no existing alerts
		// else clear all alerts and print msg, alerts cleared.
	}
}
