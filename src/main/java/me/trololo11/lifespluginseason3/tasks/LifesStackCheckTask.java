package me.trololo11.lifespluginseason3.tasks;

import me.trololo11.lifespluginseason3.managers.RecipesManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * This task unstacks all of stacked lives or revive cards in a player inventory. <br>
 * It is used while crafting multiple lives at once
 */
public class LifesStackCheckTask extends BukkitRunnable {

    private Player p;
    private RecipesManager recipesManager;
    private boolean isLife=true;

    public LifesStackCheckTask(Player p, RecipesManager recipesManager){
        this.p = p;
        this.recipesManager = recipesManager;
    }

    @Override
    public void run() {

        //check if theres a life item which is more than 1 and usnstacks it
        //(to unstack it it creats an random uuid in the localized name)
        for(ItemStack item : p.getInventory().getContents()  ) {

            if (item != null && item.hasItemMeta() && item.getItemMeta().hasLocalizedName()
                    && (item.getItemMeta().getLocalizedName().equalsIgnoreCase("life_item") || item.getItemMeta().getLocalizedName().equalsIgnoreCase("revive_card"))
                    && item.getAmount() > 1) {
                isLife = item.getItemMeta().getLocalizedName().equalsIgnoreCase("life_item");

                p.getInventory().remove(item);

                for (int i = 0; i < item.getAmount(); i++) {
                    p.getInventory().addItem(isLife ? recipesManager.getLifeItem() : recipesManager.getReviveCardItem());
                }


            }
        }
    }

}
