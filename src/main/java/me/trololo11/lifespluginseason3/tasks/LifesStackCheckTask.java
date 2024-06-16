package me.trololo11.lifespluginseason3.tasks;

import me.trololo11.lifespluginseason3.cardstuff.CardType;
import me.trololo11.lifespluginseason3.managers.CardManager;
import me.trololo11.lifespluginseason3.managers.RecipesManager;
import me.trololo11.lifespluginseason3.utils.Utils;
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
    private CardManager cardManager;
    private boolean isLife=true;

    public LifesStackCheckTask(Player p, RecipesManager recipesManager, CardManager cardManager){
        this.p = p;
        this.recipesManager = recipesManager;
        this.cardManager = cardManager;
    }

    @Override
    public void run() {

        //check if theres a life item which is more than 1 and unstacks it
        //(to unstack it it creates an random uuid in the private name)
        for(ItemStack item : p.getInventory().getContents()  ) {

            if (item != null && item.hasItemMeta() && Utils.getPrivateName(item) != null
                    && (Utils.getPrivateName(item).equalsIgnoreCase("life_item") || Utils.getPrivateName(item).equalsIgnoreCase("revive_card") || Utils.getPrivateName(item).startsWith("life_give"))
                    && item.getAmount() > 1) {
                isLife = Utils.getPrivateName(item).equalsIgnoreCase("life_item");

                p.getInventory().remove(item);

                for (int i = 0; i < item.getAmount(); i++) {
                    p.getInventory().addItem(isLife ? recipesManager.getLifeItem() :
                            (Utils.getPrivateName(item).startsWith("life_give") ? cardManager.getCard(CardType.LIFE_GIVE).getCardItem() : recipesManager.getReviveCardItem() ) );
                }


            }
        }
    }

}
