package me.trololo11.lifespluginseason3.commands;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.cardstuff.CardClass;
import me.trololo11.lifespluginseason3.managers.CardManager;
import me.trololo11.lifespluginseason3.managers.DatabaseManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GetRandomCardCommand implements CommandExecutor {
    private DatabaseManager databaseManager;
    private CardManager cardManager;
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    public GetRandomCardCommand(CardManager cardManager, DatabaseManager databaseManager){
        this.databaseManager = databaseManager;
        this.cardManager = cardManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

        if(!(sender instanceof Player)) return true;

        Player player = (Player) sender;
        try{

            if(databaseManager.hasTakenWeeklyCard(player.getUniqueId())){
                player.sendMessage(ChatColor.RED + "Już wziąłeś losową karte w tym tygodniu!");
                return true;
            }


        }catch (SQLException e){
            plugin.logger.warning("[3LifesPluginS3] Error while getting the player has taken weekly card data!");
            if(plugin.isDetailedErrors()) e.printStackTrace(System.out);
        }

        CardClass card = cardManager.getRandomCard(new Random());

        player.sendMessage(ChatColor.GREEN + "Pomyślnie dodano losową karte!");
        player.getInventory().addItem(card.getCardItem());

        try {
            databaseManager.setTakenWeeklyCard(player.getUniqueId(), true);
        } catch (SQLException e) {
            plugin.logger.warning("[3LifesPluginS3] Error while getting the player has taken weekly card data!");
            if(plugin.isDetailedErrors()) e.printStackTrace(System.out);
        }

        return true;
    }
}
