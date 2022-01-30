package me.core;

import me.core.util.nbt.NBTStorageFile;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.entity.Player;

import java.io.File;
import java.math.BigDecimal;

public class ServerPlayer {

    private final Player player;
    private final NBTStorageFile file;
    private final NBTTagCompound tagCompound;
    private BigDecimal money;

    public ServerPlayer(Player player) {
        this.player = player;
        this.file = new NBTStorageFile(new File(this.player.getWorld().getWorldFolder().getName() + "/playerdata/" + this.player.getUniqueId() + ".dat"));
        this.file.read();
        this.tagCompound = this.file.getTagCompound("server");
        this.money = this.tagCompound.e("money") ? BigDecimal.valueOf(this.tagCompound.k("money")) : new BigDecimal(0);
    }

    public void save() {
        this.tagCompound.a("money",this.money.doubleValue());
        this.file.setTagCompound("server", this.tagCompound);
        this.file.write();
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public final Player getPlayer() {
        return this.player;
    }
}