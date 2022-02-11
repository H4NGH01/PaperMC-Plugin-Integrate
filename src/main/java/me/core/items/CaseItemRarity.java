package me.core.items;

import net.kyori.adventure.text.format.NamedTextColor;

public enum CaseItemRarity {

    MIL_SPEC(625.0f / 782.0f, 0x456dfd),
    RESTRICTED(125.0f / 782.0f, 0x7e54c7),
    CLASSIFIED(25.0f / 782.0f, 0xd629e5),
    COVERT(5.0f / 782.0f, 0xe94c4c),
    EXCEEDINGLY_RARE(2.0f / 782.0f, NamedTextColor.GOLD.value());

    private final float dropRate;
    private final int color;

    CaseItemRarity(float dropRate, int color) {
        this.dropRate = dropRate;
        this.color = color;
    }

    public float getDropRate() {
        return this.dropRate;
    }

    public int getColor() {
        return this.color;
    }

}
