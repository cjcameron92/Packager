package com.vertmix.packager.api.packets;

import com.vertmix.packager.api.Packet;

public class ProfileMessagePacket extends Packet {

    private String playerName;
    private String message;

    public ProfileMessagePacket() {
        super("profile-message");
    }
    public ProfileMessagePacket(String playerName, String message) {
        this();
        this.playerName = playerName;
        this.message = message;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getMessage() {
        return message;
    }
}
