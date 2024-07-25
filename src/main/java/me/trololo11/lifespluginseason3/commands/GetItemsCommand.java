package me.trololo11.lifespluginseason3.commands;

import me.trololo11.lifespluginseason3.cardstuff.CardClass;
import me.trololo11.lifespluginseason3.managers.CardManager;
import me.trololo11.lifespluginseason3.managers.RecipesManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class GetItemsCommand implements CommandExecutor {

    private RecipesManager recipesManager;
    private CardManager cardManager;

    public GetItemsCommand(RecipesManager recipesManager, CardManager cardManager){
        this.recipesManager = recipesManager;
        this.cardManager = cardManager;
    }

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

        if(!(sender instanceof Player)) return true;
        if(!sender.hasPermission("3lifes3.admin")){
            sender.sendMessage(ChatColor.RED + "Nuh uh");
            return true;
        }

        Player player = (Player) sender;

        player.getInventory().addItem(recipesManager.getGiveLifeShardItem(), recipesManager.getLifeItem(), recipesManager.getGoldLifeItem(), recipesManager.getReviveCardItem(), recipesManager.getReviveShardItem(), recipesManager.getPlayerLifeItem(player));

        for (CardClass card : cardManager.getAllCards()){
            player.getInventory().addItem(card.getCardItem());
        }

        return true;
    }
}
