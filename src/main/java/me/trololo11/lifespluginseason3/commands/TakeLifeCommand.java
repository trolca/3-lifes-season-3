package me.trololo11.lifespluginseason3.commands;

import me.trololo11.lifespluginseason3.events.PlayerChangeLifesEvent;
import me.trololo11.lifespluginseason3.managers.LifesManager;
import me.trololo11.lifespluginseason3.managers.RecipesManager;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Removes 1 life from the player and gives them an item representation of it.
 */
public class TakeLifeCommand implements CommandExecutor {

    private LifesManager lifesManager;
    private RecipesManager recipesManager;

    public TakeLifeCommand(LifesManager lifesManager, RecipesManager recipesManager){
        this.lifesManager = lifesManager;
        this.recipesManager = recipesManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return true;

        Player player = (Player) sender;
        byte oldLifes = lifesManager.getPlayerLifes(player);

        if(oldLifes <= 1){
            player.sendMessage(ChatColor.RED + "Masz tylko 1 życie! Prosze nie zabijaj sie :<");
            return true;
        }

        if(Utils.isInventoryFull(player.getInventory())){
            player.sendMessage(ChatColor.RED + "Masz pełny ekwipunek, opróżnij go troche przed zabieraniem sobie życia!");
            return true;
        }

        Bukkit.getServer().getPluginManager().callEvent(new PlayerChangeLifesEvent(player, (byte) (oldLifes-1)));
        player.getInventory().addItem(recipesManager.getPlayerLifeItem(player));
        player.spawnParticle(Particle.REDSTONE, player.getLocation(), 500,  1, 1, 1, new Particle.DustOptions(Color.fromBGR(0, 0, 255), 1f));
        player.playSound(player.getLocation(), Sound.BLOCK_METAL_BREAK, 10f, 1.5f);

        return true;
    }
}
