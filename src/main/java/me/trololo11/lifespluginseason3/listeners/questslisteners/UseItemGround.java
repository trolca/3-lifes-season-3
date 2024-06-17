package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class UseItemGround extends QuestListener {
    public UseItemGround(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e){
        if(e.getAction() != Action.LEFT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = e.getPlayer();
        Block targetBlock = player.getTargetBlockExact(5);
        if(targetBlock == null) return;
        Material targetBlockType = targetBlock.getType();
        if(targetBlockType != Material.GRASS_BLOCK && targetBlockType != Material.DIRT) return;
        ItemStack item = e.getItem();
        if(item == null) return;

        checkTarget(item.getType(), player);
    }

    /**
     * Gets this {@link ListenerType} that its listening for.
     *
     * @return The {@link ListenerType} of this class.
     */
    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.USE_ITEM_ON_GROUND;
    }
}
