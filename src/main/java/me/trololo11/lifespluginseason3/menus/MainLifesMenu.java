package me.trololo11.lifespluginseason3.menus;

import me.trololo11.lifespluginseason3.utils.Menu;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MainLifesMenu extends Menu {


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
        ItemStack whiteFiller = Utils.createItem(Material.WHITE_STAINED_GLASS_PANE, " ", "filler");
        ItemStack blackFiller = Utils.createItem(Material.BLACK_STAINED_GLASS_PANE, " ", "filler");
        ItemStack dailyQuests = Utils.createItem(Material.ENCHANTED_BOOK, "&c&lQuesty dzienne", "daily-quests");
        ItemStack weeklyQuests = Utils.createItem(Material.ENCHANTED_BOOK, "&e&lQuesty tygodniowe", "weekly-quests");
        ItemStack cardQuests = Utils.createItem(Material.PAPER, "&b&lQuesty do karty", "card-quests");
        ItemStack statistics = Utils.createPlayerHead(player, "&2Statystyki "+ player.getName(), "statistics");
        ItemStack back = Utils.createItem(Material.RED_DYE, "&c&lPowrót", "back");

        for(int i=0; i < getSlots(); i++){
            if(i < 9) inventory.setItem(i, blackFiller);
            else inventory.setItem(i, whiteFiller);
        }

        inventory.setItem(4, statistics);
        inventory.setItem(8, back);
        inventory.setItem(19, dailyQuests);
        inventory.setItem(22, cardQuests);
        inventory.setItem(25, weeklyQuests);

    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

    }
}
