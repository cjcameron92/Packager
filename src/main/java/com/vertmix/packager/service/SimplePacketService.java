package com.vertmix.packager.service;

import com.vertmix.packager.api.Packet;
import com.vertmix.packager.api.PacketService;
import com.vertmix.packager.api.PacketSubscriber;
import me.lucko.helper.Schedulers;
import me.lucko.helper.mongo.Mongo;
import me.lucko.helper.mongo.external.mongodriver.BasicDBObject;
import me.lucko.helper.mongo.external.mongodriver.DBObject;
import me.lucko.helper.mongo.external.morphia.Datastore;
import me.lucko.helper.mongo.external.morphia.Morphia;
import me.lucko.helper.redis.Redis;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.*;
import java.util.function.Consumer;

public class SimplePacketService implements PacketService {

    private static final String PACKET_CHANNEL_NAME = "vpackets";
    private final Redis redis;
    private final Morphia morphia;
    private final Datastore datastore;
    private Map<String, Set<PacketSubscriber<?>>> listeners;

    public SimplePacketService(Redis redis, Mongo mongo) {
        this.redis = redis;
        this.morphia = mongo.getMorphia();
        this.datastore = mongo.getMorphiaDatastore();

        this.listeners = new LinkedHashMap<>();

        execute(jedis -> jedis.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                final DBObject dbObject = BasicDBObject.parse(message);
                if (listeners.containsKey(channel)) {
                    listeners.get(channel).forEach(packetSubscriber -> {
                        final Packet packet = morphia.fromDBObject(datastore, packetSubscriber.getClazz(), dbObject);
                        if (packet != null)
                            packetSubscriber.execute(packetSubscriber.getClazz().cast(packet));
                    });
                }
            }
        }, PACKET_CHANNEL_NAME));
    }

    @Override
    public void send(@NotNull Packet packet) {
        execute(jedis -> {
            final DBObject dbObject = morphia.toDBObject(packet);
            jedis.publish(PACKET_CHANNEL_NAME, dbObject.toString());
        });
    }

    @Override
    public <T extends Packet> void listen(@NotNull Class<T> clazz, @NotNull Consumer<T> consumer) {
        final PacketSubscriber<T> subscriber = new PacketSubscriber<>(clazz, consumer);
        listeners.computeIfAbsent(PACKET_CHANNEL_NAME, $ -> new HashSet<>()).add(subscriber);
    }

    public void execute(@NotNull Consumer<Jedis> consumer) {
        Schedulers.async().run(() -> {
            try (Jedis jedis = redis.getJedis()) {
                consumer.accept(jedis);
            }
        });
    }
}
