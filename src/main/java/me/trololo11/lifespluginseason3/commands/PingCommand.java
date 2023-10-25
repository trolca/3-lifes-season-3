package me.trololo11.lifespluginseason3.commands;

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

        player.sendMessage("Twój ping: "+ player.getPing()+"ms");

        return true;
    }
}
