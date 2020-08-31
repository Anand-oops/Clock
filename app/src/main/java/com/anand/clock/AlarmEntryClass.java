package com.anand.clock;

public class AlarmEntryClass {

    private String id;
    private String time;
    private String status;

    public AlarmEntryClass(String id, String time, String status) {
        this.id = id;
        this.time = time;
        this.status = status;
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
}
