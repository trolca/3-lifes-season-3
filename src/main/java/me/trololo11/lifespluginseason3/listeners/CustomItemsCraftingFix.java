package me.trololo11.lifespluginseason3.listeners;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.managers.RecipesManager;
import me.trololo11.lifespluginseason3.tasks.LifesStackCheckTask;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class CustomItemsCraftingFix implements Listener {

    private RecipesManager recipesManager;
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    public CustomItemsCraftingFix(RecipesManager recipesManager){
        this.recipesManager = recipesManager;
    }

    @EventHandler
    public void onCraft(CraftItemEvent e){

        if(!(e.getWhoClicked() instanceof Player)) return;

        ItemStack currItem;
        Player player = (Player) e.getWhoClicked();


        if(e.getRecipe().getResult().equals(recipesManager.getLifesRecipe().getResult())){
            currItem = recipesManager.getLifeItem();
        }else if(e.getRecipe().getResult().equals(recipesManager.getReviveCardRecipe().getResult())){
            currItem = recipesManager.getReviveCardItem();
        }else{
            return;
        }

        if(e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT){

            if(Utils.getMaxCraftAmount(e.getInventory()) > Utils.getEmptySpaceInInv(player.getInventory())){
                player.sendMessage(ChatColor.RED + "Upewnij sie ze masz wystarczająco miejsca dla tych przedmiotów!");
                e.setCancelled(true);
                return;
            }

            LifesStackCheckTask lifesStackCheckTask = new LifesStackCheckTask(player, recipesManager);
            lifesStackCheckTask.runTaskLater(plugin, 1L);

        }else{
            e.setCurrentItem(currItem);
        }


    }
}
