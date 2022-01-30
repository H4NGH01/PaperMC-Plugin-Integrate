package me.core.event;

import me.core.item.StatTrak;
import me.core.util.ComponentUtil;
import me.core.util.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class StatTrakEvent implements Listener {

    private static final HashMap<Projectile, ItemStack> BOW_ITEMSTACK_MAP = new HashMap<>();

    @EventHandler
    public void onKills(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;
        if (e.isCancelled()) return;
        if (e.getDamage() < ((LivingEntity) e.getEntity()).getHealth()) return;
        Entity damager = e.getDamager();
        if (!(damager instanceof Player || (damager instanceof Projectile && ((Projectile) damager).getShooter() instanceof Player))) return;
        Player killer;
        ItemStack weapon;
        if (damager instanceof Player) {
            killer = (Player) e.getDamager();
            weapon = killer.getInventory().getItemInMainHand();
        }  else {
            if (BOW_ITEMSTACK_MAP.containsKey(damager) && BOW_ITEMSTACK_MAP.get(damager) != null) {
                weapon = BOW_ITEMSTACK_MAP.get(damager);
                BOW_ITEMSTACK_MAP.remove(damager);
            } else {
                BOW_ITEMSTACK_MAP.remove(damager);
                return;
            }
        }
        if (weapon.getType().equals(Material.AIR)) return;
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
        hidePrefix(e.getInventory());
        hidePrefix(e.getPlayer().getInventory());
    }

    private void hidePrefix(Inventory inventory) {
        for (ItemStack stack : inventory.getContents()) {
            if (stack != null && !stack.getType().equals(Material.AIR) && stack.hasItemMeta() && StatTrak.isStattrak(stack)) {
                ItemMeta meta = stack.getItemMeta();
                boolean b = NBTHelper.getTag(stack).l("CustomName").equals(stack.translationKey());
                meta.displayName(b ? ComponentUtil.translate(stack.translationKey()) : ComponentUtil.text(NBTHelper.getTag(stack).l("CustomName")));
                stack.setItemMeta(meta);
            }
        }
    }

    @EventHandler
    public void onCloseAnvil(InventoryCloseEvent e) {
        if (!e.getInventory().getType().equals(InventoryType.ANVIL)) return;
        showPrefix(e.getInventory());
        showPrefix(e.getPlayer().getInventory());
        showPrefix(e.getPlayer().getItemOnCursor());
    }

    private void showPrefix(Inventory inventory) {
        for (ItemStack stack : inventory.getContents()) {
            if (stack != null && !stack.getType().equals(Material.AIR) && stack.hasItemMeta() && StatTrak.isStattrak(stack)) {
                ItemMeta meta = stack.getItemMeta();
                boolean b = NBTHelper.getTag(stack).l("CustomName").equals(stack.translationKey());
                meta.displayName(Component.text(ChatColor.GOLD + "StatTrak™ ").append(b ? ComponentUtil.translate(stack.translationKey()) : ComponentUtil.text(NBTHelper.getTag(stack).l("CustomName"))));
                stack.setItemMeta(meta);
            }
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
        if (!StatTrak.isStattrak(e.getResult())) return;
        NBTHelper.setTag(e.getResult(), "CustomName", e.getInventory().getRenameText());
    }

}
