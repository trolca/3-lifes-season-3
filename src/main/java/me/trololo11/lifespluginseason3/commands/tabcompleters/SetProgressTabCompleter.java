package me.trololo11.lifespluginseason3.commands.tabcompleters;

import me.trololo11.lifespluginseason3.managers.QuestManager;
import me.trololo11.lifespluginseason3.utils.Quest;
import me.trololo11.lifespluginseason3.utils.QuestType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class SetProgressTabCompleter implements TabCompleter {

    private QuestManager questManager;

    public SetProgressTabCompleter(QuestManager questManager){
        this.questManager = questManager;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> list = new ArrayList<>();

        if(!sender.hasPermission("3lifes3.admin")){
            list.add("kys");
            return list;
        }

        switch (args.length){

            case 1 ->{
                return null;
            }

            case 2 ->{
                for(QuestType questType : QuestType.values()){
                    list.add(questType.toString().toLowerCase());
                }
            }

            case 3 ->{
                QuestType questType;
                try {
                    questType = QuestType.valueOf(args[1].toUpperCase());
                }catch (IllegalArgumentException e){
                    list.add("Niepoprawny typ questa");
                    return list;
                }

                for(Quest quest : questManager.getCorrespondingQuestArray(questType)){
                    list.add(quest.getDatabaseName());
                }

            }

            case 4 -> list.add("<new progress>");

        }

        return list;
    }
}
