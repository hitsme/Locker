package com.hitsme.locker.app.bus;
/**
 * Created by 10093 on 2017/4/10.
 */

public class Event {
    private String vaultPath;
    private EventType eventType;

    public String getVaultPath() {
        return vaultPath;
    }

    public void setVaultPath(String vaultPath) {
        this.vaultPath = vaultPath;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}
