package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemBreakEvent;

public class BreakItemListener extends QuestListener {
    public BreakItemListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onBreak(PlayerItemBreakEvent e){
        Player p = e.getPlayer();

        checkTarget(e.getBrokenItem().getType(),  p);

    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.BREAK_ITEM;
    }
}
