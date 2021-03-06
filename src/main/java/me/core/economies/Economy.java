package me.core.economies;

import me.core.MCServerPlugin;
import me.core.ServerPlayer;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public class Economy {

    private final MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);
    private static final Economy instance = new Economy();

    public static Economy getInstance() {
        return instance;
    }

    public static void setMoney(Player player, BigDecimal bigDecimal) {
        ServerPlayer sp = ServerPlayer.getServerPlayer(player);
        sp.setMoney(bigDecimal);
    }

    public static void deposit(Player player, BigDecimal bigDecimal) {
        ServerPlayer sp = ServerPlayer.getServerPlayer(player);
        sp.setMoney(sp.getMoney().add(bigDecimal));
    }

    public static void withdraw(Player player, BigDecimal bigDecimal) {
        ServerPlayer sp = ServerPlayer.getServerPlayer(player);
        sp.setMoney(sp.getMoney().subtract(bigDecimal));
    }

}
