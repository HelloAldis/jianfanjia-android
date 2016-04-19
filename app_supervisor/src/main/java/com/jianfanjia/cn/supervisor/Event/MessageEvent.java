package com.jianfanjia.cn.supervisor.Event;

/**
 * Name: MessageEvent
 * User: fengliang
 * Date: 2015-12-01
 * Time: 11:22
 */
public class MessageEvent {
    private int eventType;
    private String msg;

    public MessageEvent(int eventType) {
        this.eventType = eventType;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
