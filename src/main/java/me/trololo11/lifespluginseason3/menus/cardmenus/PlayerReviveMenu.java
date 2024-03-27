package me.trololo11.lifespluginseason3.menus.cardmenus;

import me.trololo11.lifespluginseason3.managers.LifesManager;
import me.trololo11.lifespluginseason3.menus.cardmenus.confirmmenus.PlayerReviveConfirmMenu;
import me.trololo11.lifespluginseason3.utils.Menu;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Lists all of the dead players and allows you to revive them by giving up your life.
 */
public class PlayerReviveMenu extends Menu {

    private LifesManager lifesManager;

    public PlayerReviveMenu(LifesManager lifesManager){
        this.lifesManager = lifesManager;
    }

    @Override
    public String getMenuName() {
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
            if(i < 9 || i > 44) inventory.setItem(i, filler);
        }

        ArrayList<OfflinePlayer> deadPlayers = lifesManager.getDeadPlayers();

        for(int i=0; i < deadPlayers.size(); i++){


            OfflinePlayer deadPlayer = deadPlayers.get(i);

            SkullMeta headMeta = (SkullMeta) playerHead.getItemMeta();

            headMeta.setDisplayName(ChatColor.DARK_RED + deadPlayer.getName());
            headMeta.setOwningPlayer(deadPlayer);
            headMeta.setLocalizedName(deadPlayer.getUniqueId().toString());

            playerHead.setItemMeta(headMeta);

            inventory.setItem((i+9), playerHead);
        }

        inventory.setItem(8, back);
    }


    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();

        switch (item.getType()){

            case RED_DYE -> {
                if(!item.getItemMeta().getLocalizedName().equalsIgnoreCase("back")) return;

                player.closeInventory();
            }

            case PLAYER_HEAD -> {
                UUID deadUuid;
                try {
                    deadUuid = UUID.fromString(item.getItemMeta().getLocalizedName());
                }catch (IllegalArgumentException ex){
                    return;
                }
                OfflinePlayer deadPlayer = Bukkit.getOfflinePlayer(deadUuid);

                new PlayerReviveConfirmMenu(lifesManager, deadPlayer, this).open(player);

            }

        }


    }
}
