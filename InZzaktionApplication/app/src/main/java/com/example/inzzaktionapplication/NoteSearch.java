package com.example.inzzaktionapplication;
import java.sql.Date;

public class NoteSearch {
    String title, photo, tagNm, rgbCode, likedCount, liked, content, replys, id;
    Date rgstDt;
    int no, reNo, noteNo;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTagNm() {
        return tagNm;
    }

    public void setTagNm(String tagNm) {
        this.tagNm = tagNm;
    }

    public String getRgbCode() {
        return rgbCode;
    }

    public void setRgbCode(String rgbCode) {
        this.rgbCode = rgbCode;
    }

    public String getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(String likedCount) {
        this.likedCount = likedCount;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReplys() {
        return replys;
    }

    public void setReplys(String replys) {
        this.replys = replys;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getRgstDt() {
        return rgstDt;
    }

    public void setRgstDt(Date rgstDt) {
        this.rgstDt = rgstDt;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getReNo() {
        return reNo;
    }

    public void setReNo(int reNo) {
        this.reNo = reNo;
    }

    public int getNoteNo() {
        return noteNo;
    }

    public void setNoteNo(int noteNo) {
        this.noteNo = noteNo;
    }

    @Override
    public String toString() {
        return "TagSearch{" +
                "title='" + title + '\'' +
                ", photo='" + photo + '\'' +
                ", tagNm='" + tagNm + '\'' +
                ", rgbCode='" + rgbCode + '\'' +
                ", likedCount='" + likedCount + '\'' +
                ", liked='" + liked + '\'' +
                ", content='" + content + '\'' +
                ", replys='" + replys + '\'' +
                ", id='" + id + '\'' +
                ", rgstDt=" + rgstDt +
                ", no=" + no +
                ", reNo=" + reNo +
                ", noteNo=" + noteNo +
                '}';
    }
}

