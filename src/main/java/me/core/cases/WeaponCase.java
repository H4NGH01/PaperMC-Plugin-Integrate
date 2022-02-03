package me.core.cases;

import me.core.item.CaseItemRarity;
import me.core.item.CaseItemStack;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class WeaponCase extends Case {

    public WeaponCase() {
        super();
    }

    public WeaponCase(UUID uuid) {
        super(uuid);
    }

    @Override
    public @NotNull CaseType getCaseType() {
        return CaseType.WEAPON_CASE;
    }

    @Override
    public @NotNull CaseKey getCaseKey() {
        return CaseKey.KEY_WEAPON_CASE;
    }

    @Override
    public @NotNull Material getCaseTexture() {
        return Material.CHEST;
    }

    @Override
    public @NotNull CaseItemStack[] getCaseDrops() {
        return new CaseItemStack[]{
                new CaseItemStack(Material.BOW, CaseItemRarity.MIL_SPEC),
                new CaseItemStack(Material.CROSSBOW, CaseItemRarity.MIL_SPEC),
                new CaseItemStack(Material.WOODEN_SWORD, CaseItemRarity.MIL_SPEC),
                new CaseItemStack(Material.WOODEN_AXE, CaseItemRarity.MIL_SPEC),
                new CaseItemStack(Material.STONE_SWORD, CaseItemRarity.MIL_SPEC),
                new CaseItemStack(Material.STONE_AXE, CaseItemRarity.MIL_SPEC),
                new CaseItemStack(Material.IRON_SWORD, CaseItemRarity.RESTRICTED),
                new CaseItemStack(Material.IRON_AXE, CaseItemRarity.RESTRICTED),
                new CaseItemStack(Material.GOLDEN_SWORD, CaseItemRarity.RESTRICTED),
                new CaseItemStack(Material.GOLDEN_AXE, CaseItemRarity.RESTRICTED),
                new CaseItemStack(Material.DIAMOND_SWORD, CaseItemRarity.CLASSIFIED),
                new CaseItemStack(Material.DIAMOND_AXE, CaseItemRarity.CLASSIFIED),
                new CaseItemStack(Material.TRIDENT, CaseItemRarity.CLASSIFIED),
                new CaseItemStack(Material.NETHERITE_SWORD, CaseItemRarity.COVERT),
                new CaseItemStack(Material.NETHERITE_AXE, CaseItemRarity.COVERT),
                new CaseItemStack(Material.NETHERITE_HOE, CaseItemRarity.RARE_SPECIAL, Map.of(Enchantment.DAMAGE_ALL, 20)),
                new CaseItemStack(Material.BOW, CaseItemRarity.RARE_SPECIAL, Map.of(Enchantment.ARROW_DAMAGE, 6)),
                new CaseItemStack(Material.TRIDENT, CaseItemRarity.RARE_SPECIAL, Map.of(Enchantment.DAMAGE_ALL, 5, Enchantment.IMPALING, 5)),
        };
    }
}
