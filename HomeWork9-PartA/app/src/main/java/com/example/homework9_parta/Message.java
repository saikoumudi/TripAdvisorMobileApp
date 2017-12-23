package com.example.homework9_parta;

import java.util.ArrayList;

/**
 * Created by KS Koumudi on 4/22/2017.
 */

public class Message {
    private int msgType;
    private String msg;
    private String photourl;
    private String senderName;
    private String senderUid;
    private long time;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;
    private ArrayList<String> removedFrom;

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public ArrayList<String> getRemovedFrom() {
        return removedFrom;
    }

    public void setRemovedFrom(ArrayList<String> removedFrom) {
        this.removedFrom = removedFrom;
    }
}
