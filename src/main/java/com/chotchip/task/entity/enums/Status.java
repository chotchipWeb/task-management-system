package com.chotchip.task.entity.enums;

public enum Status {
    PENDING("PENDING"), IN_PROGRESS("IN_PROGRESS"), COMPLETED("COMPLETED");

     final String status;

    Status(String status) {
        this.status = status;
    }
}
