package me.trololo11.lifespluginseason3.menus;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.Menu;
import me.trololo11.lifespluginseason3.utils.Quest;
import me.trololo11.lifespluginseason3.utils.QuestType;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SkipQuestMenu extends Menu {

    private final String title;
    private QuestType questType;
    private QuestManager questManager;
    private final ArrayList<Quest> questSkip;


    @SuppressWarnings("unchecked")
    public SkipQuestMenu(String title, QuestType questType, QuestManager questManager){
        this.title = title;
        this.questManager = questManager;
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

        for(int i=0; i < questSkip.size(); i++){
            Quest quest = questSkip.get(i);

            ItemStack questItem = Utils.createItem(quest.getIcon(), quest.getName(), quest.getDatabaseName());

            ItemMeta questMeta = questItem.getItemMeta();

            ArrayList<String> addLore = new ArrayList<>();
            addLore.addAll(quest.getDescription());
            addLore.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + "(Kliknij by pominąć)");
            addLore.add("");
            addLore.add( quest.getShowProgress() ? Utils.chat("&2&l(Twój progress: "+quest.getPlayerProgress(player)+ "/"+quest.getMaxProgress()+")") : Utils.chat("&c&l(Nie skończony!)") );
            questMeta.setLore(addLore);
            questMeta.addItemFlags(ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
            questMeta.setLocalizedName("quest_"+quest.getDatabaseName());

            questItem.setItemMeta(questMeta);

            if(i % 4 == 0) addI++;

            inventory.setItem(( addI + (9 * ( ( ( i - 4 * addI ) + 1) ) ) )  , questItem );
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
            Quest quest = getQuestFromDatabaseName(localizedName.substring(6));

            if(quest == null){
                player.closeInventory();
                player.sendMessage(ChatColor.RED + "Error while getting the quests! Try again");
                return;
            }

            new SkipQuestConfirmMenu(quest, this).open(player);
        }

    }

    private Quest getQuestFromDatabaseName(String name){

        for(Quest quest : questSkip){
            if(quest.getDatabaseName().equalsIgnoreCase(name)) return quest;
        }

        return null;
    }
}
