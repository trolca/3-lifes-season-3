package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTameEvent;

public class TameEntityListener extends QuestListener {
    public TameEntityListener(QuestManager questManager) {
        super(questManager);
    }
    @EventHandler
    public void onTame(EntityTameEvent e)  {
        Player tamer = (Player) e.getOwner();
        checkTarget(e.getEntity().getType(), tamer);
    }


    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.TAME_ANIMAL;
    }
}
