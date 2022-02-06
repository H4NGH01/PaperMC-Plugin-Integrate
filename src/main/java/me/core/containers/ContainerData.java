package me.core.containers;

import me.core.items.ContainerItemStack;

import java.util.UUID;

public class ContainerData {

    private final UUID uuid;
    private final ContainerType type;
    private ContainerItemStack drop;

    public ContainerData(UUID uuid, ContainerType type, ContainerItemStack drop) {
        this.uuid = uuid;
        this.type = type;
        this.drop = drop;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public ContainerType getType() {
        return this.type;
    }

    public ContainerItemStack getDrop() {
        return this.drop;
    }

    public void setDrop(ContainerItemStack stack) {
        this.drop = stack;
    }
}
