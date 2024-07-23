package me.trololo11.lifespluginseason3.managers;

import me.trololo11.lifespluginseason3.cardstuff.CardClass;
import me.trololo11.lifespluginseason3.cardstuff.CardType;
import me.trololo11.lifespluginseason3.cardstuff.cardtypes.*;
import org.bukkit.ChatColor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * This class bassicaly stores all of the {@link CardClass} in a hash map
 * where the key is the {@link CardType} of the card.
 */
public class CardManager {

    private HashMap<CardType, CardClass> cardClassHashMap = new HashMap<>();
    private ArrayList<CardClass> allCards = new ArrayList<>();

    /**
     * This hash map has as a key the percentage chance of specific cards and the
     * value is al off the cards that have the chance of this key.
     */
    private final HashMap<Integer, ArrayList<CardClass>> percentageCards = new HashMap<>();

    private final int highestChance;

    public CardManager(){
        allCards.add(new GiveLifeCard());
        allCards.add(new TakeLifeCard());
        allCards.add(new DailyQuestSkipCard());
        allCards.add(new WeeklyQuestSkipCard());
        allCards.add(new RandomQuestSkipCard());
        allCards.add(new GoldLifeCard());
        allCards.add(new ChangeGlobalQuestCard());
        allCards.add(new ReduceRequirementsQuestsCard());

        for(CardClass card : allCards){
            cardClassHashMap.put(card.getCardType(), card);
        }

        int tempHighChance = 0;

        for(CardClass card : allCards){

            int percentageChance = card.getPercentageChance();
            if(!percentageCards.containsKey(percentageChance)){
                percentageCards.put(percentageChance, new ArrayList<>());
            }

            if(tempHighChance < percentageChance) tempHighChance = percentageChance;

            percentageCards.get(percentageChance).add(card);

        }

        highestChance = tempHighChance;
    }

    /**
     * Gets the {@link CardClass} of the {@link CardType} specified
     * @param cardType The cardType to get
     * @return The {@link CardClass}
     */
    public CardClass getCard(CardType cardType){
        return cardClassHashMap.get(cardType);
    }

    /**
     * Gets all of the {@link CardClass} that are stored in
     * this manager.
     * @return All of the {@link CardClass}
     */
    public ArrayList<CardClass> getAllCards(){
        return new ArrayList<>(allCards);
    }


    /**
     * Returns a random card based of the percentage chance of
     * every card that exists.
     * @param random The random to be used
     * @return A random card from the allCard ArrayList
     */
    public CardClass getRandomCard(Random random){
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

        return chosenCardsArray.get( random.nextInt(chosenCardsArray.size()) );
    }

}
