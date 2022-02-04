package me.core.listeners;

import me.core.MCServerPlugin;
import me.core.cases.Case;
import me.core.events.GUIClickEvent;
import me.core.guis.CaseGUI;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CaseListener implements Listener {

    private final MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);

    @EventHandler
    public void onPlayerOpenCase(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!e.getAction().isRightClick()) return;
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack.getType().equals(Material.AIR) || !Case.isCaseStack(stack)) return;
        e.setCancelled(true);
        CaseGUI gui = new CaseGUI(player, plugin.getCaseManager().getCase(stack));
        gui.openToPlayer();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (e.getRightClicked() instanceof Villager || stack.getType().equals(Material.AIR)) return;
        if (Case.isCaseStack(stack)) e.setCancelled(true);
    }

    @EventHandler
    public void onClick(GUIClickEvent e) {
        Player player = e.getPlayer();
        if (e.getClickedInventory() == null || e.getClickedInventory().equals(player.getInventory())) return;
        if (e.getGUI() instanceof CaseGUI) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onAnvilRenamed(InventoryClickEvent e) {
        if (e.getClickedInventory() == null || !e.getClickedInventory().equals(e.getInventory()) || !e.getInventory().getType().equals(InventoryType.ANVIL)) return;
        if (e.getSlot() == 2 && Case.isCaseStack(e.getCurrentItem())) e.setCancelled(true);
    }

    @EventHandler
    public void onDespawn(ItemDespawnEvent e) {
        if (e.isCancelled()) return;
        ItemStack stack = e.getEntity().getItemStack();
        if (Case.isCaseStack(stack)) plugin.getCaseManager().unregisterCase(plugin.getCaseManager().getCase(stack));
    }

    @EventHandler
    public void onDestroy(EntityDamageEvent e) {
        if (e.isCancelled() || !(e.getEntity() instanceof Item)) return;
        ItemStack stack = ((Item) e.getEntity()).getItemStack();
        if (Case.isCaseStack(stack)) plugin.getCaseManager().unregisterCase(plugin.getCaseManager().getCase(stack));
    }
}
