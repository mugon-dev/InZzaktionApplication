package com.example.inzzaktionapplication;

public class Note {
    private String title;
    private String tagTitle;
    private String tagRGB;
    private String serverNo;

    public Note(String title, String tagTitle, String tagRGB, String serverNo) {
        this.title = title;
        this.tagTitle = tagTitle;
        this.tagRGB = tagRGB;
        this.serverNo = serverNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTagTitle() {
        return tagTitle;
    }

    public void setTagTitle(String tagTitle) {
        this.tagTitle = tagTitle;
    }

    public String getTagRGB() {
        return tagRGB;
    }

    public void setTagRGB(String tagRGB) {
        this.tagRGB = tagRGB;
    }

    public String getServerNo() {
        return serverNo;
    }

    public void setServerNo(String serverNo) {
        this.serverNo = serverNo;
    }

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", tagTitle='" + tagTitle + '\'' +
                ", tagRGB='" + tagRGB + '\'' +
                ", serverNo='" + serverNo + '\'' +
                '}';
    }
}
