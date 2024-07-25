package me.trololo11.lifespluginseason3.listeners;

import me.trololo11.lifespluginseason3.managers.TeamsManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatColorFix implements Listener {

    private TeamsManager teamsManager;

    public ChatColorFix(TeamsManager teamsManager){
        this.teamsManager = teamsManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        //for some reason the chat doesnt respect the custom prefixes so have to do it myself
        //*insert the thanos meme here*
        Player p = e.getPlayer();
        String formatString = "<";

        if(teamsManager.getLifesTeamList().get(0).hasEntry(p.getName())) formatString = formatString+ ChatColor.DARK_RED;
        if(teamsManager.getLifesTeamList().get(1).hasEntry(p.getName())) formatString = formatString+ ChatColor.RED;
        if(teamsManager.getLifesTeamList().get(2).hasEntry(p.getName())) formatString = formatString+ ChatColor.YELLOW;
        if(teamsManager.getLifesTeamList().get(3).hasEntry(p.getName())) formatString = formatString+ ChatColor.GREEN;
        if(teamsManager.getLifesTeamList().get(4).hasEntry(p.getName())) formatString = formatString+ ChatColor.DARK_GREEN;

        formatString = formatString + p.getName() + ChatColor.RESET +"> " + e.getMessage();

        e.setFormat(formatString);
    }
}
