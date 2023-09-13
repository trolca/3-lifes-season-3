package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlocksListener extends QuestListener {
    public BreakBlocksListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        Player player = e.getPlayer();
        Material blockType = e.getBlock().getType();

        checkTarget(blockType, player);
    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.BREAK_BLOCKS;
    }
}
