package org.yy.opc.websocket;



import org.yy.entity.PageData;

import java.util.List;

public class ClockMessage {
    private String message;
    private PageData messagePageData;
    private List<PageData> messageList;

    public ClockMessage() {
    }

    public ClockMessage(String message) {
        this.message = message;
    }

    public ClockMessage(PageData messagePageData) {
        this.messagePageData = messagePageData;
    }
    public ClockMessage(List<PageData> messageList) {
        this.messageList = messageList;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PageData getMessagePageData() {
        return messagePageData;
    }

    public void setMessagePageData(PageData messagePageData) {
        this.messagePageData = messagePageData;
    }

    public List<PageData> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<PageData> messageList) {
        this.messageList = messageList;
    }


}
