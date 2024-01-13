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

import java.sql.SQLException;

public class VillagerBuyTradeListener extends QuestListener {
    public VillagerBuyTradeListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onTrade(InventoryClickEvent e) throws SQLException {
        if(!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();
        if(e.getSlot() != 2) return;
        if(e.getClickedInventory() == null) return;
        if(e.getClickedInventory().getType() != InventoryType.MERCHANT) return;
        MerchantInventory merchantInventory = (MerchantInventory) e.getClickedInventory();
        if(merchantInventory.getSelectedRecipe() == null) return;
        MerchantRecipe merchantRecipe = merchantInventory.getSelectedRecipe();
        if(merchantInventory.getSelectedRecipe() == null) return;
        if(merchantInventory.getSelectedRecipe().getAdjustedIngredient1() == null) return;
        ItemStack itemCost = merchantInventory.getSelectedRecipe().getAdjustedIngredient1();
        ItemStack itemBuy = merchantInventory.getItem(0);
        ItemStack itemTrade = e.getCurrentItem();
        if(itemCost == null) return;
        if(itemBuy == null) return;
        if(itemTrade == null) return;

        if(e.getClick() != ClickType.SHIFT_LEFT && e.getClick() != ClickType.SHIFT_RIGHT){

            checkTarget(itemTrade.getType(), player, itemTrade.getAmount());

        }else{
            int maxUsages = merchantRecipe.getMaxUses();
            int thisUses = itemBuy.getAmount()/itemCost.getAmount();

            if(thisUses > maxUsages){
                checkTarget(itemTrade.getType(), player, maxUsages*itemTrade.getAmount());
            }else{
                checkTarget(itemTrade.getType(), player, thisUses*itemTrade.getAmount());
            }

        }

    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.VILLAGER_TRADE_BUY;
    }
}
