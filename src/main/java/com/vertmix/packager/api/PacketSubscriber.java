package com.vertmix.packager.api;

import java.util.function.Consumer;

public class PacketSubscriber<T extends Packet> {
    private final Class<T> clazz;
    private final Consumer<T> consumer;

    public PacketSubscriber(Class<T> clazz, Consumer<T> consumer) {
        this.clazz = clazz;
        this.consumer = consumer;
    }

    public void execute(Packet packet) {
        consumer.accept(clazz.cast(packet));

    }

    public Class<T> getClazz() {
        return clazz;
    }

    public Consumer<T> getConsumer() {
        return consumer;
    }
}
