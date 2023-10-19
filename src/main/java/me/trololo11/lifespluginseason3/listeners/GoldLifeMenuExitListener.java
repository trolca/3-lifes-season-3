package me.trololo11.lifespluginseason3.listeners;

import me.trololo11.lifespluginseason3.managers.RecipesManager;
import me.trololo11.lifespluginseason3.menus.GoldLifeGetMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

public class GoldLifeMenuExitListener implements Listener  {

    private RecipesManager recipesManager;

    public GoldLifeMenuExitListener(RecipesManager recipesManager){
        this.recipesManager = recipesManager;
    }

    @EventHandler
    public void onExit(InventoryCloseEvent e){
        InventoryHolder inventoryHolder = e.getInventory().getHolder();
        Player player = (Player) e.getPlayer();

        if(inventoryHolder instanceof GoldLifeGetMenu goldLifeGetMenu){

            if(goldLifeGetMenu.currLife == null) return;

            if(goldLifeGetMenu.getRandomizingStage() == 0) player.getInventory().addItem(goldLifeGetMenu.currLife);

            if(goldLifeGetMenu.getRandomizingStage() == 1) {
                player.getInventory().addItem(goldLifeGetMenu.currLife);

                Bukkit.getScheduler().cancelTask(goldLifeGetMenu.getThisGoldLifeRouletteTask().getTaskId());
                goldLifeGetMenu.getThisGoldLifeRouletteTask().stop();

                player.sendMessage(ChatColor.RED + "Właśnie wyszłeś z losowania na złote życie i niestety straciłeś karte :<");
                player.sendMessage(ChatColor.GRAY + ChatColor.ITALIC.toString() + "(Nastepnym razem nie wychodz)");

            }else if(goldLifeGetMenu.getRandomizingStage() == 2 && goldLifeGetMenu.hasWon()){

                player.getInventory().addItem(recipesManager.getGoldLifeItem());

            }


        }

    }
}
