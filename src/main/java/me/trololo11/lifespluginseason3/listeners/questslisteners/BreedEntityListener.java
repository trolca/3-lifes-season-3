package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityBreedEvent;


public class BreedEntityListener extends QuestListener {
    public BreedEntityListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onBreed(EntityBreedEvent e) {
        if(!(e.getBreeder() instanceof Player)) return;
        Player player = (Player) e.getBreeder();


        checkTarget(e.getEntity().getType(), player);
    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.BREED_ENTITY;
    }
}
