package me.trololo11.lifespluginseason3.menus;

import me.trololo11.lifespluginseason3.managers.DatabaseManager;
import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.managers.QuestsAwardsManager;
import me.trololo11.lifespluginseason3.managers.RecipesManager;
import me.trololo11.lifespluginseason3.utils.Menu;
import me.trololo11.lifespluginseason3.utils.QuestType;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MainLifesMenu extends Menu {

    private QuestManager questManager;
    private RecipesManager recipesManager;
    private QuestsAwardsManager questsAwardsManager;
    private DatabaseManager databaseManager;

    public boolean developerMode = false;

    public MainLifesMenu(QuestManager questManager, RecipesManager recipesManager, QuestsAwardsManager questsAwardsManager, DatabaseManager databaseManager){
        this.questManager = questManager;
        this.recipesManager = recipesManager;
        this.questsAwardsManager = questsAwardsManager;
        this.databaseManager = databaseManager;
    }

    @Override
    public String getMenuName(Player player) {
        return ChatColor.RED + ChatColor.BOLD.toString() + "3Lifes menu";
    }

    @Override
    public int getSlots() {
        return 36;
    }

    @Override
    public void setMenuItems(Player player) {

        boolean hasFinishedWeekly = questManager.getPlayerFinishedQuests(player, QuestType.WEEKLY) >= questManager.getCorrespondingQuestArray(QuestType.WEEKLY).size();
        boolean hasFinishedDaily = questManager.getPlayerFinishedQuests(player, QuestType.DAILY) >= questManager.getCorrespondingQuestArray(QuestType.DAILY).size();
        boolean hasFinishedCard = questManager.getPlayerFinishedQuests(player, QuestType.CARD) >= questManager.getCorrespondingQuestArray(QuestType.CARD).size();

        ItemStack whiteFiller = Utils.createItem(Material.WHITE_STAINED_GLASS_PANE, " ", "filler");
        ItemStack blackFiller = Utils.createItem(Material.BLACK_STAINED_GLASS_PANE, " ", "filler");
        ItemStack devMode = developerMode ? Utils.createEnchantedItem(Material.GLOWSTONE_DUST, "&a&lDeveloper mode active", "dev-mode", Enchantment.MENDING) :
                Utils.createItem(Material.REDSTONE, "&cDeveloper mode", "dev-mode");

        ItemStack dailyQuests = Utils.createItem(Material.ENCHANTED_BOOK, "&c&lQuesty dzienne", "daily",
                "&fQuesty które sie restartują codziennie!", "&f&o(Dostajesz z nich kawałek życia)", "",
                "&bPozostały czas: "+ questManager.getQuestPageTimeText(QuestType.DAILY),
                hasFinishedDaily ? "&a&lSkończone!" : "&2&lProgress: "+ questManager.getPlayerFinishedQuests(player, QuestType.DAILY) + "/" + questManager.getActiveDailyQuests().size());

        ItemStack weeklyQuests = Utils.createItem(Material.ENCHANTED_BOOK, "&e&lQuesty tygodniowe", "weekly",
                "&fQuesty które sie restartują co tydzień!", "&f&o(Dostajesz z nich kawałek karty odrodzenia)", "",
                "&bPozostały czas: " + questManager.getQuestPageTimeText(QuestType.WEEKLY),
                hasFinishedWeekly ? "&a&lSkończone!" : "&2&lProgress: "+ questManager.getPlayerFinishedQuests(player, QuestType.WEEKLY) + "/" + questManager.getActiveWeeklyQuests().size());

        ItemStack cardQuests = Utils.createItem(Material.PAPER, "&b&lQuesty do karty", "card",
                "&fQuesty za które dostajesz randomową karte ulepszenia!", "&f&o(Restartują sie co tydzień)", "",
                "&bPozostały czas: "+ questManager.getQuestPageTimeText(QuestType.CARD),
                hasFinishedCard ? "&a&lSkończone!" : "&2&lProgress: "+ questManager.getPlayerFinishedQuests(player, QuestType.CARD) + "/" + questManager.getActiveCardQuests().size());

        ItemStack statistics = Utils.createPlayerHead(player, "&2Statystyki "+ player.getName(), "statistics");
        ItemStack back = Utils.createItem(Material.RED_DYE, "&c&lPowrót", "back");


        for(int i=0; i < getSlots(); i++){
            if(i < 9) inventory.setItem(i, blackFiller);
            else inventory.setItem(i, whiteFiller);
        }

        inventory.setItem(4, statistics);
        if(player.hasPermission("3lifes3.stats")) inventory.setItem(7, devMode);
        inventory.setItem(8, back);
        inventory.setItem(19, dailyQuests);
        inventory.setItem(22, cardQuests);
        inventory.setItem(25, weeklyQuests);

    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();


        switch (item.getType()){

            case ENCHANTED_BOOK -> {
                QuestType questType;
                try {
                    questType = QuestType.valueOf(item.getItemMeta().getLocalizedName().toUpperCase());
                }catch (IllegalArgumentException ex){
                    return;
                }

                new QuestsMenu(this, questType == QuestType.DAILY ? "&c&lQuesty dzienne" : "&e&lQuesty tygodniowe", questType, questManager,questsAwardsManager, recipesManager, databaseManager).open(player);
            }

            case PAPER -> {
                if(!item.getItemMeta().getLocalizedName().equalsIgnoreCase("card")) return;
                new QuestsMenu(this, "&f&lQuesty do karty", QuestType.CARD, questManager,questsAwardsManager, recipesManager, databaseManager).open(player);
            }

            case RED_DYE ->{
                if(!item.getItemMeta().getLocalizedName().equalsIgnoreCase("back")) return;
                player.closeInventory();
            }

            case REDSTONE, GLOWSTONE_DUST -> {
                if(!item.getItemMeta().getLocalizedName().equalsIgnoreCase("dev-mode")) return;

                if(!player.hasPermission("3lifes3.stats")){
                    player.sendMessage(ChatColor.RED + "How did you what?!!?!?");
                    player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                    player.closeInventory();
                    return;
                }

                developerMode = !developerMode;
                setMenuItems(player);

            }



        }

    }

}
