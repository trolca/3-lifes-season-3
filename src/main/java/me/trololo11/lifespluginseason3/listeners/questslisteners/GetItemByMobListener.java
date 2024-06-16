package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class GetItemByMobListener extends QuestListener {
    public GetItemByMobListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent e){
        if(e.getEntity().getKiller() == null) return;
        Player player = e.getEntity().getKiller();

        for(ItemStack itemStack : e.getDrops()){
            checkTarget(itemStack.getType(), player, itemStack.getAmount());
        }
    }

    /**
     * Gets this {@link ListenerType} that its listening for.
     *
     * @return The {@link ListenerType} of this class.
     */
    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.GET_ITEM_BY_MOB;
    }
}
