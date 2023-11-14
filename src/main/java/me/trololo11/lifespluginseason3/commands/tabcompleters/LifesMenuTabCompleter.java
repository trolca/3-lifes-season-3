package me.trololo11.lifespluginseason3.commands.tabcompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class LifesMenuTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> list = new ArrayList<>();

        if(args.length == 1){
            list.add("daily");
            list.add("weekly");
            list.add("card");
        }

        return list;
    }
}
