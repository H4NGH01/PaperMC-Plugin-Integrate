package me.core.guis;

import me.core.items.MCServerItems;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class MultiplePageGUI extends GUIBase {

    protected final int startSlot;
    protected final int endSlot;
    protected final int slotPerPage;
    private List<ItemStack> list;
    private int maxPage;
    private int page = 1;

    public MultiplePageGUI(Player player) {
        this(player, new ArrayList<>(), 9, 44);
    }

    public MultiplePageGUI(Player player, List<ItemStack> list, int start, int end) {
        super(player);
        this.startSlot = start;
        this.endSlot = end;
        this.slotPerPage = (this.endSlot - this.startSlot) + 1;
        this.list = list;
        this.maxPage = this.list.size() > this.slotPerPage ? this.list.size() / this.slotPerPage + 1 : 1;
    }

    @Override
    protected void setDefault() {
        super.setDefault();
        this.inventory.setItem(45, MCServerItems.prev);
        this.inventory.setItem(53, MCServerItems.next);
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

    public void next() {
        if (this.page < this.maxPage) {
            this.page++;
            this.getPlayer().closeInventory();
            this.getPlayer().openInventory(this.inventory);
            this.toArray(this.page);
            this.getPlayer().playSound(this.getPlayer().getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 0.7f, 1f);
        }
    }

    public void prev() {
        if (this.page > 1) {
            this.page--;
            this.getPlayer().closeInventory();
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
