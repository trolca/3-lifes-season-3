package me.trololo11.lifespluginseason3.menus.cardmenus.confirmmenus;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.events.PlayerChangeLifesEvent;
import me.trololo11.lifespluginseason3.managers.LifesManager;
import me.trololo11.lifespluginseason3.menus.cardmenus.PlayerReviveMenu;
import me.trololo11.lifespluginseason3.utils.Menu;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;

public class PlayerReviveConfirmMenu extends Menu {

    private LifesManager lifesManager;
    private OfflinePlayer deadPlayer;
    private PlayerReviveMenu playerReviveMenu;
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    public PlayerReviveConfirmMenu(LifesManager lifesManager, OfflinePlayer deadPlayer, PlayerReviveMenu playerReviveMenu){
        this.lifesManager = lifesManager;
        this.deadPlayer = deadPlayer;
        this.playerReviveMenu = playerReviveMenu;
    }

    @Override
    public String getMenuName(Player player) {
        return ChatColor.GREEN + ChatColor.BOLD.toString() + "Wskrzesić "+ deadPlayer.getName() + "?";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void setMenuItems(Player player) {
        ItemStack filler = Utils.createItem(Material.WHITE_STAINED_GLASS_PANE, " ", "filler");
        ItemStack playerHead = Utils.createPlayerHead(deadPlayer, "&2&lCzy napewno chcesz wskrzesić "+ deadPlayer.getName() + "?", "text-confirm");
        ItemStack confirmButton = Utils.createItem(Material.LIME_STAINED_GLASS_PANE, "&aTak", "yes");
        ItemStack denyButton = Utils.createItem(Material.RED_STAINED_GLASS_PANE, "&cNie", "no");

        for(int i=0; i < 9; i++){
            inventory.setItem(i, filler);
        }

        inventory.setItem(2, confirmButton);
        inventory.setItem(6, denyButton);
        inventory.setItem(4, playerHead);
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();

        switch (item.getType()){

            case RED_STAINED_GLASS_PANE -> {
                if(!item.getItemMeta().getLocalizedName().equalsIgnoreCase("no")) return;

                playerReviveMenu.open(player);
            }

            case PLAYER_HEAD -> {
                if(!item.getItemMeta().getLocalizedName().equalsIgnoreCase("text-confirm")) return;

                player.stopAllSounds();
                player.playSound(player, "secret.szumiszumi", 1f, 1f);
            }

            case LIME_STAINED_GLASS_PANE -> {
                if(!item.getItemMeta().getLocalizedName().equalsIgnoreCase("yes")) return;

                try {
                    player.getInventory().setItemInMainHand(null);
                    Bukkit.getPluginManager().callEvent(new PlayerChangeLifesEvent(player, (byte) ( lifesManager.getPlayerLifes(player) - 1 ) ));
                    lifesManager.revivePlayer(deadPlayer, (byte) 1);

                    for(Player onlinePlayer : Bukkit.getOnlinePlayers()){

                        onlinePlayer.sendMessage(Utils.chat("&6&l" + player.getName().toUpperCase() + " WŁAŚNIE WSKRZESIŁ &e&l"+ deadPlayer.getName().toUpperCase() + " &6&lPOPRZEZ ODDANIE ŻYCIA"));
                        onlinePlayer.playSound(onlinePlayer, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
                    }

                    player.closeInventory();

                }catch (SQLException ex){
                    plugin.logger.warning(plugin.loggerPerfix+ " Error while reviving player using the give life card!");
                    if(plugin.isDetailedErrors()) ex.printStackTrace(System.out);
                }


            }

        }

    }
}
