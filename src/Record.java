import java.sql.Date;
import java.sql.ResultSet;

public class Record {
	private String patientId;
	private String[] obsName;
	private String[] measure;
	private int[] obsValue;
	private Date[] obsDate;
	private Date[] recordDate;
	
	
	private static String tableName = "record";
	private static String colpatient = "patientid";
	
	
	public Record(String patientID) {
		this.patientId = patientID;
		fetchRecords();
	}
	
	public void fetchRecords() {
		String query = "SELECT * FROM "+tableName+" R WHERE R."+colpatient+" = '"+this.patientId+"'";
		
		ResultSet res = DatabaseConnector.runQuery(query);
		int l;
		try {
			if (res.next()) {
				res.last();
				l = res.getRow();
				res.beforeFirst();
				obsName = new String[l];
				measure = new String[l];
				obsValue = new int[l];
				obsDate = new Date[l];
				recordDate = new Date[l];
				int i = 0;
				while(res.next()) {
					obsName[i] = res.getString(2);
					measure[i] = res.getString(3);
					obsDate[i] = res.getDate(4);
					obsValue[i] = res.getInt(5);
					recordDate[i] = res.getDate(6);
					i++;
				}
			}
			else {
//				System.out.println("No records found");
			}
		}
		catch (Exception e) {
			System.out.println("Unable to fetch records");
		}	
	}
	
	public void showRecords() {
		//System.out.println("Obs Name, Measure, Obs Date, Obs Value, Record Date");
		if (obsName != null) {
			for (int i = 0; i < obsName.length; i++) {
				System.out.print("Obs Name: "+obsName[i]+",");
				System.out.print("Measure: "+measure[i]+",");
				System.out.print("Obs Date: "+obsDate[i]+",");
				System.out.print("Obs Value: "+obsValue[i]+",");
				System.out.println("Record Date: "+recordDate[i]);
				System.out.println("\n");
			}
		}
		else {
			System.out.println("No records found");
		}
	}
}
