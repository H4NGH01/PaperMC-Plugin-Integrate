package me.core;

import me.core.gui.GUIBase;
import me.core.utils.nbt.NBTHelper;
import me.core.utils.nbt.NBTStorageFile;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

public class ServerPlayer {

    private final Player player;
    private final NBTStorageFile file;
    private final List<ItemStack> storage = new ArrayList<>();
    private final HashMap<PlayerSettings, Boolean> settings = new HashMap<>();
    private BigDecimal money;
    private int newMail;

    private GUIBase gui = null;

    private static final HashMap<UUID, ServerPlayer> SERVER_PLAYER_HASH_MAP = new HashMap<>();

    private ServerPlayer(Player player) {
        this.player = player;
        MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);
        this.file = new NBTStorageFile(new File(plugin.getDataFolder() + "/playerdata/" + this.player.getUniqueId() + ".dat"));
        this.file.read();
        NBTTagList tagList = this.file.getList("storage", 10);
        for (NBTBase nbtBase : tagList) {
            if (nbtBase instanceof NBTTagCompound tag) storage.add(NBTHelper.asItemStack(tag));
        }
        this.settings.put(PlayerSettings.NEW_MAIL_MESSAGE, true);
        this.settings.put(PlayerSettings.CONTAINER_ANIMATION, true);
        NBTTagCompound settingsCompound = this.file.getTagCompound("settings");
        for (String key : settingsCompound.d()) {
            if (PlayerSettings.isRegistryKey(key)) this.settings.put(PlayerSettings.byKey(key), settingsCompound.q(key));
        }
        this.money = this.file.hasKey("money") ? new BigDecimal(this.file.getString("money")) : new BigDecimal(0);
        this.newMail = this.file.hasKey("NewMail") ? this.file.getInt("NewMail") : 0;
    }

    public void save() {
        NBTTagList storageList = new NBTTagList();
        for (ItemStack stack : this.storage) {
            storageList.add(NBTHelper.asNBTTagCompound(stack));
        }
        this.file.setList("storage", storageList);
        NBTTagCompound settingsCompound = new NBTTagCompound();
        for (PlayerSettings key : this.settings.keySet()) {
            settingsCompound.a(key.getKey(), this.settings.get(key));
        }
        this.file.setTagCompound("settings", settingsCompound);
        this.file.setString("money", this.money.toPlainString());
        this.file.setInt("NewMail", this.newMail);
        this.file.write();
    }

    public static ServerPlayer getServerPlayer(@NotNull OfflinePlayer player) {
        return getServerPlayer(Objects.requireNonNull(player.getPlayer()));
    }

    public static ServerPlayer getServerPlayer(@NotNull Player player) {
        if (SERVER_PLAYER_HASH_MAP.containsKey(player.getUniqueId())) return SERVER_PLAYER_HASH_MAP.get(player.getUniqueId());
        ServerPlayer sp = new ServerPlayer(player);
        SERVER_PLAYER_HASH_MAP.put(player.getUniqueId(), sp);
        return sp;
    }

    public static HashMap<UUID, ServerPlayer> getServerPlayerHashMap() {
        return SERVER_PLAYER_HASH_MAP;
    }

    public void safeAddItem(ItemStack stack) {
        if (this.player.getInventory().firstEmpty() == -1) {
            Location location = this.player.getLocation();
            Item item = location.getWorld().dropItem(location, stack);
            item.setVelocity(new Vector(0.0d, 0.0d, 0.0d));
            item.setOwner(this.player.getUniqueId());
        } else {
            this.player.getInventory().addItem(stack);
        }
    }

    public final Player getPlayer() {
        return this.player;
    }

    public List<ItemStack> getStorage() {
        return this.storage;
    }

    public HashMap<PlayerSettings, Boolean> getSettings() {
        return this.settings;
    }

    public BigDecimal getMoney() {
        return this.money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public int getNewMail() {
        return this.newMail;
    }

    public void setNewMail(int newMail) {
        this.newMail = newMail;
    }

    public GUIBase getHoldingGUI() {
        return this.gui;
    }

    public void setHoldingGUI(GUIBase gui) {
        this.gui = gui;
    }
}