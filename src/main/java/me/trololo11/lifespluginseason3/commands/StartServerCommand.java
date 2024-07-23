package me.trololo11.lifespluginseason3.commands;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.tasks.PvpTurnOnTask;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StartServerCommand implements CommandExecutor {

    private LifesPlugin plugin = LifesPlugin.getPlugin();
    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("3lifes3.admin")){
            sender.sendMessage(ChatColor.RED + "Pls dont start this serwer withough permission :>");
            return true;
        }

        Bukkit.getWorld("world").setPVP(false);
        Bukkit.getWorld("world_nether").setPVP(false);
        Bukkit.getWorld("world_the_end").setPVP(false);

        new PvpTurnOnTask(1800).runTaskTimer(plugin, 20L, 20L);

        Bukkit.broadcastMessage(Utils.chat("&2&lPvp zostanie włączone za 30 minut!"));
        return true;
    }
}
