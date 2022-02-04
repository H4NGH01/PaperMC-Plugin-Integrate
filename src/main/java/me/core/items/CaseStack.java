package me.core.items;

import me.core.cases.Case;
import me.core.cases.CaseType;
import me.core.utils.nbt.NBTHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class CaseStack extends PluginItem {

    private final CaseType type;

    public CaseStack(Case caseIn) {
        super(caseIn.getCaseTexture());
        this.type = caseIn.getCaseType();
        this.setDisplayName(Component.translatable(caseIn.getCaseType().getTranslationKey()));
        this.addLore(Component.translatable("gui.case.require_key").args(Component.translatable(caseIn.getCaseKey().getTranslationKey())).decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY));
        NBTHelper.setTag(this, "CaseUUID", caseIn.getUUID().toString());
        NBTHelper.setTag(this, "CaseType", caseIn.getCaseType().getID());
        this.setPlaceable(false);
    }

    public CaseType getCaseType() {
        return this.type;
    }
}
