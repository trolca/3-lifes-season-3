package me.trololo11.lifespluginseason3.managers;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.cardstuff.CardClass;
import me.trololo11.lifespluginseason3.cardstuff.CardType;
import me.trololo11.lifespluginseason3.cardstuff.cardtypes.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * This class bassicaly stores all of the {@link CardClass} in a hash map
 * where the key is the {@link CardType} of the card.
 */
public class CardManager {

    private HashMap<CardType, CardClass> cardClassHashMap = new HashMap<>();
    private ArrayList<CardClass> allCards = new ArrayList<>();
    private ArrayList<UUID> allPlayersTakenCards;
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    public CardManager(DatabaseManager databaseManager){
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

        try {
            allPlayersTakenCards = databaseManager.getAllPlayerTakenCard();
        } catch (SQLException e) {
            plugin.logger.severe("[3LifesS3] Error while getting all of the players who have taken card from the database!");
            if (plugin.isDetailedErrors()) throw new RuntimeException(e);
            else throw new RuntimeException("Check your sql database");
        }
    }

    public ArrayList<UUID> getAllPlayersTakenCards(){
        return allPlayersTakenCards;
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

}
