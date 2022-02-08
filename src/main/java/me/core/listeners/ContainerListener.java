package me.core.listeners;

import me.core.MCServerPlugin;
import me.core.containers.Container;
import me.core.containers.ContainerManager;
import me.core.events.GUIClickEvent;
import me.core.events.GUICloseEvent;
import me.core.gui.ContainerGUI;
import me.core.items.MCServerItems;
import net.kyori.adventure.text.Component;
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
import org.jetbrains.annotations.NotNull;

public class ContainerListener implements Listener {

    private final MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);

    @EventHandler
    public void onPlayerOpenCase(@NotNull PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!e.getAction().isRightClick()) return;
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack.getType().equals(Material.AIR) || !Container.isContainerStack(stack)) return;
        e.setCancelled(true);
        Container c = plugin.getContainerManager().getContainerByStack(stack);
        if (!c.hasData()) {
            player.sendMessage(Component.translatable("chat.container.invalid_container_opened"));
            player.getInventory().remove(stack);
            return;
        }
        ContainerGUI gui = new ContainerGUI(player, stack);
        gui.openToPlayer();
    }

    @EventHandler
    public void onPlayerInteract(@NotNull PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (e.getRightClicked() instanceof Villager || stack.getType().equals(Material.AIR)) return;
        if (Container.isContainerStack(stack)) e.setCancelled(true);
    }

    @EventHandler
    public void onClick(@NotNull GUIClickEvent e) {
        Player player = e.getPlayer();
        ItemStack stack = e.getCurrentItem();
        if (stack == null || stack.getType().equals(Material.AIR) || e.getClickedInventory() == null || e.getClickedInventory().equals(player.getInventory()))
            return;
        if (!(e.getGUI() instanceof ContainerGUI)) return;
        e.setCancelled(true);
        if (MCServerItems.equalWithTag(stack, "ItemTag", "gui.container.unlock")) {
            ((ContainerGUI) e.getGUI()).openContainer();
        }
    }

    @EventHandler
    public void onCloseGUI(@NotNull GUICloseEvent e) {
        if (!(e.getGUI() instanceof ContainerGUI gui)) return;
        if (gui.isOpening()) gui.setAnimationEnd();
    }

    @EventHandler
    public void onAnvilRenamed(@NotNull InventoryClickEvent e) {
        if (e.getClickedInventory() == null || !e.getClickedInventory().equals(e.getInventory()) || !e.getInventory().getType().equals(InventoryType.ANVIL)) return;
        if (e.getSlot() == 2 && Container.isContainerStack(e.getCurrentItem())) e.setCancelled(true);
    }

    @EventHandler
    public void onDespawn(@NotNull ItemDespawnEvent e) {
        if (e.isCancelled()) return;
        ItemStack stack = e.getEntity().getItemStack();
        if (Container.isContainerStack(stack))
            ContainerManager.unregisterContainerData(plugin.getContainerManager().getContainerByStack(stack));
    }

    @EventHandler
    public void onDestroy(@NotNull EntityDamageEvent e) {
        if (e.isCancelled() || !(e.getEntity() instanceof Item)) return;
        ItemStack stack = ((Item) e.getEntity()).getItemStack();
        if (Container.isContainerStack(stack))
            ContainerManager.unregisterContainerData(plugin.getContainerManager().getContainerByStack(stack));
    }
}
