import java.sql.Date;
import java.sql.ResultSet;

public class HealthSupporters {
	private String patientID;
	private int ptype;
	private Profile primaryHS;
	private Profile secondaryHS;
	private Date since1;
	private Date since2;
	static String primaryTableName = "hasprimary";
	static String secTableName = "hassecondary";
	static String pcolpatientid = "puserid";
	static String pcolhsid = "hsuserid";
	static String pcolsince = "since";
	static String scolpatientid = "puserid";
	static String scolhsid = "shsuserid";
	static String scolsince = "since";
	
	
	public HealthSupporters(String PatientID) {
		this.patientID = PatientID;
		fetchHealthSupporterInformation();
	}
	
	public void setPtype(int type) {
		this.ptype = type;
	}
	
	public void MainView() {
		fetchHealthSupporterInformation();
		int option = 0;
		while(option != 4) {
			System.out.println("Choose from the following options:\n"
					+ "1. View Health Supporters\n"
					+ "2. Add/Change Health Supporters.\n"
					+ "3. Remove Health Supporters.\n"
					+ "4. Go Back");
			option = StaticFunctions.nextInt();
			switch(option) {
			case 1:
				viewHealthSupporters();
				break;
			case 2:
				updateHS();
				break;
			case 3:
				deleteHS2();
				break;
			case 4:
				break;
			default:
				System.out.println("Invalid Input, enter again!");
			}
		}
		exit();
	}
	
	public boolean hasHS() {
		if (primaryHS != null || secondaryHS != null)
			return true;
		else
			return false;
	}
	
	public void SickPatientWithNoHSView() {
		if (!hasHS()) {
			System.out.println("Must enter Primary Health Supporter Details");
			addHS(1);
		}
	}
	
	private void exit() {
		if (primaryHS != null)
			primaryHS.exit();
		if (secondaryHS != null)
			secondaryHS.exit();
	}

	private void fetchHealthSupporterInformation() {
		String query1 = "SELECT HP."+pcolhsid+", HP."+pcolsince+" FROM "+primaryTableName+
				" HP WHERE HP."+pcolpatientid+" = '"+this.patientID+"'";
		ResultSet res1 = DatabaseConnector.runQuery(query1);
		
		String query2 = "SELECT HS."+scolhsid+", HS."+scolsince+" FROM "+secTableName+
				" HS WHERE HS."+scolpatientid+" = '"+this.patientID+"'";
		ResultSet res2 = DatabaseConnector.runQuery(query2);
		primaryHS = null;
		secondaryHS = null;
		
		try {
			if ((res1.next())) {
				res1.first();
				primaryHS = new Profile(res1.getString(1));
				since1 = res1.getDate(2);
			}
			
			if ((res2.next())) {
				res2.first();
				secondaryHS = new Profile(res2.getString(1));
				since2 = res2.getDate(2);
			}
		}
		catch (Exception e) {
			System.out.println("Error: Unable to fetch Health Supporter Information for Patient-"+patientID);
		}
	}
	
	private void addHS(int type) {
		String hsid="";
		String d;
		if (type == 1) {
			System.out.println("Enter new Primary Health Supporter ID:");
			hsid = StaticFunctions.next();
			System.out.println("Enter Authorization Date:");
			d = StaticFunctions.next();
			String query = "INSERT INTO "+primaryTableName+" VALUES ('"+patientID+"','"
					+hsid+"',TO_DATE('"+d+"','MM-DD-YYYY'))";
			int r = DatabaseConnector.updateDB(query);
			if (r == 0) {
				System.out.println("Couldn't add primary HS - possible invalid HS userid");
			}
		}
		else if (type == 2){
			System.out.println("Enter new Secondary Health Supporter ID:");
			hsid = StaticFunctions.next();
			System.out.println("Enter Authorization Date:");
			d = StaticFunctions.next();
			String query = "INSERT INTO "+secTableName+" VALUES ('"+patientID+"','"
					+hsid+"',TO_DATE('"+d+"','MM-DD-YYYY'))";
			int r = DatabaseConnector.updateDB(query);
			if (r == 0) {
				System.out.println("Couldn't add secondary HS - possible invalid HS userid");
			}
		}
		fetchHealthSupporterInformation();
	}
	
