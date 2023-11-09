package me.trololo11.lifespluginseason3.commands;

import me.trololo11.lifespluginseason3.managers.*;
import me.trololo11.lifespluginseason3.menus.MainLifesMenu;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LifesMenuCommand implements CommandExecutor {

    private QuestManager questManager;
    private RecipesManager recipesManager;
    private QuestsAwardsManager questsAwardsManager;
    private DatabaseManager databaseManager;
    private CardManager cardManager;

    public LifesMenuCommand(QuestManager questManager, RecipesManager recipesManager, QuestsAwardsManager questsAwardsManager, DatabaseManager databaseManager, CardManager cardManager){
        this.questManager = questManager;
        this.recipesManager = recipesManager;
        this.questsAwardsManager = questsAwardsManager;
        this.databaseManager = databaseManager;
        this.cardManager = cardManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) return true;

        new MainLifesMenu(questManager, recipesManager, questsAwardsManager, databaseManager, cardManager).open(player);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);

        return true;
    }
}
