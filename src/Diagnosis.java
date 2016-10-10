import java.sql.Date;
import java.sql.ResultSet;
import java.util.Scanner;

public class Diagnosis {
	private String UID;
	private int[] DiseaseIDs;
	private String[] DiseaseNames;
	private Date[] DiseaseDates;
	private Scanner sc;
	private String options = "Select from following options:\n"
			+ "1. View existing diagnosis\n"
			+ "2. Add a diagnosis\n"
			+ "3. Remove a diagnosis\n"
			+ "4. Go Back";
	
	public Diagnosis(String ID) {
		this.UID = ID;
		sc = new Scanner(System.in);
	}
	
	public void MainView() {
		showOptions();
		exit();
	}
	
	public void fetchDiagnosisData () {
		String query = "";
		ResultSet res = DatabaseConnector.runQuery(query);
		//Populate the DiseaseIDs and DiseaseDates based on results in res.
	}
	
	public void showOptions() {
		int option = 0;
		while (option != 4) {
			System.out.println(options);
			option = sc.nextInt();
			if (option == 1) {
				//call fetchDiagnosisData()
				System.out.println("You have the following Diagnosis:");
				for (int i = 0; i < DiseaseIDs.length; i++) {
					System.out.print(DiseaseIDs[i]+"\t");
					System.out.print(DiseaseNames[i]+"\t");
					System.out.println(DiseaseDates[i]);
				}
			}
			else if (option == 2) {
				//Ask for Disease ID and add current date.
				
			}
			else if (option == 3) {
				//Ask for Disease ID and send delete query to delete the disease
			}
			else {
				System.out.println("Invalid Input");
			}
		}
		return;
	}
	
	public void exit() {
		sc.close();
		DiseaseIDs = null;
		DiseaseDates = null;
		UID = null;
	}
}
