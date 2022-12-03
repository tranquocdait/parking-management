package com.huyendieu.parking.model.notification;

import lombok.Data;

@Data
public class NotificationModel {

    public NotificationModel(String type, Object data) {
        this.type = type;
        this.data = data;
        this.active = true;
    }

    private String type;

    private Object data;

    private boolean active;
}
