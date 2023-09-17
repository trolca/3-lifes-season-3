package me.trololo11.lifespluginseason3.menus;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.managers.QuestsAwardsManager;
import me.trololo11.lifespluginseason3.managers.RecipesManager;
import me.trololo11.lifespluginseason3.menus.questawardmenus.LifeShardAwardsMenu;
import me.trololo11.lifespluginseason3.utils.Menu;
import me.trololo11.lifespluginseason3.utils.Quest;
import me.trololo11.lifespluginseason3.utils.QuestType;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class QuestsMenu extends Menu {

    private MainLifesMenu mainLifesMenu;
    private QuestManager questManager;
    private QuestsAwardsManager questsAwardsManager;
    private QuestType questTypeOfThisInv;
    private String invName;
    private RecipesManager recipesManager;
    private ArrayList<Quest> quests;
    public QuestsMenu(MainLifesMenu mainLifesMenu, String invName, QuestType questType, QuestManager questManager, QuestsAwardsManager questsAwardsManager ,  RecipesManager recipesManager){
        this.mainLifesMenu = mainLifesMenu;
        this.questManager = questManager;
        this.questsAwardsManager = questsAwardsManager;
        this.questTypeOfThisInv = questType;
        this.recipesManager = recipesManager;
        this.invName = invName;
        quests = questManager.getCorrespondingQuestArray(questType);
    }

    @Override
    public String getMenuName(Player player) {
        return Utils.chat(invName);
    }

    @Override
    public int getSlots() {
        return 45;
    }

    @Override
    public void setMenuItems(Player player) {
        ItemStack darkFiller = Utils.createItem(Material.GRAY_STAINED_GLASS_PANE, " ", "filler");
        ItemStack filler = Utils.createItem(Material.WHITE_STAINED_GLASS_PANE, " ", "filler");
        ItemStack back = Utils.createItem(Material.RED_DYE, "&c&lPowrót", "back");
        ItemStack completed = Utils.createItem(Material.GREEN_WOOL, "&aSkończony!", "completed");
        ItemStack notCompleted = Utils.createItem(Material.RED_WOOL, "&cNie skończony!", "not-completed");
        ItemStack progress = Utils.createItem(Material.LIME_STAINED_GLASS_PANE, " ", "progress");

        float progressFillPercentage = (float) questManager.getPlayerFinishedQuests(player, questTypeOfThisInv)/questManager.getCorrespondingQuestArray(questTypeOfThisInv).size() ;

        int fillSlots = (int) (7 * progressFillPercentage);

        for(int i=0; i < getSlots(); i++){

            if(i < 9) inventory.setItem(i, darkFiller);
            else inventory.setItem(i, filler);
        }

        for(int i = 1; i <= fillSlots; i++){
            inventory.setItem(i, progress);
        }
        inventory.setItem(0, getCustomCorrespondingItem(questTypeOfThisInv));
        inventory.setItem(8, back);

        for(int i=0; i < quests.size(); i++){

            Quest quest = quests.get(i);

            ItemStack questItem = new ItemStack(quest.getIcon());

            ItemMeta questMeta = questItem.getItemMeta();

            questMeta.setDisplayName(Utils.chat(quest.getName()));

            ArrayList<String> lore = new ArrayList<>(quest.getDescription());

            //fixed it less go
            lore.add("");
            lore.add( quest.hasFinished(player) ?
                    Utils.chat("&a&lSkończony!") :
                        quest.getShowProgress() ?
                        Utils.chat("&2&lProgress: " + quest.getPlayerProgress(player) + "/" + quest.getMaxProgress())  :
                    Utils.chat("&c&lNie skończony!")
                    );

            questMeta.setLocalizedName(quest.getDatabaseName());
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

        switch (item.getType()){

            case RED_DYE -> {
                if (!item.getItemMeta().getLocalizedName().equalsIgnoreCase("back")) return;

                mainLifesMenu.open(player);
            }

            case GOLD_NUGGET -> {
                if(!item.getItemMeta().getLocalizedName().equalsIgnoreCase("shard_life-menu")) return;

                new LifeShardAwardsMenu(this, recipesManager, questManager, questsAwardsManager).open(player);
            }

        }

    }


    private ItemStack getCustomCorrespondingItem(QuestType questType){

        ItemStack item = new ItemStack(Material.PINK_WOOL);

        switch (questType){

            case DAILY -> item = recipesManager.getLifeShardItem();
            case WEEKLY -> item = recipesManager.getReviveShardItem();
            case CARD -> item = new ItemStack(Material.GHAST_TEAR);

        }

        ItemMeta itemMeta = item.getItemMeta();

        String localizedName = itemMeta.hasLocalizedName() ? itemMeta.getLocalizedName() : "";

        itemMeta.setLocalizedName(localizedName + "-menu");

        item.setItemMeta(itemMeta);
        return item;

    }
}
