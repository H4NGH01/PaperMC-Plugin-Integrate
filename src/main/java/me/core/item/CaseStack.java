package me.core.item;

import me.core.cases.Case;
import me.core.cases.CaseType;
import me.core.util.nbt.NBTHelper;
import net.kyori.adventure.text.Component;

public class CaseStack extends PluginItem {

    private final CaseType type;

    public CaseStack(Case caseIn) {
        super(caseIn.getCaseTexture());
        this.type = caseIn.getCaseType();
        this.setDisplayName(Component.text(caseIn.getCaseType().getName()));
        this.addLore(Component.text("Need key: " + caseIn.getCaseKey().getName()));
        NBTHelper.setTag(this, "CaseUUID", caseIn.getUUID().toString());
        NBTHelper.setTag(this, "CaseType", caseIn.getCaseType().getID());
        this.setPlaceable(false);
    }

    public CaseType getCaseType() {
        return this.type;
    }
}
