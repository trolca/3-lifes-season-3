package me.trololo11.lifespluginseason3.cardstuff.cardtypes;

import me.trololo11.lifespluginseason3.cardstuff.CardClass;
import me.trololo11.lifespluginseason3.cardstuff.CardType;
import org.bukkit.ChatColor;

public class ReduceRequirementsQuestsCard extends CardClass {
    @Override
    protected void createDescription() {
        description.add(ChatColor.WHITE + "THIS IS GONNA BE SO HARD AHHHH");
    }

    @Override
    public String getName() {
        return "&2&lZmniejszenie wymagań karta";
    }

    @Override
    protected int getCustomModelData() {
        return 213721;
    }

    @Override
    public CardType getCardType() {
        return CardType.REQUIREMENTS_REDUCE;
    }

    @Override
    public float getPercentageChance() {
        return 0.25f;
    }
}
