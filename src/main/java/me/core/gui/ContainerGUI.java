package me.core.gui;

import me.core.PlayerSettings;
import me.core.ServerPlayer;
import me.core.containers.Container;
import me.core.containers.ContainerData;
import me.core.containers.ContainerKey;
import me.core.containers.ContainerManager;
import me.core.items.CaseItemRarity;
import me.core.items.ContainerItemStack;
import me.core.items.InventoryItem;
import me.core.items.MCServerItems;
import me.core.utils.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ContainerGUI extends GUIBase {

    private static final HashMap<Player, ContainerGUI> VIEW_MAP = new HashMap<>();
    private final ItemStack caseStackIn;
    private final ContainerData data;
    private final Container container;
    private final ContainerKey matchKey;
    private ItemStack keyIn;
    private boolean opening = false;
    private boolean animation = false;

    public ContainerGUI(@NotNull Player player, ItemStack caseStackIn) {
        super(player, 45);
        this.caseStackIn = caseStackIn;
        this.data = Objects.requireNonNull(ContainerManager.getContainerDataByStack(caseStackIn));
        this.container = ContainerManager.getContainerByType(data.getType());
        this.matchKey = container.getKeyType();
        this.setDefault();
        ContainerManager.setClassNotNull();
    }

    @Override
    public void setInventory() {
        for (int i = 0; i < this.container.getDisplayDrops().size(); i++) {
            if (i >= 36) break;
            this.inventory.setItem(i + 9, this.container.getDisplayDrops().get(i));
        }
        this.inventory.setItem(0, requireKey());
        if (this.playerContainsKeys()) this.inventory.setItem(40, unlock());
    }

    @Override
    public Component getGUIName() {
        return Component.translatable(container.getContainerType().getTranslationKey());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends GUIBase> HashMap<Player, T> getViewMap() {
        return (HashMap<Player, T>) VIEW_MAP;
    }

    public static HashMap<Player, ContainerGUI> getViews() {
        return VIEW_MAP;
    }

    @Override
    public void openToPlayer() {
        super.openToPlayer();
        this.player.playSound(this.player.getLocation(), Sound.BLOCK_CHEST_LOCKED, 0.7f, 1f);
    }

    public void openContainer() {
        if (!playerContainsContainer()) {
            this.player.sendMessage(Component.translatable("chat.container.invalid_container"));
            return;
        }
        if (!playerContainsKeys()) {
            this.player.sendMessage(Component.translatable("chat.container.invalid_key"));
            return;
        }
        this.opening = true;
        this.player.getInventory().remove(this.caseStackIn);
        this.keyIn.setAmount(this.keyIn.getAmount() - 1);
        this.playOpenAnimation();
        ServerPlayer op = ServerPlayer.getServerPlayer(this.player);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    this.cancel();
                    op.getStorage().add(data.getDrop());
                    op.save();
                    ContainerManager.unregisterContainerData(data);
                    return;
                }
                if (!animation) {
                    this.cancel();
                    playEndAnimation();
                    op.safeAddItem(data.getDrop());
                    Component component = data.getDrop().displayName();
                    component.hoverEvent(data.getDrop().asHoverEvent());
                    player.sendMessage(Component.translatable("chat.container.opened_item").args(component));
                    ContainerManager.unregisterContainerData(data);
                }
            }
        }.runTaskTimer(this.plugin, 0, 1);
    }

    @SuppressWarnings("all")
    private void playOpenAnimation() {
        this.animation = true;
        this.player.playSound(this.getPlayer().getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 0.7f, 1f);
        this.setDefault();
        this.inventory.setItem(8, MCServerItems.board);
        this.inventory.setItem(13, select());
        this.inventory.setItem(31, select());
        final ItemStack[] display = new ItemStack[9];
        final Random random = new Random();
        for (int i = 0; i < display.length; i++) {
            display[i] = randomItemFromContainer(random);
        }
        if (!ServerPlayer.getServerPlayer(this.player).getSettings().get(PlayerSettings.CONTAINER_ANIMATION)) {
            setAnimationEnd();
            display[4] = dropDisplay();
            for (int i = 0; i < display.length; i++) {
                inventory.setItem(i + 18, display[i]);
            }
            return;
        }
        final int startSpeed = 140;
        final int acceleration = -1;
        final int rate = 100 + (random.nextBoolean() ? 1 : -1) * random.nextInt(16);
        final int endSpeed = calcEndSpeed(startSpeed, acceleration, rate);
        new BukkitRunnable() {
            private int i = 0;
            private int rollSpeed = startSpeed;

            @Override
            public void run() {
                if (!player.isOnline() || !animation || rollSpeed <= 0) {
                    setAnimationEnd();
                    this.cancel();
                    return;
                }
                this.i += rollSpeed;
                rollSpeed += acceleration;

                if (this.i >= rate) {
                    this.i = 0;
                    System.arraycopy(display, 1, display, 0, display.length - 1);
                    display[display.length - 1] = rollSpeed == endSpeed ? dropDisplay() : randomItemFromContainer(random);
                    for (int i = 0; i < display.length; i++) {
                        inventory.setItem(i + 18, display[i]);
                    }
                    player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 0.6f, 1f);
                }
            }
        }.runTaskTimer(this.plugin, 0, 1);
    }

    @SuppressWarnings("all")
    private static int calcEndSpeed(int rollSpeed, final int acceleration, final int rate) {
        int i = 0;
        int[] j = new int[5];
        while (rollSpeed > 0) {
            i += rollSpeed;
            rollSpeed += acceleration;
            if (i >= rate) {
                i = 0;
                System.arraycopy(j, 1, j, 0, j.length - 1);
                j[4] = rollSpeed;
            }
        }
        return j[0];
    }

    private void playEndAnimation() {
        switch (this.data.getDrop().getItemRarity()) {
            case MIL_SPEC -> this.player.playSound(this.player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1f, 1f);
            case RESTRICTED -> this.player.playSound(this.player.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1f, 1f);
            case CLASSIFIED -> this.player.playSound(this.player.getLocation(), Sound.ITEM_TRIDENT_THUNDER, 1f, 1f);
            case COVERT -> this.player.playSound(this.player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f, 1f);
            case EXCEEDINGLY_RARE -> this.player.playSound(this.player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
            default -> {
            }
        }
        if (!inventory.getViewers().contains(player)) return;
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, rarityColor());
            inventory.setItem(size - 1 - i, rarityColor());
        }
    }

    private @NotNull ItemStack rarityColor() {
        switch (this.data.getDrop().getItemRarity()) {
            case MIL_SPEC -> {
                return colorPane(Material.BLUE_STAINED_GLASS_PANE);
            }
            case RESTRICTED -> {
                return colorPane(Material.PURPLE_STAINED_GLASS_PANE);
            }
            case CLASSIFIED -> {
                return colorPane(Material.MAGENTA_STAINED_GLASS_PANE);
            }
            case COVERT -> {
                return colorPane(Material.RED_STAINED_GLASS_PANE);
            }
            case EXCEEDINGLY_RARE -> {
                return colorPane(Material.ORANGE_STAINED_GLASS_PANE);
            }
        }
        return colorPane(Material.GRAY_STAINED_GLASS_PANE);
    }

    private @NotNull ItemStack dropDisplay() {
        ContainerItemStack drop = new ContainerItemStack(this.data.getDrop());
        if (drop.getItemRarity().equals(CaseItemRarity.EXCEEDINGLY_RARE)) return Container.superRarity();
        for (ContainerItemStack display : container.getDisplayDrops()) {
            if (drop.getType().equals(display.getType())) {
                return display;
            }
        }
        return drop;
    }

    private ContainerItemStack randomItemFromContainer(@NotNull Random random) {
        List<ContainerItemStack> displayDrops = this.container.getDisplayDrops();
        int i = (int) (random.nextFloat() * displayDrops.size());
        return displayDrops.get(i);
    }

    private @NotNull InventoryItem requireKey() {
        InventoryItem item = new InventoryItem(Material.TRIPWIRE_HOOK);
        item.setDisplayName(Component.translatable("gui.container.require_key").args(Component.translatable(container.getKeyType().getTranslationKey())));
        return item;
    }

    private @NotNull InventoryItem unlock() {
        InventoryItem item = new InventoryItem(Material.GREEN_CONCRETE).setTag("ItemTag", "gui.container.unlock");
        item.setDisplayName(Component.translatable("gui.container.unlock"));
        return item;
    }

    private @NotNull InventoryItem select() {
        return colorPane(Material.YELLOW_STAINED_GLASS_PANE);
    }

    private @NotNull InventoryItem colorPane(Material material) {
        InventoryItem item = new InventoryItem(material);
        item.setDisplayName(Component.text(" "));
        return item;
    }

    private boolean playerContainsContainer() {
        return this.player.getInventory().contains(this.caseStackIn);
    }

    private boolean playerContainsKeys() {
        for (ItemStack stack : this.player.getInventory()) {
            if (stack != null && !stack.getType().equals(Material.AIR) && stack.hasItemMeta() && NBTHelper.hasTag(stack, "ContainerKey")) {
                if (matchKey.getID().equals(NBTHelper.getTag(stack).l("ContainerKey"))) {
                    this.keyIn = stack;
                    return true;
                }
            }
        }
        return false;
    }

    public Container getContainer() {
        return this.container;
    }

    public boolean isOpening() {
        return opening;
    }

    public void setAnimationEnd() {
        this.animation = false;
    }

    public ContainerData getData() {
        return this.data;
    }
}
