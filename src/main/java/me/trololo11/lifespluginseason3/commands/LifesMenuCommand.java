package me.trololo11.lifespluginseason3.commands;

import me.trololo11.lifespluginseason3.managers.*;
import me.trololo11.lifespluginseason3.menus.MainLifesMenu;
import me.trololo11.lifespluginseason3.menus.QuestsMenu;
import me.trololo11.lifespluginseason3.utils.QuestType;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Opens the {@link MainLifesMenu} for the player.
 */
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

        MainLifesMenu mainLifesMenu = new MainLifesMenu(questManager, recipesManager, questsAwardsManager, databaseManager, cardManager);
        if(args.length > 0){
            String text = args[0];

            if(text.equalsIgnoreCase("daily")) new QuestsMenu(mainLifesMenu, "&c&lQuesty dzienne", QuestType.DAILY, questManager, questsAwardsManager, recipesManager, databaseManager, cardManager).open(player);
            else if(text.equalsIgnoreCase("weekly")) new QuestsMenu(mainLifesMenu, "&e&lQuesty tygodniowe", QuestType.WEEKLY, questManager, questsAwardsManager, recipesManager, databaseManager, cardManager).open(player);
            else if(text.equalsIgnoreCase("card")) new QuestsMenu(mainLifesMenu, "&f&lQuesty do karty", QuestType.CARD, questManager, questsAwardsManager, recipesManager, databaseManager, cardManager).open(player);
            else mainLifesMenu.open(player);

        }else{
            mainLifesMenu.open(player);
        }

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);

        return true;
    }
}
