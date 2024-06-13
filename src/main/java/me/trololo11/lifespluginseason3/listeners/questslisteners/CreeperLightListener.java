package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class CreeperLightListener extends QuestListener {
    public CreeperLightListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {
        Player player = e.getPlayer();
        if(player.getInventory().getItemInMainHand().getType() != Material.FLINT_AND_STEEL) return;
        if(e.getRightClicked().getType() != EntityType.CREEPER) return;

        checkTarget(null, player);
    }

    /**
     * Gets this {@link ListenerType} that its listening for.
     *
     * @return The {@link ListenerType} of this class.
     */
    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.CREEPER_LIGHTER;
    }
}
