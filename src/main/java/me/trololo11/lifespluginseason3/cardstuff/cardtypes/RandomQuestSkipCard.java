package me.trololo11.lifespluginseason3.cardstuff.cardtypes;

import me.trololo11.lifespluginseason3.cardstuff.CardClass;
import me.trololo11.lifespluginseason3.cardstuff.CardType;
import org.bukkit.ChatColor;

public class RandomQuestSkipCard extends CardClass {
    @Override
    protected void createDescription() {
        description.add(ChatColor.WHITE + "Pls use this lol");
    }

    @Override
    public String getName() {
        return ChatColor.GOLD + ChatColor.BOLD.toString() + "Losowy quest skip";
    }

    @Override
    protected int getCustomModelData() {
        return 237321;
    }

    @Override
    public CardType getCardType() {
        return CardType.RANDOM_SKIP;
    }

    @Override
    public float getPercentageChance() {
        return 0.4f;
    }
}
