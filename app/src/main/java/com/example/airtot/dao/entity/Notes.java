package com.example.airtot.dao.entity;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

public class Notes extends LitePalSupport implements Serializable {

    private long id;

    private String title;

    private String preview;

    private String content;

    private Date createTime;

    private Date modifiedTime;

    private Boolean delete_flag;

    private String category;

    private int titleImg;

    private Date alarmTime;

    private Boolean alarm;

    private int senderId;

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public Boolean getAlarm() {
        return alarm;
    }

    public void setAlarm(Boolean alarm) {
        this.alarm = alarm;
    }

    public int getTitleImg() {
        return titleImg;
    }

    public void setTitleImg(int titleImg) {
        this.titleImg = titleImg;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Notes() {
    }

    public Notes(String title, String preview, String content, Date createTime, Date modifiedTime, Boolean delete_flag) {
        this.title = title;
        this.preview = preview;
        this.content = content;
        this.createTime = createTime;
        this.modifiedTime = modifiedTime;
        this.delete_flag = delete_flag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Boolean getDelete_flag() {
        return delete_flag;
    }

    public void setDelete_flag(Boolean delete_flag) {
        this.delete_flag = delete_flag;
    }
}
