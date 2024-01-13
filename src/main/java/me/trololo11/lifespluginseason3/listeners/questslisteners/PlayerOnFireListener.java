package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerOnFireListener extends QuestListener {
    public PlayerOnFireListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if(e.getEntity().getType() != EntityType.PLAYER) return;
        if(e.getCause() != EntityDamageEvent.DamageCause.FIRE && e.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK) return;
        Player player = (Player) e.getEntity();

        checkTarget(null, player);
    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.ON_FIRE;
    }
}
