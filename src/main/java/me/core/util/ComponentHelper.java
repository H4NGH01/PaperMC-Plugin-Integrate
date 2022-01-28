package me.core.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;

public class ComponentHelper {

    public static Component translate(String... key) {
        Component component = Component.translatable(key[0]).decoration(TextDecoration.ITALIC, false);
        for (String s : key) {
            component.append(Component.translatable(s));
        }
        return component;
    }

    public static Component setBold(Component component, boolean bold) {
        return component.decoration(TextDecoration.BOLD, bold);
    }

    public static Component setItalic(Component component, boolean bold) {
        return component.decoration(TextDecoration.ITALIC, bold);
    }

    public static TextColor convertTextColor(Color color) {
        return TextColor.color(color.asRGB());
    }

}
