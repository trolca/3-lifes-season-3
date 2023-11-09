package me.trololo11.lifespluginseason3.listeners.cardlisteners;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.cardstuff.CardType;
import me.trololo11.lifespluginseason3.events.PlayerChangeLifesEvent;
import me.trololo11.lifespluginseason3.managers.LifesManager;
import me.trololo11.lifespluginseason3.managers.RecipesManager;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class TakeLifeCardListener implements Listener {

    private LifesManager lifesManager;
    private RecipesManager recipesManager;
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    public TakeLifeCardListener(LifesManager lifesManager, RecipesManager recipesManager){
        this.lifesManager = lifesManager;
        this.recipesManager = recipesManager;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player playerDie = e.getEntity();
        if(playerDie.getKiller() == null) return;
        Player killer = playerDie.getKiller();

        int indexCard = Utils.indexOfItemLocalized(CardType.TAKE_LIFE.toString().toLowerCase(), killer.getInventory());
        if(indexCard == -1) return;

        byte playerLifes = lifesManager.getPlayerLifes(killer);
        killer.getInventory().setItem(indexCard, null);

        if(playerLifes >= 3){

            Utils.givePlayerItemSafely(killer, recipesManager.getPlayerLifeItem(playerDie));

        }else{
            Bukkit.getServer().getPluginManager().callEvent(new PlayerChangeLifesEvent(killer, (byte) (playerLifes+1)));
        }

        killer.sendMessage(ChatColor.GREEN + "Zabrano życie od "+ playerDie.getName() + "!");
        killer.playSound(killer, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);

        plugin.getPlayerStats(killer).cardsUsed++;

    }
}
