import java.sql.ResultSet;

public class Observations {
	private String[] ObservationNames;
//	private String[] OridnalObs;
	private String[] ObsTypes;
	private int[] upperLimit;
	private int[] lowerlimit;
	private int[] frequency;
	private Record R;
	
	static String smtableName = "phassm";
	static String smcolpatientid = "patientid";
	static String smcolobsname = "obsname";
	static String smcolobstype = "obstype";
	static String smcolobsvalue = "obsvalue";
	static String smcolfreq = "freq";
	static String smcolul = "upperlimit";
	static String smcolll = "lowerlimit";

	static String mtableName = "measure";
	static String mcolobsname = "obsname";
	static String mcolobstype = "obstype";
	static String mcolul = "upperlimit";
	static String mcolll = "lowerlimit";
	static String mcolfreq = "freq";
	
	static String dhotablename = "dhasobs";
	static String dhocoldid = "diseaseid";
	static String dhocolobsname = "obsname";
	static String dhocolobstype = "obstype";
	static String dhocolul = "upperlimit";
	static String dhocolll = "lowerlimit";
	static String dhocolfreq = "freq";
	
	static String gotableName ="generalobs";
	static String gocolobsname = "obsname";
	static String gocolobstype = "obstype";
	static String gocolul = "upperlimit";
	static String gocolll = "lowerlimit";
	static String gocolfreq = "freq";
	
	private String UID;
	
	public Observations(String ID) {
		this.UID = ID;
		R = new Record(ID);
	}
	
	public void MainView() {
		int option = 0;
		while (option != 4) {
			System.out.println("Select from following options:\n"
					+ "1. View observation records\n"
					+ "2. Enter new observation record\n"
					+ "3. View Health Indicators\n"
					+ "4. Go Back");
			option = StaticFunctions.nextInt();
			if (option == 1) {
				R.fetchRecords();
				R.showRecords();
			}
			else if(option == 2) {
				GetObservationTypes();
				addRecord();
			}
			else if(option == 3) {
				GetObservationTypes();
				viewHealthIndicators();
			}
		}
		return;
	}
	
	private void viewHealthIndicators() {
		System.out.println("Following are your health indicators:");
		for (int i = 0; i < ObservationNames.length; i++) {
			System.out.println(ObservationNames[i] +" "+ObsTypes[i] + " " + upperLimit[i] +" "+lowerlimit[i]+" "+frequency[i]);
		}
	}
	
