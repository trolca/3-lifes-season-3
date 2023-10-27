package me.trololo11.lifespluginseason3.commands;

import me.trololo11.lifespluginseason3.cardstuff.CardClass;
import me.trololo11.lifespluginseason3.managers.CardManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GetRandomCardCommand implements CommandExecutor {

    private CardManager cardManager;
    private int lowestChance = 100;
    private int highestChance = 0;
    private HashMap<Integer, ArrayList<CardClass>> percentageCards = new HashMap<>();

    public GetRandomCardCommand(CardManager cardManager){
        this.cardManager = cardManager;
        ArrayList<CardClass> allCards = cardManager.getAllCards();

        for(CardClass card : allCards){

            int percentageChance = card.getPercentageChance();
            if(!percentageCards.containsKey(percentageChance)){
                percentageCards.put(percentageChance, new ArrayList<>());
            }else{
                if(lowestChance > percentageChance) lowestChance = percentageChance;
                if(highestChance < percentageChance) highestChance = percentageChance;
            }

            percentageCards.get(percentageChance).add(card);

        }

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

        if(!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        Random random = new Random();
        int randNum = random.nextInt(100)+1;
        System.out.println(randNum);



        int theChosenChance = 0;

        for(int chance : percentageCards.keySet()){

            if(randNum <= chance ){
                theChosenChance = chance;
                break;
            }
        }

        if(randNum < lowestChance) theChosenChance = highestChance;
        else if(randNum > highestChance) theChosenChance = lowestChance;

        ArrayList<CardClass> chosenCardsArray = percentageCards.get(theChosenChance);

        CardClass card = chosenCardsArray.get( random.nextInt(chosenCardsArray.size()) );

        player.sendMessage(ChatColor.GREEN + "Pomyślnie dodano losową karte!");
        player.getInventory().addItem(card.getCardItem());



        return true;
    }
}
