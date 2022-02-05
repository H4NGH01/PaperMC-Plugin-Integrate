package me.core.listeners;

import me.core.items.StatTrak;
import me.core.utils.ComponentUtil;
import me.core.utils.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class StatTrakListener implements Listener {

    private static final HashMap<Projectile, ItemStack> BOW_ITEMSTACK_MAP = new HashMap<>();

    @EventHandler
    public void onKills(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;
        if (e.isCancelled()) return;
        if (e.getDamage() < ((LivingEntity) e.getEntity()).getHealth()) return;
        Entity damager = e.getDamager();
        if (!(damager instanceof Player || (damager instanceof Projectile && ((Projectile) damager).getShooter() instanceof Player))) return;
        Player killer;
        ItemStack weapon = null;
        if (damager instanceof Player) {
            killer = (Player) e.getDamager();
            if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) {
                weapon = killer.getInventory().getItemInMainHand();
            } else if (e.getCause().equals(EntityDamageEvent.DamageCause.THORNS)) {
                for (ItemStack armor : killer.getInventory().getArmorContents()) {
                    if (armor != null && !armor.getType().equals(Material.AIR) && armor.getEnchantments().containsKey(Enchantment.THORNS) && StatTrak.isStattrak(armor)) StatTrak.addKills(armor);
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
    public void onShoot(EntityShootBowEvent e) {
        Projectile projectile = (Projectile) e.getProjectile();
        if (!(projectile.getShooter() instanceof Player && StatTrak.isStattrak(e.getBow()))) return;
        BOW_ITEMSTACK_MAP.put(projectile, e.getBow());
    }

    @EventHandler
    public void onOpenAnvil(InventoryOpenEvent e) {
        if (!e.getInventory().getType().equals(InventoryType.ANVIL)) return;
        hidePrefix(e.getInventory().getContents());
        hidePrefix(e.getPlayer().getInventory().getContents());
        hidePrefix(e.getPlayer().getInventory().getArmorContents());
        hidePrefix(e.getPlayer().getInventory().getItemInOffHand());
    }

    private void hidePrefix(ItemStack[] contents) {
        for (ItemStack stack : contents) {
            hidePrefix(stack);
        }
    }

    private void hidePrefix(ItemStack stack) {
        if (stack != null && !stack.getType().equals(Material.AIR) && stack.hasItemMeta() && StatTrak.isStattrak(stack)) {
            ItemMeta meta = stack.getItemMeta();
            boolean b = NBTHelper.getTag(stack).l("CustomName").equals(stack.translationKey());
            meta.displayName(b ? null : ComponentUtil.text(NBTHelper.getTag(stack).l("CustomName")));
            stack.setItemMeta(meta);
        }
    }

    @EventHandler
    public void onCloseAnvil(InventoryCloseEvent e) {
        if (!e.getInventory().getType().equals(InventoryType.ANVIL)) return;
        showPrefix(e.getInventory().getContents());
        showPrefix(e.getPlayer().getInventory().getContents());
        showPrefix(e.getPlayer().getInventory().getArmorContents());
        showPrefix(e.getPlayer().getItemOnCursor());
        showPrefix(e.getPlayer().getInventory().getItemInOffHand());
    }

    private void showPrefix(ItemStack[] contents) {
        for (ItemStack stack : contents) {
            showPrefix(stack);
        }
    }

    private void showPrefix(ItemStack stack) {
        if (stack != null && !stack.getType().equals(Material.AIR) && stack.hasItemMeta() && StatTrak.isStattrak(stack)) {
            ItemMeta meta = stack.getItemMeta();
            boolean b = NBTHelper.getTag(stack).l("CustomName").equals(stack.translationKey());
            meta.displayName(Component.text(ChatColor.GOLD + "StatTrak™ ").append(b ? ComponentUtil.translate(stack.translationKey()) : ComponentUtil.text(NBTHelper.getTag(stack).l("CustomName"))));
            stack.setItemMeta(meta);
        }
    }

    @EventHandler
    public void onAnvilRenamed(PrepareAnvilEvent e) {
        if (e.getResult() == null || e.getResult().getType().equals(Material.AIR) || !StatTrak.isStattrak(e.getResult())) return;
        String s = e.getInventory().getRenameText() == null || e.getInventory().getRenameText().replaceAll(" ", "").equals("") ? e.getResult().translationKey() : e.getInventory().getRenameText();
        NBTHelper.setTag(e.getResult(), "CustomName", s);
    }

}