	private void addRecord() {
		int option = 0;
		int l = ObservationNames.length;
		
		while (option !=l+1) {
			System.out.println("These are available Health Indicators for you:");
			int i=0;
			for (i = 1; i <= ObservationNames.length;i++) {
				System.out.println(i+". "+ObservationNames[i-1]);
			}
			System.out.println(i+". Go Back");
			option = StaticFunctions.nextInt();
			if (option < 1 || option > l+1) {
				System.out.println("Invalid Selection");
			}
			else if (option != l+1) {
				System.out.println("Enter Observation Date (MM/DD/YYYY hh:mm:ss):");
				StaticFunctions.nextLine();
				String obsDate = StaticFunctions.nextLine();
				System.out.println("Enter Record Date (MM/DD/YYYY hh:mm:ss):");
				String entryDate = StaticFunctions.nextLine();
				String query = "SELECT M."+mcolobstype+" FROM "+mtableName+" M WHERE M."+mcolobsname+" = '"+
						ObservationNames[option-1]+"'";
				ResultSet res2 = DatabaseConnector.runQuery(query);
				try {
					if (res2.next()) {
						res2.beforeFirst();
						while(res2.next()) {
							String measure = res2.getString(1);
							System.out.println("Enter Obs value for " + measure);
							int obsValue = StaticFunctions.nextInt();
							String query1 = "INSERT INTO record VALUES('"+UID+"','"+ObservationNames[option-1]+
									"','"+measure+"',TO_DATE('"+obsDate+"','MM/DD/YYYY hh/mi/ss'),'"+obsValue+
									"', TO_DATE('"+entryDate+"','MM/DD/YYYY hh/mi/ss'))";
							int r = DatabaseConnector.updateDB(query1);
							if (r==0) {
								System.out.println("Couldn't add record - Invalid input");
							}
							else {
								System.out.println("Record Added");
							}
						}
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	private void GetObservationTypes() {
		
		String query1 = "select hspm."+smcolobsname+",hspm."+smcolobstype+",hspm."+smcolul+",hspm."+smcolll+
				",hspm."+smcolfreq+" from "+smtableName+" hspm where hspm."+smcolpatientid+" = '"+this.UID+"'";
		String query2 = "SELECT DHO."+dhocolobsname+", DHO."+dhocolobstype+", DHO."+dhocolul+", DHO."+dhocolll+
				", DHO."+dhocolfreq+" FROM "+dhotablename+" DHO WHERE DHO."+dhocoldid+" IN (SELECT DI."+
				Diagnosis.coldid+" FROM "+Diagnosis.tableName+" DI WHERE DI."+Diagnosis.colid+"='"+this.UID+
				"') AND DHO."+dhocolobsname+" NOT IN (select hspm."+smcolobsname+" from "+smtableName+
				" hspm where hspm."+smcolpatientid+" = '"+this.UID+"')";
		
		String query3 = "SELECT GO."+gocolobsname+", GO."+gocolobstype+", GO."+gocolul+", GO."+gocolll+", GO."+
				gocolfreq+" FROM "+gotableName+" GO WHERE GO."+gocolobsname+" NOT IN (SELECT D."+dhocolobsname+
				" FROM "+dhotablename+" D WHERE D."+dhocoldid+" IN (SELECT DI."+Diagnosis.coldid+" FROM "+
				Diagnosis.tableName+" DI WHERE DI."+Diagnosis.colid+" = '"+this.UID+"')) AND GO."+gocolobsname+
				" NOT IN (SELECT SR."+smcolobsname+" FROM "+smtableName+" SR WHERE SR."+smcolpatientid+" ='"+
				this.UID+"')";
		
/*		String subquery1 = "(select hspm.obsname,hspm.obstype,hspm.freq,hspm.upperlimit,hspm.lowerlimit "+
				"from "+smtableName+" hspm where hspm."+smcolpatientid+" = '"+UID+"')";
		String subq21 = "(select m.obsname,m.obstype from measure m where m.obsname IN "+
				"( (select do.obsname from dhasobs do where do.diseaseid IN "+
				"( select diag.diseaseid from diagnosis diag where diag.patientid = '"+UID+"')) "+
				"UNION (SELECT GO.obsname FROM generalobs GO) ))";
		String subq22 = "(select hspm.obsname,hspm.obstype from phassm hspm where hspm.patientid = '"+UID+"')";
		String subq2 = "("+subq21+" MINUS "+subq22+")";
		
		String subq20 = "(select final.obsname,final.obstype,final.freq,final.upperlimit,final.lowerlimit "+
					"from measure final," + subq2+" Q where final.obsname = Q.obsname and final.obstype = Q.obstype)";
		String query = subquery1+" UNION "+subq20;
*/
		String query = "("+query1+") UNION ("+query2+") UNION ("+query3+")";
		
		ResultSet res = DatabaseConnector.runQuery(query);
		try {
			if (res.next()) {
				res.last();
				int l = res.getRow();
				ObservationNames = new String[l];
				ObsTypes = new String[l];
				upperLimit = new int[l];
				lowerlimit = new int[l];
				frequency = new int[l];
				res.beforeFirst();
				int i = 0;
				while(res.next()) {
					ObservationNames[i] = res.getString(1);
					ObsTypes[i] = res.getString(2);
					upperLimit[i] = res.getInt(3);
					lowerlimit[i] = res.getInt(4);
					frequency[i] = res.getInt(5);
					i++;
				}
			}
			else {
				System.out.println("No observation Types valid for patient");
			}
		}
		catch (Exception e) {
			System.out.println("Unable to fetch Observation types for the patient");
		}
	}
}
