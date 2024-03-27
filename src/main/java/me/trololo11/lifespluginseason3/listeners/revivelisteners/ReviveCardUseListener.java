package me.trololo11.lifespluginseason3.listeners.revivelisteners;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.managers.LifesManager;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;

/**
 * Listens for a revive card use. It unbans the player and gives them 2 lives.
 */
public class ReviveCardUseListener implements Listener {

    private LifesManager lifesManager;
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    public ReviveCardUseListener(LifesManager lifesManager){
        this.lifesManager = lifesManager;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e) throws SQLException {
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) return;
        ItemStack reviveCard = e.getItem();
        if(reviveCard == null) return;
        if(!reviveCard.hasItemMeta()) return;
        if(!reviveCard.getItemMeta().hasLocalizedName()) return;
        if(!reviveCard.getItemMeta().getLocalizedName().startsWith("revive_card")) return;
        String playerName = reviveCard.getItemMeta().getLocalizedName().split("_")[2].split(":")[1];
        Player player = e.getPlayer();


        //We check if the player is not targeting an anvil so they dont get the
        //error message when they are trying to rename the card
        Block playerTargetBlock = player.getTargetBlockExact(5);

        if(playerName.equalsIgnoreCase("null") && ( playerTargetBlock == null || playerTargetBlock.getType() != Material.ANVIL) ){
            player.sendMessage(ChatColor.RED + "Ta karta nie ma nikogo przypisanego!");
            player.sendMessage(ChatColor.RED + "Zmień nazwe w kowadle na gracza którego chcesz wskrzesić i użyj tej karty!");
            return;
        }else if(playerTargetBlock != null && playerTargetBlock.getType() == Material.ANVIL ){
            return;
        }

        OfflinePlayer deadPlayer = null;

        for(OfflinePlayer checkPlayer : lifesManager.getDeadPlayers()){
            if(checkPlayer.getName().equalsIgnoreCase(playerName)){
                deadPlayer = checkPlayer;
                break;
            }
        }

        if(deadPlayer == null){
            player.sendMessage(ChatColor.RED + "Osoba przypisana do tej karty jest just żywa! Prosze zmień przypisanego gracza.");
            return;
        }

        player.getInventory().setItemInMainHand(null);
        lifesManager.revivePlayer(deadPlayer, (byte) 2);

        plugin.getPlayerStats(player).revivedSomeone++;

        for(Player onlinePlayer : Bukkit.getOnlinePlayers()){

            onlinePlayer.sendMessage(Utils.chat("&e&l"+deadPlayer.getName()+" &6&lWŁAŚNIE ZOSTAŁ WSKRZESZONY!"));
            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
        }

    }
}
