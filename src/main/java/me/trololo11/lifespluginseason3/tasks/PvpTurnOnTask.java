package me.trololo11.lifespluginseason3.tasks;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PvpTurnOnTask extends BukkitRunnable {

    private int time;
    private int localTime = 0;
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    public PvpTurnOnTask(int time){
        this.time = time;
    }

    @Override
    public void run() {
        time--;

        switch (time){
            case 600 -> Bukkit.broadcastMessage(Utils.chat("&2&lPvp zostanie włączone za 10 minut!"));
            case 300 -> Bukkit.broadcastMessage(Utils.chat("&2&lPvp zostanie włączone za &a&l5&2&l minut!"));
            case 60 -> Bukkit.broadcastMessage(Utils.chat("&2&lPvp zostanie włączone za &a&l1&2&l minute!"));
            case 1 -> Bukkit.broadcastMessage(Utils.chat("&a&lPvp zostanie włączone za &2&l1&2&l sekundę!"));
        }

        if(time <= 10 && time > 1){
            Bukkit.broadcastMessage(Utils.chat("&2&lPvp zostanie włączone za &a&l"+time+"&2&l sekund!"));
        }else if(time <= 0){
            Bukkit.getWorld("world").setPVP(true);
            Bukkit.getWorld("world_nether").setPVP(true);
            Bukkit.getWorld("world_the_end").setPVP(true);


            for(Player player : Bukkit.getOnlinePlayers()){
                player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Pvp zostało włączone.".toUpperCase());
                player.sendTitle(ChatColor.RED + ChatColor.BOLD.toString() + "Pvp zostało włączone.".toUpperCase(), "", 5, 20, 15);
                player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f, 1f);
            }

            cancel();

        }


    }
}
