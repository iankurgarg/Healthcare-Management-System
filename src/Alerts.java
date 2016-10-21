import java.sql.ResultSet;

public class Alerts {
	private String patientUID;
	static String tableName = "alerts";
	static String colpatientID = "patientID";
	static String colAlertType = "type";
	static String colmsg = "message";
	static String coldatec = "creation_date";
	static String coldatee = "clear_date";
	
	
	public Alerts(String patientID){
		this.patientUID = patientID;
		generateAlerts();
	}
	
	public void MainView() {
		
	}
	
	private void findAlerts() {
		String query = "SELECT A."+colAlertType+", A."+colmsg+", A."+coldatec+", A."+coldatee+
				" FROM "+tableName+" A WHERE A."+colpatientID+"='"+this.patientUID+"'";
		ResultSet res = DatabaseConnector.runQuery(query);
		
	}
	
	private void generateAlerts() {
		findAlerts();
		
	}
}
