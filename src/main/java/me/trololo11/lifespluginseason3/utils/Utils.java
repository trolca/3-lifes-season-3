package me.trololo11.lifespluginseason3.utils;

import me.trololo11.lifespluginseason3.cardstuff.CardType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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

    public static boolean isPlayerInvFull(PlayerInventory playerInventory){
        int howMuch = Utils.getEmptySpaceInInv(playerInventory);

        return howMuch-6 < 0;
    }

    /**
     * Gets the max amount of items that a player can craft from the
     * specified recipe
     * @param inv The inventory to check
     * @return The max amount of items
     */
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

        item.setItemMeta(getItemMeta(item.getItemMeta(), name, localizedName, loreArray));

        return item;
    }

    public static ItemStack createEnchantedItem(Material material, String name, String localizedName, Enchantment enchantment, String... lore){
        ItemStack item = new ItemStack(material);

        ArrayList<String> loreArray = new ArrayList<>();

        for(String string : lore){
            loreArray.add(Utils.chat(string));
        }

        item.setItemMeta(getItemMeta(item.getItemMeta(), name, localizedName, loreArray));

        item.addUnsafeEnchantment(enchantment, 1);

        return item;
    }

    public static ItemStack createItem(Material material, String name, String localizedName, List<String> lore){
        ItemStack item = new ItemStack(material);

        item.setItemMeta(getItemMeta(item.getItemMeta(), name, localizedName, lore));

        return item;
    }

    public static ItemStack createPlayerHead(OfflinePlayer owner, String name, String localizedName, String... lore){
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);

        ArrayList<String> loreArray = new ArrayList<>();

        for(String string : lore){
            loreArray.add(Utils.chat(string));
        }

        SkullMeta skullMeta = (SkullMeta) getItemMeta(item.getItemMeta(), name, localizedName, loreArray);

        skullMeta.setOwningPlayer(owner);

        item.setItemMeta(skullMeta);

        return item;
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

    /**
     * Creates a new text file using the data from the provided {@link InputStream}
     * @param is The inputStream to get the text from
     * @param file The file to write to
     * @throws IOException {@inheritDoc}
     */
    public static void createTextFile(InputStream is, File file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8);
        int read = reader.read();
        while(read != -1){
            fileWriter.write((char) read);
            read = reader.read();
        }

        reader.close();
        fileWriter.close();
    }


    private static ItemMeta getItemMeta(ItemMeta itemMeta, String displayName, String localizedName, List<String> lore){

        itemMeta.setDisplayName(Utils.chat(displayName));
        itemMeta.setLocalizedName(localizedName);
        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);

        return itemMeta;
    }

    /**
     * Checks the inventory for an item that has a localized name which starts the same as the one provided
     * and returns the index of the slot of this item. <br><br>
     * If no item is found it returns -1
     * @param localizedName The localized name to check in the item
     * @param inventory The inventory to check
     * @return The index of this item, if none is found then returns -1.
     */
    public static int indexOfItemLocalized(String localizedName, Inventory inventory){
        ItemStack[] contents = inventory.getContents();
        int lenghtCon = contents.length;

        for(int i=0; i < lenghtCon; i++){
            ItemStack item = contents[i];

            if(item == null || !item.hasItemMeta() || !item.getItemMeta().hasLocalizedName()) continue;

            if(item.getItemMeta().getLocalizedName().startsWith(localizedName)) return i;

        }

        return -1;

    }

    public static void givePlayerItemSafely(Player player, ItemStack itemStack){
        PlayerInventory inventory = player.getInventory();
        if(Utils.isPlayerInvFull(inventory)){
            player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
        }else{
            inventory.addItem(itemStack);
        }

    }

    public static boolean checkCardItem(ItemStack item, CardType cardType){
        if(item.getType() == Material.AIR) return false;
        if(!item.hasItemMeta()) return false;
        if(!item.getItemMeta().hasLocalizedName()) return false;

        return item.getItemMeta().getLocalizedName().startsWith(cardType.toString().toLowerCase());
    }


}
