package me.trololo11.lifespluginseason3.menus;

import me.trololo11.lifespluginseason3.managers.*;
import me.trololo11.lifespluginseason3.menus.questawardmenus.LifeShardAwardsMenu;
import me.trololo11.lifespluginseason3.menus.questawardmenus.WeeklyQuestsAwardsMenu;
import me.trololo11.lifespluginseason3.utils.Menu;
import me.trololo11.lifespluginseason3.utils.Quest;
import me.trololo11.lifespluginseason3.utils.QuestType;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Random;

/**
 * This menu shows all of the quests and their progress for a specified {@link QuestType}. <br>
 * If developer mode is turned on you can see go to the {@link QuestsStatisticsMenu}
 */
public class QuestsMenu extends Menu {

    private MainLifesMenu mainLifesMenu;
    private QuestManager questManager;
    private QuestsAwardsManager questsAwardsManager;
    private QuestType questTypeOfThisInv;
    private DatabaseManager databaseManager;
    private String invName;
    private RecipesManager recipesManager;
    private CardManager cardManager;
    private ArrayList<Quest> quests;

    /**
     * This menu shows all of the quests and their progress for a specified {@link QuestType}.
     * @param mainLifesMenu The main lifes menu instance
     * @param invName The name of this inventory you want to have. (It automatically translates color codes)
     * @param questType The {@link QuestType} to show the quests for.
     */
    public QuestsMenu(MainLifesMenu mainLifesMenu, String invName, QuestType questType, QuestManager questManager, QuestsAwardsManager questsAwardsManager,  RecipesManager recipesManager, DatabaseManager databaseManager, CardManager cardManager){
        this.mainLifesMenu = mainLifesMenu;
        this.questManager = questManager;
        this.questsAwardsManager = questsAwardsManager;
        this.questTypeOfThisInv = questType;
        this.recipesManager = recipesManager;
        this.invName = invName;
        this.databaseManager = databaseManager;
        this.cardManager = cardManager;
        quests = questManager.getCorrespondingQuestArray(questType);
    }

    @Override
    public String getMenuName() {
        return Utils.chat(invName);
    }

    @Override
    public int getSlots() {
        return 45;
    }

    @Override
    public void setMenuItems(Player player) {
        float progressFillPercentage = (float) questManager.getPlayerFinishedQuests(player, questTypeOfThisInv)/questManager.getCorrespondingQuestArray(questTypeOfThisInv).size() ;
        int progressInt = Math.round(progressFillPercentage*100);

        ItemStack darkFiller = Utils.createItem(Material.GRAY_STAINED_GLASS_PANE, "&7"+progressInt+"%", "filler");
        ItemStack filler = Utils.createItem(Material.WHITE_STAINED_GLASS_PANE, " ", "filler");
        ItemStack back = Utils.createItem(Material.RED_DYE, "&c&lPowrót", "back");
        ItemStack completed = Utils.createItem(Material.GREEN_WOOL, "&aSkończony!", "completed");
        ItemStack notCompleted = Utils.createItem(Material.RED_WOOL, "&cNie skończony!", "not-completed");
        ItemStack progress = Utils.createItem(Material.LIME_STAINED_GLASS_PANE, "&a"+progressInt+"%" , "progress");

        int fillSlots = (int) (7 * progressFillPercentage);

        for(int i=0; i < getSlots(); i++){

            if(i < 9) inventory.setItem(i, darkFiller);
            else inventory.setItem(i, filler);
        }

        for(int i = 1; i <= fillSlots; i++){
            inventory.setItem(i, progress);
        }
        inventory.setItem(0, getCustomCorrespondingItem(questTypeOfThisInv, player));
        inventory.setItem(8, back);

        for(int i=0; i < quests.size(); i++){

            Quest quest = quests.get(i);

            ItemStack questItem = new ItemStack(quest.getIcon());
            ItemMeta questMeta = questItem.getItemMeta();
            questMeta.setDisplayName(Utils.chat(quest.getName()));
            questMeta.setDisplayName(ChatColor.BOLD + questMeta.getDisplayName());

            if(mainLifesMenu.developerMode) questMeta.addEnchant(Enchantment.MENDING, 1, true);

            ArrayList<String> lore = new ArrayList<>(quest.getDescription());


            lore.add("");

            if(quest.getShowProgress() && !quest.hasFinished(player)){

                lore.add(quest.isHalfed() ? Utils.chat("&2&lProgress: "+quest.getPlayerProgress(player) + " / &2&m"+quest.getMaxProgress()+ "&a&l " + (quest.getMaxProgress()/2) )
                        : Utils.chat("&2&lProgress: " + quest.getPlayerProgress(player) + "/" + quest.getMaxProgress()));

            }else if(!quest.hasFinished(player)){
                lore.add(Utils.chat("&c&lNie skończony!"));
            }else{
                lore.add(Utils.chat("&a&lSkończony!"));
            }

            Utils.setPrivateName(questMeta, "quest-"+quest.getDatabaseName());
            questMeta.setLore(lore);
            questMeta.addItemFlags(ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);

            questItem.setItemMeta(questMeta);

            int slot; //sets the slot number for quests
            if (i < 4) slot = 9 * (i + 1);
            else if (i < 8) slot = (9 * (i - 3)) + 3;
            else slot = (9 * (i - 7)) + 6;

            if (slot >= 45) slot = 42;

            inventory.setItem(slot, quest.hasFinished(player) ? completed : notCompleted);
            inventory.setItem(slot + 1, questItem);
        }


    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        String privateName = Utils.getPrivateName(item);

        if(mainLifesMenu.developerMode && privateName.startsWith("quest-") && player.hasPermission("3lifes3.stats")){
            Quest clickedQuest = questManager.getQuestByDatabaseName(questTypeOfThisInv, privateName.substring(6));
            new QuestsStatisticsMenu(this, clickedQuest, databaseManager).open(player);
            return;
        }

        switch (item.getType()){

            case RED_DYE -> {
                if (!privateName.equalsIgnoreCase("back")) return;

                mainLifesMenu.open(player);
            }

            case GOLD_NUGGET -> {
                if(!privateName.equalsIgnoreCase("shard_life-menu")) return;

                new LifeShardAwardsMenu(this, recipesManager, questManager, questsAwardsManager).open(player);
            }

            case IRON_NUGGET -> {
                if(!privateName.equalsIgnoreCase("shard_revive_card-menu")) return;

                new WeeklyQuestsAwardsMenu(this, recipesManager, questManager, questsAwardsManager).open(player);
            }

            case PAPER -> {
                if(!privateName.equalsIgnoreCase("card-shard-take")) return;

                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                player.getInventory().addItem(cardManager.getRandomCard(new Random()).getCardItem());
                questsAwardsManager.setAwardsTakenForPlayer(player, QuestType.CARD, (byte) 1);
                setMenuItems(player);
            }

        }

    }


