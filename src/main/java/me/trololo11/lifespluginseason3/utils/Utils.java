package me.trololo11.lifespluginseason3.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class Utils {

    /**
     * This is just a shortcut from using {@link ChatColor#translateAlternateColorCodes(char, String)}
     * which is using altColorChar as '&'
     * @param s The string to translate
     * @return The translated string
     */
    public static String chat(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static boolean isInventoryFull(Inventory inventory){

        for(ItemStack itemStack : inventory.getContents()){
            if(itemStack == null) return false;
        }

        return true;
    }

    /**
     * Returns how much empty space there is in an inventory
     * @param inventory The inventory check
     * @return The empty space
     */
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

    /**
     * Creates a new {@link ItemStack} from the specified parameters
     * @param material The material of this item
     * @param name The display name of this item
     * @param localizedName The localized name of this item
     * @param lore The LOREEE of this item
     * @return The created {@link ItemStack}
     */
    public static ItemStack createItem(Material material, String name, String localizedName, String... lore){
        ItemStack item = new ItemStack(material);

        ArrayList<String> loreArray = new ArrayList<>();

        for(String string : lore){
            loreArray.add(Utils.chat(string));
        }

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.chat(name));
        itemMeta.setLocalizedName(localizedName);
        itemMeta.setLore(loreArray);

        item.setItemMeta(itemMeta);

        return item;
    }

    public boolean checkByUUID(ArrayList<OfflinePlayer> checkArray, OfflinePlayer player){
        for(OfflinePlayer checkPlayer : checkArray){
            if(player.getUniqueId().equals(checkPlayer.getUniqueId())) return true;
        }

        return false;
    }

    /**
     * Tries to add an item to a player's inventory if it's full it drops the item on the ground
     * @param itemStack The item stack to add
     * @param player The player to add it to
     */
    public static void addSafelyItem(ItemStack itemStack, Player player){

        if(isInventoryFull(player.getInventory())){
            player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
        }else{
            player.getInventory().addItem(itemStack);
        }

    }
}
