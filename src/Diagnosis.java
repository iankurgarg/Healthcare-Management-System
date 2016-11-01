import java.sql.Date;
import java.sql.ResultSet;

public class Diagnosis {
	private String UID;
	private String[] DiseaseNames;
	private Date[] DiseaseDates;
	private int num = 0;
	private String options = "Select from following options:\n"
			+ "1. View existing diagnosis\n"
			+ "2. Add a diagnosis\n"
			+ "3. Remove a diagnosis\n"
			+ "4. Go Back";
	
	static String tableName = "diagnosis";
	static String colid = "patientid";
	static String coldid = "diseaseid";
	static String colsince = "since";
	static String colenddate = "enddate";
	
	public Diagnosis(String ID) {
		this.UID = ID;
		fetchDiagnosisData();
	}
	
	public void MainView() {
		showOptions();
		exit();
	}
	
	public int isSick() {
		return num;
	}
	
	private void fetchDiagnosisData () {
		String query = "SELECT D."+coldid+", D."+colsince+" FROM "+tableName+" D WHERE D."+colid+"='"+this.UID+
				"' AND D."+colenddate+" IS NULL";
		ResultSet res = DatabaseConnector.runQuery(query);
		try {
			int len;
			if (!res.next()) {
				len = 0;
			}
			else {
				res.last();
				len = res.getRow();	
			}
			
			if (len > 0) {
				res.beforeFirst();
				DiseaseNames = new String[len];
				DiseaseDates = new Date[len];
				int i = 0;
				while (res.next()) {
					DiseaseNames[i] = res.getString(1);
					DiseaseDates[i++] = res.getDate(2);
				}	
			}
			num = len;
			
		}
		catch (Exception e) {
			System.out.println("Unable to fetch patient diagnosis data");
		}
	}
	
	public void showOptions() {
		int option = 0;
		while (option != 4) {
			System.out.println(options);
			option = StaticFunctions.nextInt();
			if (option < 1 || option > 4)
				System.out.println("Invalid Input");
			else if (option == 1) {
				if (num > 0) {
					System.out.println("Patient has the following Diagnosis:");
					for (int i = 0; i < num; i++) {
						System.out.print(DiseaseNames[i]+"\t");
						System.out.println(DiseaseDates[i]);
					}
				}
				else {
					System.out.println("Patient is a well patient");
				}
			}
			else if (option == 2) {
				System.out.println("Enter correct Disease Name:");
				StaticFunctions.nextLine();
				String d = StaticFunctions.nextLine();
				System.out.println("Enter diagnosis date (MM-DD-YYYY):");
				String date = StaticFunctions.next();
				addDisease(d, date);
				
			}
			else if (option == 3) {
				System.out.println("Enter the correct disease name to be removed:");
				String line = StaticFunctions.nextLine();
				if (line.equals(""))
					line = StaticFunctions.nextLine();
				removeDisease(line);
			}
		}
		return;
	}
	
	private void addDisease(String disease, String d) {
		String query = "INSERT INTO "+tableName+" VALUES('"+this.UID+"','"+disease+"',TO_DATE('"+d+"','MM-DD-YYYY'), NULL)";
		int c = DatabaseConnector.updateDB(query);
		if (c == 0) {
			System.out.println("Coudn't add diagnosis, either disease id invalid or date format incorrect");
			System.out.println(query);
		}
		else {
			fetchDiagnosisData();
			System.out.println("Diagnosis added successfully");
		}
	}
	
	private void removeDisease(String disease) {
		if (disease.equals(""))
			System.out.println("No such disease related to the given patient");
		
//		String query = "DELETE FROM "+tableName+" WHERE "+colid+"='"+this.UID+"' AND "+coldid+"='"+disease+"'";
		String query2 = "UPDATE "+tableName+" SET "+colenddate+"=sysdate WHERE "+colid+"='"+this.UID+"' AND "+
				coldid+"='"+disease+"' AND "+colenddate+" IS NULL";
		int r = DatabaseConnector.updateDB(query2);
		if (r == 0) {
			System.out.println("No such disease related to the given patient");
		}
		else {
			fetchDiagnosisData();
			System.out.println("Diagnosis successfully removed");
		}
	}
	
	public void exit() {
	}
}
