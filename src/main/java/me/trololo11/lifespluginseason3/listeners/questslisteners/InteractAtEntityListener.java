package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class InteractAtEntityListener extends QuestListener {
    public InteractAtEntityListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();

        checkTarget(e.getRightClicked().getType(), player);
    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.RIGHT_CLICK_ENTITY;
    }
}
