package me.core.cases;

import me.core.items.CaseStack;
import me.core.utils.nbt.NBTHelper;
import me.core.utils.nbt.NBTStorageFile;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class CaseManager {

    private final NBTStorageFile file;
    private final List<CaseData> caseDataList;

    public CaseManager() {
        this.caseDataList = new ArrayList<>();
        this.file = new NBTStorageFile(new File("case-data.nbt"));
        this.file.read();
        for (String key : this.file.getKeys()) {
            NBTTagCompound caseTag = this.file.getTagCompound(key);
            ItemStack stack = NBTHelper.getItemStack(caseTag.p("item"));
            this.caseDataList.add(new CaseData(UUID.fromString(key), CaseType.valueOf(caseTag.l("type")), stack));
        }
    }

    public void save() {
        this.file.clear();
        for (CaseData data : this.caseDataList) {
            NBTTagCompound caseTag = new NBTTagCompound();
            NBTTagCompound itemTag = NBTHelper.getNBTTagCompound(data.getDrop());
            caseTag.a("type", data.getType().toString());
            caseTag.a("item", itemTag);
            this.file.setTagCompound(data.getUUID().toString(), caseTag);
        }
        this.file.write();
    }

    public void registryCase(CaseData caseData) {
        this.caseDataList.add(caseData);
    }

    public CaseStack generateRandomCaseStack() {
        int i = (int) (new Random().nextFloat() * CaseType.values().length);
        CaseType type = CaseType.values()[i];
        return new CaseStack(getCaseByType(type));
    }

    public Case getCaseByType(CaseType type) {
        Case c;
        if (type.getCase().isAssignableFrom(WeaponCase.class)) {
            c = new WeaponCase();
        } else {
            throw new IllegalArgumentException("Unknown case type");
        }
        return c;
    }

    public Case getCase(ItemStack stack) {
        Case c;
        UUID uuid = UUID.fromString(NBTHelper.getTag(stack).l("CaseUUID"));
        if (getCaseTypeByID(NBTHelper.getTag(stack).l("CaseType")).getCase().isAssignableFrom(WeaponCase.class)) {
            c = new WeaponCase(uuid);
        } else {
            throw new IllegalArgumentException("Unknown case type");
        }
        return c;
    }

    private CaseType getCaseTypeByID(String id) {
        for (CaseType type : CaseType.values()) {
            if (type.getID().equals(id)) {
                return type;
            }
        }
        throw new IllegalArgumentException("case id cannot be found.");
    }

    public List<CaseData> getCaseDataList() {
        return this.caseDataList;
    }

    public void unregisterCase(Case caseIn) {
        for (CaseData data : this.caseDataList) {
            if (caseIn.getUUID().equals(data.getUUID())) {
                this.caseDataList.remove(data);
                break;
            }
        }
    }
}
