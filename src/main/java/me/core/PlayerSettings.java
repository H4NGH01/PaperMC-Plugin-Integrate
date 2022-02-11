package me.core;

import org.jetbrains.annotations.NotNull;

public enum PlayerSettings {
    NEW_MAIL_MESSAGE("NewMailMessage"),
    CONTAINER_ANIMATION("ContainerAnimation"),
    ;

    private final String s;

    PlayerSettings(String s) {
        this.s = s;
    }

    public String getKey() {
        return this.s;
    }

    public static boolean isRegistryKey(String key) {
        for (PlayerSettings ps : values()) {
            if (ps.getKey().equals(key)) return true;
        }
        return false;
    }

    public static @NotNull PlayerSettings byKey(String key) {
        for (PlayerSettings ps : values()) {
            if (ps.getKey().equals(key)) return ps;
        }
        throw new IllegalArgumentException("Unknown player settings key");
    }
}