package me.trololo11.lifespluginseason3.listeners.questslisteners.movelisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class WalkDistanceListener extends QuestListener {

    public WalkDistanceListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onWalk(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (e.getTo() == null) return;
        if(e.getPlayer().isInsideVehicle()) return;
        if(e.getPlayer().isGliding()) return;

        checkTarget(null, p, Utils.getDistance(e.getFrom(), e.getTo()));

    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.WALK_DISTANCE;
    }
}
