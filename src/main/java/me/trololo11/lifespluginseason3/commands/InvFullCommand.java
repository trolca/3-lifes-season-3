package me.trololo11.lifespluginseason3.commands;

import me.trololo11.lifespluginseason3.managers.DatabaseManager;
import me.trololo11.lifespluginseason3.utils.QuestType;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class InvFullCommand implements CommandExecutor {

    private DatabaseManager databaseManager;

    public InvFullCommand(DatabaseManager databaseManager){
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        player.openInventory(player.getInventory());

        return true;
    }
}
