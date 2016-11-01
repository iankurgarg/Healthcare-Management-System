import java.sql.Date;
import java.sql.ResultSet;

public class PFORHS {
	private String patientid;
	private Profile p;
	private Record r;
	private Diagnosis d;
	private Date authDate;
	private String options = "Select from the following:\n"
			+ "1. View Patient Profile\n"
			+ "2. View Patient Records\n"
			+ "3. Diagnois\n"
			+ "4. Add Specific Reccommendation\n"
			+ "5. View Alerts\n"
			+ "6. Clear Alerts\n"
			+ "7. Go Back";
	private String alert1 = "";
	private String alert2 = "";
	private Date alert1d = null;
	private Date alert2d = null;
	
	public PFORHS(String patientID, Date auth) {
		this.patientid = patientID;
		p = new Profile(this.patientid);
		r = new Record(this.patientid);
		d = new Diagnosis(this.patientid);
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
			while (option != 7) {
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
					d.MainView();
					break;
				case 4:
					addRecco();
					break;
				case 5:
					viewAlerts();
					break;
				case 6:
					clearAlerts();
					break;
				case 7:
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
		
		String query = "SELECT M."+Observations.mcolobstype+" FROM "+Observations.mtableName+" M WHERE M."
				+Observations.mcolobsname+" = '"+obsName+"'";
		ResultSet res = DatabaseConnector.runQuery(query);
		
		try {
			if (res.next()) {
				res.beforeFirst();
				while(res.next()) {
					String measure = res.getString(1);
					System.out.println("Enter Lower Limit for "+measure+":");
					int lowerlimit = StaticFunctions.nextInt();
					System.out.println("Enter Upper Limit for "+measure+":");
					int upperlimit = StaticFunctions.nextInt();
					System.out.println("Enter frequency for "+measure+":");
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
	
	private void fetchAlerts() {
		alert1 = "";
		alert2 = "";
		String query = "SELECT A."+Alerts.colAlertType+", A."+Alerts.colmsg+", A."+Alerts.coldatec+" FROM "
				+Alerts.atableName+" A WHERE A."+Alerts.colpatientID+"='"+this.patientid+"'";
		ResultSet res = DatabaseConnector.runQuery(query);
		
		try {
			if (res.next()) {
				res.beforeFirst();
				while(res.next()) {
					String type = res.getString(1);
					if (type.equals("outside limit")) {
						alert1 = res.getString(2);
						alert1d = res.getDate(3);
					}
					else if (type.equals("low activity")) {
						alert2 = res.getString(2);
						alert2d = res.getDate(3);
					}
				}
			}
			else {
				System.out.println("No active alerts for the patient "+this.patientid);
			}
		} catch (Exception e) {
			System.out.println("Unable to fetch alerts for the patient");
		}
	}
	
	public void viewAlerts() {
		fetchAlerts();
		System.out.println("Alerts:");
		int i = 1;
		if (!alert1.equals("")) {
			System.out.println(i+". Outside the limit - "+alert1+" - "+alert1d.toString());
			i++;
		}
		if (!alert2.equals("")) {
			System.out.println(i+". Low Activity - "+alert2+" - "+alert2d.toString());
		}
	}
	
	private void clearAlerts() {
		viewAlerts();
		if (alert1.equals("") && alert2.equals("")) {
			System.out.println("No Alerts to clear");
		}
		else {
			System.out.println("Enter alert number to clear:");
			int option = StaticFunctions.nextInt();
			if (option == 1) {
				if(alert1.equals("")) {
					String query = "DELETE FROM "+Alerts.atableName+" WHERE "+Alerts.colpatientID+"='"+
							this.patientid+"' AND "+Alerts.colAlertType+"='low activity'";
					int r = DatabaseConnector.updateDB(query);
					if (r == 0)
						System.out.println("Couldn't clear alert");
					else
						System.out.println("Alert cleared.");
				}
				else {
					String query = "DELETE FROM "+Alerts.atableName+" WHERE "+Alerts.colpatientID+"='"+
							this.patientid+"' AND "+Alerts.colAlertType+"='outside limit'";
					int r = DatabaseConnector.updateDB(query);
					if (r == 0)
						System.out.println("Couldn't clear alert");
					else
						System.out.println("Alert cleared.");
				}
			}
			else if (option == 2) {
				if (!alert2.equals("")) {
					String query = "DELETE FROM "+Alerts.atableName+" WHERE "+Alerts.colpatientID+"='"+
							this.patientid+"' AND "+Alerts.colAlertType+"='low activity'";
					int r = DatabaseConnector.updateDB(query);
					if (r == 0)
						System.out.println("Couldn't clear alert");
					else
						System.out.println("Alert cleared.");
				}
				else {
					System.out.println("Invalid Selection");
				}
			}
			else {
				System.out.println("Invalid Selection");
			}
			
		}
		fetchAlerts();
	}
}
