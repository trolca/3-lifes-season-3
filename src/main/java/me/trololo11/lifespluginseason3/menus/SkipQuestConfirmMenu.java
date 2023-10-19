package me.trololo11.lifespluginseason3.menus;

import me.trololo11.lifespluginseason3.utils.Menu;
import me.trololo11.lifespluginseason3.utils.Quest;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SkipQuestConfirmMenu extends Menu {

    private Quest quest;
    private QuestSelectMenu questSelectMenu;

    public SkipQuestConfirmMenu(Quest quest, QuestSelectMenu questSelectMenu){
        this.quest = quest;
        this.questSelectMenu = questSelectMenu;
    }

    @Override
    public String getMenuName(Player player) {
        return ChatColor.RED + ChatColor.BOLD.toString() + "Czy napewno pominąć?";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void setMenuItems(Player player) {
        ItemStack filler = Utils.createItem(Material.WHITE_STAINED_GLASS_PANE, " ", "filler");
        ItemStack questItem = Utils.createItem(quest.getIcon(), "&aCzy napewno pominąć &f["+quest.getName()+"&f]?", "skip-label");
        ItemStack yes = Utils.createItem(Material.LIME_STAINED_GLASS_PANE, "&aTak", "yes");
        ItemStack no = Utils.createItem(Material.RED_STAINED_GLASS_PANE, "&cNie", "no");

        for(int i=0; i < getSlots(); i++){
            inventory.setItem(i, filler);
        }

        inventory.setItem(2, yes);
        inventory.setItem(4, questItem);
        inventory.setItem(7, no);
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();

        switch (item.getType()){

            case RED_STAINED_GLASS_PANE -> {
                if(!item.getItemMeta().getLocalizedName().equalsIgnoreCase("no")) return;

                questSelectMenu.open(player);
            }

            case LIME_STAINED_GLASS_PANE -> {
                if(!item.getItemMeta().getLocalizedName().equalsIgnoreCase("yes")) return;

                player.sendMessage(Utils.chat("&ePomyślnie pominięto quest &f["+quest.getName()+"&f]"));
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);

                quest.setSilentPlayerProgress(player, quest.getMaxProgress());
                player.getInventory().setItemInMainHand(null);
                player.closeInventory();
            }


        }

    }
}
