package vo;

public class tagName {
	String tag;
	String title;
	
	public tagName(String tag, String title) {
		super();
		this.tag = tag;
		this.title = title;
	}
	
	public tagName() {}
	
	public String getTag() {
		return tag;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return "Note [tag:" + tag + ", title=:"+ title + "]";
	}
}