	private void updateHS(int type) {
		String hsid="";
		String d;
		if (type == 1) {
			System.out.println("Enter new Primary Health Supporter ID:");
			hsid = StaticFunctions.next();
			System.out.println("Enter Authorization Date:");
			d = StaticFunctions.next();
			String query = "UPDATE "+primaryTableName+" SET "+pcolhsid+"='"
					+hsid+"',"+pcolsince+"=TO_DATE('"+d+"','MM-DD-YYYY') WHERE "+pcolpatientid+"='"+patientID+"'";
			int r = DatabaseConnector.updateDB(query);
			if (r == 0) {
				System.out.println("Couldn't update primary HS - possible invalid HS userid");
			}
		}
		else if (type == 2){
			System.out.println("Enter new Secondary Health Supporter ID:");
			hsid = StaticFunctions.next();
			System.out.println("Enter Authorization Date:");
			d = StaticFunctions.next();
			String query = "UPDATE "+secTableName+" SET "+scolhsid+"='"
					+hsid+"',"+scolsince+"=TO_DATE('"+d+"','MM-DD-YYYY') WHERE "+scolpatientid+"='"+patientID+"'";
			int r = DatabaseConnector.updateDB(query);
			if (r == 0) {
				System.out.println("Couldn't update secondary HS - possible invalid HS userid");
			}
		}
		fetchHealthSupporterInformation();
	}
	
	private void updateHS() {
		int option = 0;
		while (option != 3) {
			System.out.println("Choose:\n"
					+ "1. Update Primary HS\n"
					+ "2. Update Secondary HS\n"
					+ "3. Go Back");
			option = StaticFunctions.nextInt();
			if (option < 1 || option > 3) {
				System.out.println("Invalid Selection");
				continue;
			}
			else if (option == 1) {
				if (primaryHS == null)
					addHS(1);
				else
					updateHS(1);
			}
			else if (option == 2) {
				if (secondaryHS == null)
					addHS(2);
				else
					updateHS(2);	
			}
				
		}
	}
	
	private void deleteHS2() {
		int option = 0;
		while (option != 3) {
			System.out.println("Choose from following:\n"
					+ "1. Delete Primary HS\n"
					+ "2. Delete Secondary HS\n"
					+ "3. Go Back");
			option = StaticFunctions.nextInt();
			if (option == 1){
				if (secondaryHS == null && ptype == 1) {
					System.out.println("You must have atleast one health supporter");
				}
				else if (secondaryHS != null) {
					String query = "DELETE FROM "+secTableName+" HS WHERE HS."+scolpatientid+"='"+this.patientID+"'";
					DatabaseConnector.updateDB(query);
					query = "DELETE FROM "+primaryTableName+" HS WHERE HS."+pcolpatientid+"='"+this.patientID+"'";
					DatabaseConnector.updateDB(query);
					
					query = "INSERT INTO "+primaryTableName+" VALUES('"+patientID+"','"+secondaryHS.getUID()+
							"',TO_DATE('"+since2.toString()+"','YYYY-MM-DD'))";
					DatabaseConnector.updateDB(query);
				}
				else {
					String query = "DELETE FROM "+primaryTableName+" HS WHERE HS."+pcolpatientid+"='"+this.patientID+"'";
					int r = DatabaseConnector.updateDB(query);
					
					if (r == 0) {
						System.out.println("Unable to delete HS");
					}
					else {
						fetchHealthSupporterInformation();
						System.out.println("Primary HS deleted");
						break;
					}
				}
			}
			else if (option == 2) {
				if (secondaryHS != null) {
					String query = "DELETE FROM "+secTableName+" HS WHERE HS."+scolpatientid+"='"+this.patientID+"'";
					int r = DatabaseConnector.updateDB(query);
					if (r == 0) {
						System.out.println("Unable to delete HS");
					}
					else {
						fetchHealthSupporterInformation();
						System.out.println("Secondary HS deleted");
						break;
					}
				}
				else {
					System.out.println("You don't have a secondary HS");
				}
			}
			else if (option < 1 || option > 3) {
				System.out.println("Invalid Selection");
			}
		}
		fetchHealthSupporterInformation();
	}
	
