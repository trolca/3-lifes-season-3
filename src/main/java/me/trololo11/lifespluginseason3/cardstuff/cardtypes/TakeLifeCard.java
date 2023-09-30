package me.trololo11.lifespluginseason3.cardstuff.cardtypes;

import me.trololo11.lifespluginseason3.cardstuff.CardClass;
import me.trololo11.lifespluginseason3.cardstuff.CardType;
import org.bukkit.ChatColor;

public class TakeLifeCard extends CardClass {
    @Override
    protected void createDescription() {
        description.add(ChatColor.WHITE + "Takes life");
    }

    @Override
    public String getName() {
        return "&9&lZabierz zycie karta";
    }

    @Override
    protected int getCustomModelData() {
        return 7321;
    }

    @Override
    public CardType getCardType() {
        return CardType.TAKE_LIFE;
    }

    @Override
    public float getPercentageChance() {
        return 0.6f;
    }
}
