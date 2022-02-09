package me.core.containers;

import me.core.items.CaseStack;
import me.core.items.ContainerItemStack;
import me.core.utils.nbt.NBTHelper;
import me.core.utils.nbt.NBTStorageFile;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ContainerManager {

    private static final NBTStorageFile file = new NBTStorageFile(new File("container-data.nbt"));
    private static final List<ContainerData> CONTAINER_DATA = new ArrayList<>();
    private static boolean b = false;

    public ContainerManager() {
        file.read();
        for (String key : file.getKeys()) {
            NBTTagCompound containerTag = file.getTagCompound(key);
            ContainerItemStack stack = new ContainerItemStack(NBTHelper.asItemStack(containerTag.p("item")));
            CONTAINER_DATA.add(new ContainerData(UUID.fromString(key), ContainerType.valueOf(containerTag.l("type")), stack));
        }
        b = false;
    }

    public void save() {
        file.clear();
        for (ContainerData data : CONTAINER_DATA) {
            NBTTagCompound containerTag = new NBTTagCompound();
            NBTTagCompound itemTag = NBTHelper.asNBTTagCompound(data.getDrop());
            containerTag.a("type", data.getType().toString());
            containerTag.a("item", itemTag);
            file.setTagCompound(data.getUUID().toString(), containerTag);
        }
        file.write();
    }

    public static void registryContainerData(ContainerData containerData) {
        CONTAINER_DATA.add(containerData);
    }

    public static @NotNull CaseStack generateRandomCaseStack() {
        int i = (int) (new Random().nextFloat() * ContainerType.values().length);
        ContainerType type = ContainerType.values()[i];
        return new CaseStack(getContainerByType(type));
    }

    public static @NotNull Container getContainerByType(@NotNull ContainerType type) {
        Container c;
        if (type.getContainer().isAssignableFrom(WeaponCase.class)) {
            c = new WeaponCase();
        } else {
            throw new IllegalArgumentException("Unknown container type");
        }
        return c;
    }

    public static @NotNull Container getContainerByStack(ItemStack stack) {
        Container c;
        UUID uuid = UUID.fromString(NBTHelper.getTag(stack).l("ContainerUUID"));
        if (ContainerType.byID(NBTHelper.getTag(stack).l("ContainerType")).getContainer().isAssignableFrom(WeaponCase.class)) {
            c = new WeaponCase(uuid);
        } else {
            throw new IllegalArgumentException("Unknown container type");
        }
        return c;
    }

    public static List<ContainerData> getCaseDataList() {
        return CONTAINER_DATA;
    }

    public static void unregisterContainerData(Container containerIn) {
        for (ContainerData data : CONTAINER_DATA) {
            if (containerIn.getUUID().equals(data.getUUID())) {
                CONTAINER_DATA.remove(data);
                break;
            }
        }
    }

    public static boolean c() {
        return b;
    }

    public static void d() {
        b = true;
    }
}
