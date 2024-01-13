package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

public class KillMobListener extends QuestListener {
    public KillMobListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null) return;
        Player p = e.getEntity().getKiller();
        EntityType entityType = e.getEntityType();

        checkTarget(entityType, p);

    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.KILL_MOBS;
    }
}
