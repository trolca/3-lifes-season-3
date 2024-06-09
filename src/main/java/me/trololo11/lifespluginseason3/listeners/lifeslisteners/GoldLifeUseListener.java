package me.trololo11.lifespluginseason3.listeners.lifeslisteners;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.events.PlayerChangeLifesEvent;
import me.trololo11.lifespluginseason3.managers.LifesManager;
import me.trololo11.lifespluginseason3.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * Listens for a gold life use. It gives the player 2 lives.
 */
public class GoldLifeUseListener implements Listener {

    private LifesManager lifesManager;
    private LifesPlugin plugin = LifesPlugin.getPlugin();

    public GoldLifeUseListener(LifesManager lifesManager){
        this.lifesManager = lifesManager;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = e.getPlayer();
        ItemStack item = e.getItem();
        if(item == null) return;
        if(!item.hasItemMeta()) return;
        if(!Utils.getPrivateName(item).startsWith("gold_life")) return;

        byte newLifes = (byte) (lifesManager.getPlayerLifes(player)+2);
        int slot = e.getHand() == EquipmentSlot.HAND ? player.getInventory().getHeldItemSlot() : 40;

        if(lifesManager.getPlayerLifes(player) >= 3){
            player.sendMessage(ChatColor.RED + "Musisz mieć mniej niż 3 życia by użyć tego przedmiotu!");
            return;
        }

        player.getInventory().setItem(slot, null);

        Bukkit.getPluginManager().callEvent(new PlayerChangeLifesEvent(player, newLifes));

        plugin.getPlayerStats(player).goldLifesUsed++;

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        player.sendMessage(ChatColor.YELLOW + "Pomyślnie dodano życia!");
    }
}
