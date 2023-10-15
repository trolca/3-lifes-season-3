package me.trololo11.lifespluginseason3.menus;

import me.trololo11.lifespluginseason3.utils.Menu;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GetLifeMenu extends Menu {
    @Override
    public String getMenuName(Player player) {
        return ChatColor.YELLOW + ChatColor.BOLD.toString() + "Hazard";
    }

    @Override
    public int getSlots() {
        return 3*9;
    }

    @Override
    public void setMenuItems(Player player) {
        ItemStack filler = Utils.createItem(Material.GRAY_STAINED_GLASS_PANE, " ", "filler");
        ItemStack notGet = Utils.createItem(Material.WHITE_STAINED_GLASS_PANE, " ", "not-get");
        ItemStack get = Utils.createItem(Material.YELLOW_STAINED_GLASS_PANE, " ", "get");
        ItemStack setLife = Utils.createItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, "&fWybierz życie ze swojego ekwipunku", "set-item");

        for(int i=0; i < getSlots(); i++){

        }
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

    }
}
