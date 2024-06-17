package me.trololo11.lifespluginseason3.cardstuff.cardtypes;

import me.trololo11.lifespluginseason3.cardstuff.CardClass;
import me.trololo11.lifespluginseason3.cardstuff.CardType;
import org.bukkit.ChatColor;

public class RandomQuestSkipCard extends CardClass {
    @Override
    protected void createDescription() {
        description.add(ChatColor.WHITE + "Pomija losowy quest dla wszystkich graczy.");
    }

    @Override
    public String getName() {
        return ChatColor.GOLD + ChatColor.BOLD.toString() + "Losowy quest skip";
    }

    @Override
    protected int getCustomModelData() {
        return 8760006;
    }

    @Override
    public CardType getCardType() {
        return CardType.RANDOM_SKIP;
    }

    @Override
    public int getPercentageChance() {
        return 10;
    }
}
