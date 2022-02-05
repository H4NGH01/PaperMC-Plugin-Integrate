package me.core.listeners;

import me.core.MCServerPlugin;
import me.core.containers.Container;
import me.core.events.GUIClickEvent;
import me.core.gui.ContainerGUI;
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

public class ContainerListener implements Listener {

    private final MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);

    @EventHandler
    public void onPlayerOpenCase(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!e.getAction().isRightClick()) return;
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack.getType().equals(Material.AIR) || !Container.isContainerStack(stack)) return;
        e.setCancelled(true);
        ContainerGUI gui = new ContainerGUI(player, plugin.getContainerManager().getContainerByStack(stack));
        gui.openToPlayer();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (e.getRightClicked() instanceof Villager || stack.getType().equals(Material.AIR)) return;
        if (Container.isContainerStack(stack)) e.setCancelled(true);
    }

    @EventHandler
    public void onClick(GUIClickEvent e) {
        Player player = e.getPlayer();
        ItemStack stack = e.getCurrentItem();
        if (stack == null || stack.getType().equals(Material.AIR) || e.getClickedInventory() == null || e.getClickedInventory().equals(player.getInventory())) return;
        if (!(e.getGUI() instanceof ContainerGUI)) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onAnvilRenamed(InventoryClickEvent e) {
        if (e.getClickedInventory() == null || !e.getClickedInventory().equals(e.getInventory()) || !e.getInventory().getType().equals(InventoryType.ANVIL)) return;
        if (e.getSlot() == 2 && Container.isContainerStack(e.getCurrentItem())) e.setCancelled(true);
    }

    @EventHandler
    public void onDespawn(ItemDespawnEvent e) {
        if (e.isCancelled()) return;
        ItemStack stack = e.getEntity().getItemStack();
        if (Container.isContainerStack(stack)) plugin.getContainerManager().unregisterContainerData(plugin.getContainerManager().getContainerByStack(stack));
    }

    @EventHandler
    public void onDestroy(EntityDamageEvent e) {
        if (e.isCancelled() || !(e.getEntity() instanceof Item)) return;
        ItemStack stack = ((Item) e.getEntity()).getItemStack();
        if (Container.isContainerStack(stack)) plugin.getContainerManager().unregisterContainerData(plugin.getContainerManager().getContainerByStack(stack));
    }
}
