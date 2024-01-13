package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceBlockListener extends QuestListener {

    public PlaceBlockListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        checkTarget(e.getBlock().getType(), e.getPlayer());
    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.PLACE_BLOCKS;
    }
}
