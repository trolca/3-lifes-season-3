package me.trololo11.lifespluginseason3.listeners.questslisteners.movelisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.sql.SQLException;

public class RideDistanceListener extends QuestListener {
    public RideDistanceListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onRide(PlayerMoveEvent e) throws SQLException {
        if (!e.getPlayer().isInsideVehicle()) return;
        Player p = e.getPlayer();
        if(p.getVehicle() == null) return;
        if (e.getTo() == null) return;

        checkTarget(p.getVehicle().getType(), p, Utils.getDistance(e.getFrom(), e.getTo()));

    }


    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.RIDE_DISTANCE;
    }
}
