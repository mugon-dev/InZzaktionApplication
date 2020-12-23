package vo;

public class Join {
	 private String id;
	 private String pw;
	
	 
	 public Join() {}
	
	 
	 
	 
	 
	 
	 public Join(String id, String pw) {
		super();
		this.id = id;
		this.pw = pw;
	}






	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}






	@Override
	public String toString() {
		return "Join [id=" + id + ", pw=" + pw + "]";
	}
	 
	
	 
}
