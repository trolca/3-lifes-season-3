package me.trololo11.lifespluginseason3.managers;

import me.trololo11.lifespluginseason3.LifesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.UUID;

public class RecipesManager {

    private ItemStack lifeItem;
    private ItemStack playerLifeItem;
    private ItemStack reviveCardItem;
    private ItemStack lifeShardItem;
    private ItemStack reviveShardItem;

    private ShapedRecipe lifesRecipe;
    private ShapedRecipe reviveCardRecipe;

    private LifesPlugin plugin = LifesPlugin.getPlugin();

    public void initalize(){
        createItems();
        createRecipes();
    }

    private void createItems(){
        ArrayList<String> lore = new ArrayList<>();

        lifeItem = new ItemStack(Material.SCUTE);
        playerLifeItem = new ItemStack(Material.SCUTE);
        reviveCardItem = new ItemStack(Material.SCUTE);
        lifeShardItem = new ItemStack(Material.GOLD_NUGGET);
        reviveShardItem = new ItemStack(Material.IRON_NUGGET);

        ItemMeta heartMeta = lifeItem.getItemMeta();
        heartMeta.setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Życie");
        lore.add(ChatColor.WHITE + "Użyj PPM by użyć!");
        heartMeta.setLore(lore);
        heartMeta.setCustomModelData(8760001);
        heartMeta.setLocalizedName("life");

        ItemMeta playerHeartMeta = playerLifeItem.getItemMeta();
        playerHeartMeta.setDisplayName(ChatColor.DARK_RED + ChatColor.BOLD.toString() + "Życie <player>");
        playerHeartMeta.setLore(lore);
        playerHeartMeta.setCustomModelData(8760002);
        playerHeartMeta.setLocalizedName("player_life");

        ItemMeta reviveMeta = reviveCardItem.getItemMeta();
        reviveMeta.setDisplayName(ChatColor.AQUA + ChatColor.BOLD.toString() + "Karta odrodzenia");
        lore.clear();
        lore.add(ChatColor.WHITE + "Zmień nazwe tego itemu na osobe");
        lore.add(ChatColor.WHITE + "którą chcesz wskrzesić.");
        lore.add(ChatColor.WHITE + "Później użyj PPM by og wskrzesić!");
        reviveMeta.setLore(lore);
        reviveMeta.setCustomModelData(8760005);
        reviveMeta.setLocalizedName("revive_card");

        ItemMeta heartShardMeta = lifeShardItem.getItemMeta();
        heartShardMeta.setDisplayName(ChatColor.RED + "Kawałek życia");
        heartShardMeta.setCustomModelData(8760001);
        heartShardMeta.setLocalizedName("heart_shard");

        ItemMeta reviveCardShardMeta = reviveCardItem.getItemMeta();
        reviveCardShardMeta.setDisplayName(ChatColor.AQUA + "Kawałek karty odrodzenia");
        reviveCardShardMeta.setCustomModelData(8760001);
        reviveCardShardMeta.setLocalizedName("revive_card");

        lifeItem.setItemMeta(heartMeta);
        reviveCardItem.setItemMeta(reviveMeta);
        playerLifeItem.setItemMeta(playerHeartMeta);
        lifeShardItem.setItemMeta(heartShardMeta);
        reviveShardItem.setItemMeta(reviveCardShardMeta);
    }

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

    public ItemStack getLifeItem() {
        ItemStack lifeItemCopy = lifeItem.clone();
        ItemMeta lifeMeta = lifeItemCopy.getItemMeta();
        lifeMeta.setLocalizedName(lifeMeta.getLocalizedName()+"_"+UUID.randomUUID());

        lifeItemCopy.setItemMeta(lifeMeta);

        return lifeItemCopy;
    }

    public ItemStack getPlayerLifeItem(Player player) {

        ItemStack playerLifeItemCopy = playerLifeItem.clone();
        ItemMeta playerLifeMeta = playerLifeItemCopy.getItemMeta();
        playerLifeMeta.setDisplayName(playerLifeMeta.getDisplayName().replace("<player>", player.getDisplayName()));
        playerLifeMeta.setLocalizedName(playerLifeMeta.getLocalizedName()+"_"+UUID.randomUUID());
        playerLifeItemCopy.setItemMeta(playerLifeMeta);

        return playerLifeItemCopy;
    }

    public ItemStack getReviveCardItem() {
        ItemStack reviveItemCopy = reviveCardItem.clone();
        ItemMeta reviveMeta = reviveItemCopy.getItemMeta();
        reviveMeta.setLocalizedName(reviveMeta.getLocalizedName()+"_"+UUID.randomUUID());

        reviveItemCopy.setItemMeta(reviveMeta);

        return reviveItemCopy;
    }

    public ItemStack getLifeShardItem() {
        return lifeShardItem;
    }

    public ItemStack getReviveShardItem() {
        return reviveShardItem;
    }

    public ShapedRecipe getLifesRecipe() {
        return lifesRecipe;
    }

    public ShapedRecipe getReviveCardRecipe() {
        return reviveCardRecipe;
    }


}
