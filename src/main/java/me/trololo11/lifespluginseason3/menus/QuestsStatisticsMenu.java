package me.trololo11.lifespluginseason3.menus;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.managers.DatabaseManager;
import me.trololo11.lifespluginseason3.utils.Menu;
import me.trololo11.lifespluginseason3.utils.Quest;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.*;

public class QuestsStatisticsMenu extends Menu  {

    private QuestsMenu questsMenu;
    private Quest quest;

    private LinkedHashMap<OfflinePlayer, Integer> playerQuestProgressMap;

    public QuestsStatisticsMenu(QuestsMenu questsMenu, Quest quest, DatabaseManager databaseManager){
        LifesPlugin plugin = LifesPlugin.getPlugin();

        this.questsMenu = questsMenu;
        this.quest = quest;

        HashMap<OfflinePlayer, Integer> playerQuestProgressHashMap;

        try {
            playerQuestProgressHashMap = databaseManager.getProgressOfAllOfflinePlayers(quest);
        } catch (SQLException e) {
            plugin.logger.severe("[3LifesS3] Error while getting all of the offline player progress!");
            plugin.logger.severe("[3LifesS3] Check your database connection!");
            if(plugin.isDetailedErrors()) e.printStackTrace(System.out);

            return;
        }

        //Adds to the hash map that holds all of the player's progress the progress of online people since the database
        //function only adds offline players
        for(Player player : Bukkit.getOnlinePlayers()){
            playerQuestProgressHashMap.put(player, quest.getPlayerProgress(player));
        }

        //We setup the list off all of the entires that are in the hashMap
        List<Map.Entry<OfflinePlayer, Integer> > playerProgress = new LinkedList<>(playerQuestProgressHashMap.entrySet());

        //We sort them by their value
        playerProgress.sort((o1, o2) -> o2.getValue() - o1.getValue());

        playerQuestProgressMap = new LinkedHashMap<>();

        //We add them to the real hash map so we have them sorted
        for(Map.Entry<OfflinePlayer, Integer> entry : playerProgress){
            playerQuestProgressMap.put(entry.getKey(), entry.getValue());
        }


    }

    @Override
    public String getMenuName(Player player) {
        return Utils.chat("&eStatystyki &f["+quest.getName()+"&f]");
    }

    @Override
    public int getSlots() {
        return 6*9;
    }

    @Override
    public void setMenuItems(Player player) {
        ItemStack filler = Utils.createItem(Material.WHITE_STAINED_GLASS_PANE, " ", "filler");
        ItemStack back = Utils.createItem(Material.RED_DYE, "&c&lPowrót", "back");

        for(int i=0; i < 8; i++){
            inventory.setItem(i, filler);
        }

        inventory.setItem(8, back);

        int i = 0;
        //int addI = 0;

        //we dont do a for loop bcs a hash map returns a Set
        for(OfflinePlayer questPlayer : playerQuestProgressMap.keySet()){
            int progress = playerQuestProgressMap.get(questPlayer);

            ItemStack playerHead = Utils.createPlayerHead(questPlayer, Utils.chat(
                    (player.getUniqueId() == questPlayer.getUniqueId() ? "&2&l" : "&6&l") + (i+1)+". "+
                    questPlayer.getName() + "&a&l " +
                            (progress >= quest.getMaxProgress() ? "Skończył!" : progress + "/" + quest.getMaxProgress()))
                    , questPlayer.getName());

            inventory.setItem((i+1)*9-( (i/5) * 45 )   , playerHead );

            i++;

        }

    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();

        if(item.getType() == Material.RED_DYE){
            if(!item.getItemMeta().getLocalizedName().equalsIgnoreCase("back")) return;

            questsMenu.open(player);

        }

    }
}
