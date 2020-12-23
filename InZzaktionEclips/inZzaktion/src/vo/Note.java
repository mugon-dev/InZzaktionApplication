package vo;

import java.util.Date;

public class Note {
	String noteNo;
	String memberNo;
	String title;
	String content;
	String photo;
	String share;
	Date rgstDt;

	public Note() {
		super();
	}
	public Note(String title, String content, String share) {
		super();
		this.title = title;
		this.content = content;
		this.share = share;
	}
	public Note(String title, String content, String photo, String share) {
		super();
		this.title = title;
		this.content = content;
		this.photo = photo;
		this.share = share;
	}
	public Note(String noteNo, String title, String content, String photo, String share, Date rgstDt) {
		super();
		this.noteNo = noteNo;
		this.title = title;
		this.content = content;
		this.photo = photo;
		this.share = share;
		this.rgstDt = rgstDt;
	}
	
	
	public Note(String noteNo, String memberNo, String title, String content, String photo, String share, Date rgstDt) {
		super();
		this.noteNo = noteNo;
		this.memberNo = memberNo;
		this.title = title;
		this.content = content;
		this.photo = photo;
		this.share = share;
		this.rgstDt = rgstDt;
	}
	public String getMemberNo() {
		return memberNo;
	}
	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}
	public String getNoteNo() {
		return noteNo;
	}
	public void setNoteNo(String noteNo) {
		this.noteNo = noteNo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getShare() {
		return share;
	}
	public void setShare(String share) {
		this.share = share;
	}
	public Date getRgstDt() {
		return rgstDt;
	}
	public void setRgstDt(Date rgstDt) {
		this.rgstDt = rgstDt;
	}
	@Override
	public String toString() {
		return "Note [noteNo=" + noteNo + ", memberNo=" + memberNo + ", title=" + title + ", content=" + content
				+ ", photo=" + photo + ", share=" + share + ", rgstDt=" + rgstDt + "]";
	}
	
}
