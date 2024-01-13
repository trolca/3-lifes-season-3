package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;

public class VillagerPayTradeListener extends QuestListener {
    public VillagerPayTradeListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onTrade(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();
        if(e.getClickedInventory() == null) return;
        if(e.getSlot() != 2) return;
        if(e.getClickedInventory().getType() != InventoryType.MERCHANT) return;
        MerchantInventory merchantInventory = (MerchantInventory) e.getClickedInventory();
        if(merchantInventory.getSelectedRecipe() == null) return;
        if(merchantInventory.getSelectedRecipe().getAdjustedIngredient1() == null) return;
        MerchantRecipe merchantRecipe = merchantInventory.getSelectedRecipe();
        ItemStack item = merchantInventory.getSelectedRecipe().getAdjustedIngredient1();
        ItemStack itemSus = merchantInventory.getItem(0);
        if(itemSus == null) return;

        if(e.getClick() != ClickType.SHIFT_LEFT && e.getClick() != ClickType.SHIFT_RIGHT){

            checkTarget(item.getType(), player, item.getAmount());

        }else{
            int maxUsages = merchantRecipe.getMaxUses();
            int thisUses = itemSus.getAmount()/item.getAmount();
            if(thisUses > maxUsages){
                checkTarget(item.getType(), player, maxUsages*item.getAmount());
            }else{
                checkTarget(item.getType(), player, thisUses*item.getAmount());
            }

        }
    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.VILLAGER_TRADE_SPEND;
    }
}
