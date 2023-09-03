package me.trololo11.lifespluginseason3.menus;

import me.trololo11.lifespluginseason3.managers.LifesManager;
import me.trololo11.lifespluginseason3.utils.Menu;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class PlayerReviveMenu extends Menu {

    private LifesManager lifesManager;

    public PlayerReviveMenu(LifesManager lifesManager){
        this.lifesManager = lifesManager;
    }

    @Override
    public String getMenuName(Player p) {
        return ChatColor.AQUA + ChatColor.BOLD.toString() + "Wybierz osobe do wskrzeszenia";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void setMenuItems(Player p) {
        ItemStack filler = Utils.createItem(Material.WHITE_STAINED_GLASS_PANE, " ", "filler");
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        ItemStack back = Utils.createItem(Material.RED_DYE, "&c&lWyjdź", "back");

        for(int i = 0; i < getSlots(); i++){
            if(i < 9 || i % 8 == 0 || ( i + (i/9) ) % 9 == 0 || i > 44) inventory.setItem(i, filler);
        }

        ArrayList<OfflinePlayer> deadPlayers = lifesManager.getDeadPlayers();

        for(int i=0; i < deadPlayers.size(); i++){

            int currI = i+10+(addI(i));
            OfflinePlayer deadPlayer = deadPlayers.get(i);

            SkullMeta headMeta = (SkullMeta) playerHead.getItemMeta();

            headMeta.setDisplayName(ChatColor.RED + deadPlayer.getName());
            headMeta.setOwningPlayer(deadPlayer);

            playerHead.setItemMeta(headMeta);

            inventory.setItem(currI, playerHead);
        }

        inventory.setItem(8, back);
    }

    private int addI(int i){
        switch (i){

            case 16, 25, 34, 43 ->{
                return 2;
            }

            case 17, 26, 35, 44 ->{
                return 1;
            }

            default -> {
                return 0;
            }

        }
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

    }
}
