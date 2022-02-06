package me.core.containers;

import me.core.MCServerPlugin;
import me.core.items.CaseItemRarity;
import me.core.items.ContainerItemStack;
import me.core.items.StatTrak;
import me.core.utils.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public abstract class Container {

    private final MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);
    protected final UUID uuid;
    private ContainerItemStack drop;
    private final ContainerData data;

    /**
     * Generate a new Container
     */
    public Container() {
        this.uuid = UUID.randomUUID();
        this.drop = generateDrop();
        this.data = new ContainerData(this.uuid, this.getContainerType(), this.drop);
        plugin.getContainerManager().registryContainerData(data);
    }

    /**
     * Load an existed Container
     * @param uuid UUID of Container
     */
    public Container(UUID uuid) {
        this.uuid = uuid;
        for (ContainerData data : plugin.getContainerManager().getCaseDataList()) {
            if (this.uuid.equals(data.getUUID())) {
                this.data = data;
                this.drop = data.getDrop();
                return;
            }
        }
        this.data = null;
    }

    public ContainerItemStack generateDrop() {
        List<ContainerItemStack> list = new ArrayList<>();
        CaseItemRarity rarity = CaseItemRarity.MIL_SPEC;
        float f = 0;
        for (CaseItemRarity rarity1 : CaseItemRarity.values()) {
            f += rarity1.getDropRate();
        }
        float rate = new Random().nextFloat() * f;
        for (CaseItemRarity itemRarity : CaseItemRarity.values()) {
            float j = f - itemRarity.getDropRate();
            if (rate > j && rate <= f) {
                rarity = itemRarity;
                break;
            }
            f = j;
        }
        for (ContainerItemStack stack : getContainerDrops()) {
            if (stack.getItemRarity().equals(rarity)) list.add(stack);
        }
        rate = new Random().nextFloat() * list.size();
        ContainerItemStack stack = list.get((int) rate);
        rate = new Random().nextFloat() * 10;
        return rate <= 1 ? new ContainerItemStack(new StatTrak(stack)) : stack;
    }

    @NotNull
    public abstract ContainerItemStack[] getContainerDrops();

    @NotNull
    public List<ContainerItemStack> getDisplayDrops() {
        List<ContainerItemStack> stackList = new ArrayList<>();
        boolean hasRarity = false;
        for (ContainerItemStack stack : getContainerDrops()) {
            if (!stack.isRareSpecial()) {
                stack.setDisplayName(Component.translatable(stack.translationKey()).color(TextColor.color(stack.getItemRarity().getColor())));
                stackList.add(stack);
            } else {
                hasRarity = true;
            }
        }
        if (hasRarity) stackList.add(superRarity());
        return stackList;
    }

    @NotNull
    public UUID getUUID() {
        return this.uuid;
    }

    @NotNull
    public abstract ContainerType getContainerType();

    @NotNull
    public abstract ContainerKey getKeyType();

    @NotNull
    public abstract Material getContainerTexture();

    @NotNull
    public ContainerItemStack getDrop() {
        return this.drop;
    }

    public boolean hasData() {
        return this.data != null;
    }

    public static ContainerItemStack superRarity() {
        ContainerItemStack item = (ContainerItemStack) new ContainerItemStack(Material.NETHER_STAR, CaseItemRarity.RARE_SPECIAL).setTag("ItemTag", "rare_special_item");
        item.setDisplayName(Component.translatable("container.rare_special_item"));
        return item;
    }

    public static boolean isContainerStack(ItemStack stack) {
        return NBTHelper.hasTag(stack, "ContainerType");
    }
}
