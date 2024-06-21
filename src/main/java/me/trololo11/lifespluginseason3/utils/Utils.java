package me.trololo11.lifespluginseason3.utils;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.cardstuff.CardType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    /**
     * Checks if a player's is full and no items can get in.
     * @param playerInventory The inventory to check.
     * @return If the inventory is full.
     */
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
     * @param privateName The private name of this item
     * @param lore The LOREEE of this item
     * @return The created {@link ItemStack}
     */
    public static ItemStack createItem(Material material, String name, String privateName, String... lore){
        ItemStack item = new ItemStack(material);

        ArrayList<String> loreArray = new ArrayList<>();

        for(String string : lore){
            loreArray.add(Utils.chat(string));
        }

        item.setItemMeta(getItemMeta(item.getItemMeta(), name, privateName, loreArray));

        return item;
    }
    /**
     * Creates a new enchanted {@link ItemStack} from the specified parameters
     * @param material The material of this item
     * @param name The display name of this item
     * @param privateName The private name of this item
     * @param enchantment The {@link Enchantment} that will be aplied to this item
     * @param lore The LOREEE of this item
     * @return The created {@link ItemStack}
     */
    public static ItemStack createEnchantedItem(Material material, String name, String privateName, Enchantment enchantment, String... lore){
        ItemStack item = new ItemStack(material);

        ArrayList<String> loreArray = new ArrayList<>();

        for(String string : lore){
            loreArray.add(Utils.chat(string));
        }

        item.setItemMeta(getItemMeta(item.getItemMeta(), name, privateName, loreArray));

        item.addUnsafeEnchantment(enchantment, 1);

        return item;
    }

    /**
     * Creates a new {@link ItemStack} from the specified parameters
     * @param material The material of this item
     * @param name The display name of this item
     * @param privateName The private name of this item
     * @param lore The LOREEE of this item
     * @return The created {@link ItemStack}
     */
    public static ItemStack createItem(Material material, String name, String privateName, List<String> lore){
        ItemStack item = new ItemStack(material);

        item.setItemMeta(getItemMeta(item.getItemMeta(), name, privateName, lore));

        return item;
    }

    /**
     * Creates a new {@link ItemStack} from the specified parameters
     * @param material The material of this item
     * @param customModelData The cutom model data that will be aplied to this item
     * @param name The display name of this item
     * @param privateName The private name of this item
     * @param lore The LOREEE of this item
     * @return The created {@link ItemStack}
     */
    public static ItemStack createCustomModelItem(Material material, int customModelData, String name, String privateName, String... lore){

        ItemStack item = new ItemStack(material);
        ArrayList<String> loreArray = new ArrayList<>();

        for(String string : lore){
            loreArray.add(Utils.chat(string));
        }

        ItemMeta itemMeta = getItemMeta(Objects.requireNonNull(item.getItemMeta()), name, privateName, loreArray);

        itemMeta.setCustomModelData(customModelData);

        item.setItemMeta(itemMeta);

        return item;

    }

    /**
     * Creates a new player head from the specified parameters
     * @param owner The player that will be displayed on this head
     * @param name The display name of this item
     * @param privateName The private name of this item
     * @param lore The LOREEE of this item
     * @return The created {@link ItemStack}
     */
    public static ItemStack createPlayerHead(OfflinePlayer owner, String name, String privateName, String... lore){
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);

        ArrayList<String> loreArray = new ArrayList<>();

        for(String string : lore){
            loreArray.add(Utils.chat(string));
        }

        SkullMeta skullMeta = (SkullMeta) getItemMeta(item.getItemMeta(), name, privateName, loreArray);

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

        if(isPlayerInvFull(player.getInventory())){
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


    private static ItemMeta getItemMeta(ItemMeta itemMeta, String displayName, String privateName, List<String> lore){

        itemMeta.setDisplayName(Utils.chat(displayName));
        setPrivateName(itemMeta, privateName);
        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);

        return itemMeta;
    }

    /**
     * Checks the inventory for an item that has a private name which starts the same as the one provided
     * and returns the index of the slot of this item. <br><br>
     * If no item is found it returns -1
     * @param privateName The private name to check in the item
     * @param inventory The inventory to check
     * @return The index of this item, if none is found then returns -1.
     */
    public static int indexOfItemPrivateName(String privateName, Inventory inventory){
        ItemStack[] contents = inventory.getContents();
        int lengthCon = contents.length;

        for(int i=0; i < lengthCon; i++){
            ItemStack item = contents[i];

            if(item == null || !item.hasItemMeta() || getPrivateName(item) == null) continue;

            if(getPrivateName(item).startsWith(privateName)) return i;

        }

        return -1;

    }

    /**
     * Tries to give the specified item to the player and
     * if their inventory is full then it drops it into the ground.
     * @param player The player to give the item to.
     * @param itemStack The item to give.
     */
    public static void givePlayerItemSafely(Player player, ItemStack itemStack){
        PlayerInventory inventory = player.getInventory();
        if(Utils.isPlayerInvFull(inventory)){
            player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
        }else{
            inventory.addItem(itemStack);
        }

    }

    /**
     * Check if the private name of the card is equal to the card
     * type that is specified.
     * @param item The item of the card to check.
     * @param cardType The type to be compared with the private name.
     * @return If the card is correct.
     */
    public static boolean checkCardItem(ItemStack item, CardType cardType){
        if(item.getType() == Material.AIR) return false;
        if(!item.hasItemMeta()) return false;
        if(getPrivateName(item) == null) return false;

        return getPrivateName(item).startsWith(cardType.toString().toLowerCase());
    }

    public static boolean hasPrivateName(@NotNull ItemStack itemStack){
        return getPrivateName(itemStack) == null;
    }

    /**
     * Checks if the private name of the item is equal to the specified string. (Case insensitive)
     * @param item The item to check the private name.
     * @param string The string to check to.
     * @return If the private name is equal
     * @see LifesPlugin#getPrivateNameKey()
     */
    public static boolean isPrivateNameEqual(@NotNull ItemStack item, String string){
        String privateName = getPrivateName(item);
        if(privateName == null) return false;

        return privateName.equalsIgnoreCase(string);
    }

    /**
     * Gets the private name from the item.
     * @param item The item to get the name from.
     * @return The private name of the item.
     */
    public static String getPrivateName(@NotNull ItemStack item){
        if(item.getItemMeta() == null) return null;

        return getPrivateName(item.getItemMeta());
    }

    public static void setPrivateName(@NotNull ItemMeta itemMeta, String privateName){
        itemMeta.getPersistentDataContainer().set(LifesPlugin.getPrivateNameKey(), PersistentDataType.STRING, privateName);
    }

    /**
     * Gets the private name from the item meta.
     * @param itemMeta The item meta to get the name from.
     * @return The private name of the item meta.
     */
    public static String getPrivateName(@NotNull ItemMeta itemMeta){
        return itemMeta.getPersistentDataContainer().get(LifesPlugin.getPrivateNameKey(), PersistentDataType.STRING);
    }

    /**
     * Returns how much blocks seperates the provided locations
     * @param from The starting location
     * @param to The end location
     * @return Amount of blocks that seperate the provided locations
     */
    public static int getDistance(Location from, Location to) {
        return Math.abs((from.getBlockX() - to.getBlockX()) + (from.getBlockZ() - to.getBlockZ()));
    }


}
