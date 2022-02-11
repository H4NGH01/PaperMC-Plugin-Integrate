package me.core.items;

import me.core.containers.Container;
import me.core.containers.ContainerData;
import me.core.containers.ContainerType;
import me.core.utils.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CaseStack extends PluginItem {

    private final ContainerType type;

    public CaseStack(@NotNull Container containerIn, @NotNull ContainerData data) {
        super(containerIn.getContainerTexture());
        this.type = containerIn.getContainerType();
        this.setDisplayName(Component.translatable(containerIn.getContainerType().getTranslationKey()));
        this.addLore(Component.translatable("gui.container.require_key").args(Component.translatable(containerIn.getKeyType().getTranslationKey())).decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        this.addLore(Component.translatable("gui.container.contains").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        for (ContainerItemStack stack : containerIn.getDisplayDrops()) {
            this.addLore(Objects.requireNonNull(stack.getItemMeta().displayName()).decoration(TextDecoration.ITALIC, false));
        }
        NBTHelper.setTag(this, "ContainerUUID", data.getUUID().toString());
        NBTHelper.setTag(this, "ContainerType", data.getType().getID());
        this.setPlaceable(false);
    }

    public ContainerType getCaseType() {
        return this.type;
    }
}
