package me.trololo11.lifespluginseason3.commands;

import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InvFullCommand implements CommandExecutor {



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return true;

        Player player = (Player)  sender;

        player.sendMessage(Utils.isPlayerInvFull(player.getInventory()) + "");

        return true;
    }
}
