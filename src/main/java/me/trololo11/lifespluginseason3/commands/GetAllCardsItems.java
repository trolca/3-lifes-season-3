package me.trololo11.lifespluginseason3.commands;

import me.trololo11.lifespluginseason3.cardstuff.CardClass;
import me.trololo11.lifespluginseason3.managers.CardManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetAllCardsItems implements CommandExecutor {

    private CardManager cardManager;

    public GetAllCardsItems(CardManager cardManager){
        this.cardManager = cardManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return true;

        if(!sender.hasPermission("3lifes3.admin")){
            sender.sendMessage(ChatColor.RED + "Nie masz permow lolololololo");
            return true;
        }

        Player player = (Player) sender;

        for(CardClass cardClass : cardManager.getAllCards()){
            player.getInventory().addItem(cardClass.getCardItem());
        }

        player.sendMessage(ChatColor.GREEN + "Pomyślnie dodano karty!");

        return true;
    }
}
