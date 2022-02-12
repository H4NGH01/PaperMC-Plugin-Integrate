package me.core.items;

import me.core.containers.ContainerKey;
import me.core.utils.ComponentUtil;
import me.core.utils.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class CaseKeyStack extends PluginItem {

    private final ContainerKey type;

    public CaseKeyStack(@NotNull ContainerKey containerKey) {
        super(Material.TRIPWIRE_HOOK);
        this.type = containerKey;
        this.setDisplayName(Component.translatable(containerKey.getTranslationKey()));
        this.addLore(ComponentUtil.translate(NamedTextColor.GRAY, "container.key.can_open").args(ComponentUtil.translate(NamedTextColor.YELLOW, containerKey.canOpen().getTranslationKey())));
        NBTHelper.setTag(this, "ContainerKey", containerKey.getID());
        this.setPlaceable(false);
    }

    public ContainerKey getKeyType() {
        return this.type;
    }
}
