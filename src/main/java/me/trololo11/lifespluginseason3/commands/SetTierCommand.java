package me.trololo11.lifespluginseason3.commands;

import me.trololo11.lifespluginseason3.LifesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SetTierCommand implements CommandExecutor {

    LifesPlugin plugin = LifesPlugin.getPlugin();

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
            sender.sendMessage(ChatColor.RED + "Hmmmmm?");
            return true;
        }
        plugin.setTier(Integer.getInteger(args[0]));
        sender.sendMessage(ChatColor.GREEN + "Pomyślnie ustawiono tier!");
        return true;
    }
}
