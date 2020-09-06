package com.anand.clock;

public class AlarmEntryClass {

    private String id;
    private String time;
    private String status;
    private String mediaCode;

    public AlarmEntryClass(String id, String time, String status, String mediaCode) {
        this.id = id;
        this.time = time;
        this.status = status;
        this.mediaCode = mediaCode;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public String getMediaCode() {
        return mediaCode;
    }
}
