package com.example.rsocket.requester.constants;

public enum OperationType {
    READ("read"), WRITE("write");

    private String name;

    OperationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
