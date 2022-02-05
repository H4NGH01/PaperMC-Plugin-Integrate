package me.core.containers;

import me.core.MCServerPlugin;
import me.core.items.CaseItemRarity;
import me.core.items.ContainerItemStack;
import me.core.items.StatTrak;
import me.core.utils.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public abstract class Container {

    private final MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);
    protected final UUID uuid;
    private ItemStack drop;

    /**
     * Generate a new Container
     */
    public Container() {
        this.uuid = UUID.randomUUID();
        generateDrop();
        plugin.getContainerManager().registryContainerData(new ContainerData(this.uuid, this.getContainerType(), this.drop));
    }

    /**
     * Load an existed Container
     * @param uuid UUID of Container
     */
    public Container(UUID uuid) {
        this.uuid = uuid;
        for (ContainerData data : plugin.getContainerManager().getCaseDataList()) {
            if (this.uuid.equals(data.getUUID())) {
                this.drop = data.getDrop();
                break;
            }
        }
    }

    public void generateDrop() {
        List<ContainerItemStack> list = new ArrayList<>();
        CaseItemRarity rarity = CaseItemRarity.MIL_SPEC;
        int i = 0;
        for (CaseItemRarity itemRarity : CaseItemRarity.values()) {
            i += itemRarity.getDropRate();
        }
        float rate = new Random().nextFloat() * i;
        for (CaseItemRarity itemRarity : CaseItemRarity.values()) {
            int j = (int) (i - itemRarity.getDropRate());
            if (rate > j && rate <= i) {
                rarity = itemRarity;
                break;
            }
            i = j;
        }
        for (ContainerItemStack stack : getContainerDrops()) {
            if (stack.getItemRarity().equals(rarity)) list.add(stack);
        }
        rate = new Random().nextFloat() * list.size();
        ContainerItemStack stack = list.get((int) rate);
        rate = new Random().nextFloat() * 10;
        this.drop = rate <= 1 ? new StatTrak(stack) : stack;
    }

    @NotNull
    public abstract ContainerItemStack[] getContainerDrops();

    @NotNull
    public List<ItemStack> getDisplayDrops() {
        List<ItemStack> stackList = new ArrayList<>();
        boolean hasRarity = false;
        for (ContainerItemStack stack : getContainerDrops()) {
            if (!stack.isRareSpecial()) {
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
    public ItemStack getDrop() {
        return drop;
    }

    private static ItemStack superRarity() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.translatable("container.rare_special_item"));
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isContainerStack(ItemStack stack) {
        return NBTHelper.hasTag(stack, "ContainerType");
    }
}
