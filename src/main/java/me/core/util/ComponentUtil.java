package me.core.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class ComponentUtil {

    public static Component text(String... string) {
        Component[] components = new Component[string.length];
        for (int i = 0; i < string.length; i++) {
            components[i] = Component.text(string[i]);
        }
        return component(components);
    }

    public static Component text(TextColor color, String... string) {
        return component(color, text(string));
    }

    public static Component component(Component... components) {
        TextComponent.Builder builder = Component.text().decoration(TextDecoration.ITALIC, false);
        for (Component c : components) {
            builder.append(c);
        }
        return builder.build();
    }

    public static Component component(TextColor color, Component... component) {
        return component(component).color(color);
    }

    public static Component translate(String... keys) {
        Component[] components = new Component[keys.length];
        for (int i = 0; i < keys.length; i++) {
            components[i] = Component.translatable(keys[i]);
        }
        return component(components);
    }

    public static Component translate(TextColor color, String... keys) {
        return translate(keys).color(color);
    }

    public static Component setBold(Component component, boolean bold) {
        return component.decoration(TextDecoration.BOLD, bold);
    }

    public static Component setItalic(Component component, boolean bold) {
        return component.decoration(TextDecoration.ITALIC, bold);
    }

    public static String plainText(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

}
