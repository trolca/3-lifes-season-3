package me.trololo11.lifespluginseason3.utils;

import me.trololo11.lifespluginseason3.menus.QuestSelectMenu;
import org.bukkit.entity.Player;

/**
 * This lambda function runs when a player selects a quest in the {@link QuestSelectMenu}.
 */
@FunctionalInterface
public interface QuestSelectFunction {
    void run(Quest quest, Player player, QuestSelectMenu questSelectMenu);
}
