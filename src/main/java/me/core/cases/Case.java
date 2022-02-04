package me.core.cases;

import me.core.MCServerPlugin;
import me.core.items.CaseItemRarity;
import me.core.items.CaseItemStack;
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

public abstract class Case {

    private final MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);
    protected final UUID uuid;
    private ItemStack drop;

    public Case() {
        this.uuid = UUID.randomUUID();
        generateDrop();
        plugin.getCaseManager().registryCase(new CaseData(this.uuid, this.getCaseType(), this.drop));
    }

    public Case(UUID uuid) {
        this.uuid = uuid;
        for (CaseData data : plugin.getCaseManager().getCaseDataList()) {
            if (this.uuid.equals(data.getUUID())) {
                this.drop = data.getDrop();
                break;
            }
        }
    }

    public void generateDrop() {
        List<CaseItemStack> list = new ArrayList<>();
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
        for (CaseItemStack stack : getCaseDrops()) {
            if (stack.getItemRarity().equals(rarity)) list.add(stack);
        }
        rate = new Random().nextFloat() * list.size();
        CaseItemStack stack = list.get((int) rate);
        rate = new Random().nextFloat() * 10;
        this.drop = rate <= 1 ? new StatTrak(stack) : stack;
    }

    @NotNull
    public abstract CaseItemStack[] getCaseDrops();

    @NotNull
    public List<ItemStack> getDisplayDrops() {
        List<ItemStack> stackList = new ArrayList<>();
        boolean hasRarity = false;
        for (CaseItemStack stack : getCaseDrops()) {
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
    public abstract CaseType getCaseType();

    @NotNull
    public abstract CaseKey getCaseKey();

    @NotNull
    public abstract Material getCaseTexture();

    @NotNull
    public ItemStack getDrop() {
        return drop;
    }

    private static ItemStack superRarity() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.translatable("case.rare_special_item"));
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isCaseStack(ItemStack stack) {
        return NBTHelper.hasTag(stack, "CaseType");
    }
}
