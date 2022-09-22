package com.vertmix.packager.api;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface PacketService {

    void send(@NotNull Packet packet);

    <T extends Packet> void listen(@NotNull Class<T> clazz, @NotNull Consumer<T> consumer);
}
