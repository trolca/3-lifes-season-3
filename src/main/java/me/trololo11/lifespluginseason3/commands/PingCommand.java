package me.trololo11.lifespluginseason3.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

//Idk why i added this lol
//Just because
public class PingCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if(args.length == 0) {
            player.sendMessage("Twój ping: " + player.getPing() + "ms");
        }else if(!args[0].equalsIgnoreCase("*")){
            Player pingPlayer = Bukkit.getPlayer(args[0]);
            if(pingPlayer == null){
                player.sendMessage(ChatColor.RED + "Ta osoba nie jest online");
                return true;
            }

            player.sendMessage("Ping " + pingPlayer.getName() + ": "+ pingPlayer.getPing() + "ms");
        }else{
            player.sendMessage("Ping wszystkich graczy:");
            player.sendMessage("");
            for(Player pingPlayer : Bukkit.getOnlinePlayers()){
                player.sendMessage(pingPlayer.getName() + ": " + pingPlayer.getPing() + "ms");
            }

        }


        return true;
    }
}
