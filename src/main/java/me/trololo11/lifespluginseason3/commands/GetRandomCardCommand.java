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
import org.checkerframework.checker.units.qual.A;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class GetRandomCardCommand implements CommandExecutor {

    private int highestChance = 0;
    private HashMap<Integer, ArrayList<CardClass>> percentageCards = new HashMap<>();
    private DatabaseManager databaseManager;
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    public GetRandomCardCommand(CardManager cardManager, DatabaseManager databaseManager){
        ArrayList<CardClass> allCards = cardManager.getAllCards();

        for(CardClass card : allCards){

            int percentageChance = card.getPercentageChance();
            if(!percentageCards.containsKey(percentageChance)){
                percentageCards.put(percentageChance, new ArrayList<>());
            }

            if(highestChance < percentageChance) highestChance = percentageChance;

            percentageCards.get(percentageChance).add(card);

        }
        this.databaseManager = databaseManager;

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

        Random random = new Random();

        int theChosenChance = -1;

        for(int chance : percentageCards.keySet()){

            int randNum = random.nextInt(100)+1;
            if(randNum <= chance ){
                theChosenChance = chance;
                break;
            }

        }


        if(theChosenChance == -1) theChosenChance = highestChance;

        ArrayList<CardClass> chosenCardsArray = percentageCards.get(theChosenChance);
        CardClass card = chosenCardsArray.get( random.nextInt(chosenCardsArray.size()) );

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
