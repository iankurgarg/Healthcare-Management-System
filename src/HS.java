
public class HS {
	private String HSID;
	private Profile p;
	
	public HS(String ID) {
		this.HSID = ID;
		p = new Profile(ID);
	}
	
	public String getName() {
		return p.getName();
	}
	
	public String getAddress() {
		return p.getAddress();
	}
	
	public String getPhone() {
		return p.getPhone();
	}
	
}
