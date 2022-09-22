package com.vertmix.packager.api.packets;

import com.vertmix.packager.api.Packet;
import me.lucko.helper.serialize.Position;

import java.util.UUID;

public class PlayerTeleportPacket extends Packet {

    private UUID playerUuid;
    private String serverId;
    private Position position;

    public PlayerTeleportPacket() {
        super("packet-teleport");
    }

    public PlayerTeleportPacket(UUID playerUuid, String serverId, Position position) {
        this();
        this.playerUuid = playerUuid;
        this.serverId = serverId;
        this.position = position;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getServerId() {
        return serverId;
    }

    public Position getPosition() {
        return position;
    }
}
