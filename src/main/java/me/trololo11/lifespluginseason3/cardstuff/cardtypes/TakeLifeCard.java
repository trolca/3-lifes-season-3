package me.trololo11.lifespluginseason3.cardstuff.cardtypes;

import me.trololo11.lifespluginseason3.cardstuff.CardClass;
import me.trololo11.lifespluginseason3.cardstuff.CardType;
import org.bukkit.ChatColor;

public class  TakeLifeCard extends CardClass {
    @Override
    protected void createDescription() {
        description.add(ChatColor.WHITE + "Kiedy masz tą kartę w ekwipunku");
        description.add(ChatColor.WHITE + "i zabijesz innego gracza to dostajesz jego");
        description.add(ChatColor.WHITE + "życie i tracisz tą kartę.");
    }

    @Override
    public String getName() {
        return "&9&lZabierz życie karta";
    }

    @Override
    protected int getCustomModelData() {
        return 8760008;
    }

    @Override
    public CardType getCardType() {
        return CardType.TAKE_LIFE;
    }

    @Override
    public int getPercentageChance() {
        return 60;
    }
}
