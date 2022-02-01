package me.core.util.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class NBTHelper {

    /* ----------------------------------------------/
    net.minecraft.nbt.NBTTagCompound
    $.a(key, var)   return  $.set(key, var)
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
    /* ----------------------------------------------/
    net.minecraft.world.item.ItemStack
    $.c()           return  $.getItem()
    $.c(var)        return  $.setTag(var)
    $.r()           return  $.hasTag()
    $.s()           return  $.getTag()
    $.v()           return  $.getName()
    $.I()           return  $.getCount()
    /* -------------------------------------------- */

    public static @NotNull ItemStack getItemStack(@NotNull NBTTagCompound tagCompound) {
        ItemStack stack = new ItemStack(Objects.requireNonNull(Material.getMaterial(tagCompound.l("id").toUpperCase())), tagCompound.h("Count"));
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        if (tagCompound.c("tag") != null) nmsStack.c(tagCompound.p("tag"));
        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    public static NBTTagCompound getNBTTagCompound(@NotNull ItemStack stack) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.a("id", stack.getType().toString().toLowerCase());
        tag.a("Count", stack.getAmount());
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        if (nmsStack.r()) tag.a("tag", nmsStack.s());
        return tag;
    }

    public static <T extends ItemStack> T setTag(T stack, String key, NBTBase value) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tag = nmsStack.r() ? nmsStack.s() : new NBTTagCompound();
        assert tag != null;
        tag.a(key, value);
        stack.setItemMeta(CraftItemStack.asBukkitCopy(nmsStack).getItemMeta());
        return stack;
    }

    public static <T extends ItemStack> T setTag(T stack, String key, int value) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tag = nmsStack.r() ? nmsStack.s() : new NBTTagCompound();
        assert tag != null;
        tag.a(key, value);
        nmsStack.c(tag);
        stack.setItemMeta(CraftItemStack.asBukkitCopy(nmsStack).getItemMeta());
        return stack;
    }

    public static <T extends ItemStack> T setTag(T stack, String key, String value) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tag = nmsStack.r() ? nmsStack.s() : new NBTTagCompound();
        assert tag != null;
        tag.a(key, value);
        nmsStack.c(tag);
        stack.setItemMeta(CraftItemStack.asBukkitCopy(nmsStack).getItemMeta());
        return stack;
    }

    public static <T extends ItemStack> T setTag(T stack, String key, boolean value) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tag = nmsStack.r() ? nmsStack.s() : new NBTTagCompound();
        assert tag != null;
        tag.a(key, value);
        nmsStack.c(tag);
        stack.setItemMeta(CraftItemStack.asBukkitCopy(nmsStack).getItemMeta());
        return stack;
    }

    public static <T extends ItemStack> void removeTag(T stack, String key) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound tag = nmsStack.r() ? nmsStack.s() : new NBTTagCompound();
        assert tag != null;
        tag.r(key);
        nmsStack.c(tag);
        stack.setItemMeta(CraftItemStack.asBukkitCopy(nmsStack).getItemMeta());
    }

    public static boolean hasTag(ItemStack stack) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        return nmsStack.r();
    }

    public static boolean hasTag(ItemStack stack, String key) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        return nmsStack.s() != null && nmsStack.s().e(key);
    }

    public static NBTTagCompound getTag(ItemStack stack) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        return nmsStack.r() ? nmsStack.s() : new NBTTagCompound();
    }
}
