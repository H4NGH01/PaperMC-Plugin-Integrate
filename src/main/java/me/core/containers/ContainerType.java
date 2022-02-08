package me.core.containers;

import org.jetbrains.annotations.NotNull;

public enum ContainerType {

    WEAPON_CASE("weapon_case", "container.weapon_case.name", WeaponCase.class);

    private final String id;
    private final String translationKey;
    private final Class<? extends Container> container;

    ContainerType(String id, String translationKey, Class<? extends Container> container) {
        this.id = id;
        this.translationKey = translationKey;
        this.container = container;
    }

    public String getID() {
        return this.id;
    }

    public String getTranslationKey() {
        return this.translationKey;
    }

    public Class<? extends Container> getContainer() {
        return this.container;
    }

    public static @NotNull ContainerType byID(String id) {
        for (ContainerType type : values()) {
            if (type.getID().equals(id)) {
                return type;
            }
        }
        throw new IllegalArgumentException("container id cannot be found.");
    }
}
