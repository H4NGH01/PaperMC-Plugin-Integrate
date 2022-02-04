package me.core;

import me.core.utils.nbt.NBTStorageFile;
import org.bukkit.entity.Player;

import java.io.File;
import java.math.BigDecimal;

public class ServerPlayer {

    private final MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);
    private final Player player;
    private final NBTStorageFile file;
    private BigDecimal money;
    private int newMail;

    public ServerPlayer(Player player) {
        this.player = player;
        this.file = new NBTStorageFile(new File(plugin.getDataFolder() + "/playerdata/" + this.player.getUniqueId() + ".dat"));
        this.file.read();
        this.money = this.file.hasKey("money") ? BigDecimal.valueOf(this.file.getDouble("money")) : new BigDecimal(0);
        this.newMail = this.file.hasKey("NewMail") ? this.file.getInt("NewMail") : 0;
    }

    public void save() {
        this.file.setDouble("money",this.money.doubleValue());
        this.file.setInt("NewMail",this.newMail);
        this.file.write();
    }

    public final Player getPlayer() {
        return this.player;
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