    private ItemStack getCustomCorrespondingItem(QuestType questType, Player player){

        ItemStack item = new ItemStack(Material.PINK_WOOL);

        switch (questType){

            case DAILY -> item = recipesManager.getLifeShardItem();
            case WEEKLY -> item = recipesManager.getReviveShardItem();
            case CARD ->{
                int playerTaken = questsAwardsManager.getAwardsTakenForPlayer(player, questType);

                if(playerTaken >= 1){
                    item = new ItemStack(Material.BARRIER);
                }else{
                    item = new ItemStack(Material.PAPER);
                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.setCustomModelData(8760002);
                    item.setItemMeta(itemMeta);
                }

                if(playerTaken < 1 && questManager.getPlayerFinishedQuests(player, questType) < questManager.getCorrespondingQuestArray(QuestType.CARD).size()) break;

                if(playerTaken >= 1){
                    ItemMeta blockedMeta = item.getItemMeta();
                    blockedMeta.setDisplayName(ChatColor.DARK_RED  + "To już zostało odebrane!");
                    Utils.setPrivateName(blockedMeta, "card-shard-blocked");
                    item.setItemMeta(blockedMeta);
                }else{
                    ItemMeta takeMeta = item.getItemMeta();
                    takeMeta.setDisplayName(ChatColor.BLUE + "Kliknij by mnie odebrać!");
                    takeMeta.setCustomModelData(8760002);
                    takeMeta.addEnchant(Enchantment.MENDING, 1, false);
                    takeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    Utils.setPrivateName(takeMeta, "card-shard-take");
                    item.setItemMeta(takeMeta);
                }

                return item;

            }

        }

        ItemMeta itemMeta = item.getItemMeta();
        byte howManyTaken = questsAwardsManager.getAwardsTakenForPlayer(player, questTypeOfThisInv);
        int questsPerAward = questsAwardsManager.getQuestsPerAward(questTypeOfThisInv);
        int playerFinishedQuests = questManager.getPlayerFinishedQuests(player, questTypeOfThisInv);
        int howMuchShouldTake = playerFinishedQuests >= questManager.getCorrespondingQuestArray(questTypeOfThisInv).size() ? (questsAwardsManager.getMaxAmountOfAwards(QuestType.DAILY)-1) : playerFinishedQuests/questsPerAward;

        if(howMuchShouldTake <= howManyTaken){
            itemMeta.setDisplayName((questType == QuestType.DAILY ? ChatColor.RED : ChatColor.AQUA) + "Postęp w questach");
        } else {
            itemMeta.addEnchant(Enchantment.MENDING, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.setDisplayName((questType == QuestType.DAILY ? ChatColor.RED : ChatColor.AQUA) + "Kliknij by odebrać nagrody!");
        }

        String privateName = Utils.getPrivateName(item) != null ? Utils.getPrivateName(item) : "";

        Utils.setPrivateName(itemMeta, privateName + "-menu");

        item.setItemMeta(itemMeta);
        return item;

    }

}
