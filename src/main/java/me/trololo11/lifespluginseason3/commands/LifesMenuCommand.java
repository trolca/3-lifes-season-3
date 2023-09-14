package me.trololo11.lifespluginseason3.commands;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.managers.RecipesManager;
import me.trololo11.lifespluginseason3.menus.MainLifesMenu;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LifesMenuCommand implements CommandExecutor {

    private QuestManager questManager;
    private RecipesManager recipesManager;

    public LifesMenuCommand(QuestManager questManager, RecipesManager recipesManager){
        this.questManager = questManager;
        this.recipesManager = recipesManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        new MainLifesMenu(questManager, recipesManager).open(player);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);

        return true;
    }
}
