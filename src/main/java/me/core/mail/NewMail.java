package me.core.mail;

import me.core.MCServerPlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class NewMail {

    private final MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);

    private final String id;
    private final UUID sender;
    private UUID[] addressee = new UUID[0];
    private String title = "gui.mail.new_mail";
    private String text = "gui.mail.no_text";
    private ItemStack[] itemStacks = new ItemStack[8];

    public NewMail(Player sender) {
        this.id = "NewMail@" + UUID.randomUUID();
        this.sender = sender.getUniqueId();
    }

    public NewMail(String id, String sender, UUID[] addressee, String title, String text, ItemStack[] itemStacks) {
        this.id = id;
        this.sender = UUID.fromString(sender);
        this.addressee = addressee;
        this.title = title;
        this.text = text;
        this.itemStacks = itemStacks;
    }

    public boolean containAddressee(OfflinePlayer op) {
        return new ArrayList<>(Arrays.asList(addressee)).contains(op.getUniqueId());
    }

    @Deprecated
    public void setAddressee(OfflinePlayer[] addressee) {
        this.addressee = new UUID[addressee.length];
        int i = 0;
        for (OfflinePlayer p : addressee) {
            this.addressee[i++] = p.getUniqueId();
        }
    }

    public void addAddressee(OfflinePlayer addressee) {
        for (UUID uuid : this.addressee) {
            if (plugin.getServer().getOfflinePlayer(uuid).equals(addressee)) return;
        }
        UUID[] old = this.addressee;
        this.addressee = new UUID[this.addressee.length + 1];
        int i = 0;
        for (UUID uuid : old) {
            this.addressee[i++] = uuid;
        }
        this.addressee[this.addressee.length - 1] = addressee.getUniqueId();
    }

    public void removeAddressee(OfflinePlayer addressee) {
        for (int i = 0; i < this.addressee.length; i++) {
            if (plugin.getServer().getOfflinePlayer(this.addressee[i]).equals(addressee)) {
                this.addressee[i] = null;
            }
        }
        UUID[] old = this.addressee;
        this.addressee = new UUID[this.addressee.length - 1];
        int i = 0;
        for (UUID uuid : old) {
            if (uuid == null) continue;
            this.addressee[i++] = uuid;
        }
    }

    public String getMailID() {
        return this.id;
    }

    public UUID getSender() {
        return this.sender;
    }

    public OfflinePlayer[] getAddressee() {
        OfflinePlayer[] opa = new OfflinePlayer[this.addressee.length];
        int i = 0;
        for (UUID uuid : this.addressee) {
            opa[i++] = plugin.getServer().getOfflinePlayer(uuid);
        }
        return opa;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ItemStack[] getItemStacks() {
        return this.itemStacks;
    }

    public boolean isItemStacksFull() {
        for (ItemStack stack : this.itemStacks) {
            if (stack == null) return false;
        }
        return true;
    }

    public void addItemStack(ItemStack stack) {
        for (int i = 0; i < this.itemStacks.length; i++) {
            if (this.itemStacks[i] != null && this.itemStacks[i].equals(stack)) return;
            if (this.itemStacks[i] == null) {
                this.itemStacks[i] = stack;
                break;
            }
        }
        ItemStack[] clone = new ItemStack[8];
        int i = 0;
        for (ItemStack stack1 : this.itemStacks) if (stack1 != null) clone[i++] = stack1;
        this.itemStacks = clone;
    }

    public void removeItemStack(ItemStack stack) {
        for (int i = 0; i < this.itemStacks.length; i++) {
            if (this.itemStacks[i] != null && this.itemStacks[i].equals(stack)) {
                this.itemStacks[i] = null;
                break;
            }
        }
        ItemStack[] clone = new ItemStack[8];
        int i = 0;
        for (ItemStack stack1 : this.itemStacks) if (stack1 != null) clone[i++] = stack1;
        this.itemStacks = clone;
    }
}
