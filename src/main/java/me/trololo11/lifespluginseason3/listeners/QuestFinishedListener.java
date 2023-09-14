package me.trololo11.lifespluginseason3.listeners;

import me.trololo11.lifespluginseason3.events.QuestFinishedEvent;
import me.trololo11.lifespluginseason3.utils.Quest;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class QuestFinishedListener implements Listener {

    @EventHandler
    public void onFinish(QuestFinishedEvent e){
        Player player = e.getPlayer();
        Quest quest = e.getQuest();

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        player.sendMessage(Utils.chat("&aWłaśnie ukończyłeś quest &f["+ quest.getName() + "&f]"));
    }
}
