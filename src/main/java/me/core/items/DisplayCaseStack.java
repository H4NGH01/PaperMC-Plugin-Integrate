package me.core.items;

import me.core.containers.Container;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DisplayCaseStack extends PluginItem {

    public DisplayCaseStack(@NotNull Container container) {
        super(container.getContainerTexture());
        this.setDisplayName(Component.translatable(container.getContainerType().getTranslationKey()));
        this.addLore(Component.translatable("gui.container.require_key").args(Component.translatable(container.getKeyType().getTranslationKey())).decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        this.addLore(Component.translatable("gui.container.contains").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        for (ContainerItemStack stack : container.getDisplayDrops()) {
            this.addLore(Objects.requireNonNull(stack.getItemMeta().displayName()).decoration(TextDecoration.ITALIC, false));
        }
    }
}
