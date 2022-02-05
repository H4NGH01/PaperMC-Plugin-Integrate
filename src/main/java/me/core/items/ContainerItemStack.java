package me.core.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ContainerItemStack extends ItemStack {

    private final CaseItemRarity rarity;

    @SafeVarargs
    public ContainerItemStack(Material material, CaseItemRarity rarity, Map<Enchantment, Integer>... enchantments) {
        super(material);
        this.rarity = rarity;
        for (Map<Enchantment, Integer> enchantment : enchantments) {
            this.addUnsafeEnchantments(enchantment);
        }
    }

    public CaseItemRarity getItemRarity() {
        return this.rarity;
    }

    public boolean isRareSpecial() {
        return this.rarity.equals(CaseItemRarity.RARE_SPECIAL);
    }

}
