package com.holela.Models;

public class GetChatModel {

    String senderid , msg , reveiverid , chatdatetime;

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getReveiverid() {
        return reveiverid;
    }

    public void setReveiverid(String reveiverid) {
        this.reveiverid = reveiverid;
    }

    public String getChatdatetime() {
        return chatdatetime;
    }

    public void setChatdatetime(String chatdatetime) {
        this.chatdatetime = chatdatetime;
    }
}
