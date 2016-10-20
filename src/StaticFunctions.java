import java.util.Scanner;

public class StaticFunctions {
	private static Scanner sc = null;
	
	private StaticFunctions(){
	}
	
	public static void Initialise() {
		sc = new Scanner(System.in);
	}
	
	public static int nextInt() {
		if (sc.hasNextInt())
			return sc.nextInt();
		else {
			sc.nextLine();
		}
		return -1;
	}
	
	public static String next() {
		return sc.next();
	}
	
	public static String nextLine() {
		return sc.nextLine();
	}
	
	public static void closeScanner() {
		sc.close();
	}
}
