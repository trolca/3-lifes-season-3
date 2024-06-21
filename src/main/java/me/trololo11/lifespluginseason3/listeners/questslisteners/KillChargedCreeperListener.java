package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import javax.swing.text.html.parser.Entity;

public class KillChargedCreeperListener extends QuestListener {
    public KillChargedCreeperListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onKill(EntityDeathEvent e){
        if(e.getEntity().getType() != EntityType.CREEPER) return;
        Creeper creeper = (Creeper) e.getEntity();
        if(creeper.getKiller() == null) return;
        if(!creeper.isPowered()) return;

        checkTarget(null, creeper.getKiller());
    }

    /**
     * Gets this {@link ListenerType} that its listening for.
     *
     * @return The {@link ListenerType} of this class.
     */
    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.KILL_CHARGED_CREEPER;
    }
}
