import java.sql.Date;
import java.sql.ResultSet;

public class Alerts {
	private String patientUID;
	private String alert1 = "";
	private String alert2 = "";
	private Date alert1d = null;
	private Date alert2d = null;
	
	static String atableName = "activealerts";
	static String artableName = "archivealerts";
	static String colpatientID = "patientid";
	static String colAlertType = "alerttype";
	static String colmsg = "message";
	static String coldatec = "generatedate";
	static String arcoldateclear = "cleardate";
	
	static String procedureAlert1 = "create_alert";
	static String procedureAlert2 = "create_alert2";
	
	
	public Alerts(String patientID){
		this.patientUID = patientID;
		generateAlerts();
	}
	
	public void MainView() {
		int option = 0;
		while(option !=2) {
			System.out.println("Choose from the following:\n"
					+ "1. View Alerts\n"
					+ "2. Go Back");
			option = StaticFunctions.nextInt();
			if (option == 1) {
				fetchAlerts();
				viewAlerts();
			}
			else if (option < 1 || option > 2) {
				System.out.println("Invalid Selection");
			}
		}
	}
	
	private void viewAlerts() {
		generateAlerts();
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
	
	private void fetchAlerts() {
		String query = "SELECT A."+Alerts.colAlertType+", A."+Alerts.colmsg+", A."+Alerts.coldatec+" FROM "
				+Alerts.atableName+" A WHERE A."+Alerts.colpatientID+"='"+this.patientUID+"'";
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
				System.out.println("No active alerts for the patient "+this.patientUID);
			}
		} catch (Exception e) {
			System.out.println("Unable to fetch alerts for the patient");
		}
	}
	
	private void generateAlerts() {
		String query = "{CALL "+procedureAlert1+"() }";
		DatabaseConnector.callVoidProcedure(query);
		
		query = "{CALL "+procedureAlert2+"() }";
		DatabaseConnector.callVoidProcedure(query);
	}
}
