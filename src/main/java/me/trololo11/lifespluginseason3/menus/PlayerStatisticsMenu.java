package me.trololo11.lifespluginseason3.menus;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.utils.Menu;
import me.trololo11.lifespluginseason3.utils.PlayerStats;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerStatisticsMenu extends Menu {

    private Player player;
    private MainLifesMenu mainLifesMenu;
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    public PlayerStatisticsMenu(Player player, MainLifesMenu mainLifesMenu){
        this.player = player;
        this.mainLifesMenu = mainLifesMenu;
    }

    @Override
    public String getMenuName() {
        return ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "Statystyki "+ player.getName();
    }

    /*
         8760001 - normal life
         8760002 - player life
         8760003 - gold life
         8760005 - revive card
     */

    @Override
    public int getSlots() {
        return 45;
    }

    @Override
    public void setMenuItems(Player player) {
        PlayerStats playerStats = plugin.getPlayerStats(player);
        ItemStack filler = Utils.createItem(Material.WHITE_STAINED_GLASS_PANE, " ", "filler");
        ItemStack darkFiller = Utils.createItem(Material.GRAY_STAINED_GLASS_PANE, " ", "filler");
        ItemStack playerHead = Utils.createPlayerHead(player, "&2"+player.getName(), "player-head");
        ItemStack peopleKilled = Utils.createItem(Material.IRON_SWORD, "&4&lGracze zabici: "+ playerStats.kills,
                "people-killed", "&fIlość zabitych przez ciebie graczy");
        ItemStack lifesCrafted = Utils.createCustomModelItem(Material.SCUTE, 8760001,
                "&c&lStworzone życia: "+ playerStats.lifesCrafted, "lifes-crafted",
                "&fIlość stworzonych żyć przez ciebie!");
        ItemStack revivesCrafted = Utils.createCustomModelItem(Material.SCUTE, 8760005, "&b&lKarty odrodzenia stworzone: "+ playerStats.revivesCrafted,
                "revies-crafted", "&fIlość wszystkich stworzonych kart odrodzeń", "&fprzez ciebie!");
        ItemStack revivedSomeone = Utils.createItem(Material.GOLDEN_APPLE, "&b&lOsoby wskrzeszone: " + playerStats.revivedSomeone,
                "revived-someone", "&fIlość wskrzeszonych przez ciebie", "&fgraczy");
        ItemStack allQuests = Utils.createItem(Material.NETHERITE_PICKAXE, "&6&lWszystkie questy: " + playerStats.allQuestCompleted,
                "all-quests", "&fIlość wszystkich twoich skończonych questów");
        ItemStack dailyQuests = Utils.createItem(Material.DIAMOND, "&c&lDzienne questy: " + playerStats.dailyQuestCompleted,
                "daily-quests", "&fIlość wszystkich skończonych questów dziennych");
        ItemStack weeklyQuests = Utils.createItem(Material.NETHERITE_INGOT, "&e&lTygodniowe questy: "+ playerStats.weeklyQuestCompleted,
                "weekly-quests", "&fIlość wszystkich skończonych questów tygodniowych ");
        ItemStack cardQuests = Utils.createItem(Material.PAPER, "&b&lQuesty do karty: "+ playerStats.cardQuestCompleted,
                "card-quests", "&fIlość wszystkich skonczonych questów", "&fdo losowej karty");
        ItemStack goldLifesUsed = Utils.createCustomModelItem(Material.SCUTE, 8760004, "&e&lUżyte złote życia: "+ playerStats.goldLifesUsed,
                "gold-lifes-used", "&fWszystkie złote życie użyte przez ciebie!");
        ItemStack cardsUsed = Utils.createCustomModelItem(Material.PAPER, 2137, "&9&lKarty użyte: "+ playerStats.cardsUsed,
                "cards-used", "&fWszystkie użyte przez ciebie karty!");
        ItemStack dailyShardsReedemed = Utils.createCustomModelItem(Material.GOLD_NUGGET, 8760001,
                "&4&lOdebrane kawałki żyć: " + playerStats.dailyShardsReedemed, "daily-shard-reedemed",
                "&fWszystkie kawałki życia które zostały", "&fodebrane przez ciebie");
        ItemStack weeklyShardsReedemed = Utils.createCustomModelItem(Material.IRON_NUGGET, 8760001,
                "&3&lOdebrane kawałki karty odrodzenia: "+ playerStats.weeklyShardReedemed, "weekly-shards-reedemed",
                "&fWszystkie kawałki odrodzenia", "&fktóre odebrałeś");
        ItemStack backItem = Utils.createItem(Material.RED_DYE, "&c&lPowrót", "back");

        for(int i=0; i < 9; i++){
            inventory.setItem(i, darkFiller);
        }

        for(int i=9; i < getSlots(); i++){
            inventory.setItem(i, filler);
        }

        inventory.setItem(0, playerHead);
        inventory.setItem(9, peopleKilled);
        inventory.setItem(18, lifesCrafted);
        inventory.setItem(27, revivesCrafted);
        inventory.setItem(36, revivedSomeone);
        inventory.setItem(13, allQuests);
        inventory.setItem(22, dailyQuests);
        inventory.setItem(31, weeklyQuests);
        inventory.setItem(40, cardQuests);
        inventory.setItem(17, goldLifesUsed);
        inventory.setItem(26, cardsUsed);
        inventory.setItem(35, dailyShardsReedemed);
        inventory.setItem(44, weeklyShardsReedemed);
        inventory.setItem(8, backItem);




    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();

        if(item.getType() == Material.RED_DYE && item.getItemMeta().getLocalizedName().equalsIgnoreCase("back")){
            mainLifesMenu.open(player);
        }

    }
}
