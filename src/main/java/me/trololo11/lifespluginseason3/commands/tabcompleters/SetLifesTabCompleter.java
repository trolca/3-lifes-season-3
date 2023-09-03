package me.trololo11.lifespluginseason3.commands.tabcompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class SetLifesTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> completer = new ArrayList<>();

        if(args.length == 1){
            return null;
        }

        if(args.length == 2){
            completer.add("<ilość żyć>");
        }

        return completer;

    }
}
