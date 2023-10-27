package me.trololo11.lifespluginseason3.cardstuff.cardtypes;

import me.trololo11.lifespluginseason3.cardstuff.CardClass;
import me.trololo11.lifespluginseason3.cardstuff.CardType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GiveLifeCard extends CardClass {

    @Override
    protected void createDescription() {
        description.add(ChatColor.WHITE + "This is a card lol");
    }

    @Override
    public String getName() {
        return "&4&lOddaj życie karta";
    }

    @Override
    protected int getCustomModelData() {
        return 2137;
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
