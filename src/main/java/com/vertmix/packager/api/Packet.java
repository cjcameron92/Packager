package com.vertmix.packager.api;

public class Packet {

    private String id;

    public Packet() {
        this.id = "unknown";
    }

    public Packet(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
