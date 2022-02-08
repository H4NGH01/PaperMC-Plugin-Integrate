package me.core.listeners;

import me.core.items.StatTrak;
import me.core.utils.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class StatTrakListener implements Listener {

    private static final HashMap<Projectile, ItemStack> BOW_ITEMSTACK_MAP = new HashMap<>();

    @EventHandler
    public void onKills(@NotNull EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;
        if (e.isCancelled()) return;
        if (e.getDamage() < ((LivingEntity) e.getEntity()).getHealth()) return;
        Entity damager = e.getDamager();
        if (!(damager instanceof Player || (damager instanceof Projectile && ((Projectile) damager).getShooter() instanceof Player)))
            return;
        Player killer;
        ItemStack weapon = null;
        if (damager instanceof Player) {
            killer = (Player) e.getDamager();
            if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) {
                weapon = killer.getInventory().getItemInMainHand();
            } else if (e.getCause().equals(EntityDamageEvent.DamageCause.THORNS)) {
                for (ItemStack armor : killer.getInventory().getArmorContents()) {
                    if (armor != null && !armor.getType().equals(Material.AIR) && armor.getEnchantments().containsKey(Enchantment.THORNS) && StatTrak.isStattrak(armor))
                        StatTrak.addKills(armor);
                }
                return;
            }
        } else if (damager instanceof Arrow) {
            if (BOW_ITEMSTACK_MAP.containsKey(damager) && BOW_ITEMSTACK_MAP.get(damager) != null) {
                weapon = BOW_ITEMSTACK_MAP.get(damager);
                BOW_ITEMSTACK_MAP.remove(damager);
            } else {
                BOW_ITEMSTACK_MAP.remove(damager);
                return;
            }
        } else if (damager instanceof Trident) {
            Trident trident = (Trident) e.getDamager();
            ItemStack tridentItem = trident.getItem();
            if (!StatTrak.isStattrak(tridentItem)) return;
            StatTrak.addKills(tridentItem);
            trident.setItem(tridentItem);
            return;
        } else {
            return;
        }
        if (weapon == null || weapon.getType().equals(Material.AIR)) return;
        if (!weapon.hasItemMeta() || !StatTrak.isStattrak(weapon)) return;
        StatTrak.addKills(weapon);
    }

    @EventHandler
    public void onShoot(@NotNull EntityShootBowEvent e) {
        Projectile projectile = (Projectile) e.getProjectile();
        if (!(projectile.getShooter() instanceof Player && StatTrak.isStattrak(e.getBow()))) return;
        BOW_ITEMSTACK_MAP.put(projectile, e.getBow());
    }

    @EventHandler
    public void onOpenSpecialGUI(@NotNull InventoryOpenEvent e) {
        if (!(e.getInventory().getType().equals(InventoryType.ANVIL) || e.getInventory().getType().equals(InventoryType.SMITHING))) return;
        hidePrefix(e.getInventory().getContents());
        hidePrefix(e.getPlayer().getInventory().getContents());
        hidePrefix(e.getPlayer().getInventory().getArmorContents());
        hidePrefix(e.getPlayer().getInventory().getItemInOffHand());
    }

    private void hidePrefix(ItemStack @NotNull [] contents) {
        for (ItemStack stack : contents) {
            hidePrefix(stack);
        }
    }

    private void hidePrefix(ItemStack stack) {
        if (stack != null && !stack.getType().equals(Material.AIR) && stack.hasItemMeta() && StatTrak.isStattrak(stack)) {
            ItemMeta meta = stack.getItemMeta();
            meta.displayName(StatTrak.getCustomName(stack).build());
            stack.setItemMeta(meta);
        }
    }

    @EventHandler
    public void onCloseSpecialGUI(@NotNull InventoryCloseEvent e) {
        if (!(e.getInventory().getType().equals(InventoryType.ANVIL) || e.getInventory().getType().equals(InventoryType.SMITHING))) return;
        showPrefix(e.getInventory().getContents());
        showPrefix(e.getPlayer().getInventory().getContents());
        showPrefix(e.getPlayer().getInventory().getArmorContents());
        showPrefix(e.getPlayer().getItemOnCursor());
        showPrefix(e.getPlayer().getInventory().getItemInOffHand());
    }

    private void showPrefix(ItemStack @NotNull [] contents) {
        for (ItemStack stack : contents) {
            showPrefix(stack);
        }
    }

    private void showPrefix(ItemStack stack) {
        if (stack != null && !stack.getType().equals(Material.AIR) && stack.hasItemMeta() && StatTrak.isStattrak(stack)) {
            ItemMeta meta = stack.getItemMeta();
            TextComponent.Builder builder = Component.text();
            builder.append(Component.text("§6StatTrak™ "));
            meta.displayName(StatTrak.getCustomName(builder, stack).build());
            stack.setItemMeta(meta);
        }
    }

    @EventHandler
    public void onAnvilRenamed(@NotNull PrepareAnvilEvent e) {
        if (e.getResult() == null || e.getResult().getType().equals(Material.AIR) || !e.getResult().hasItemMeta() || !StatTrak.isStattrak(e.getResult()))
            return;
        String s = e.getInventory().getRenameText() == null || e.getInventory().getRenameText().replaceAll(" ", "").equals("") ? "\\t" + e.getResult().translationKey() : e.getInventory().getRenameText();
        StatTrak.setCustomName(e.getResult(), s.replaceAll("\\\\", "\\\\s"));
    }

    @EventHandler
    public void onUpgrade(@NotNull PrepareSmithingEvent e) {
        if (e.getResult() == null || e.getResult().getType().equals(Material.AIR) || !e.getResult().hasItemMeta() || !StatTrak.isStattrak(e.getResult()))
            return;
        ItemStack base = e.getInventory().getItem(0);
        assert base != null;
        if (NBTHelper.getTag(base).l("CustomName").equals("\\t" + base.translationKey())) StatTrak.setCustomName(e.getResult(), "\\t" + e.getResult().translationKey());
    }

}
