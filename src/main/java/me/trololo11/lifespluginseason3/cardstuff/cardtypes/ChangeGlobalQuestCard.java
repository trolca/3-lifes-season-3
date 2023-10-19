package me.trololo11.lifespluginseason3.cardstuff.cardtypes;

import me.trololo11.lifespluginseason3.cardstuff.CardClass;
import me.trololo11.lifespluginseason3.cardstuff.CardType;
import org.bukkit.ChatColor;

public class ChangeGlobalQuestCard extends CardClass {
    @Override
    protected void createDescription() {
        description.add(ChatColor.WHITE + "Sussy baka impostor");
    }

    @Override
    public String getName() {
        return "&b&lGlobal quest skip";
    }

    @Override
    protected int getCustomModelData() {
        return 6969;
    }

    @Override
    public CardType getCardType() {
        return CardType.QUEST_CHANGE;
    }

    @Override
    public float getPercentageChance() {
        return 0.4f;
    }
}