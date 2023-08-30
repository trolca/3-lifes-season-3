package me.trololo11.lifespluginseason3.utils;

import org.bukkit.ChatColor;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Utils {

    public static String chat(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static int getEmptySpaceInInv(Inventory inventory){

        int empty = 0;

        for(ItemStack itemStack : inventory.getContents()){

            if(itemStack == null) empty++;
        }

        return empty;
    }

    public static int getMaxCraftAmount(CraftingInventory inv) {
        if (inv.getResult() == null)
            return 0;

        int resultCount = inv.getResult().getAmount();
        int materialCount = Integer.MAX_VALUE;

        for (ItemStack is : inv.getMatrix())
            if (is != null && is.getAmount() < materialCount)
                materialCount = is.getAmount();

        return resultCount * materialCount;
    }
}
