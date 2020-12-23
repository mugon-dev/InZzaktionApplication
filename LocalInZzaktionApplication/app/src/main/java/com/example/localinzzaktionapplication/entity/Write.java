package com.example.localinzzaktionapplication.entity;

import com.google.gson.GsonBuilder;

public class Write {
    private String memberNo;
    private String title;
    private String shared;
    private String photo;
    private String content;

    public Write() {
    }

    public Write(String title) {
        this.title = title;
    }

    public Write(String memberNo, String title, String shared, String photo, String content) {
        this.memberNo = memberNo;
        this.title = title;
        this.shared = shared;
        this.photo = photo;
        this.content = content;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShared() {
        return shared;
    }

    public void setShared(String shared) {
        this.shared = shared;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String toJsonString(){
        return new GsonBuilder().create().toJson(this,Write.class);
    }
}
