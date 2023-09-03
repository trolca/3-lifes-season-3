package me.trololo11.lifespluginseason3.listeners.lifeslisteners;

import me.trololo11.lifespluginseason3.LifesPlugin;
import me.trololo11.lifespluginseason3.events.PlayerChangeLifesEvent;
import me.trololo11.lifespluginseason3.managers.LifesManager;
import me.trololo11.lifespluginseason3.tasks.SetItemTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class LifeUseListener implements Listener {

    private LifesPlugin plugin = LifesPlugin.getPlugin();
    private LifesManager lifesManager;

    public LifeUseListener(LifesManager lifesManager){
        this.lifesManager = lifesManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = e.getPlayer();
        ItemStack item = e.getItem();
        if(item == null) return;
        if(!item.hasItemMeta()) return;
        if(!item.getItemMeta().getLocalizedName().startsWith("life") && !item.getItemMeta().getLocalizedName().startsWith("player_life")) return;
        byte newLifes = (byte) (lifesManager.getPlayerLifes(player)+1);
        int slot = e.getHand() == EquipmentSlot.HAND ? player.getInventory().getHeldItemSlot() : 40;

        if(newLifes > 3){
            player.sendMessage(ChatColor.RED + "Nie możesz mieć więcej niż 3 życia!");
            return;
        }

        SetItemTask setItemTask = new SetItemTask(player.getInventory(), slot, null);

        setItemTask.runTaskLater(plugin, 1L);

        Bukkit.getServer().getPluginManager().callEvent(new PlayerChangeLifesEvent(player, newLifes));

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        player.sendMessage(ChatColor.GREEN + "Pomyślnie dodano życie!");




    }

}
