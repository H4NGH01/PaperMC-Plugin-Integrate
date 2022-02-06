package me.core.containers;

import me.core.items.ContainerItemStack;

import java.util.UUID;

public record ContainerData(UUID uuid, ContainerType type, ContainerItemStack drop) {

    public UUID getUUID() {
        return this.uuid;
    }

    public ContainerType getType() {
        return this.type;
    }

    public ContainerItemStack getDrop() {
        return this.drop;
    }
}
