package me.trololo11.lifespluginseason3.managers;

import me.trololo11.lifespluginseason3.LifesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.UUID;

/**
 * This class stores all of the custom items used by this plugin. <br>
 * It also stores and creates the recipes for the life item and revive card item. <br>
 * The custom items stored:
 * <ul>
 *     <li>Life item</li>
 *     <li>Player life item</li>
 *     <li>Revive card item</li>
 *     <li>Life shard item</li>
 *     <li>Revive card shard item</li>
 *     <li>Gold life item</li>
 * </ul>
 */
public class RecipesManager {

    private ItemStack lifeItem;
    private ItemStack playerLifeItem;
    private ItemStack reviveCardItem;
    private ItemStack lifeShardItem;
    private ItemStack reviveShardItem;
    private ItemStack goldLifeItem;

    private ShapedRecipe lifesRecipe;
    private ShapedRecipe reviveCardRecipe;

    private LifesPlugin plugin = LifesPlugin.getPlugin();

    public RecipesManager(){
        createItems();
        createRecipes();
    }

    /**
     * Creates all of the custom items.
     */
    private void createItems(){
        ArrayList<String> lore = new ArrayList<>();

        lifeItem = new ItemStack(Material.SCUTE);
        playerLifeItem = new ItemStack(Material.SCUTE);
        reviveCardItem = new ItemStack(Material.SCUTE);
        lifeShardItem = new ItemStack(Material.GOLD_NUGGET);
        reviveShardItem = new ItemStack(Material.IRON_NUGGET);
        goldLifeItem = new ItemStack(Material.SCUTE);

        ItemMeta heartMeta = lifeItem.getItemMeta();
        heartMeta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Życie");
        lore.add(ChatColor.WHITE + "Użyj PPM by użyć!");
        heartMeta.setLore(lore);
        heartMeta.setCustomModelData(8760001);
        heartMeta.setLocalizedName("life_item");

        ItemMeta playerHeartMeta = playerLifeItem.getItemMeta();
        playerHeartMeta.setDisplayName(ChatColor.DARK_RED + ChatColor.BOLD.toString() + "Życie <player>");
        playerHeartMeta.setLore(lore);
        playerHeartMeta.setCustomModelData(8760002);
        playerHeartMeta.setLocalizedName("player_life");

        ItemMeta goldLifeMeta = goldLifeItem.getItemMeta();
        goldLifeMeta.setDisplayName(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Złote życie");

        lore.clear();
        lore.add(ChatColor.WHITE + "Dodaje 2 życia");
        lore.add(ChatColor.WHITE + "Użyj PPM by użyć!");

        goldLifeMeta.setLore(lore);
        goldLifeMeta.setCustomModelData(8760003);
        goldLifeMeta.setLocalizedName("gold_life");

        ItemMeta reviveMeta = reviveCardItem.getItemMeta();
        reviveMeta.setDisplayName(ChatColor.AQUA + ChatColor.BOLD.toString() + "Karta odrodzenia");
        lore.clear();
        lore.add(ChatColor.GRAY + "Przypisany gracz: nikt");
        lore.add(ChatColor.WHITE + "Zmień nazwe tego itemu na osobe");
        lore.add(ChatColor.WHITE + "którą chcesz wskrzesić.");
        lore.add(ChatColor.WHITE + "Później użyj PPM by og wskrzesić!");
        reviveMeta.setLore(lore);
        reviveMeta.setCustomModelData(8760005);
        reviveMeta.setLocalizedName("revive_card_player:null");

        ItemMeta heartShardMeta = lifeShardItem.getItemMeta();
        heartShardMeta.setDisplayName(ChatColor.RED + "Kawałek życia");
        heartShardMeta.setCustomModelData(8760001);
        heartShardMeta.setLocalizedName("shard_life");

        ItemMeta reviveCardShardMeta = reviveCardItem.getItemMeta();
        reviveCardShardMeta.setDisplayName(ChatColor.AQUA + "Kawałek karty odrodzenia");
        reviveCardShardMeta.setCustomModelData(8760001);
        reviveCardShardMeta.setLocalizedName("shard_revive_card");

        lifeItem.setItemMeta(heartMeta);
        reviveCardItem.setItemMeta(reviveMeta);
        playerLifeItem.setItemMeta(playerHeartMeta);
        lifeShardItem.setItemMeta(heartShardMeta);
        reviveShardItem.setItemMeta(reviveCardShardMeta);
        goldLifeItem.setItemMeta(goldLifeMeta);
    }

    /**
     * Creates and initializes al of the custom recipes.
     */
    private void createRecipes(){
        NamespacedKey lifesNameKey = new NamespacedKey(plugin, "life_recipe");
        NamespacedKey reviveNameKey = new NamespacedKey(plugin, "revive_card_recipe");

        lifesRecipe = new ShapedRecipe(lifesNameKey, lifeItem);
        reviveCardRecipe = new ShapedRecipe(reviveNameKey, reviveCardItem);


        lifesRecipe.shape("###", "#D#", "###");
        lifesRecipe.setIngredient('#', new RecipeChoice.ExactChoice(lifeShardItem));
        lifesRecipe.setIngredient('D', Material.DIAMOND);

        reviveCardRecipe.shape("###", "#D#", "###");
        reviveCardRecipe.setIngredient('#', new RecipeChoice.ExactChoice(reviveShardItem));
        reviveCardRecipe.setIngredient('D', Material.NETHERITE_INGOT);

        //adds them to the server so player can use them
        Bukkit.getServer().addRecipe(lifesRecipe);
        Bukkit.getServer().addRecipe(reviveCardRecipe);
    }

    /**
     * Returns a new life item with a random UUID as it's localized name
     * to make the items non-stackable.
     * @return The new non-stackable life.
     */
    public ItemStack getLifeItem() {
        ItemStack lifeItemCopy = lifeItem.clone();
        ItemMeta lifeMeta = lifeItemCopy.getItemMeta();
        lifeMeta.setLocalizedName(lifeMeta.getLocalizedName()+"_"+UUID.randomUUID());

        lifeItemCopy.setItemMeta(lifeMeta);

        return lifeItemCopy;
    }

    /**
     * Returns a new player life with the specified player's name
     * on the display name. It makes it's localized name
     * a random UUID to make it non-stackable.
     *
     * @param player The player to put on the display name of this life.
     * @return A new player life
     */
    public ItemStack getPlayerLifeItem(Player player) {

        ItemStack playerLifeItemCopy = playerLifeItem.clone();
        ItemMeta playerLifeMeta = playerLifeItemCopy.getItemMeta();
        playerLifeMeta.setDisplayName(playerLifeMeta.getDisplayName().replace("<player>", player.getDisplayName()));
        playerLifeMeta.setLocalizedName(playerLifeMeta.getLocalizedName()+"_"+UUID.randomUUID());
        playerLifeItemCopy.setItemMeta(playerLifeMeta);

        return playerLifeItemCopy;
    }

    /**
     * Returns a new revive card item with a random UUID as it's localized name
     * to make the items non-stackable.
     * @return The new non-stackable revive card.
     */
    public ItemStack getReviveCardItem() {
        return getReviveCardItem(UUID.randomUUID());
    }

    /**
     * Returns a new revive card item with the specified uuid in it's
     * localized name.
     * @param uuid The uuid to set
     * @return The new revive card item
     */
    public ItemStack getReviveCardItem(UUID uuid) {
        ItemStack reviveItemCopy = reviveCardItem.clone();
        ItemMeta reviveMeta = reviveItemCopy.getItemMeta();
        reviveMeta.setLocalizedName(reviveMeta.getLocalizedName()+"_"+uuid);

        reviveItemCopy.setItemMeta(reviveMeta);

        return reviveItemCopy;
    }

    /**
     * Returns a new gold life. It makes it's localized name
     * a random UUID to make it non-stackable.
     * @return A new gold life
     */
    public ItemStack getGoldLifeItem(){
        ItemStack goldLifeCopy = goldLifeItem.clone();
        ItemMeta goldLifeMeta = goldLifeCopy.getItemMeta();
        goldLifeMeta.setLocalizedName(goldLifeMeta.getLocalizedName() + "_" + UUID.randomUUID());

        goldLifeCopy.setItemMeta(goldLifeMeta);

        return goldLifeCopy;
    }

    public ItemStack getLifeShardItem() {
        return lifeShardItem.clone();
    }

    public ItemStack getReviveShardItem() {
        return reviveShardItem.clone();
    }

    public ShapedRecipe getLifesRecipe() {
        return lifesRecipe;
    }

    public ShapedRecipe getReviveCardRecipe() {
        return reviveCardRecipe;
    }


}
