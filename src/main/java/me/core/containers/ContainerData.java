package me.core.containers;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ContainerData {

    private final UUID uuid;
    private final ContainerType type;
    private final ItemStack drop;

    public ContainerData(UUID uuid, ContainerType type, ItemStack drop) {
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

    public ItemStack getDrop() {
        return this.drop;
    }
}
