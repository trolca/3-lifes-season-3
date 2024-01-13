package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class EatItemListener extends QuestListener  {
    public EatItemListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent e) {

        Player p = e.getPlayer();

        checkTarget(e.getItem().getType(), p);


    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.EAT;
    }
}
