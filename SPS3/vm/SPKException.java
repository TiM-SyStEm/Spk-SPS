package com.timsystem.spk.vm;

public class SPKException extends RuntimeException {

    private String type, message;
    private int line;

    public SPKException(String type, String message, int line) {
        super(message);
        this.type = type;
        this.message = message;
        this.line = line;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
