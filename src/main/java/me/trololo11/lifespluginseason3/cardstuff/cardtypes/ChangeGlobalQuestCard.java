package me.trololo11.lifespluginseason3.cardstuff.cardtypes;

import me.trololo11.lifespluginseason3.cardstuff.CardClass;
import me.trololo11.lifespluginseason3.cardstuff.CardType;
import org.bukkit.ChatColor;

public class ChangeGlobalQuestCard extends CardClass {
    @Override
    protected void createDescription() {
        description.add(ChatColor.WHITE + "Zmienia wybrany quest na inny losowy");
        description.add(ChatColor.WHITE + "dla wszystkich graczy.");
    }

    @Override
    public String getName() {
        return "&b&lGlobalny quest skip";
    }

    @Override
    protected int getCustomModelData() {
        return 8760004;
    }

    @Override
    public CardType getCardType() {
        return CardType.QUEST_CHANGE;
    }

    @Override
    public int getPercentageChance() {
        return 20;
    }
}
