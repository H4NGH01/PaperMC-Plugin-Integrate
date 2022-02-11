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
    private static boolean gui_class_not_null = false;

    public ContainerManager() {
        file.read();
        for (String key : file.getKeys()) {
            NBTTagCompound containerTag = file.getTagCompound(key);
            ContainerItemStack stack = new ContainerItemStack(NBTHelper.asItemStack(containerTag.p("item")));
            CONTAINER_DATA.add(new ContainerData(UUID.fromString(key), ContainerType.valueOf(containerTag.l("type")), stack));
        }
        gui_class_not_null = false;
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
        Container c = getContainerByType(type);
        return new CaseStack(c, new ContainerData(type, c.generateDrop()));
    }

    /**
     * Get new container by type
     * @param type type of container
     * @return new container
     */
    public static @NotNull Container getContainerByType(@NotNull ContainerType type) {
        Container c;
        if (type.getContainer().isAssignableFrom(WeaponCase.class)) {
            c = new WeaponCase();
        } else {
            throw new IllegalArgumentException("Unknown container type");
        }
        return c;
    }

    /**
     * Check is container valid
     * @param stack Container item
     * @return container has data
     */
    public static boolean hasContainerData(ItemStack stack) {
        UUID uuid = UUID.fromString(NBTHelper.getTag(stack).l("ContainerUUID"));
        for (ContainerData data : CONTAINER_DATA) {
            if (data.getUUID().equals(uuid)) return true;
        }
        return false;
    }

    /**
     * Get container data from a container item
     * @param stack container item
     * @return exist container
     */
    public static @NotNull ContainerData getContainerDataByStack(ItemStack stack) {
        UUID uuid = UUID.fromString(NBTHelper.getTag(stack).l("ContainerUUID"));
        for (ContainerData data : CONTAINER_DATA) {
            if (data.getUUID().equals(uuid)) return data;
        }
        throw new IllegalArgumentException("Container data cannot be found");
    }

    public static void unregisterContainerData(ContainerData data) {
        CONTAINER_DATA.remove(data);
    }

    public static boolean isClassNotNull() {
        return gui_class_not_null;
    }

    public static void setClassNotNull() {
        gui_class_not_null = true;
    }
}
