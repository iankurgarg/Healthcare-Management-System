/**
 * @author Ankur Garg, agarg12@ncsu.edu
 */

public class Home {
    public static void main(String[] args) throws Exception{
    	DatabaseConnector.connect();
    	StaticFunctions.Initialise();
    	UserSession us = new UserSession();
    	Signup s = new Signup();
    	int option = 0;
    	
    	while (option != 3) {
	    	System.out.println("Select from following options:\n"
	    			+ "1. Login\n"
	    			+ "2. Signup\n"
	    			+ "3. Exit");
	    	
	    	option = StaticFunctions.nextInt();
	    	
	    	switch(option) {
	    	case 1:
	        	us.MainView();
	        	break;
	    	case 2:
	    		s.MainView();
	    		break;
	    	case 3:
	    		break;
	    	default:
	    		System.out.println("Invalid Input");
	    	}
    	}
    	StaticFunctions.closeScanner();
    	DatabaseConnector.disconnect();
    }
}



