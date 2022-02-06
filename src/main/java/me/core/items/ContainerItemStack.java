package me.core.items;

import me.core.utils.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ContainerItemStack extends PluginItem {

    private final CaseItemRarity rarity;

    @SafeVarargs
    public ContainerItemStack(Material material, CaseItemRarity rarity, Map<Enchantment, Integer>... enchantments) {
        super(material);
        this.rarity = rarity;
        if (this.rarity.equals(CaseItemRarity.RARE_SPECIAL)) this.setDisplayName(Component.translatable(this.translationKey()).append(Component.text(" (★)")));
        for (Map<Enchantment, Integer> enchantment : enchantments) {
            this.addUnsafeEnchantments(enchantment);
        }
        NBTHelper.setTag(this, "rarity", this.rarity.name());
    }

    public ContainerItemStack(ItemStack stack) {
        super(stack);
        if (!NBTHelper.hasTag(this, "rarity")) throw new IllegalArgumentException("This ItemStack cannot be cast to ContainerItemStack");
        this.rarity = CaseItemRarity.valueOf(NBTHelper.getTag(this).l("rarity"));
    }

    public CaseItemRarity getItemRarity() {
        return this.rarity;
    }

    public boolean isRareSpecial() {
        return this.rarity.equals(CaseItemRarity.RARE_SPECIAL);
    }

}
