package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SmeltItemListener extends QuestListener {
    public SmeltItemListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onSmelt(InventoryClickEvent e) {
        if(e.getClick() == ClickType.MIDDLE) return;
        if(e.getCurrentItem() == null) return;
        if(e.getClickedInventory() == null) return;
        if(e.getSlot() != 2) return;
        if(!(e.getWhoClicked() instanceof Player)) return;
        Inventory inventory = e.getClickedInventory();
        if(inventory.getType() != InventoryType.BLAST_FURNACE
                && inventory.getType() != InventoryType.FURNACE
                && inventory.getType() != InventoryType.SMOKER) return;
        if(e.getClick() == ClickType.NUMBER_KEY){
            e.setCancelled(true);
            return;
        }
        ItemStack item = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();

        checkTarget(item.getType(), player, item.getAmount());
    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.SMELT_ITEM;
    }
}
