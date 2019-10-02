package com.bt.bbcp;

enum Status {
    OK,
    INVALID
    };


public class MetricState {

    private String message;
    private Status status;

    public MetricState() {
    }

    public MetricState(String message, Status status) {
        this.message = message;
        this.status = status;
    }

    public void makeState(String message, Status status) {
        this.message = message;
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "MetricState{" +
                "message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
