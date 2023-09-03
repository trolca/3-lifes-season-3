package me.trololo11.lifespluginseason3.commands;

import me.trololo11.lifespluginseason3.managers.RecipesManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetItemsCommand implements CommandExecutor {
    private RecipesManager recipesManager;

    public GetItemsCommand(RecipesManager recipesManager){
        this.recipesManager = recipesManager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("3lifes3.admin")){
            sender.sendMessage(ChatColor.RED + "Nie masz permisji!!!");
            sender.sendMessage(ChatColor.GRAY + ChatColor.ITALIC.toString() + "(Also kinda sus)");
            return true;
        }
        if(!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        player.getInventory().addItem(recipesManager.getLifeItem());
        player.getInventory().addItem(recipesManager.getReviveCardItem());
        player.getInventory().addItem(recipesManager.getPlayerLifeItem(player));
        player.getInventory().addItem(recipesManager.getLifeShardItem());
        player.getInventory().addItem(recipesManager.getReviveShardItem());


        return true;
    }
}
