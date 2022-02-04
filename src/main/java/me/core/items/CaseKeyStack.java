package me.core.items;

import me.core.cases.CaseKey;
import me.core.utils.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;

public class CaseKeyStack extends PluginItem {

    private final CaseKey type;

    public CaseKeyStack(CaseKey caseKey) {
        super(Material.TRIPWIRE_HOOK);
        this.type = caseKey;
        this.setDisplayName(Component.translatable(caseKey.getTranslationKey()));
        this.addLore(Component.translatable("case.key.can_open").args(Component.translatable(caseKey.canOpen().getTranslationKey())).decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        NBTHelper.setTag(this, "CaseKey", caseKey.getID());
        this.setPlaceable(false);
    }

    public CaseKey getKeyType() {
        return this.type;
    }
}
