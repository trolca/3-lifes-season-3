package me.trololo11.lifespluginseason3.commands;

import me.trololo11.lifespluginseason3.events.PlayerChangeLifesEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Sets the specified amount of lives for the specified player.
 */
public class SetLifesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("3lifes3.admin")){
            sender.sendMessage(ChatColor.RED + "Nie dla psa!");
            return true;
        }

        if(args.length == 0){
            sender.sendMessage(ChatColor.RED + "Napisz gracza któremu chcesz ustawić życia!");
            return true;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if(player == null){
            sender.sendMessage(ChatColor.RED + "Ten gracz nie jest online!");
            return true;
        }

        if(args.length == 1){
            sender.sendMessage(ChatColor.RED + "Prosze napisać ile żyć ustawić");
            return true;
        }

        byte lifes = 0;

        try{
            lifes = Byte.parseByte(args[1]);
        }catch (NumberFormatException e){
            sender.sendMessage(ChatColor.RED + "Ilość żyć jest niepoprawna!");
            return true;
        }

        Bukkit.getServer().getPluginManager().callEvent(new PlayerChangeLifesEvent(player, lifes));

        sender.sendMessage(ChatColor.GREEN + "Pomyślnie zmieniono ilość żyć "+ player.getName() + " na "+ lifes + "!");

        return true;
    }
}
