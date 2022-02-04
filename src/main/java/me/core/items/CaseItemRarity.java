package me.core.items;

public enum CaseItemRarity {

    MIL_SPEC(625.0f / 782.0f),
    RESTRICTED(125.0f / 782.0f),
    CLASSIFIED(25.0f / 782.0f),
    COVERT(5.0f / 782.0f),
    RARE_SPECIAL(2.0f / 782.0f);

    private final float dropRate;

    CaseItemRarity(float dropRate) {
        this.dropRate = dropRate;
    }

    public float getDropRate() {
        return this.dropRate;
    }

}
