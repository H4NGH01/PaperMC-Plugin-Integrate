package me.core.gui;

import me.core.item.MCServerItems;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class MultiplePageGUI extends GUIBase {

    private final int startSlot;
    private final int endSlot;
    private final int slotPerPage;
    private List<ItemStack> list;
    private int maxPage;
    private int page = 1;

    public MultiplePageGUI(Player player) {
        this(player, new ArrayList<>(), 9, 44);
    }

    public MultiplePageGUI(Player player, List<ItemStack> list, int start, int end) {
        super(player);
        this.list = list;
        this.startSlot = start;
        this.endSlot = end;
        this.slotPerPage = (this.endSlot - this.startSlot) + 1;
        this.maxPage = this.list.size() > this.slotPerPage ? this.list.size() / this.slotPerPage + 1 : 1;
    }

    public void setContents(List<ItemStack> list) {
        this.list = list;
        this.maxPage = this.list.size() > this.slotPerPage ? this.list.size() / this.slotPerPage + 1 : 1;
    }

    public void toArray(int page) {
        int j = 0;
        for (int i = this.startSlot; i < this.endSlot + 1; i++) {
            this.inventory.setItem(i, list.size() > j + ((page - 1) * slotPerPage) ? list.get(j + ((page - 1) * slotPerPage)) : MCServerItems.air);
            j++;
        }
    }

    protected void updateGUIName() {
        this.setTitle(this.getGUIName(), " (" + this.getPage() + "/" + this.getMaxPage() + ")");
    }

    public void next() {
        if (this.page < this.maxPage) {
            this.page++;
            this.getPlayer().closeInventory();
            this.updateGUIName();
            this.getPlayer().openInventory(this.inventory);
            this.toArray(this.page);
            this.getPlayer().playSound(this.getPlayer().getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 0.7f, 1f);
        }
    }

    public void prev() {
        if (this.page > 1) {
            this.page--;
            this.getPlayer().closeInventory();
            this.updateGUIName();
            this.getPlayer().openInventory(this.inventory);
            this.toArray(this.page);
            this.getPlayer().playSound(this.getPlayer().getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 0.7f, 1f);
        }
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getMaxPage() {
        return this.maxPage;
    }
}
