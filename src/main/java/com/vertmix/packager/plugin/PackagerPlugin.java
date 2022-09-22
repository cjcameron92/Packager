package com.vertmix.packager.plugin;

import com.vertmix.packager.api.PacketService;
import com.vertmix.packager.service.SimplePacketService;
import me.lucko.helper.Services;
import me.lucko.helper.mongo.Mongo;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import me.lucko.helper.redis.Redis;

public class PackagerPlugin extends ExtendedJavaPlugin {

    protected PacketService packetService;

    @Override
    protected void enable() {
        packetService = provideService(PacketService.class, new SimplePacketService(Services.load(Redis.class), Services.load(Mongo.class)));
    }
}
