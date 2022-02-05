package me.core.items;

import me.core.containers.Container;
import me.core.containers.ContainerType;
import me.core.utils.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class CaseStack extends PluginItem {

    private final ContainerType type;

    public CaseStack(Container containerIn) {
        super(containerIn.getContainerTexture());
        this.type = containerIn.getContainerType();
        this.setDisplayName(Component.translatable(containerIn.getContainerType().getTranslationKey()));
        this.addLore(Component.translatable("gui.container.require_key").args(Component.translatable(containerIn.getKeyType().getTranslationKey())).decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        NBTHelper.setTag(this, "ContainerUUID", containerIn.getUUID().toString());
        NBTHelper.setTag(this, "ContainerType", containerIn.getContainerType().getID());
        this.setPlaceable(false);
    }

    public ContainerType getCaseType() {
        return this.type;
    }
}
