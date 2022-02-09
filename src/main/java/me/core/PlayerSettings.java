package me.core;

import org.jetbrains.annotations.NotNull;

public enum PlayerSettings {
    NEW_MAIL_HINT("NewMailHint");

    private final String s;

    PlayerSettings(String s) {
        this.s = s;
    }

    public String getKey() {
        return this.s;
    }

    public static @NotNull PlayerSettings byKey(String s) {
        for (PlayerSettings ps : values()) {
            if (ps.getKey().equals(s)) return ps;
        }
        throw new IllegalArgumentException("Unknown player settings key");
    }
}