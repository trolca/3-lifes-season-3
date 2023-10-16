package me.trololo11.lifespluginseason3.menus;

import me.trololo11.lifespluginseason3.utils.Menu;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class GetLifeMenu extends Menu {

    private ItemStack currLife = null;
    int[] rouletteSlots = {3,4,5,12,14,21,22,23};

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

            System.out.println("i: "+ i);
            System.out.println("sus: " + ( i%3 != 0 && (i-1)%3 != 0 && (i-2)%3 != 0 ) );

            if(!Utils.containsIntArray(rouletteSlots, i)){
                inventory.setItem(i, filler);
            }else{
                inventory.setItem(i, isGold(i) ? get : notGet);
            }

        }

        if(currLife == null){
            inventory.setItem(13, setLife);
        }else {
            inventory.setItem(13, currLife);
        }



    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

    }

    public boolean isGold(int slot){

        switch (slot){

            case 3,4,5,14,23 ->{
                return true;
            }

            default -> {
                return false;
            }
        }

    }

}
