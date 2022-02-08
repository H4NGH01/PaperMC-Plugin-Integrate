package me.core.items;

import me.core.containers.ContainerKey;
import me.core.utils.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class CaseKeyStack extends PluginItem {

    private final ContainerKey type;

    public CaseKeyStack(@NotNull ContainerKey containerKey) {
        super(Material.TRIPWIRE_HOOK);
        this.type = containerKey;
        this.setDisplayName(Component.translatable(containerKey.getTranslationKey()));
        this.addLore(Component.translatable("container.key.can_open").args(Component.translatable(containerKey.canOpen().getTranslationKey())).decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        NBTHelper.setTag(this, "ContainerKey", containerKey.getID());
        this.setPlaceable(false);
    }

    public ContainerKey getKeyType() {
        return this.type;
    }
}
