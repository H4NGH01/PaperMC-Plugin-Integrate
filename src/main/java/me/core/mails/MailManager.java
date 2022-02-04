package me.core.mails;

import me.core.MCServerPlugin;
import me.core.guis.mails.MailWriterGUI;
import me.core.utils.nbt.NBTHelper;
import me.core.utils.nbt.NBTStorageFile;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class MailManager {

    private final MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);
    private final List<Mail> mailList;
    private final NBTStorageFile mailFile;
    private final NBTStorageFile newMailFile;

    public MailManager() {
        //read mail data
        this.mailList = new ArrayList<>();
        this.mailFile = new NBTStorageFile(new File("mail.nbt"));
        this.newMailFile = new NBTStorageFile(new File("new-mail.nbt"));
        this.mailFile.read();
        this.newMailFile.read();
        for (String key : this.mailFile.getKeys()) {
            NBTTagCompound mailTag = this.mailFile.getTagCompound(key);
            NBTTagList itemTagList = mailTag.c("ItemList", 10);
            List<ItemStack> stacks = new ArrayList<>();
            for (NBTBase b : itemTagList) {
                if (b instanceof NBTTagCompound) stacks.add(NBTHelper.getItemStack((NBTTagCompound) b));
            }
            Mail mail = new Mail(key, mailTag.l("sender"), mailTag.l("addressee"), mailTag.l("title"), mailTag.l("text"), stacks, mailTag.l("date"), mailTag.q("received"), mailTag.q("deleted"));
            this.mailList.add(mail);
        }
        this.mailList.sort(Comparator.comparing(Mail::getDate));
        //read new mail data
        for (String key : this.newMailFile.getKeys()) {
            NBTTagCompound mailTag = this.newMailFile.getTagCompound(key);
            NBTTagList addresseeTagList = mailTag.c("addressee", 10);
            UUID[] uuids = new UUID[addresseeTagList.size()];
            for (int i = 0; i < addresseeTagList.size(); i++) {
                NBTBase b = addresseeTagList.get(i);
                if (b instanceof NBTTagCompound) {
                    uuids[i] = UUID.fromString(((NBTTagCompound) b).l("uuid"));
                }
            }
            NBTTagList itemTagList = mailTag.c("ItemList", 10);
            ItemStack[] stacks = new ItemStack[8];
            for (int i = 0; i < itemTagList.size(); i++) {
                NBTBase b = itemTagList.get(i);
                if (b instanceof NBTTagCompound) {
                    NBTTagCompound tag = (NBTTagCompound) b;
                    stacks[i] = NBTHelper.getItemStack(tag);
                }
            }
            NewMail mail = new NewMail(key, mailTag.l("sender"), uuids, mailTag.l("title"), mailTag.l("text"), stacks);
            Player p = plugin.getServer().getOfflinePlayer(mail.getSender()).getPlayer();
            MailWriterGUI.NEW_MAP_MAP.put(p, mail);
        }
    }

    public void save() {
        //Save mail data
        this.mailFile.clear();
        for (Mail mail : this.mailList) {
            NBTTagCompound mailTag = new NBTTagCompound();
            mailTag.a("sender", mail.getSender());
            mailTag.a("addressee", mail.getAddressee().toString());
            mailTag.a("title", mail.getTitle());
            mailTag.a("text", mail.getText());
            NBTTagList itemTagList = new NBTTagList();
            for (ItemStack stack : mail.getItemList()) {
                itemTagList.add(NBTHelper.getNBTTagCompound(stack));
            }
            mailTag.a("ItemList", itemTagList);
            mailTag.a("date", mail.getDate());
            mailTag.a("received", mail.isReceived());
            mailTag.a("deleted", mail.isDeleted());
            this.mailFile.setTagCompound(mail.getMailID(), mailTag);
        }
        this.mailFile.write();
        //Save new mail data
        List<NewMail> newMailList = new ArrayList<>(MailWriterGUI.NEW_MAP_MAP.values());
        this.newMailFile.clear();
        for (NewMail mail : newMailList) {
            NBTTagCompound mailTag = new NBTTagCompound();
            mailTag.a("sender", mail.getSender().toString());
            NBTTagList addresseeList = new NBTTagList();
            for (OfflinePlayer op : mail.getAddressee()) {
                NBTTagCompound playerTag = new NBTTagCompound();
                playerTag.a("uuid", op.getUniqueId().toString());
                addresseeList.add(playerTag);
            }
            mailTag.a("addressee", addresseeList);
            mailTag.a("title", mail.getTitle());
            mailTag.a("text", mail.getText());
            NBTTagList itemTagList = new NBTTagList();
            for (ItemStack stack : mail.getItemStacks()) {
                if (stack != null) itemTagList.add(NBTHelper.getNBTTagCompound(stack));
            }
            mailTag.a("ItemList", itemTagList);
            this.newMailFile.setTagCompound(mail.getMailID(), mailTag);
        }
        this.newMailFile.write();
    }

    public void sendMail(Mail mail) {
        this.mailList.add(mail);
    }

    public List<Mail> getMailList(Player p) {
        List<Mail> mailList = new ArrayList<>();
        for (Mail mail : this.mailList) if (p.getUniqueId().equals(mail.getAddressee())) mailList.add(mail);
        return mailList;
    }

    public List<Mail> getUnreadMail(Player p) {
        List<Mail> mailList = new ArrayList<>();
        for (Mail mail : getMailList(p)) if (!mail.isReceived()) mailList.add(mail);
        return mailList;
    }

    public int getMailCount(Player p) {
        int i = 0;
        for (Mail mail : this.mailList) if (p.getUniqueId().equals(mail.getAddressee()) && !mail.isDeleted()) i++;
        return i;
    }

    public List<Mail> getMailListBySender(Player p) {
        List<Mail> mailList = new ArrayList<>();
        for (Mail mail : this.mailList) {
            if (mail.getSender().startsWith("player@") && p.getUniqueId().equals(UUID.fromString(mail.getSender().substring(7)))) mailList.add(mail);
        }
        return mailList;
    }

    public List<Mail> getReceivedMail(Player p) {
        List<Mail> mailList = new ArrayList<>();
        for (Mail mail : plugin.getMailManager().getMailList(p)) {
            if (mail.isReceived()) mailList.add(mail);
        }
        return mailList;
    }

    public List<Mail> getDeletedMail(Player p) {
        List<Mail> mailList = new ArrayList<>();
        for (Mail mail : plugin.getMailManager().getMailList(p)) {
            if (mail.isDeleted()) mailList.add(mail);
        }
        return mailList;
    }

    public List<Mail> getMailList() {
        return this.mailList;
    }
}
