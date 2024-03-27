package me.trololo11.lifespluginseason3.cardstuff.cardtypes;

import me.trololo11.lifespluginseason3.cardstuff.CardClass;
import me.trololo11.lifespluginseason3.cardstuff.CardType;
import org.bukkit.ChatColor;

public class ReduceRequirementsQuestsCard extends CardClass {
    @Override
    protected void createDescription() {
        description.add(ChatColor.WHITE + "Zmniejsza wymagania wybranego questa o 50%");
        description.add(ChatColor.WHITE + "dla wszystkich graczy.");
    }

    @Override
    public String getName() {
        return "&2&lZmniejszenie wymagań karta";
    }

    @Override
    protected int getCustomModelData() {
        return 8760007;
    }

    @Override
    public CardType getCardType() {
        return CardType.REQUIREMENTS_REDUCE;
    }

    @Override
    public int getPercentageChance() {
        return 25;
    }
}
