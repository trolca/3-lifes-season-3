package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class SmithingUseListener extends QuestListener {
    public SmithingUseListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onSmith(InventoryClickEvent e)  {
        if(e.getClick() == ClickType.MIDDLE) return;
        if(e.getClickedInventory() == null) return;
        if(e.getClickedInventory().getType() != InventoryType.SMITHING) return;
        if(e.getClick() == ClickType.NUMBER_KEY){
            e.setCancelled(true);
            return;
        }
        if(!(e.getWhoClicked() instanceof Player)) return;
        if(e.getSlot() != 3) return;
        if(e.getClickedInventory().getItem(e.getSlot()) == null || e.getCurrentItem().getType() == Material.AIR) return;
        ItemStack item = e.getClickedInventory().getItem(0);


        Player player = (Player) e.getWhoClicked();

        checkTarget(item.getType(), player);

    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.SMITHING_USE;
    }
}
