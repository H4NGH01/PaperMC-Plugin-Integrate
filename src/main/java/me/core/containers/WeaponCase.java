package me.core.containers;

import me.core.items.CaseItemRarity;
import me.core.items.ContainerItemStack;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class WeaponCase extends Container {

    public WeaponCase() {
        super();
    }

    public WeaponCase(UUID uuid) {
        super(uuid);
    }

    @Override
    public @NotNull ContainerType getContainerType() {
        return ContainerType.WEAPON_CASE;
    }

    @Override
    public @NotNull ContainerKey getKeyType() {
        return ContainerKey.KEY_WEAPON_CASE;
    }

    @Override
    public @NotNull Material getContainerTexture() {
        return Material.CHEST;
    }

    @Override
    public @NotNull ContainerItemStack[] getContainerDrops() {
        return new ContainerItemStack[]{
                new ContainerItemStack(Material.BOW, CaseItemRarity.MIL_SPEC),
                new ContainerItemStack(Material.CROSSBOW, CaseItemRarity.MIL_SPEC),
                new ContainerItemStack(Material.WOODEN_SWORD, CaseItemRarity.MIL_SPEC),
                new ContainerItemStack(Material.WOODEN_AXE, CaseItemRarity.MIL_SPEC),
                new ContainerItemStack(Material.STONE_SWORD, CaseItemRarity.MIL_SPEC),
                new ContainerItemStack(Material.STONE_AXE, CaseItemRarity.MIL_SPEC),
                new ContainerItemStack(Material.IRON_SWORD, CaseItemRarity.RESTRICTED),
                new ContainerItemStack(Material.IRON_AXE, CaseItemRarity.RESTRICTED),
                new ContainerItemStack(Material.GOLDEN_SWORD, CaseItemRarity.RESTRICTED),
                new ContainerItemStack(Material.GOLDEN_AXE, CaseItemRarity.RESTRICTED),
                new ContainerItemStack(Material.DIAMOND_SWORD, CaseItemRarity.CLASSIFIED),
                new ContainerItemStack(Material.DIAMOND_AXE, CaseItemRarity.CLASSIFIED),
                new ContainerItemStack(Material.TRIDENT, CaseItemRarity.CLASSIFIED),
                new ContainerItemStack(Material.NETHERITE_SWORD, CaseItemRarity.COVERT),
                new ContainerItemStack(Material.NETHERITE_AXE, CaseItemRarity.COVERT),
                new ContainerItemStack(Material.NETHERITE_HOE, CaseItemRarity.RARE_SPECIAL, Map.of(Enchantment.DAMAGE_ALL, 20)),
                new ContainerItemStack(Material.BOW, CaseItemRarity.RARE_SPECIAL, Map.of(Enchantment.ARROW_DAMAGE, 6, Enchantment.DAMAGE_ALL, 10)),
                new ContainerItemStack(Material.TRIDENT, CaseItemRarity.RARE_SPECIAL, Map.of(Enchantment.IMPALING, 5, Enchantment.DAMAGE_ALL, 5)),
        };
    }
}
