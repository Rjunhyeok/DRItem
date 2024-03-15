package kr.squirrel.item.util;

import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBTUtil {
    public static ItemStack putTag(ItemStack itemStack, String key, String value) {
        net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = stack.v();
        compound.a(key, value);
        stack.b(compound);
        return CraftItemStack.asBukkitCopy(stack);
    }

    public static String getTag(ItemStack itemStack, String key) {
        net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = stack.v();
        return compound.l(key);
    }

    public static ItemStack removeTag(ItemStack itemStack, String key) {
        net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = stack.v();
        compound.r(key);
        stack.b(compound);
        return CraftItemStack.asBukkitCopy(stack);
    }

    public static boolean hasTag(ItemStack itemStack, String key) {
        net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = stack.v();
        return compound.e(key);
    }

}
