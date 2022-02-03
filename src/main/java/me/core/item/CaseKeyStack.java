package me.core.item;

import me.core.cases.CaseKey;
import me.core.util.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public class CaseKeyStack extends PluginItem {

    private final CaseKey type;

    public CaseKeyStack(CaseKey caseKey) {
        super(Material.TRIPWIRE_HOOK);
        this.type = caseKey;
        this.setDisplayName(Component.text(caseKey.getName()));
        this.addLore(Component.text("This key can be open: " + caseKey.canOpen().getName()));
        NBTHelper.setTag(this, "CaseKey", caseKey.getID());
        this.setPlaceable(false);
    }

    public CaseKey getKeyType() {
        return this.type;
    }
}
