package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;

public class CraftItemListener extends QuestListener {
    public CraftItemListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) throws SQLException {
        if(e.getClick() == ClickType.MIDDLE) return;
        if(e.getClick() == ClickType.NUMBER_KEY){
            e.setCancelled(true);
            return;
        }
        if (e.getCurrentItem() == null) return;

        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        ItemStack test = e.getRecipe().getResult().clone();
        int recipeAmount = test.getAmount();

        //tyyy ezeiger92 for the code :D (yeahhhh i copied it :P)
        if(e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT){

            int maxCraftable = Utils.getMaxCraftAmount(e.getInventory());
            int capacity = fits(test, e.getView().getBottomInventory());

            if(recipeAmount == 0) maxCraftable = 1;

            if (capacity < maxCraftable && recipeAmount != 0)

                maxCraftable = ((capacity + recipeAmount - 1) / recipeAmount) * recipeAmount;

            recipeAmount = maxCraftable;

            checkTarget(item.getType(), p, recipeAmount);
            return;
        }

        checkTarget(item.getType(), p);
    }

    private int fits(ItemStack stack, Inventory inv) {
        ItemStack[] contents = inv.getContents();
        int result = 0;

        for (ItemStack is : contents)
            if (is == null)
                result += stack.getMaxStackSize();
            else if (is.isSimilar(stack))
                result += Math.max(stack.getMaxStackSize() - is.getAmount(), 0);

        return result;
    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.CRAFT;
    }
}
