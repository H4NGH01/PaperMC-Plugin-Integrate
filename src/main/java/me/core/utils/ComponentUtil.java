package me.core.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ComponentUtil {

    public static @NotNull Component component(Component @NotNull ... components) {
        TextComponent.Builder builder = Component.text().decoration(TextDecoration.ITALIC, false);
        for (Component c : components) {
            builder.append(c);
        }
        return builder.build();
    }

    public static @NotNull Component component(TextColor color, Component... component) {
        return component(component).color(color);
    }

    public static @NotNull Component text(String @NotNull ... string) {
        Component[] components = new Component[string.length];
        for (int i = 0; i < string.length; i++) {
            components[i] = Component.text(string[i]);
        }
        return component(components);
    }

    public static @NotNull Component text(TextColor color, String... string) {
        return component(color, text(string));
    }

    public static @NotNull Component text(int @NotNull ... i) {
        Component[] components = new Component[i.length];
        for (int j = 0; j < i.length; j++) {
            components[j] = Component.text(i[j]);
        }
        return component(components);
    }

    public static @NotNull Component text(TextColor color, int... i) {
        return component(color, text(i));
    }

    public static @NotNull Component translate(String @NotNull ... keys) {
        Component[] components = new Component[keys.length];
        for (int i = 0; i < keys.length; i++) {
            components[i] = Component.translatable(keys[i]);
        }
        return component(components);
    }

    public static @NotNull Component translate(TextColor color, String... keys) {
        return translate(keys).color(color);
    }

    @Contract(pure = true)
    public static @NotNull Component setBold(@NotNull Component component, boolean bold) {
        return component.decoration(TextDecoration.BOLD, bold);
    }

    @Contract(pure = true)
    public static @NotNull Component setItalic(@NotNull Component component, boolean bold) {
        return component.decoration(TextDecoration.ITALIC, bold);
    }

    public static @NotNull String plainText(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }
}
