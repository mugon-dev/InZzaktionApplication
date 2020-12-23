package vo;

public class Login {
	
	private String id;
	private String pw;
	private String memberNo;
	
	public Login() {}

	public Login(String id, String pw, String memberNo) {
		super();
		this.id = id;
		this.pw = pw;
		this.memberNo = memberNo;
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

	public String getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}

	@Override
	public String toString() {
		return "Login [id=" + id + ", pw=" + pw + ", memberNo=" + memberNo + "]";
	}
	
	
	
	

}
