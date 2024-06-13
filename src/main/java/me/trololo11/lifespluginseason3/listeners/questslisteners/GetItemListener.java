package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.sql.SQLException;

public class GetItemListener extends QuestListener {
    public GetItemListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if(e.getEntity() instanceof Player p) checkTarget(e.getItem().getItemStack().getType(), p);
    }

    @EventHandler
    public void onInventoryGetting(InventoryClickEvent e) {
        if(e.isCancelled()) return;
        if(e.getCurrentItem() == null) return;
        if(e.getWhoClicked() instanceof Player p) checkTarget(e.getCurrentItem().getType(), p);
    }

    /**
     * Gets this {@link ListenerType} that its listening for.
     *
     * @return The {@link ListenerType} of this class.
     */
    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.GET_ITEM;
    }
}
