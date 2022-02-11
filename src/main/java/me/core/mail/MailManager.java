package me.core.mail;

import me.core.MCServerPlugin;
import me.core.utils.nbt.NBTHelper;
import me.core.utils.nbt.NBTStorageFile;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public class MailManager {

    private final MCServerPlugin plugin = MCServerPlugin.getPlugin(MCServerPlugin.class);
    private static final List<Mail> mailList = new ArrayList<>();
    private static final NBTStorageFile mailFile = new NBTStorageFile(new File("mails.nbt"));
    private static final NBTStorageFile newMailFile = new NBTStorageFile(new File("new-mails.nbt"));
    private static final HashMap<UUID, NewMail> NEW_MAIL_MAP = new HashMap<>();

    public MailManager() {
        //read mail data
        mailFile.read();
        newMailFile.read();
        for (String key : mailFile.getKeys()) {
            NBTTagCompound mailTag = mailFile.getTagCompound(key);
            NBTTagList itemTagList = mailTag.c("ItemList", 10);
            List<ItemStack> stacks = new ArrayList<>();
            for (NBTBase b : itemTagList) {
                if (b instanceof NBTTagCompound tag) stacks.add(NBTHelper.asItemStack(tag));
            }
            Mail mail = new Mail(key, mailTag.l("sender"), mailTag.l("addressee"), mailTag.l("title"), mailTag.l("text"), stacks, mailTag.l("date"), mailTag.q("received"), mailTag.q("deleted"));
            mailList.add(mail);
        }
        mailList.sort(Comparator.comparing(Mail::getDate));
        //read new mail data
        for (String key : newMailFile.getKeys()) {
            NBTTagCompound mailTag = newMailFile.getTagCompound(key);
            NBTTagList addresseeTagList = mailTag.c("addressee", 10);
            UUID[] uuids = new UUID[addresseeTagList.size()];
            for (int i = 0; i < addresseeTagList.size(); i++) {
                NBTBase b = addresseeTagList.get(i);
                if (b instanceof NBTTagCompound tag) {
                    uuids[i] = UUID.fromString((tag).l("uuid"));
                }
            }
            NBTTagList itemTagList = mailTag.c("ItemList", 10);
            ItemStack[] stacks = new ItemStack[8];
            for (int i = 0; i < itemTagList.size(); i++) {
                NBTBase b = itemTagList.get(i);
                if (b instanceof NBTTagCompound tag) {
                    stacks[i] = NBTHelper.asItemStack(tag);
                }
            }
            NewMail mail = new NewMail(key, mailTag.l("sender"), uuids, mailTag.l("title"), mailTag.l("text"), stacks);
            getNewMailMap().put(mail.getSender(), mail);
        }
    }

    public void save() {
        //Save mail data
        mailFile.clear();
        for (Mail mail : mailList) {
            NBTTagCompound mailTag = new NBTTagCompound();
            mailTag.a("sender", mail.getSender());
            mailTag.a("addressee", mail.getAddressee().toString());
            mailTag.a("title", mail.getTitle());
            mailTag.a("text", mail.getText());
            NBTTagList itemTagList = new NBTTagList();
            for (ItemStack stack : mail.getItemList()) {
                itemTagList.add(NBTHelper.asNBTTagCompound(stack));
            }
            mailTag.a("ItemList", itemTagList);
            mailTag.a("date", mail.getDate());
            mailTag.a("received", mail.isReceived());
            mailTag.a("deleted", mail.isDeleted());
            mailFile.setTagCompound(mail.getMailID(), mailTag);
        }
        mailFile.write();
        //Save new mail data
        List<NewMail> newMailList = new ArrayList<>(getNewMailMap().values());
        newMailFile.clear();
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
                if (stack != null) itemTagList.add(NBTHelper.asNBTTagCompound(stack));
            }
            mailTag.a("ItemList", itemTagList);
            newMailFile.setTagCompound(mail.getMailID(), mailTag);
        }
        newMailFile.write();
    }

    public static @Nullable Mail getMailByID(String id) {
        for (Mail mail : mailList) {
            if (mail.getMailID().equals(id)) {
                return mail;
            }
        }
        return null;
    }

    public static void sendMail(Mail mail) {
        mailList.add(mail);
    }

    public static @NotNull List<Mail> getMailList(Player p) {
        List<Mail> mailList = new ArrayList<>();
        for (Mail mail : MailManager.mailList) if (p.getUniqueId().equals(mail.getAddressee())) mailList.add(mail);
        return mailList;
    }

    public static @NotNull List<Mail> getUnreadMail(Player p) {
        List<Mail> mailList = new ArrayList<>();
        for (Mail mail : getMailList(p)) if (!mail.isReceived()) mailList.add(mail);
        return mailList;
    }

    public static int getMailCount(Player p) {
        int i = 0;
        for (Mail mail : mailList) if (p.getUniqueId().equals(mail.getAddressee()) && !mail.isDeleted()) i++;
        return i;
    }

    public static @NotNull List<Mail> getMailListBySender(Player p) {
        List<Mail> mailList = new ArrayList<>();
        for (Mail mail : MailManager.mailList) {
            if (mail.getSender().startsWith("player@") && p.getUniqueId().equals(UUID.fromString(mail.getSender().substring(7))))
                mailList.add(mail);
        }
        return mailList;
    }

    public static @NotNull List<Mail> getReceivedMail(Player p) {
        List<Mail> mailList = new ArrayList<>();
        for (Mail mail : getMailList(p)) {
            if (mail.isReceived()) mailList.add(mail);
        }
        return mailList;
    }

    public static @NotNull List<Mail> getDeletedMail(Player p) {
        List<Mail> mailList = new ArrayList<>();
        for (Mail mail : getMailList(p)) {
            if (mail.isDeleted()) mailList.add(mail);
        }
        return mailList;
    }

    public static List<Mail> getMailList() {
        return mailList;
    }

    public static HashMap<UUID, NewMail> getNewMailMap() {
        return NEW_MAIL_MAP;
    }
}
