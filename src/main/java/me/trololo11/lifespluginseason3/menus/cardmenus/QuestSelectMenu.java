package me.trololo11.lifespluginseason3.menus.cardmenus;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * This menu shows all of the quests that are in the QuestType,
 * and allows you to select them and do something with them.
 */
public class QuestSelectMenu extends Menu {

    private final String title;
    private QuestType questType;
    private QuestManager questManager;
    private final ArrayList<Quest> questSkip;
    private Function<Quest, Boolean> questFilterFunction = null;
    private QuestSelectFunction questSelectFunction;


    /**
     * This menu shows you all of the quests that you specified and
     * allows you to select them.
     * @param title The title of this menu
     * @param questType The quest type of the quests that are gonna show in this menu
     * @param questManager The questManager class
     * @param questSelectFunction A lambda function that runs after a player select a specific quest
     * @param questFilterFunction A lambda function that runs at every iteration of setting the quests in the menu.
     *                            It should be used to filter quests. When it returns false the quest doesn't show in the menu
     */
    @SuppressWarnings("unchecked")
    public QuestSelectMenu(String title, QuestType questType, QuestManager questManager, QuestSelectFunction questSelectFunction, Function<Quest, Boolean> questFilterFunction){
        this.questSelectFunction = questSelectFunction;
        this.title = title;
        this.questManager = questManager;
        this.questType = questType;
        //We are 100% sure that it's gonna be an array list of quest so the warning was useless lol
        if(questType != null) questSkip = (ArrayList<Quest>) questManager.getCorrespondingQuestArray(questType).clone();
        else questSkip = (ArrayList<Quest>) questManager.getAllActiveQuests().clone();
        this.questFilterFunction = questFilterFunction;

    }

    /**
     * This menu shows you all of the quests that you specified and
     * allows you to select them.
     * @param title The title of this menu
     * @param questType The quest type of the quests that are gonna show in this menu
     * @param questManager The questManager class
     * @param questSelectFunction A lambda function that runs after a player select a specific quest
     */
    @SuppressWarnings("unchecked")
    public QuestSelectMenu(String title, QuestType questType, QuestManager questManager, QuestSelectFunction questSelectFunction){
        this.questSelectFunction = questSelectFunction;
        this.title = title;
        this.questManager = questManager;
        this.questType = questType;
        //We are 100% sure that it's gonna be an array list of quest so the warning was useless lol
        if(questType != null) questSkip = (ArrayList<Quest>) questManager.getCorrespondingQuestArray(questType).clone();
        else questSkip = (ArrayList<Quest>) questManager.getAllActiveQuests().clone();


    }

    @Override
    public String getMenuName(Player player) {
        return Utils.chat(title);
    }

    @Override
    public int getSlots() {
        return 45;
    }
    @Override
    public void setMenuItems(Player player) {

        ItemStack darkFiller = Utils.createItem(Material.GRAY_STAINED_GLASS_PANE, " ", "filler");
        ItemStack back = Utils.createItem(Material.RED_DYE, "&c&lWyjdź", "back");

        questSkip.removeIf(quest -> quest.hasFinished(player));

        for(int i=0; i < 8; i++){
            inventory.setItem(i, darkFiller);
        }

        int addI = -1;
        int skippedQuests = 0;

        for(int i=0; i < questSkip.size(); i++){
            Quest quest = questSkip.get(i);

            if(questFilterFunction != null && !questFilterFunction.apply(quest)){
                skippedQuests++;
                continue;
            }

            ItemStack questItem = Utils.createItem(quest.getIcon(), quest.getName(), quest.getDatabaseName());

            ItemMeta questMeta = questItem.getItemMeta();

            ArrayList<String> addLore = new ArrayList<>();
            addLore.addAll(quest.getDescription());
            addLore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + "(Kliknij by pominąć)");
            if(questType == null) addLore.add(ChatColor.GOLD + ChatColor.ITALIC.toString() + "("+getQuestTypeName(quest.getQuestType()) + ")");
            addLore.add("");
            addLore.add( quest.getShowProgress() ? Utils.chat("&2&l(Twój progress: "+quest.getPlayerProgress(player)+ "/"+quest.getMaxProgress()+")") : Utils.chat("&c&l(Nie skończony!)") );
            questMeta.setLore(addLore);
            questMeta.addItemFlags(ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
            questMeta.setLocalizedName("quest_"+quest.getDatabaseName());

            questItem.setItemMeta(questMeta);

            if((i-skippedQuests) % 4 == 0) addI++;

            inventory.setItem(( addI + (9 * ( ( ((i-skippedQuests) - 4 * addI )+ 1) ) ) )  , questItem );
        }

        inventory.setItem(8, back);


    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();
        String localizedName = item.getItemMeta().getLocalizedName();

        if(item.getType() == Material.RED_DYE && localizedName.equalsIgnoreCase("back")){
            player.closeInventory();
            return;
        }

        if(localizedName.startsWith("quest")){
             Quest quest = questType != null ? questManager.getQuestByDatabaseName(questType ,localizedName.substring(6)) :
                     questManager.getQuestByDatabaseName(localizedName.substring(6));

            if(quest == null){
                player.closeInventory();
                player.sendMessage(ChatColor.RED + "Error while getting the quests! Try again");
                return;
            }

            questSelectFunction.run(quest, player, this);
        }

    }

    private String getQuestTypeName(QuestType questType){

        switch (questType){

            case DAILY -> {
                return "dzienny";
            }

            case WEEKLY -> {
                return "tygodniowy";
            }

            case CARD -> {
                return "do karty";
            }

            default -> {
                return "quest";
            }

        }

    }

}
