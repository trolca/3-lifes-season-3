package me.trololo11.lifespluginseason3.commands;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.managers.DatabaseManager;
import me.trololo11.lifespluginseason3.managers.LifesManager;
import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.managers.QuestsAwardsManager;
import me.trololo11.lifespluginseason3.tasks.SaveDataTask;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SaveDataCommand implements CommandExecutor {

    private LifesPlugin plugin = LifesPlugin.getPlugin();
    private DatabaseManager databaseManager;
    private LifesManager lifesManager;
    private QuestsAwardsManager questsAwardsManager;
    private QuestManager questManager;

    public SaveDataCommand(DatabaseManager databaseManager, LifesManager lifesManager, QuestsAwardsManager questsAwardsManager, QuestManager questManager){
        this.databaseManager = databaseManager;
        this.lifesManager = lifesManager;
        this.questsAwardsManager = questsAwardsManager;
        this.questManager = questManager;
    }

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!sender.hasPermission("3lifes3.admin")){
            sender.sendMessage(ChatColor.RED + "Hehe");
            return true;
        }

        new SaveDataTask(databaseManager, lifesManager, questsAwardsManager, questManager).runTask(plugin);

        sender.sendMessage(ChatColor.GREEN + "Pomyślnie zapisano dane!");
        return true;
    }
}
