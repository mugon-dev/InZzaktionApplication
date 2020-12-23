package vo;

import java.util.Date;

public class Tag {
	String tagNo;
	String noteNo;
	String tagNm;
	String rgbNo;
	Date rgstDt;
	public Tag() {
		super();
	}
	public Tag(String noteNo, String tagNm, String rgbNo) {
		super();
		this.noteNo = noteNo;
		this.tagNm = tagNm;
		this.rgbNo = rgbNo;
	}
	public String getTagNo() {
		return tagNo;
	}
	public void setTagNo(String tagNo) {
		this.tagNo = tagNo;
	}
	public String getNoteNo() {
		return noteNo;
	}
	public void setNoteNo(String noteNo) {
		this.noteNo = noteNo;
	}
	public String getTagNm() {
		return tagNm;
	}
	public void setTagNm(String tagNm) {
		this.tagNm = tagNm;
	}
	public String getRgbNo() {
		return rgbNo;
	}
	public void setRgbNo(String rgbNo) {
		this.rgbNo = rgbNo;
	}
	public Date getRgstDt() {
		return rgstDt;
	}
	public void setRgstDt(Date rgstDt) {
		this.rgstDt = rgstDt;
	}
	@Override
	public String toString() {
		return "Tag [tagNo=" + tagNo + ", noteNo=" + noteNo + ", tagNm=" + tagNm + ", rgbNo=" + rgbNo + ", rgstDt="
				+ rgstDt + "]";
	}
	

}
