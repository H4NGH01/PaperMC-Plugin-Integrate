package me.core.util;

import net.minecraft.network.protocol.game.PacketPlayOutNamedSoundEffect;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.sounds.SoundCategory;
import net.minecraft.sounds.SoundEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SoundPlayer {

    public static void playTo(Player player, Sound sound, float volume, float pitch) {
        SoundEffect se = new SoundEffect(new MinecraftKey(sound.name()));
        Location loc = player.getLocation();
        PacketPlayOutNamedSoundEffect packetPlayOutNamedSoundEffect = new PacketPlayOutNamedSoundEffect(se, SoundCategory.a, loc.getX(), loc.getY(), loc.getZ(), volume, pitch);
        ((CraftPlayer) player).getHandle().b.a(packetPlayOutNamedSoundEffect);
    }

}
