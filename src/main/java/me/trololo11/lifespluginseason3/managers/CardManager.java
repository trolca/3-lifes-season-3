package me.trololo11.lifespluginseason3.managers;

import me.trololo11.lifespluginseason3.cardstuff.CardClass;
import me.trololo11.lifespluginseason3.cardstuff.CardType;
import me.trololo11.lifespluginseason3.cardstuff.cardtypes.GiveLifeCard;
import me.trololo11.lifespluginseason3.cardstuff.cardtypes.TakeLifeCard;

import java.util.ArrayList;
import java.util.HashMap;

public class CardManager {

    private HashMap<CardType, CardClass> cardClassHashMap = new HashMap<>();
    private ArrayList<CardClass> allCards = new ArrayList<>();

    public CardManager(){
        allCards.add(new GiveLifeCard());
        allCards.add(new TakeLifeCard());

        for(CardClass card : allCards){
            cardClassHashMap.put(card.getCardType(), card);
        }
    }

    public CardClass getCard(CardType cardType){
        return cardClassHashMap.get(cardType);
    }

    public ArrayList<CardClass> getAllCards(){
        return allCards;
    }

}
