import java.sql.ResultSet;
import java.util.Scanner;

public class Observations {
	private String[] ObservationNames;
	private int[] upperLimit;
	private int[] lowerlimit;
	private int[] frequency;
	
	private String UID;
	
	private Scanner sc = null;
	
	public Observations(String ID) {
		this.UID = ID;
		sc = new Scanner(System.in);
	}
	
	public void MainView() {
		int option = 0;
		while (option != 3) {
			System.out.println("Select from following options:\n"
					+ "1. View observation records\n"
					+ "2. Enter new observation record\n"
					+ "3. Go Back");
			option = sc.nextInt();
			if (option == 1) {
				GetRecords();
				
			}
			else if(option == 2) {
				GetObservationTypes();
			}
		}
		exit();
		return;
	}
	
	private void exit() {
		sc.close();
		
	}

	private void GetObservationTypes() {
		String Query1 = "Select general observation types";
		ResultSet res1 = DatabaseConnector.runQuery(Query1);
		
		String Query2 = "Select specific observation types";
		ResultSet res2 = DatabaseConnector.runQuery(Query2);
		
		String Query3 = "Select disease specific observation types";
		ResultSet res3 = DatabaseConnector.runQuery(Query3);
		
		try {
			if (res2.isLast()) {
				//fill ObservationNames with res1 union res3
			}
			else {
				//fill ObservationNames with res1 union res2;
			}
		}
		catch (Exception e) {
			
		}
	}
	
	private void GetRecords() {
		 String Query = "SELECT * FROM RECORDS R WHERE R.patientid=UID";
		 
		 //Populate the records.
	}
	
	
}
