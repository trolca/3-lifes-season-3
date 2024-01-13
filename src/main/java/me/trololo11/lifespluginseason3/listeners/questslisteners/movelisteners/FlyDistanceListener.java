package me.trololo11.lifespluginseason3.listeners.questslisteners.movelisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class FlyDistanceListener extends QuestListener {
    public FlyDistanceListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onFly(PlayerMoveEvent e){
        if (!e.getPlayer().isGliding()) return;
        Player p = e.getPlayer();
        if (e.getTo() == null) return;

        checkTarget(null, p, Utils.getDistance(e.getFrom(), e.getTo()));
    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.FLY_DISTANCE;
    }
}
