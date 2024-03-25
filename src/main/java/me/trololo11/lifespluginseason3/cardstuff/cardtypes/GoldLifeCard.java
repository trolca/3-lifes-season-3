package me.trololo11.lifespluginseason3.cardstuff.cardtypes;

import me.trololo11.lifespluginseason3.cardstuff.CardClass;
import me.trololo11.lifespluginseason3.cardstuff.CardType;
import org.bukkit.ChatColor;

public class GoldLifeCard extends CardClass {
    @Override
    protected void createDescription() {
        description.add(ChatColor.WHITE + "HAZARDDD");
    }

    @Override
    public String getName() {
        return "&6&lZłote życie karta";
    }

    @Override
    protected int getCustomModelData() {
        return 8760005;
    }

    @Override
    public CardType getCardType() {
        return CardType.GOLD_CARD;
    }

    @Override
    public int getPercentageChance() {
        return 33;
    }
}
