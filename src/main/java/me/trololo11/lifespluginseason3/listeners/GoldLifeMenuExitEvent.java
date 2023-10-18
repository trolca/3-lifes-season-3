package me.trololo11.lifespluginseason3.listeners;

import me.trololo11.lifespluginseason3.managers.RecipesManager;
import me.trololo11.lifespluginseason3.menus.GetLifeMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GoldLifeMenuExitEvent implements Listener  {

    private RecipesManager recipesManager;

    public GoldLifeMenuExitEvent(RecipesManager recipesManager){
        this.recipesManager = recipesManager;
    }

    @EventHandler
    public void onExit(InventoryCloseEvent e){
        InventoryHolder inventoryHolder = e.getInventory().getHolder();
        Player player = (Player) e.getPlayer();

        if(inventoryHolder instanceof GetLifeMenu getLifeMenu){

            if(getLifeMenu.currLife == null) return;

            if(getLifeMenu.getRandomizingStage() == 0) player.getInventory().addItem(getLifeMenu.currLife);

            if(getLifeMenu.getRandomizingStage() == 1) {
                player.getInventory().addItem(getLifeMenu.currLife);

                Bukkit.getScheduler().cancelTask(getLifeMenu.getThisGoldLifeRouletteTask().getTaskId());
                getLifeMenu.getThisGoldLifeRouletteTask().stop();

                player.sendMessage(ChatColor.RED + "Właśnie wyszłeś z losowania na złote życie i niestety straciłeś karte :<");
                player.sendMessage(ChatColor.GRAY + ChatColor.ITALIC.toString() + "(Nastepnym razem nie wychodz)");

            }else if(getLifeMenu.getRandomizingStage() == 2 && getLifeMenu.hasWon()){

                player.getInventory().addItem(recipesManager.getGoldLifeItem());

            }


        }

    }
}
