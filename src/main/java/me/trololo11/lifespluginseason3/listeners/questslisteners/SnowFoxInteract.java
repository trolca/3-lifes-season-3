package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class SnowFoxInteract extends QuestListener {
    public SnowFoxInteract(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onSnowFoxInteract(PlayerInteractEntityEvent e)  {
        if(e.getRightClicked().getType() != EntityType.FOX) return;
        Fox fox = (Fox) e.getRightClicked();
        if(fox.getFoxType() != Fox.Type.SNOW) return;

        Player player = e.getPlayer();

        checkTarget(null, player);
    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.SNOW_FOX_INTERACT;
    }
}
