package me.trololo11.lifespluginseason3.listeners.questslisteners;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.ListenerType;
import me.trololo11.lifespluginseason3.utils.QuestListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import java.sql.SQLException;

public class PlayerLevelUpListener extends QuestListener {
    public PlayerLevelUpListener(QuestManager questManager) {
        super(questManager);
    }

    @EventHandler
    public void onLevelUp(PlayerLevelChangeEvent e) throws SQLException {

        int howMuch = e.getNewLevel() - e.getOldLevel();

        if(howMuch <= 0) return;

        Player p = e.getPlayer();

        checkTarget(null, p, howMuch);


    }

    @Override
    public ListenerType getThisListenerType() {
        return ListenerType.LEVEL_UP;
    }
}
