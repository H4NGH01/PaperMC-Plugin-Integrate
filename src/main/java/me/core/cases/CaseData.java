package me.core.cases;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CaseData {

    private final UUID uuid;
    private final CaseType type;
    private final ItemStack drop;

    public CaseData(UUID uuid, CaseType type, ItemStack drop) {
        this.uuid = uuid;
        this.type = type;
        this.drop = drop;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public CaseType getType() {
        return this.type;
    }

    public ItemStack getDrop() {
        return this.drop;
    }
}
