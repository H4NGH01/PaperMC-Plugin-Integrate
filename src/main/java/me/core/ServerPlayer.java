package me.core;

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

    private final MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);
    private final Player player;
    private final NBTStorageFile file;
    private final List<ItemStack> storage = new ArrayList<>();
    private BigDecimal money;
    private int newMail;

    private static final HashMap<UUID, ServerPlayer> SERVER_PLAYER_HASH_MAP = new HashMap<>();

    private ServerPlayer(Player player) {
        this.player = player;
        this.file = new NBTStorageFile(new File(plugin.getDataFolder() + "/playerdata/" + this.player.getUniqueId() + ".dat"));
        this.file.read();
        NBTTagList tagList = this.file.getList("storage", 10);
        for (NBTBase nbtBase : tagList) {
            if (nbtBase instanceof NBTTagCompound) storage.add(NBTHelper.asItemStack((NBTTagCompound) nbtBase));
        }
        this.money = this.file.hasKey("money") ? BigDecimal.valueOf(this.file.getDouble("money")) : new BigDecimal(0);
        this.newMail = this.file.hasKey("NewMail") ? this.file.getInt("NewMail") : 0;
    }

    public void save() {
        NBTTagList tagList = new NBTTagList();
        for (ItemStack stack : this.storage) {
            tagList.add(NBTHelper.asNBTTagCompound(stack));
        }
        this.file.setList("storage", tagList);
        this.file.setDouble("money", this.money.doubleValue());
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

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public int getNewMail() {
        return newMail;
    }

    public void setNewMail(int newMail) {
        this.newMail = newMail;
    }

}