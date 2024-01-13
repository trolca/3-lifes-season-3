package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PunchEntityListener extends QuestListener {
    public PunchEntityListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if(e.getDamager().getType() != EntityType.PLAYER) return;
        Player player = (Player) e.getDamager();

        checkTarget(e.getEntity().getType(), player);
    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.PUNCH_ENTITY;
    }
}
