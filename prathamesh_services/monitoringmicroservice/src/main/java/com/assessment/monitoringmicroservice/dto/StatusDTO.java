package com.assessment.monitoringmicroservice.dto;

import java.time.ZonedDateTime;

public class StatusDTO {
    private ZonedDateTime timestamp;
    private String flag; // "GREEN" or "RED"

    // Constructors, getters, setters

    public StatusDTO() {}

    public StatusDTO(ZonedDateTime timestamp, String flag) {
        this.timestamp = timestamp;
        this.flag = flag;
    }


    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
