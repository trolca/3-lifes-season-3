package me.trololo11.lifespluginseason3.cardstuff.cardtypes;

import me.trololo11.lifespluginseason3.cardstuff.CardClass;
import me.trololo11.lifespluginseason3.cardstuff.CardType;
import org.bukkit.ChatColor;

public class GiveLifeCard extends CardClass {

    @Override
    protected void createDescription() {
        description.add(ChatColor.WHITE + "Oddajesz 1 swoje życie martwiej osobie.");
        description.add(ChatColor.WHITE + "Osoba wybrana zostanie wskrzeszona");
        description.add(ChatColor.WHITE + "z 1 życiem.");
    }

    @Override
    public String getName() {
        return "&4&lOddaj życie karta";
    }

    @Override
    protected int getCustomModelData() {
        return 8760003;
    }

    @Override
    public CardType getCardType() {
        return CardType.LIFE_GIVE;
    }

    @Override
    public int getPercentageChance() {
        return 0;
    }

}
