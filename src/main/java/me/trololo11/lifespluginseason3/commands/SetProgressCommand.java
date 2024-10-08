package me.trololo11.lifespluginseason3.commands;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.Quest;
import me.trololo11.lifespluginseason3.utils.QuestType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Sets a specified amount of progress for a quest for the specified quest for
 * the specified player.
 */
public class SetProgressCommand implements CommandExecutor {

    private QuestManager questManager;

    public SetProgressCommand(QuestManager questManager){
        this.questManager = questManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("3lifes3.admin")){
            sender.sendMessage(ChatColor.RED + "Ty kur");
            return true;
        }

        if(args.length == 0){
            sender.sendMessage(ChatColor.RED + "Napisz nazwe gracza!");
            return true;
        }

        if(args.length == 1){
            sender.sendMessage(ChatColor.RED + "Napisz typ questa!");
            return true;
        }

        if(args.length == 2){
            sender.sendMessage(ChatColor.RED + "Napisz jaki quest");
            return true;
        }

        if(args.length == 3){
            sender.sendMessage(ChatColor.RED + "Napisz jaki nowy ma być progress!");
            return true;
        }


        Player player = Bukkit.getPlayer(args[0]);
        if(player == null){
            sender.sendMessage(ChatColor.RED + "Ten gracz nie istnieje!");
            return true;
        }
        QuestType questType;
        try{
            questType = QuestType.valueOf(args[1].toUpperCase());
        }catch (IllegalArgumentException e){
            player.sendMessage(ChatColor.RED + "Taki typ questów nie istnieje!");
            return true;
        }

        Quest setProgressQuest = questManager.getQuestByDatabaseName(questType, args[2]);

        if(setProgressQuest == null || !questManager.getAllActiveQuests().contains(setProgressQuest)){
            sender.sendMessage(ChatColor.RED + "Ten quest nie istnieje!");
            return true;
        }
        int newProgress;
        try {
            newProgress = Integer.parseInt(args[3]);
        }catch (NumberFormatException e){
            sender.sendMessage(ChatColor.RED + "Napisz poprawną liczbe!");
            return true;
        }

        setProgressQuest.setPlayerProgress(player, newProgress);

        player.sendMessage(ChatColor.GREEN + "Pomyślnie ustawiono progress!");

        return true;
    }
}
