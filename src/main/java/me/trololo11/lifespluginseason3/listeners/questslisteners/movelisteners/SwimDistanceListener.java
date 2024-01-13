package me.trololo11.lifespluginseason3.listeners.questslisteners.movelisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class SwimDistanceListener extends QuestListener {
    public SwimDistanceListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onSwim(PlayerMoveEvent e) {
        if (!e.getPlayer().isSwimming()) return;
        Player p = e.getPlayer();
        if (e.getTo() == null) return;

        checkTarget(null, p, Utils.getDistance(e.getFrom(), e.getTo()));
    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.SWIM_DISTANCE;
    }
}
