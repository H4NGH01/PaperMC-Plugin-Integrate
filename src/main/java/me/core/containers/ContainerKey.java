package me.core.containers;

public enum ContainerKey {

    KEY_WEAPON_CASE("key_weapon_case", "container.key.weapon_case.name", ContainerType.WEAPON_CASE);

    private final String id;
    private final String translationKey;
    private final ContainerType canOpen;

    ContainerKey(String id, String translationKey, ContainerType canOpen) {
        this.id = id;
        this.translationKey = translationKey;
        this.canOpen = canOpen;
    }

    public String getID() {
        return this.id;
    }

    public String getTranslationKey() {
        return this.translationKey;
    }

    public ContainerType canOpen() {
        return this.canOpen;
    }
}
