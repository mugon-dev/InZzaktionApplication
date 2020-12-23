package com.example.localinzzaktionapplication.entity;

import com.google.gson.GsonBuilder;

public class Tag {
    private String tagNo;
    private String noteNo;
    private String tagName;
    private String rgbNo;
    private String tagPosition;

    public Tag(String noteNo, String tagName, String rgbNo) {
        this.noteNo = noteNo;
        this.tagName = tagName;
        this.rgbNo = rgbNo;
    }

    public Tag(String tagNo, String noteNo, String tagName, String rgbNo) {
        this.tagNo = tagNo;
        this.noteNo = noteNo;
        this.tagName = tagName;
        this.rgbNo = rgbNo;
    }

    public Tag(String tagNo, String noteNo, String tagName, String rgbNo, String tagPosition) {
        this.tagNo = tagNo;
        this.noteNo = noteNo;
        this.tagName = tagName;
        this.rgbNo = rgbNo;
        this.tagPosition = tagPosition;
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

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getRgbNo() {
        return rgbNo;
    }

    public void setRgbNo(String rgbNo) {
        this.rgbNo = rgbNo;
    }

    public String getTagPosition() {
        return tagPosition;
    }

    public void setTagPosition(String tagPosition) {
        this.tagPosition = tagPosition;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tagNo='" + tagNo + '\'' +
                ", noteNo='" + noteNo + '\'' +
                ", tagName='" + tagName + '\'' +
                ", rgbNo='" + rgbNo + '\'' +
                ", tagPosition='" + tagPosition + '\'' +
                '}';
    }
    public String toJsonString(){
        return new GsonBuilder().create().toJson(this,Tag.class);
    }
}
