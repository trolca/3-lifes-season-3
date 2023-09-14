package me.trololo11.lifespluginseason3.menus;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.Menu;
import me.trololo11.lifespluginseason3.utils.Quest;
import me.trololo11.lifespluginseason3.utils.QuestType;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class QuestsMenu extends Menu {

    private MainLifesMenu mainLifesMenu;
    private String invName;
    private ArrayList<Quest> quests;
    public QuestsMenu(MainLifesMenu mainLifesMenu, String invName, QuestType questType, QuestManager questManager){
        this.mainLifesMenu = mainLifesMenu;
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

        for(int i=0; i < getSlots(); i++){

            if(i < 9) inventory.setItem(i, darkFiller);
            else inventory.setItem(i, filler);
        }

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

        if (item.getType() == Material.RED_DYE) {
            if(!item.getItemMeta().getLocalizedName().equalsIgnoreCase("back")) return;

            mainLifesMenu.open(player);

        }

    }
}
