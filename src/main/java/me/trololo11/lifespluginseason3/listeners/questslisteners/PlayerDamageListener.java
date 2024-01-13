package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener extends QuestListener {

    public PlayerDamageListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if(e.getEntity().getType() != EntityType.PLAYER) return;
        Player player = (Player) e.getEntity();

        checkTarget(e.getCause(), player);
    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.PLAYER_DAMAGE;
    }
}
