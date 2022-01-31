package me.core.enchantments;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.Map;

public class PluginEnchantments {

    public static final EnchantmentWrapper WRAPPER = new EnchantmentWrapper();

    public static void loadEnchantments() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        Enchantment.registerEnchantment(PluginEnchantments.WRAPPER);
    }

    @SuppressWarnings("unchecked")
    public static void unloadEnchantments() {
        try {
            Field byKeyField = Enchantment.class.getDeclaredField("byKey");
            Field byNameField = Enchantment.class.getDeclaredField("byName");
            byKeyField.setAccessible(true);
            byNameField.setAccessible(true);
            Map<NamespacedKey, Enchantment> byKey = (Map<NamespacedKey, Enchantment>) byKeyField.get(null);
            Map<String, Enchantment> byName = (Map<String, Enchantment>) byNameField.get(null);
            byKey.remove(PluginEnchantments.WRAPPER.getKey());
            byName.remove(PluginEnchantments.WRAPPER.getName());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
