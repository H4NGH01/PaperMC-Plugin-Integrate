package me.core.utils.nbt;

import me.core.MCServerPlugin;
import net.minecraft.nbt.NBTCompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

public class NBTStorageFile {

    private final MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);
    private final File file;
    private NBTTagCompound tagCompound;

    public NBTStorageFile(File file) {
        this.file = file;
    }

    public void read() {
        this.tagCompound = new NBTTagCompound();
        try {
            if (!this.file.exists()) {
                if (this.file.getParentFile() != null && !this.file.getParentFile().exists()) {
                    this.file.getParentFile().mkdirs();
                }
                this.file.createNewFile();
                this.plugin.log(ChatColor.GREEN + this.file.getName() + " file created.");
                return;
            }
            FileInputStream fis = new FileInputStream(this.file);
            this.tagCompound = NBTCompressedStreamTools.a(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write() {
        try {
            if (!this.file.exists()) {
                this.file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(this.file);
            NBTCompressedStreamTools.a(this.tagCompound, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    net.minecraft.nbt.NBTTagCompound
    $.a(key, var)   return  $.set(key, var)
    $.c(key, int)   return  $.getList(key, int)
    $.d()           return  $.getKeys()
    $.e(key)        return  $.hasKey(key)
    $.f()           return  $.isEmpty()
    $.f(key)        return  $.getByte(key)
    $.g(key)        return  $.getShort(key)
    $.h(key)        return  $.getInt(key)
    $.i(key)        return  $.getLong(key)
    $.j(key)        return  $.getFloat(key)
    $.k(key)        return  $.getDouble(key)
    $.l(key)        return  $.getString(key)
    $.m(key)        return  $.getByteArray(key)
    $.n(key)        return  $.getIntArray(key)
    $.o(key)        return  $.getLongArray(key)
    $.p(key)        return  $.getTagCompound(key)
    $.q(key)        return  $.getBoolean(key)
    $.r(key)        return  $.remove(key)
     */

    public void setByte(String key, byte value) {
        this.tagCompound.a(key, value);
    }

    public void setShort(String key, short value) {
        this.tagCompound.a(key, value);
    }

    public void setInt(String key, int value) {
        this.tagCompound.a(key, value);
    }

    public void setLong(String key, long value) {
        this.tagCompound.a(key, value);
    }

    public void setDouble(String key, double value) {
        this.tagCompound.a(key, value);
    }

    public void setString(String key, String value) {
        this.tagCompound.a(key, value);
    }

    public void setIntArray(String key, int[] value) {
        this.tagCompound.a(key, value);
    }

    public void setLongArray(String key, long[] value) {
        this.tagCompound.a(key, value);
    }

    public void setTagCompound(String key, NBTTagCompound tagCompound) {
        this.tagCompound.a(key, tagCompound);
    }

    public void setBoolean(String key, boolean value) {
        this.tagCompound.a(key, value);
    }

    public void setList(String key, NBTTagList tagList) {
        this.tagCompound.a(key, tagList);
    }

    public NBTTagList getList(String key, int var) {
        return this.tagCompound.c(key, var);
    }

    public Set<String> getKeys() {
        return this.tagCompound.d();
    }

    public boolean hasKey(String key) {
        return this.tagCompound.e(key);
    }

    public boolean isEmpty() {
        return this.tagCompound.f();
    }

    public Byte getByte(String key) {
        return this.tagCompound.f(key);
    }

    public Short getShort(String key) {
        return this.tagCompound.g(key);
    }

    public int getInt(String key) {
        return this.tagCompound.h(key);
    }

    public long getLong(String key) {
        return this.tagCompound.i(key);
    }

    public double getDouble(String key) {
        return this.tagCompound.k(key);
    }

    public String getString(String key) {
        return this.tagCompound.l(key);
    }

    public byte[] getByteArray(String key) {
        return this.tagCompound.m(key);
    }

    public int[] getIntArray(String key) {
        return this.tagCompound.n(key);
    }

    public long[] getLongArray(String key) {
        return this.tagCompound.o(key);
    }

    public NBTTagCompound getTagCompound(String key) {
        return this.tagCompound.p(key);
    }

    public boolean getBoolean(String key) {
        return this.tagCompound.q(key);
    }

    public void remove(String key) {
        this.tagCompound.r(key);
    }

    public void clear() {
        for (String key : this.getKeys()) this.remove(key);
    }
}