	private void deleteHS() {
		if (ptype == 1) {
			if (secondaryHS == null) {
				System.out.println("Sick Patient can't delete Primary Health Supporter");
			}
			else {
				int option = 0;
				while (option != 2) {
					System.out.println("Choose from following:\n"
							+ "1. Delete Secondary HS\n"
							+ "2. Go Back");
					option = StaticFunctions.nextInt();
					if (option == 1) {
						String query = "DELETE FROM "+secTableName+" HS WHERE HS."+scolpatientid+"='"+this.patientID+"'";
						int r = DatabaseConnector.updateDB(query);
						if (r == 0) {
							System.out.println("Unable to delete HS");
						}
						else {
							fetchHealthSupporterInformation();
							System.out.println("Secondary HS deleted");
							break;
						}
					}
					else if (option < 1 || option > 2) {
						System.out.println("Invalid Selection");
					}
				}
			}
		}
		else if (ptype == 0) {
			int option = 0;
			while (option != 3) {
				System.out.println("Choose from following:\n"
						+ "1. Delete Primary HS\n"
						+ "2. Delete Secondary HS\n"
						+ "3. Go Back");
				option = StaticFunctions.nextInt();
				if (option == 1) {
					if (primaryHS != null) {
						
						
						if (secondaryHS != null) {
							String query = "DELETE FROM "+secTableName+" HS WHERE HS."+scolpatientid+"='"+this.patientID+"'";
							DatabaseConnector.updateDB(query);
							query = "DELETE FROM "+primaryTableName+" HS WHERE HS."+pcolpatientid+"='"+this.patientID+"'";
							DatabaseConnector.updateDB(query);
							
							query = "INSERT INTO "+primaryTableName+" VALUES('"+patientID+"','"+secondaryHS.getUID()+
									"',TO_DATE('"+since2.toString()+"','YYYY-MM-DD'))";
							DatabaseConnector.updateDB(query);
							
						}
						else {
							String query = "DELETE FROM "+primaryTableName+" HS WHERE HS."+pcolpatientid+"='"+this.patientID+"'";
							int r = DatabaseConnector.updateDB(query);
							
							if (r == 0) {
								System.out.println("Unable to delete HS");
							}
							else {
								fetchHealthSupporterInformation();
								System.out.println("Primary HS deleted");
								break;
							}
						}
					}
					else {
						System.out.println("No Primary HS");
					}
				}
				else if (option == 2) {
					if (secondaryHS != null) {
						String query = "DELETE FROM "+secTableName+" HS WHERE HS."+scolpatientid+"='"+this.patientID+"'";
						int r = DatabaseConnector.updateDB(query);
						if (r == 0) {
							System.out.println("Unable to delete HS");
						}
						else {
							fetchHealthSupporterInformation();
							System.out.println("Secondary HS deleted");
							break;
						}
					}
					else {
						System.out.println("No Secondary HS");
					}
				}
				else if (option < 1 || option > 3) {
					System.out.println("Invalid Selection");
				}
			}
		}
		fetchHealthSupporterInformation();
	}
	
	private void viewHealthSupporters() {
		fetchHealthSupporterInformation();
		if (primaryHS != null) {
			System.out.println("Primary:");
			System.out.println("ID: "+primaryHS.getUID());
			System.out.println("Name: "+primaryHS.getName());
			System.out.println("Gender: "+primaryHS.getGender());
			System.out.println("Date of Birth: "+primaryHS.getDOB().toString());
			System.out.println("Address: "+primaryHS.getAddress());
			System.out.println("Phone: "+primaryHS.getPhone());
			System.out.println("Auth Date: "+since1.toString());
		}
		else {
			System.out.println("No Primary Health Supporter");
		}
		
		if (secondaryHS != null) {
			System.out.println("Secondary:");
			System.out.println("ID: "+secondaryHS.getUID());
			System.out.println("Name: "+secondaryHS.getName());
			System.out.println("Gender: "+secondaryHS.getGender());
			System.out.println("Date of Birth: "+secondaryHS.getDOB().toString());
			System.out.println("Address: "+secondaryHS.getAddress());
			System.out.println("Phone: "+secondaryHS.getPhone());
			System.out.println("Auth Date: "+since2.toString());
		}
		else {
			System.out.println("No Secondary Health Supporter");
		}
	}
}
