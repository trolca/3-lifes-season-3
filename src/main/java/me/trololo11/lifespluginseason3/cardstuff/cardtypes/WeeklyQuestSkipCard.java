package me.trololo11.lifespluginseason3.cardstuff.cardtypes;

import me.trololo11.lifespluginseason3.cardstuff.CardClass;
import me.trololo11.lifespluginseason3.cardstuff.CardType;
import org.bukkit.ChatColor;

public class WeeklyQuestSkipCard extends CardClass {
    @Override
    protected void createDescription() {
        description.add(ChatColor.WHITE + "Pomija wybrany tygodniowy quest dla ciebie.");
    }

    @Override
    public String getName() {
        return "&e&lPominięcie tygodniowych";
    }

    @Override
    protected int getCustomModelData() {
        return 8760009;
    }

    @Override
    public CardType getCardType() {
        return CardType.WEEKLY_SKIP;
    }

    @Override
    public int getPercentageChance() {
        return 30;
    }
}
